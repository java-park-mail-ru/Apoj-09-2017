package application.mechanic;

import application.mechanic.avatar.Player;
import application.mechanic.internal.GameSessionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLong;

public class SingleGameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Long sessionId;
    @NotNull
    private final Player singer;
    @NotNull
    private String songName;
    @NotNull
    private Config.Step status;
    @NotNull
    private final GameSessionService gameSessionService;
    private boolean result = false;

    public SingleGameSession(@NotNull Player singer,
                             @NotNull String songName,
                             @NotNull Config.Step status,
                             @NotNull GameSessionService gameSessionService) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.singer = singer;
        this.songName = songName;
        this.status = status;
        this.gameSessionService = gameSessionService;
        singer.setRole(Config.Role.SINGER);
    }

    @NotNull
    public Player getSinger() {
        return singer;
    }

    public long getId() {
        return sessionId;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final SingleGameSession other = (SingleGameSession) object;

        return sessionId.equals(other.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    public boolean tryFinishGame() {
        if (status == Config.Step.RESULT) {
            gameSessionService.finishSingleGame(this);
            return true;
        }
        return false;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @NotNull
    public String getSongName() {
        return songName;
    }

    public long getSingerId() {
        return singer.getId();
    }

    @NotNull
    public Config.Step getStatus() {
        return status;
    }

    public void setStatus(@NotNull Config.Step status) {
        this.status = status;
    }

    @NotNull
    public GameSessionService getGameSessionService() {
        return gameSessionService;
    }
}
