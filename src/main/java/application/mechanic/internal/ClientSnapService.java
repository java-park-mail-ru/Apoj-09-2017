package application.mechanic.internal;

import application.mechanic.MultiGameSession;
import application.mechanic.SingleGameSession;
import application.mechanic.music.Music;
import application.mechanic.snapshots.ClientSnap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

import static application.mechanic.Config.*;

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
        this.snaps.put(user, snap);
    }

    @Nullable
    public ClientSnap getSnapForUser(@NotNull Long user) {
        return snaps.get(user);
    }

    public void processSnapshotsFor(@NotNull MultiGameSession gameSession) {
        final Step step = gameSession.getStatus();
        if (step == Step.RECORDING) {
            final ClientSnap snap = getSnapForUser(gameSession.getSingerId());
            if (snap != null) {
                final byte[] data = music.reverseRecord(decoder.decode(snap.getData().substring(22)));
                if (Step.valueOf(snap.getType()) == Step.RECORDING) {
                    gameSession.setStatus(Step.SECOND_RECORDING);
                    serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
                } else {
                    throw new RuntimeException("Server error");
                }
            }
        }
        if (step == Step.SECOND_RECORDING) {
            final ClientSnap snap = getSnapForUser(gameSession.getListenerId());
            if (snap != null) {
                final byte[] data = music.reverseRecord(decoder.decode(snap.getData().substring(22)));
                if (Step.valueOf(snap.getType()) == Step.SECOND_RECORDING) {
                    gameSession.setStatus(Step.LISTENING);
                    serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
                } else {
                    throw new RuntimeException("Server error");
                }
            }
        }
        if (step == Step.LISTENING) {
            final ClientSnap snap = getSnapForUser(gameSession.getListenerId());
            if (snap != null) {
                if (Step.valueOf(snap.getType()) == Step.LISTENING) {
                    gameSession.setStatus(Step.RESULT);
                    gameSession.setResult(snap.getData().toLowerCase().equals(gameSession.getSongName().toLowerCase()));
                } else {
                    throw new RuntimeException("Server error");
                }
            }
        }
    }

    public void processSnapshotsFor(@NotNull SingleGameSession gameSession) {
        final ClientSnap snap = getSnapForUser(gameSession.getSingerId());
        if (snap != null) {
            final Step step = gameSession.getStatus();
            if (step == Step.RECORDING) {
                final byte[] data = music.reverseRecord(decoder.decode(snap.getData().substring(22)));
                if (Step.valueOf(snap.getType()) == Step.RECORDING) {
                    gameSession.setStatus(Step.LISTENING);
                    serverSnapshotService.sendSnapshotsFor(gameSession, encoder.encodeToString(data));
                } else {
                    throw new RuntimeException("Server error");
                }
            }
            if (step == Step.LISTENING) {
                if (Step.valueOf(snap.getType()) == Step.LISTENING) {
                    gameSession.setStatus(Step.RESULT);
                    gameSession.setResult(snap.getData().toLowerCase().equals(gameSession.getSongName().toLowerCase()));
                } else {
                    throw new RuntimeException("Server error");
                }
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
