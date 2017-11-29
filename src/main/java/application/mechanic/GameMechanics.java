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
    private ConcurrentLinkedQueue<Player> waiters = new ConcurrentLinkedQueue<>();

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
            waiters.add(new Player(user, mode));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("User %s added to the waiting list", user.getLogin()));
            }
        }
    }

    public void gmStep() {

    }


    public void reset() {
        final Set<GameSession> sessions = gameSessionService.getSessions();
        for (GameSession session : sessions) {
            gameSessionService.notifyGameIsOver(session, CloseStatus.SERVER_ERROR);
        }
    }

}
