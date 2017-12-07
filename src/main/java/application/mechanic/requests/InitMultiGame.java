package application.mechanic.requests;

import application.mechanic.Config;
import application.websocket.Message;

public class InitMultiGame {
    public static final class Request extends Message {
        private String type = Config.STEP_0;
        private String role;
        private String secondUser;

        public Request(String role, String secondUser) {
            this.role = role;
            this.secondUser = secondUser;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
