package application.mechanic.requests;

import application.websocket.Message;

public class InitGame {
    public static final class Request extends Message {
        private String opponent;

        public Request(String opponent) {
            this.opponent = opponent;
        }

        public String getOpponent() {
            return opponent;
        }

        public void setOpponent(String opponent) {
            this.opponent = opponent;
        }
    }
}
