package application.mechanic;

import application.mechanic.internal.ClientSnapService;
import application.mechanic.internal.GameSessionService;
import application.mechanic.snapshots.ClientSnap;
import application.models.User;
import application.services.AccountService;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanics {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanics.class);
    @NotNull
    private final ClientSnapService clientSnapshotsService;
    @NotNull
    private final GameSessionService gameSessionService;
    @NotNull
    private final AccountService accountService;
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private ConcurrentLinkedQueue<User> singleWaiters = new ConcurrentLinkedQueue<>();
    @NotNull
    private ConcurrentLinkedQueue<User> multiWaiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanics(@NotNull ClientSnapService clientSnapshotsService,
                         @NotNull GameSessionService gameSessionService,
                         @NotNull AccountService accountService,
                         @NotNull RemotePointService remotePointService) {
        this.clientSnapshotsService = clientSnapshotsService;
        this.gameSessionService = gameSessionService;
        this.accountService = accountService;
        this.remotePointService = remotePointService;
    }

    public void addClientSnapshot(long userId, ClientSnap userSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, userSnap));
    }

    public void addUser(@NotNull Long userId, String mode) {
        if (gameSessionService.isPlaying(userId)) {
            return;
        }
        final User user = accountService.getUser(userId);
        if (user != null) {
            if (Config.Mode.valueOf(mode) == Config.Mode.SINGLE) {
                singleWaiters.add(user);
                LOGGER.info(String.format("User %s added to the single waiting list", user.getLogin()));
            } else if (Config.Mode.valueOf(mode) == Config.Mode.MULTI) {
                multiWaiters.add(user);
                LOGGER.info(String.format("User %s added to the multi waiting list", user.getLogin()));
            }
        }
    }

    private void tryStartGames() {
        final Set<User> matchedPlayers = new LinkedHashSet<>();

        while (multiWaiters.size() >= 2 || multiWaiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final User candidate = multiWaiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(candidate);
            if (matchedPlayers.size() == 2) {
                final Iterator<User> iterator = matchedPlayers.iterator();
                final User user1 = iterator.next();
                final User user2 = iterator.next();
                if (user1.getId() != user2.getId()) {
                    gameSessionService.startMultiGame(user1, user2);
                } else {
                    multiWaiters.add(user1);
                }
                matchedPlayers.clear();
            }
        }
        multiWaiters.addAll(matchedPlayers);

        while (!singleWaiters.isEmpty()) {
            final User candidate = singleWaiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            gameSessionService.startSingleGame(candidate);
        }
    }

    public void gmStep() {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Can't handle game task", ex);
                }
            }
        }

        multiSessionsProcess();
        singleSessionsProcess();

        tryStartGames();
        clientSnapshotsService.reset();
    }


    private void multiSessionsProcess() {
        final List<MultiGameSession> multiSessionsToTerminate = new ArrayList<>();

        for (MultiGameSession session : gameSessionService.getMultiSessions()) {
            try {
                clientSnapshotsService.processSnapshotsFor(session);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed to send snapshots, terminating the session", ex);
                multiSessionsToTerminate.add(session);
            }
        }

        final List<MultiGameSession> multiSessionsToFinish = new ArrayList<>();
        for (MultiGameSession session : gameSessionService.getMultiSessions()) {
            if (session.tryFinishGame()) {
                multiSessionsToFinish.add(session);
                continue;
            }

            if (!gameSessionService.checkHealthState(session)) {
                multiSessionsToTerminate.add(session);
            }
        }
        multiSessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));
        multiSessionsToFinish.forEach(session -> gameSessionService.forceTerminate(session, false));
    }

    private void singleSessionsProcess() {
        final List<SingleGameSession> singleSessionsToTerminate = new ArrayList<>();

        for (SingleGameSession session : gameSessionService.getSingleSessions()) {
            try {
                clientSnapshotsService.processSnapshotsFor(session);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed to send snapshots, terminating the session", ex);
                singleSessionsToTerminate.add(session);
            }
        }

        final List<SingleGameSession> singleSessionsToFinish = new ArrayList<>();
        for (SingleGameSession session : gameSessionService.getSingleSessions()) {
            if (session.tryFinishGame()) {
                singleSessionsToFinish.add(session);
                continue;
            }

            if (!gameSessionService.checkHealthState(session)) {
                singleSessionsToTerminate.add(session);
            }
        }
        singleSessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));
        singleSessionsToFinish.forEach(session -> gameSessionService.forceTerminate(session, false));
    }


    private boolean insureCandidate(@NotNull User candidate) {
        return remotePointService.isConnected(candidate.getId())
                && accountService.getUser(candidate.getId()) != null;
    }


    public void reset() {
        for (MultiGameSession session : gameSessionService.getMultiSessions()) {
            gameSessionService.forceTerminate(session, true);
        }
        for (SingleGameSession session : gameSessionService.getSingleSessions()) {
            gameSessionService.forceTerminate(session, true);
        }
        singleWaiters.forEach(user -> remotePointService.cutDownConnection(user.getId(), CloseStatus.SERVER_ERROR));
        singleWaiters.clear();
        multiWaiters.forEach(user -> remotePointService.cutDownConnection(user.getId(), CloseStatus.SERVER_ERROR));
        multiWaiters.clear();
        tasks.clear();
        clientSnapshotsService.reset();
    }

}
