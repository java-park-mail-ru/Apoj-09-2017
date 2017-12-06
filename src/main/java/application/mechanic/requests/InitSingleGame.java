package application.mechanic.requests;

import application.websocket.Message;
import org.jetbrains.annotations.Nullable;

public class InitSingleGame {
    public static final class Request extends Message {
        private String type;
        private String data;

        public Request(String type, @Nullable String data) {
            this.type = type;
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
