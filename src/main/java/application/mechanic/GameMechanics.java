package application.mechanic;


import application.mechanic.avatar.Player;
import application.mechanic.internal.ClientSnapService;
import application.mechanic.internal.GameSessionService;
import application.mechanic.internal.ServerSnapService;
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

@SuppressWarnings("MissortedModifiers")
@Service
public class GameMechanics {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanics.class);
    @NotNull
    private final ClientSnapService clientSnapshotsService;
    @NotNull
    private final ServerSnapService serverSnapshotService;
    @NotNull
    private final GameSessionService gameSessionService;
    @NotNull
    private final AccountService accountService;
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private ConcurrentLinkedQueue<Player> singleWaiters = new ConcurrentLinkedQueue<>();
    @NotNull
    private ConcurrentLinkedQueue<Player> multiWaiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanics(@NotNull ClientSnapService clientSnapshotsService,
                         @NotNull ServerSnapService serverSnapshotService,
                         @NotNull GameSessionService gameSessionService,
                         @NotNull AccountService accountService,
                         @NotNull RemotePointService remotePointService) {
        this.clientSnapshotsService = clientSnapshotsService;
        this.serverSnapshotService = serverSnapshotService;
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
            if(mode.equals(Config.SINGLE_MODE)) {
                singleWaiters.add(new Player(user));
            } else {
                multiWaiters.add(new Player(user));
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("User %s added to the waiting list", user.getLogin()));
            }
        }
    }

    private void tryStartGames() {
        final Set<Player> matchedPlayers = new LinkedHashSet<>();

        while (multiWaiters.size() >= 2 || multiWaiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final Player candidate = multiWaiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(candidate);
            if (matchedPlayers.size() == 2) {
                final Iterator<Player> iterator = matchedPlayers.iterator();
                gameSessionService.startGame(iterator.next(), iterator.next());
                matchedPlayers.clear();
            }
        }
        multiWaiters.addAll(matchedPlayers);

        while (!singleWaiters.isEmpty()){
            final Player candidate = singleWaiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            gameSessionService.startGame(candidate);
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

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }

        final List<GameSession> sessionsToTerminate = new ArrayList<>();
        final List<GameSession> sessionsToFinish = new ArrayList<>();
        for (GameSession session : gameSessionService.getSessions()) {
            if (session.tryFinishGame()) {
                sessionsToFinish.add(session);
                continue;
            }

            if (!gameSessionService.checkHealthState(session)) {
                sessionsToTerminate.add(session);
                continue;
            }

            try {
                serverSnapshotService.sendSnapshotsFor(session);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed to send snapshots, terminating the session", ex);
                sessionsToTerminate.add(session);
            }
        }
        sessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));
        sessionsToFinish.forEach(session -> gameSessionService.forceTerminate(session, false));

        tryStartGames();
        clientSnapshotsService.reset();
    }

    private boolean insureCandidate(Player candidate) {
        return remotePointService.isConnected(candidate.getId()) &&
                accountService.getUser(candidate.getId()) != null;
    }


    public void reset() {
        for (GameSession session : gameSessionService.getSessions()) {
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
