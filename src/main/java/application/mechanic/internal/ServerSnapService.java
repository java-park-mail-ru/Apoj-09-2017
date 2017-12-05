package application.mechanic.internal;

import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.snapshots.SingleServerSnap;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;


@SuppressWarnings("MissortedModifiers")
@Service
public class ServerSnapService {
    @NotNull
    private final RemotePointService remotePointService;

    public ServerSnapService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull MultiGameSession gameSession) {

    }

    public void sendSnapshotsFor(@NotNull SingleGameSession gameSession, @NotNull byte[] data) {
        final String status = gameSession.getStatus();
        final SingleServerSnap snap = new SingleServerSnap(status);
        snap.setData(data);
        try {
            remotePointService.sendMessageToUser(gameSession.getUserId(), snap);
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }
}
