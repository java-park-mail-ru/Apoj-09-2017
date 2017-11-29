package application.mechanic.requests;

import application.websocket.Message;

public class FinishGame extends Message {
    private boolean result;

    public FinishGame(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }
}
