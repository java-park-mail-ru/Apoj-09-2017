package application.mechanic.internal;

import application.mechanic.GameSession;
import application.mechanic.avatar.Player;
import application.mechanic.snapshots.ServerPlayerSnap;
import application.websocket.Message;
import application.websocket.RemotePointService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("MissortedModifiers")
public class ServerSnapService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapService.class.getSimpleName());
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerSnapService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull GameSession gameSession) {
//        final Collection<Player> players = gameSession.getPlayers();
//        final List<ServerPlayerSnap> playersSnaps = players.stream()
//                .map(Player::generateSnap).collect(Collectors.toList());
//
//        if (playersSnaps.isEmpty()) {
//            throw new RuntimeException("No players snapshots");
//        }
//
//        final ServerPlayerSnap snap = new ServerPlayerSnap(playersSnaps);
//        try {
//            final Message message = new Message();
//            message.setType(Message.SNAPSHOT);
//            for (GameUser player : players) {
//                snap.setPlayer(player);
//
//                message.setData(objectMapper.writeValueAsString(snap));
//                remotePointService.sendMessageToUser(player.getId(), message);
//                player.resetForNextSnap();
//            }
//        } catch (JsonProcessingException e) {
//            LOGGER.error("Error processing JSON {}", snap.toString());
//        } catch (IOException e) {
//            LOGGER.error("Error sending server snap! {}", e.getMessage());
//        }
    }
}
