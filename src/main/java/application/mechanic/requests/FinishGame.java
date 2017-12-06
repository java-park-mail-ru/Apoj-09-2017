package application.mechanic.requests;

import application.mechanic.Config;
import application.websocket.Message;

public class FinishGame extends Message {
    private String type = Config.FINAL_STEP;
    private boolean result;

    public FinishGame(boolean result) {
        this.result = result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }
}
