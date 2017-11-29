package application.mechanic.snapshots;

import application.mechanic.avatar.Player;
import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("MissortedModifiers")
public class ServerSnap extends Message{
    @NotNull
    private List<Player> players;
    @NotNull
    private byte[] data;

    @NotNull
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(@NotNull List<Player> players) {
        this.players = players;
    }

    @NotNull
    public byte[] getData() {
        return data;
    }

    public void setData(@NotNull byte[] data) {
        this.data = data;
    }
}
