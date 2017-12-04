package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.music.Music;
import application.mechanic.snapshots.SingleServerSnap;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;


@SuppressWarnings("MissortedModifiers")
@Service
public class ServerSnapService {
    @NotNull
    private final RemotePointService remotePointService;

    private final Music music = new Music();

    public ServerSnapService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull MultiGameSession gameSession) {

    }

    public void sendSnapshotsFor(@NotNull SingleGameSession gameSession, @Nullable byte[] data) {
        final String status = gameSession.getStatus();
        final SingleServerSnap snap = new SingleServerSnap(status);
        switch (status) {
            case Config.STEP_3:
                snap.setData(data);
                break;
            case Config.STEP_5:
                snap.setData(data);
                break;
            case Config.STEP_7:
                break;
            default:
                throw new RuntimeException("Server error");
        }
        try {
            remotePointService.sendMessageToUser(gameSession.getUserId(), snap);
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }
    }
}
