package application.websocket;

import org.jetbrains.annotations.NotNull;

public class HandleException extends Exception {
    public HandleException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public HandleException(@NotNull String message) {
        super(message);
    }
}