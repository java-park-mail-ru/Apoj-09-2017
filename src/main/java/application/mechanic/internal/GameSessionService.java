package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.avatar.Player;
import application.mechanic.requests.FinishGame;
import application.models.User;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import application.mechanic.GameSession;

@SuppressWarnings("MissortedModifiers")
@Service
public class GameSessionService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class.getSimpleName());

    @NotNull
    private final Map<Long, GameSession> usersMap = new ConcurrentHashMap<>();
    @NotNull
    private final Set<GameSession> gameSessions = new LinkedHashSet<>();
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final ClientSnapService clientSnapshotsService;

    public GameSessionService(@NotNull RemotePointService remotePointService, @NotNull ClientSnapService clientSnapshotsService) {
        this.remotePointService = remotePointService;
        this.clientSnapshotsService = clientSnapshotsService;
    }


    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    public void startGame(@NotNull Player first, @NotNull Player second) {
        final GameSession gameSession = new GameSession(first, second, Config.MULTI_MODE, this);
        gameSessions.add(gameSession);
        usersMap.put(first.getId(), gameSession);
        usersMap.put(second.getId(), gameSession);
        LOGGER.info("Game session " + gameSession.getId() + " started. " + gameSession.toString());
    }

    public void startGame(@NotNull Player first) {
        final GameSession gameSession = new GameSession(first, Config.SINGLE_MODE, this);
        gameSessions.add(gameSession);
        usersMap.put(first.getId(), gameSession);
        LOGGER.info("Game session " + gameSession.getId() + " started. " + gameSession.toString());
    }

    public boolean isPlaying(@NotNull Long id) {
        return usersMap.containsKey(id);
    }

    public void forceTerminate(@NotNull GameSession gameSession, boolean error) {
        final boolean exists = gameSessions.remove(gameSession);
        gameSession.setFinished();
        usersMap.remove(gameSession.getFirst().getId());
        usersMap.remove(gameSession.getSecond().getId());
        final CloseStatus status = error ? CloseStatus.SERVER_ERROR : CloseStatus.NORMAL;
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getFirst().getId(), status);
            remotePointService.cutDownConnection(gameSession.getSecond().getId(), status);
        }
        clientSnapshotsService.clearForUser(gameSession.getFirst().getId());
        clientSnapshotsService.clearForUser(gameSession.getSecond().getId());

        LOGGER.info("Game session " + gameSession.getId() + (error ? " was terminated due to error. " : " was cleaned. ")
                + gameSession.toString());
    }

    public boolean checkHealthState(@NotNull GameSession gameSession) {
        return gameSession.getPlayers().stream().map(Player::getId).allMatch(remotePointService::isConnected);
    }

    public void finishGame(@NotNull GameSession gameSession) {
        gameSession.setFinished();
        final boolean result = gameSession.getResult();

        try {
            remotePointService.sendMessageToUser(gameSession.getFirst().getId(), new FinishGame(result));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getFirst().getUser().getLogin()), ex);
        }
        if (gameSession.getSecond() != null) {
            try {
                remotePointService.sendMessageToUser(gameSession.getSecond().getId(), new FinishGame(result));
            } catch (IOException ex) {
                LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                        gameSession.getSecond().getUser().getLogin()), ex);
            }
        }
    }
}
