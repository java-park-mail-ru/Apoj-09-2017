package application.mechanic.internal;

import application.mechanic.Config;
import application.mechanic.GameSession;
import application.mechanic.avatar.Player;
import application.mechanic.music.Music;
import application.mechanic.snapshots.ClientSnap;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("MissortedModifiers")
@Service
public class ClientSnapService {
    private final Map<Long, ClientSnap> snaps = new HashMap<>();
    private final Music music = new Music();

    public void pushClientSnap(@NotNull Long user, @NotNull ClientSnap snap) {
        this.snaps.putIfAbsent(user, snap);
    }

    @NotNull
    public ClientSnap getSnapForUser(@NotNull Long user) {
        return snaps.get(user);
    }

    public void processSnapshotsFor(@NotNull GameSession gameSession) {
        if (gameSession.getMode().equals(Config.SINGLE_MODE)) {
            singleProcess(gameSession);
        } else {
            multiProcess(gameSession);
        }
    }

    public void singleProcess(@NotNull GameSession gameSession) {
        final Player player = gameSession.getFirst();
        final ClientSnap playerSnap = getSnapForUser(player.getId());
        final String status = playerSnap.getStatus();
        if(status.equals(Config.WAITING_SONG_STATUS)){
            gameSession.setSongName(music.getSongName());
            gameSession.setData(music.getSong(gameSession.getSongName()));
            gameSession.setFirstStatus(Config.WAITING_SONG_STATUS);
            return;
        }
        if(status.equals(Config.RECORDING_STATUS)){
            gameSession.setFirstStatus(Config.RECORDING_STATUS);
            return;
        }
        if(status.equals(Config.WAITING_RECORD_STATUS)){
            gameSession.setData(music.reverseRecord(playerSnap.getSong()));
            gameSession.setFirstStatus(Config.WAITING_RECORD_STATUS);
            return;
        }
        if(status.equals(Config.FINISH_STATUS)){
            gameSession.setResult(gameSession.getSongName().equals(playerSnap.getAnswer()));
            gameSession.setFirstStatus(Config.FINISH_STATUS);
        }
    }

    public void multiProcess(@NotNull GameSession gameSession) {

    }

    public void clearForUser(Long userProfileId) {
        snaps.remove(userProfileId);
    }

    public void reset() {
        snaps.clear();
    }
}
