package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.SingleGameSession;
import application.mechanic.avatar.Player;
import application.mechanic.music.Music;
import application.mechanic.requests.FinishGame;
import application.models.User;
import application.services.AccountService;
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
    private final AccountService accountService;
    @NotNull
    private final Music music;

    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull ClientSnapService clientSnapshotsService,
                              @NotNull GameInitService gameInitService,
                              @NotNull AccountService accountService,
                              @NotNull Music music) {
        this.remotePointService = remotePointService;
        this.clientSnapshotsService = clientSnapshotsService;
        this.gameInitService = gameInitService;
        this.music = music;
        this.accountService = accountService;
    }

    public Set<MultiGameSession> getMultiSessions() {
        return multiGameSessions;
    }

    public Set<SingleGameSession> getSingleSessions() {
        return singleGameSessions;
    }

    public void startMultiGame(@NotNull User first, @NotNull User second) {
        final Player singer = new Player(first, Config.Role.SINGER);
        final Player listener = new Player(second, Config.Role.LISTENER);
        final MultiGameSession gameSession = new MultiGameSession(singer, listener, music.getSongName(), Config.Step.RECORDING, this);
        multiGameSessions.add(gameSession);
        multiUsersMap.put(first.getId(), gameSession);
        multiUsersMap.put(second.getId(), gameSession);
        gameInitService.initGameFor(gameSession);
        LOGGER.info("Game session " + gameSession.getId() + " started. " + gameSession.toString());
    }

    public void startSingleGame(@NotNull User user) {
        final Player player = new Player(user, Config.Role.SINGER);
        final SingleGameSession gameSession = new SingleGameSession(player, music.getSongName(), Config.Step.RECORDING, this);
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
        final boolean singerConnection = remotePointService.isConnected(singer);
        final boolean listenerConnection = remotePointService.isConnected(listener);
        try {
            if (!singerConnection && !listenerConnection) {
                return false;
            }
            if (!singerConnection) {
                remotePointService.sendMessageToUser(listener, new FinishGame());
                return false;
            }
            if (!listenerConnection) {
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
        final boolean result = gameSession.getResult();

        try {
            final int score = accountService.updateMScore(gameSession.getSingerId(), result);
            remotePointService.sendMessageToUser(gameSession.getSingerId(), new FinishGame(result, score));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getSinger().getUser().getLogin()), ex);
        }

        try {
            final int score = accountService.updateMScore(gameSession.getListenerId(), result);
            remotePointService.sendMessageToUser(gameSession.getListenerId(), new FinishGame(result, score));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getListener().getUser().getLogin()), ex);
        }
    }

    public void finishSingleGame(@NotNull SingleGameSession gameSession) {
        final boolean result = gameSession.getResult();

        try {
            final int score = accountService.updateSScore(gameSession.getUserId(), result);
            remotePointService.sendMessageToUser(gameSession.getPlayer().getId(), new FinishGame(result, score));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getPlayer().getUser().getLogin()), ex);
        }
    }
}
