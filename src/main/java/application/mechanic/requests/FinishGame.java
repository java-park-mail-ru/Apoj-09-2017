package application.mechanic.requests;

import application.mechanic.Config;
import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("MissortedModifiers")
public class FinishGame extends Message {
    @NotNull
    private String type;
    private boolean result;
    private int score;

    public FinishGame(boolean result, int score) {
        this.result = result;
        this.type = Config.FINAL_STEP;
        this.score = score;
    }

    public FinishGame() {
        this.result = false;
        this.type = Config.LEAVE;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
