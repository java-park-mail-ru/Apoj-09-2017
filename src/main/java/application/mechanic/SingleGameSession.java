package application.mechanic;

import application.mechanic.avatar.Player;
import application.mechanic.internal.GameSessionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("MissortedModifiers")
public class SingleGameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Long sessionId;
    @NotNull
    private final Player player;
    @NotNull
    private String songName;
    @NotNull
    private String status;
    @NotNull
    private final GameSessionService gameSessionService;
    private boolean result = false;

    public SingleGameSession(@NotNull Player player,
                             @NotNull String songName,
                             @NotNull String status,
                             @NotNull GameSessionService gameSessionService) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.player = player;
        this.songName = songName;
        this.status = status;
        this.gameSessionService = gameSessionService;
    }

    @NotNull
    public Player getPlayer() {
        return player;
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
        if (status.equals(Config.FINAL_STEP)) {
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

    public long getUserId() {
        return player.getId();
    }

    @NotNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NotNull String status) {
        this.status = status;
    }
}
