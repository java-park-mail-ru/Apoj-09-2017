package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.snapshots.ServerSnap;
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

    public void sendSnapshotsFor(@NotNull MultiGameSession gameSession, @NotNull byte[] data) {
        final String status = gameSession.getStatus();
        final ServerSnap snap = new ServerSnap(gameSession.getStatus());
        snap.setData(data);
        try {
            if (status.equals(Config.STEP_1)) {
                remotePointService.sendMessageToUser(gameSession.getSingerId(), snap);
            }
            if (status.equals(Config.STEP_1_5)) {
                remotePointService.sendMessageToUser(gameSession.getListenerId(), snap);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }

    public void sendSnapshotsFor(@NotNull SingleGameSession gameSession, @NotNull byte[] data) {
        final ServerSnap snap = new ServerSnap(gameSession.getStatus());
        snap.setData(data);
        try {
            remotePointService.sendMessageToUser(gameSession.getUserId(), snap);
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }
}
