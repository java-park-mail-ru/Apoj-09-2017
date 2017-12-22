package application.mechanic;

import application.mechanic.avatar.Player;
import application.mechanic.internal.GameSessionService;
import org.jetbrains.annotations.NotNull;

public class MultiGameSession extends SingleGameSession {
    private final Player listener;

    public MultiGameSession(@NotNull Player singer,
                            @NotNull Player listener,
                            @NotNull String songName,
                            @NotNull Config.Step step,
                            @NotNull GameSessionService gameSessionService) {
        super(singer, songName, step, gameSessionService);
        this.listener = listener;
        listener.setRole(Config.Role.LISTENER);
    }

    @NotNull
    public Player getListener() {
        return listener;
    }

    @Override
    public boolean tryFinishGame() {
        if (this.getStatus() == Config.Step.RESULT) {
            this.getGameSessionService().finishMultiGame(this);
            return true;
        }
        return false;
    }

    public long getListenerId() {
        return listener.getId();
    }
}
