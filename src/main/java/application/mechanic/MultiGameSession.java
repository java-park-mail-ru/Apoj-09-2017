package application.mechanic;

import application.mechanic.avatar.Player;
import application.mechanic.internal.GameSessionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("MissortedModifiers")
public class MultiGameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;
    @NotNull
    private final Long sessionId;
    @NotNull
    private final Player singer;
    @NotNull
    private Player listener;
    @NotNull
    private String songName;
    @NotNull
    private String status;
    @NotNull
    private final GameSessionService gameSessionService;
    private boolean result = false;


    public MultiGameSession(@NotNull Player singer, @NotNull Player listener, @NotNull String songName, @NotNull String status, @NotNull GameSessionService gameSessionService) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.singer = singer;
        this.listener = listener;
        this.gameSessionService = gameSessionService;
        this.isFinished = false;
        this.songName = songName;
        this.status = status;
        singer.setRole(Config.SINGER_ROLE);
        listener.setRole(Config.LISTENER_ROLE);
    }

    @NotNull
    public Player getSinger() {
        return singer;
    }

    @NotNull
    public Player getListener() {
        return listener;
    }

    @NotNull
    public List<Player> getPlayers() {
        return Arrays.asList(singer, listener);
    }

    public long getId() {
        return sessionId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MultiGameSession other = (MultiGameSession) o;

        return sessionId.equals(other.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }

    public boolean tryFinishGame() {
        if (status.equals(Config.STEP_7)) {
            gameSessionService.finishMultiGame(this);
            isFinished = true;
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

    public void setSongName(@NotNull String songName) {
        this.songName = songName;
    }

    @NotNull
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
