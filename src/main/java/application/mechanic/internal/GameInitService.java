package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.avatar.Player;
import application.mechanic.music.Music;
import application.mechanic.requests.InitSingleGame;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;

@SuppressWarnings("MissortedModifiers")
@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapService.class);

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final Music music = new Music();

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull SingleGameSession gameSession) {
        final Player player = gameSession.getPlayer();
        final InitSingleGame.Request initMessage = new InitSingleGame.Request(Config.STEP_1, music.getSong(gameSession.getSongName()));
        try {
            remotePointService.sendMessageToUser(player.getId(), initMessage);
        } catch (IOException e) {
            remotePointService.cutDownConnection(player.getId(), CloseStatus.SERVER_ERROR);
            LOGGER.error("Unnable to start a game", e);
        }
    }

    public void initGameFor(@NotNull MultiGameSession gameSession) {
//        final Player singer = gameSession.getSinger();
//        final Player listener = gameSession.getListener();
//        final InitSingleGame.Request initSinger = new InitSingleGame.Request(listener.getUser().getLogin());
//        final InitSingleGame.Request initListener = new InitSingleGame.Request(singer.getUser().getLogin());
//        try {
//            remotePointService.sendMessageToUser(singer.getId(), initSinger);
//            remotePointService.sendMessageToUser(listener.getId(), initListener);
//        } catch (IOException e) {
//            remotePointService.cutDownConnection(singer.getId(), CloseStatus.SERVER_ERROR);
//            remotePointService.cutDownConnection(listener.getId(), CloseStatus.SERVER_ERROR);
//            LOGGER.error("Unnable to start a game", e);
//        }
    }

}
