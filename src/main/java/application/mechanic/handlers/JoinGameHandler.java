package application.mechanic.handlers;

import application.mechanic.GameMechanics;
import application.mechanic.requests.JoinGame;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.Request.class, this);
    }

    @Override
    public void handle(@NotNull JoinGame.Request message, @NotNull Long forUser) {
        gameMechanics.addUser(forUser, message.getMode());
    }
}
