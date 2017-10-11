package application.utils.responses;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class ValidatorResponse {
    @NotNull
    private final ArrayList<String> messageList;

    public ValidatorResponse(@NotNull ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    @NotNull
    public ArrayList<String> getMessage() {
        return messageList;
    }
}

