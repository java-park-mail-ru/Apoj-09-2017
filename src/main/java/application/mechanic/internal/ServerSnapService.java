package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.snapshots.ServerSnap;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ServerSnapService {
    @NotNull
    private final RemotePointService remotePointService;

    public ServerSnapService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull MultiGameSession gameSession, @NotNull String data) {
        final ServerSnap snap = new ServerSnap(gameSession.getStatus().toString(), data);
        try {
            if (!(gameSession.getStatus() == Config.Step.RECORDING)) {
                remotePointService.sendMessageToUser(gameSession.getListenerId(), snap);
            }
            remotePointService.sendMessageToUser(gameSession.getSingerId(), snap);
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }

    public void sendSnapshotsFor(@NotNull SingleGameSession gameSession, @NotNull String data) {
        final ServerSnap snap = new ServerSnap(gameSession.getStatus().toString(), data);
        try {
            remotePointService.sendMessageToUser(gameSession.getSingerId(), snap);
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }
}
