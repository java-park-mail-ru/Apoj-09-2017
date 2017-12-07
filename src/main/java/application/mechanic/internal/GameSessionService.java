package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.SingleGameSession;
import application.mechanic.avatar.Player;
import application.mechanic.music.Music;
import application.mechanic.requests.FinishGame;
import application.models.User;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import application.mechanic.MultiGameSession;

@SuppressWarnings("MissortedModifiers")
@Service
public class GameSessionService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class.getSimpleName());

    @NotNull
    private final Map<Long, MultiGameSession> multiUsersMap = new ConcurrentHashMap<>();
    @NotNull
    private final Set<MultiGameSession> multiGameSessions = new LinkedHashSet<>();
    @NotNull
    private final Map<Long, SingleGameSession> singleUsersMap = new ConcurrentHashMap<>();
    @NotNull
    private final Set<SingleGameSession> singleGameSessions = new LinkedHashSet<>();
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final ClientSnapService clientSnapshotsService;
    @NotNull
    private final GameInitService gameInitService;
    @NotNull
    private final Music music;

    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull ClientSnapService clientSnapshotsService,
                              @NotNull GameInitService gameInitService,
                              @NotNull Music music) {
        this.remotePointService = remotePointService;
        this.clientSnapshotsService = clientSnapshotsService;
        this.gameInitService = gameInitService;
        this.music = music;
    }

    public Set<MultiGameSession> getMultiSessions() {
        return multiGameSessions;
    }

    public Set<SingleGameSession> getSingleSessions() {
        return singleGameSessions;
    }

    public void startMultiGame(@NotNull User first, @NotNull User second) {
        final Player singer = new Player(first, Config.SINGER_ROLE);
        final Player listener = new Player(second, Config.LISTENER_ROLE);
        final MultiGameSession gameSession = new MultiGameSession(singer, listener, music.getSongName(), Config.STEP_1, this);
        multiGameSessions.add(gameSession);
        multiUsersMap.put(first.getId(), gameSession);
        multiUsersMap.put(second.getId(), gameSession);
        gameInitService.initGameFor(gameSession);
        LOGGER.info("Game session " + gameSession.getId() + " started. " + gameSession.toString());
    }

    public void startSingleGame(@NotNull User user) {
        final Player player = new Player(user, Config.SINGER_ROLE);
        final SingleGameSession gameSession = new SingleGameSession(player, music.getSongName(), Config.STEP_1, this);
        singleGameSessions.add(gameSession);
        singleUsersMap.put(user.getId(), gameSession);
        gameInitService.initGameFor(gameSession);
        LOGGER.info("Game session " + gameSession.getId() + " started. " + gameSession.toString());
    }

    public boolean isPlaying(@NotNull Long id) {
        return multiUsersMap.containsKey(id) || singleUsersMap.containsKey(id);
    }

    public void forceTerminate(@NotNull MultiGameSession gameSession, boolean error) {
        final boolean exists = multiGameSessions.remove(gameSession);
        gameSession.setFinished();
        multiUsersMap.remove(gameSession.getSinger().getId());
        multiUsersMap.remove(gameSession.getListener().getId());
        final CloseStatus status;
        if (error) {
            status = CloseStatus.SERVER_ERROR;
        } else {
            status = CloseStatus.NORMAL;
        }
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getSinger().getId(), status);
            remotePointService.cutDownConnection(gameSession.getListener().getId(), status);
        }
        clientSnapshotsService.clearForUser(gameSession.getSinger().getId());
        clientSnapshotsService.clearForUser(gameSession.getListener().getId());
        final String logger;
        if (error) {
            logger = " was terminated due to error. ";
        } else {
            logger = " was cleaned. ";
        }
        LOGGER.info("Game session " + gameSession.getId() + logger + gameSession.toString());
    }

    public void forceTerminate(@NotNull SingleGameSession gameSession, boolean error) {
        final boolean exists = singleGameSessions.remove(gameSession);
        gameSession.setFinished();
        singleUsersMap.remove(gameSession.getPlayer().getId());
        final CloseStatus status;
        if (error) {
            status = CloseStatus.SERVER_ERROR;
        } else {
            status = CloseStatus.NORMAL;
        }
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getPlayer().getId(), status);
        }
        clientSnapshotsService.clearForUser(gameSession.getPlayer().getId());
        final String logger;
        if (error) {
            logger = " was terminated due to error. ";
        } else {
            logger = " was cleaned. ";
        }
        LOGGER.info("Game session " + gameSession.getId() + logger + gameSession.toString());
    }

    public boolean checkHealthState(@NotNull MultiGameSession gameSession) {
        final long singer = gameSession.getSingerId();
        final long listener = gameSession.getListenerId();
        try {
            if (!remotePointService.isConnected(singer)) {
                remotePointService.sendMessageToUser(listener, new FinishGame());
                return false;
            }
            if (!remotePointService.isConnected(listener)) {
                remotePointService.sendMessageToUser(singer, new FinishGame());
                return false;
            }
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send Leave message to user %s", ex));
            return false;
        }
        return true;
    }

    public boolean checkHealthState(@NotNull SingleGameSession gameSession) {
        return remotePointService.isConnected(gameSession.getUserId());
    }

    public void finishMultiGame(@NotNull MultiGameSession gameSession) {
        gameSession.setFinished();
        final boolean result = gameSession.getResult();

        try {
            remotePointService.sendMessageToUser(gameSession.getSinger().getId(), new FinishGame(result));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getSinger().getUser().getLogin()), ex);
        }

        try {
            remotePointService.sendMessageToUser(gameSession.getListener().getId(), new FinishGame(result));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getListener().getUser().getLogin()), ex);
        }
    }

    public void finishSingleGame(@NotNull SingleGameSession gameSession) {
        gameSession.setFinished();
        final boolean result = gameSession.getResult();

        try {
            LOGGER.info(String.valueOf(result));
            remotePointService.sendMessageToUser(gameSession.getPlayer().getId(), new FinishGame(result));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getPlayer().getUser().getLogin()), ex);
        }
    }
}
