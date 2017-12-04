package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.music.Music;
import application.mechanic.snapshots.ClientSnap;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("MissortedModifiers")
@Service
public class ClientSnapService {
    @NotNull
    private final ServerSnapService serverSnapshotService;

    private final Map<Long, ClientSnap> snaps = new HashMap<>();
    private final Music music = new Music();

    public ClientSnapService(@NotNull ServerSnapService serverSnapshotService) {
        this.serverSnapshotService = serverSnapshotService;
    }

    public void pushClientSnap(@NotNull Long user, @NotNull ClientSnap snap) {
        this.snaps.putIfAbsent(user, snap);
    }

    @NotNull
    public ClientSnap getSnapForUser(@NotNull Long user) {
        return snaps.get(user);
    }

    public void processSnapshotsFor(@NotNull MultiGameSession gameSession) {

    }

    public void processSnapshotsFor(@NotNull SingleGameSession gameSession) {
        final ClientSnap snap = getSnapForUser(gameSession.getUserId());
        switch (snap.getType()) {
            case Config.STEP_1:
                if (gameSession.getStatus().equals(Config.STEP_1)) {
                    gameSession.setStatus(Config.STEP_2);
                    serverSnapshotService.sendSnapshotsFor(gameSession, music.reverseRecord(snap.getData()));
                } else {
                    throw new RuntimeException("Server error");
                }
                break;
            case Config.STEP_2:
                if (gameSession.getStatus().equals(Config.STEP_2)) {
                    gameSession.setStatus(Config.STEP_3);
                    gameSession.setResult(snap.getAnswer().toLowerCase().equals(gameSession.getSongName().toLowerCase()));
                } else {
                    throw new RuntimeException("Server error");
                }
                break;
            default:
                throw new RuntimeException("Server error");
        }
    }

    public void clearForUser(Long userProfileId) {
        snaps.remove(userProfileId);
    }

    public void reset() {
        snaps.clear();
    }
}
