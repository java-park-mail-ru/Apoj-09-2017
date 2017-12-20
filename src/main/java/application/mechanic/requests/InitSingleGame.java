package application.mechanic.requests;

import application.mechanic.Config;
import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

public class InitSingleGame {
    public static final class Request extends Message {
        private String type;
        private String data;

        public Request(@NotNull String data) {
            this.type = Config.Step.RECORDING.toString();
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public String getData() {
            return data;
        }
    }
}
