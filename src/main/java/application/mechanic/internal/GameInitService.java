package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.avatar.Player;
import application.mechanic.music.Music;
import application.mechanic.requests.InitMultiGame;
import application.mechanic.requests.InitSingleGame;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.Base64;

@SuppressWarnings("MissortedModifiers")
@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapService.class);

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final ServerSnapService serverSnapService;

    @NotNull
    private final Base64.Encoder encoder = Base64.getEncoder();

    @NotNull
    private final Music music;

    public GameInitService(@NotNull RemotePointService remotePointService,
                           @NotNull ServerSnapService serverSnapService,
                           @NotNull Music music) {
        this.remotePointService = remotePointService;
        this.serverSnapService = serverSnapService;
        this.music = music;
    }

    public void initGameFor(@NotNull SingleGameSession gameSession) {
        final Player player = gameSession.getPlayer();
        final byte[] data = music.getSong(gameSession.getSongName());
        if (data != null) {
            final InitSingleGame.Request initMessage = new InitSingleGame.Request(Config.STEP_1, encoder.encodeToString(data));
            try {
                remotePointService.sendMessageToUser(player.getId(), initMessage);
            } catch (IOException e) {
                remotePointService.cutDownConnection(player.getId(), CloseStatus.SERVER_ERROR);
                LOGGER.error("Unnable to start a game", e);
            }
        } else {
            remotePointService.cutDownConnection(player.getId(), CloseStatus.SERVER_ERROR);
            LOGGER.error("Music service error");
        }
    }

    public void initGameFor(@NotNull MultiGameSession gameSession) {
        final Player singer = gameSession.getSinger();
        final Player listener = gameSession.getListener();
        final InitMultiGame.Request initSinger = new InitMultiGame.Request(Config.SINGER_ROLE, listener.getUser().getLogin());
        final InitMultiGame.Request initListener = new InitMultiGame.Request(Config.LISTENER_ROLE, singer.getUser().getLogin());
        final byte[] data = music.reverseRecord(music.getSong(gameSession.getSongName()));
        if (data != null) {
            try {
                remotePointService.sendMessageToUser(singer.getId(), initSinger);
                remotePointService.sendMessageToUser(listener.getId(), initListener);
                serverSnapService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
            } catch (IOException e) {
                remotePointService.cutDownConnection(singer.getId(), CloseStatus.SERVER_ERROR);
                remotePointService.cutDownConnection(listener.getId(), CloseStatus.SERVER_ERROR);
                LOGGER.error("Unnable to start a game", e);
            }
        } else {
            remotePointService.cutDownConnection(singer.getId(), CloseStatus.SERVER_ERROR);
            remotePointService.cutDownConnection(listener.getId(), CloseStatus.SERVER_ERROR);
            LOGGER.error("Music service error");
        }
    }

}
