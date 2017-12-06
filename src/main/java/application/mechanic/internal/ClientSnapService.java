package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.music.Music;
import application.mechanic.snapshots.ClientSnap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Nullable
    public ClientSnap getSnapForUser(@NotNull Long user) {
        return snaps.get(user);
    }

    public void processSnapshotsFor(@NotNull MultiGameSession gameSession) {
        final String status = gameSession.getStatus();
        if (status.equals(Config.STEP_1)) {
            final ClientSnap snap = getSnapForUser(gameSession.getSingerId());
            final byte[] data = music.reverseRecord(decoder.decode(snap.getData()));
            if (snap.getType().equals(Config.STEP_1) && data != null) {
                serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
                gameSession.setStatus(Config.STEP_1_5);
            } else {
                throw new RuntimeException("Server error");
            }
        }
        if (status.equals(Config.STEP_1_5)) {
            final ClientSnap snap = getSnapForUser(gameSession.getListenerId());
            final byte[] data = music.reverseRecord(decoder.decode(snap.getData()));
            if (snap.getType().equals(Config.STEP_1_5) && data != null) {
                serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
                gameSession.setStatus(Config.STEP_2);
            } else {
                throw new RuntimeException("Server error");
            }
        }
        if (status.equals(Config.STEP_2)) {
            final ClientSnap snap = getSnapForUser(gameSession.getListenerId());
            final byte[] data = music.reverseRecord(decoder.decode(snap.getData()));
            if (snap.getType().equals(Config.STEP_2) && data != null) {
                gameSession.setStatus(Config.FINAL_STEP);
                gameSession.setResult(snap.getData().toLowerCase().equals(gameSession.getSongName().toLowerCase()));
            } else {
                throw new RuntimeException("Server error");
            }
        }
    }

    public void processSnapshotsFor(@NotNull SingleGameSession gameSession) {
        final ClientSnap snap = getSnapForUser(gameSession.getUserId());
        snaps.remove(gameSession.getUserId());
        if (snap != null) {
            switch (snap.getType()) {
                case Config.STEP_1:
                    final byte[] data = music.reverseRecord(decoder.decode(snap.getData().substring(22)));
                    if (gameSession.getStatus().equals(Config.STEP_1) && data != null) {
                        gameSession.setStatus(Config.STEP_2);
                        serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
                    } else {
                        throw new RuntimeException("Server error");
                    }
                    break;
                case Config.STEP_2:
                    if (gameSession.getStatus().equals(Config.STEP_2)) {
                        gameSession.setStatus(Config.FINAL_STEP);
                        gameSession.setResult(snap.getData().toLowerCase().equals(gameSession.getSongName().toLowerCase()));
                    } else {
                        throw new RuntimeException("Server error");
                    }
                    break;
                default:
                    throw new RuntimeException("Server error");
            }
        }
    }

    public void clearForUser(Long userProfileId) {
        snaps.remove(userProfileId);
    }

    public void reset() {
        snaps.clear();
    }
}
