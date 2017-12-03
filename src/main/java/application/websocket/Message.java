package application.websocket;


import application.mechanic.requests.FinishGame;
import application.mechanic.requests.JoinGame;
import application.mechanic.snapshots.ClientSnap;

import application.mechanic.snapshots.MultiServerSnap;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(JoinGame.Request.class),
        @Type(FinishGame.class),
        @Type(ClientSnap.class),
        @Type(MultiServerSnap.class),
})
public abstract class Message {
}
