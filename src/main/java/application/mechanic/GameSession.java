package application.mechanic;

import application.mechanic.avatar.Player;
import application.mechanic.internal.GameSessionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("MissortedModifiers")
public class GameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;
    @NotNull
    private final Long sessionId;
    @NotNull
    private final Player firstPlayer;
    @Nullable
    private Player secondPlayer;
    @NotNull
    private final String mode;
    @NotNull
    private final GameSessionService gameSessionService;
    private boolean result = false;
    private String songName;
    private byte[] data;

    public GameSession(@NotNull Player firstPlayer, @NotNull Player secondPlayer, @NotNull String mode, @NotNull GameSessionService gameSessionService) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gameSessionService = gameSessionService;
        this.mode = mode;
        this.isFinished = false;
        if(mode.equals(Config.MULTI_MODE)){
            firstPlayer.setRole(Config.SINGER_ROLE);
            secondPlayer.setRole(Config.LISTENER_ROLE);
        }
    }

    public GameSession(@NotNull Player firstPlayer, @NotNull String mode, @NotNull GameSessionService gameSessionService) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = null;
        this.gameSessionService = gameSessionService;
        this.mode = mode;
    }

    @NotNull
    public Player getFirst() {
        return firstPlayer;
    }

    @Nullable
    public Player getSecond() {
        return secondPlayer;
    }

    @NotNull
    public List<Player> getPlayers() {
        return Arrays.asList(firstPlayer, secondPlayer);
    }

    public long getId() {
        return sessionId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession other = (GameSession) o;

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
        if (firstPlayer.getStatus().equals(Config.FINISH_STATUS)) {
            gameSessionService.finishGame(this);
            isFinished = true;
            return true;
        }
        return false;
    }

    public String getMode() {
        return mode;
    }

    public void setFirstStatus(String status){
        firstPlayer.setStatus(status);
    }

    public void setSecondStatus(String status){
        secondPlayer.setStatus(status);
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(@Nullable byte[] data) {
        this.data = data;
    }
}
