package application.mechanic.internal;

import application.mechanic.GameSession;
import application.websocket.RemotePointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@SuppressWarnings("MissortedModifiers")
@Service
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

    }
}
