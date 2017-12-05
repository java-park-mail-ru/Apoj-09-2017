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

    @NotNull
    private final Base64.Encoder encoder = Base64.getEncoder();

    @NotNull
    private final Base64.Decoder decoder = Base64.getDecoder();

    private final Map<Long, ClientSnap> snaps = new HashMap<>();
    private final Music music;

    public ClientSnapService(@NotNull ServerSnapService serverSnapshotService, Music music) {
        this.serverSnapshotService = serverSnapshotService;
        this.music = music;
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
                final byte[] data = music.reverseRecord(decoder.decode(snap.getData()));
                if (gameSession.getStatus().equals(Config.STEP_1) && data != null) {
                    gameSession.setStatus(Config.STEP_2);
                    serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encode(data));
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
