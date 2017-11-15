package application.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameMessageHandlerContainer implements MessageHandlerContainer {

    private final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    @Override
    public void handle(@NotNull Message message, @NotNull Long forUser) throws HandleException {
        final Class clazz;
        try {
            clazz = Class.forName(message.getType());
        } catch (ClassNotFoundException e) {
            throw new HandleException("Can't handle message of " + message.getType() + " type", e);
        }
        final MessageHandler<?> messageHandler = handlerMap.get(clazz);
        if (messageHandler == null) {
            throw new HandleException("No handler for message of " + message.getType() + " type");
        }
        messageHandler.handleMessage(message, forUser);
    }

    @Override
    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, @NotNull MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }

}
