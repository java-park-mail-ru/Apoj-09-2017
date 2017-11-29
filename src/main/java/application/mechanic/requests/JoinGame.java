package application.mechanic.requests;

import application.websocket.Message;

public class JoinGame {
    public static class Request extends Message {
        private String mode;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}

