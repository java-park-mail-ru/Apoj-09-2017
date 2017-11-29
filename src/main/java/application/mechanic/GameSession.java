package application.mechanic;

import application.mechanic.avatar.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("MissortedModifiers")
public class GameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Long sessionId;
    @NotNull
    private final Set<Player> players = new HashSet<>();
    private String mode = null;


    public GameSession() {
        this.sessionId = ID_GENERATOR.getAndIncrement();
    }

    public boolean isFull() {
        if(mode.equals("Single")) {
            return players.size() == Config.SINGLE_ROOM_SIZE;
        } else {
            return players.size() == Config.MULTI_ROOM_SIZE;
        }
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void addPlayer(@NotNull Player newPlayer) {
        if (isFull()) {
            throw new RuntimeException("No more players for this session!");
        }
        if (mode == null){
            mode = newPlayer.getMode();
        }
        players.add(newPlayer);
    }

    public void removePlayer(long userId) {
        for (Player player: players) {
            if (player.getId() == userId) {
                players.remove(player);
                break;
            }
        }
    }

    @NotNull
    public Set<Player> getPlayers() {
        return players;
    }

    public long getId() {
        return sessionId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession other = (GameSession) o;

        return sessionId.equals(other.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}
