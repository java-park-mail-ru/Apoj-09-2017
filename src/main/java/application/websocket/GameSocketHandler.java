package application.websocket;

import application.mechanic.requests.JoinGame;
import application.mechanic.snapshots.ClientSnap;
import application.models.User;
import application.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@SuppressWarnings("MissortedModifiers")
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");
    private static final int MESSAGE_SIZE = 5000000;

    @NotNull
    private AccountService accountService;
    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;
    @NotNull
    private final RemotePointService remotePointService;

    private final ObjectMapper objectMapper;


    public GameSocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                             @NotNull AccountService authService,
                             @NotNull RemotePointService remotePointService,
                             ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.accountService = authService;
        this.remotePointService = remotePointService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final Long id = (Long) webSocketSession.getAttributes().get("userId");
        if (id == null || (accountService.getUser(id)) == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in. Openning websocket session is denied.");
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        remotePointService.registerUser(id, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (webSocketSession.getTextMessageSizeLimit() != MESSAGE_SIZE) {
            webSocketSession.setTextMessageSizeLimit(MESSAGE_SIZE);
        }
        if (!webSocketSession.isOpen()) {
            return;
        }
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        final User user = accountService.getUser(userId);
        if (user == null) {
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        handleMessage(user, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(User userProfile, TextMessage text) {
        final Message message;
        try {
            final ObjectNode node = objectMapper.readValue(text.getPayload(), ObjectNode.class);
            if (node.has("mode")) {
                message = new JoinGame.Request(node.get("mode").asText());
            } else {
                message = new ClientSnap(node.get("type").asText(), node.get("data").asText());
            }
        } catch (IOException ex) {
            LOGGER.error("wrong json format at mechanic response", ex);
            return;
        }
        try {
            messageHandlerContainer.handle(message, userProfile.getId());
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getClass().getName() + " with content: " + text, e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        if (userId == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        remotePointService.removeUser(userId);
    }

    private void closeSessionSilently(@NotNull WebSocketSession session, @Nullable CloseStatus closeStatus) {
        final CloseStatus status;
        if (closeStatus == null) {
            status = SERVER_ERROR;
        } else {
            status = closeStatus;
        }
        try {
            session.close(status);
        } catch (IOException ignore) {
            ignore.fillInStackTrace();
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
