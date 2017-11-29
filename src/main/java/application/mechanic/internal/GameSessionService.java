package application.mechanic.internal;

import application.models.User;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import application.mechanic.GameSession;

@SuppressWarnings("MissortedModifiers")
public class GameSessionService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class.getSimpleName());

    @NotNull
    private final Map<Long, GameSession> usersMap = new ConcurrentHashMap<>();
    @NotNull
    private final Set<GameSession> gameSessions = new LinkedHashSet<>();
    @NotNull
    private final RemotePointService remotePointService;

    public GameSessionService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    @NotNull
    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    public boolean hasFreeSlots() {
        for (GameSession session : gameSessions) {
            if (!session.isFull()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public GameSession getSessionForUser(long userId) {
        return usersMap.get(userId);
    }

    public boolean isPlaying(long userId) {
        return usersMap.containsKey(userId);
    }

    public void addNewPlayer(@NotNull User user) {
        for (GameSession session : gameSessions) {
            if (!session.isFull()) {
                session.addPlayer(user);
                usersMap.put(user.getId(), session);
                LOGGER.info("Added player #{} to room #{} (players here: {})", user.getId(), session.getId(), 1);
                return;
            }
        }

        final GameSession newSession = new GameSession();
        newSession.addPlayer(user);
        gameSessions.add(newSession);
        usersMap.put(user.getId(), newSession);
        LOGGER.info("Started new room #{}, total rooms: {}", newSession.getId(), gameSessions.size());
    }


    public void notifyGameIsOver(@NotNull GameSession gameSession, @NotNull CloseStatus closeStatus) {
        final boolean exists = gameSessions.remove(gameSession);
        final User player = gameSession.getPlayers();
        usersMap.remove(player.getId());
        if (exists) {
            remotePointService.cutDownConnection(player.getId(), closeStatus);
        }
        LOGGER.info("Game #{} is over, total rooms: {}", gameSession.getId(), gameSessions.size());
    }
}
