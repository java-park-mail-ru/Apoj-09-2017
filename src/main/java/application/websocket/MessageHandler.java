package application.websocket;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public abstract class MessageHandler<T extends Message> {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class.getSimpleName());
    @NotNull
    private final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(@NotNull Message message, long forUser) throws HandleException {
        try {
            try {
                final Object data = new ObjectMapper().readValue(message.getData(), clazz);
                handle(clazz.cast(data), forUser);
            } catch (JsonParseException e) {
                LOGGER.error("Can't parse message with data: " + message.getData() + " to class " + clazz, e);
            }
        } catch (IOException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type "
                    + message.getType()
                    + " with content: "
                    + message.getData(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Long forUser) throws HandleException;
}
