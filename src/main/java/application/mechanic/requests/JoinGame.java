package application.mechanic.requests;

import application.websocket.Message;

public class JoinGame {
    public static class Request extends Message {
        private String mode;

        public Request(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }
    }
}

