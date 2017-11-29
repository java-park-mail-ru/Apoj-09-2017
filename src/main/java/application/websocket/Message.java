package application.websocket;


import application.mechanic.requests.Disconnect;
import application.mechanic.requests.JoinGame;
import application.mechanic.snapshots.ClientSnap;

import application.mechanic.snapshots.ServerSnap;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(JoinGame.Request.class),
        @Type(Disconnect.Request.class),
        @Type(ClientSnap.class),
        @Type(ServerSnap.class),
})
public abstract class Message {
}
