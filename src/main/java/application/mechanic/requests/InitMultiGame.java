package application.mechanic.requests;

import application.mechanic.Config;
import application.websocket.Message;

public class InitMultiGame {
    public static final class Request extends Message {
        private String type = Config.Step.PRE_GAME_DATA.toString();
        private String role;
        private String secondUser;

        public Request(String role, String secondUser) {
            this.role = role;
            this.secondUser = secondUser;
        }

        public String getType() {
            return type;
        }

        public String getRole() {
            return role;
        }

        public String getSecondUser() {
            return secondUser;
        }
    }
}
