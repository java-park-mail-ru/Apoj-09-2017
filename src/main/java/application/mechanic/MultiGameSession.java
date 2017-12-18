package application.mechanic;

import application.mechanic.avatar.Player;
import application.mechanic.internal.GameSessionService;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public class MultiGameSession extends SingleGameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Long sessionId;
    @NotNull
    private final Player singer;
    @NotNull
    private final Player listener;
    @NotNull
    private String songName;
    @NotNull
    private Config.Step status;
    @NotNull
    private final GameSessionService gameSessionService;
    private boolean result = false;


    public MultiGameSession(@NotNull Player singer,
                            @NotNull Player listener,
                            @NotNull String songName,
                            @NotNull Config.Step step,
                            @NotNull GameSessionService gameSessionService) {
        super(singer, songName, step, gameSessionService);
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.singer = singer;
        this.listener = listener;
        this.gameSessionService = gameSessionService;
        this.songName = songName;
        this.status = step;
        singer.setRole(Config.Role.SINGER);
        listener.setRole(Config.Role.LISTENER);
    }

    @NotNull
    public Player getListener() {
        return listener;
    }

    @Override
    public boolean tryFinishGame() {
        if (status == Config.Step.RESULT) {
            gameSessionService.finishMultiGame(this);
            return true;
        }
        return false;
    }

    public long getSingerId() {
        return singer.getId();
    }

    public long getListenerId() {
        return listener.getId();
    }
}
