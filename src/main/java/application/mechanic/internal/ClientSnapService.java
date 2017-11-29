package application.mechanic.internal;

import application.mechanic.GameSession;
import application.mechanic.snapshots.ClientSnap;
import application.services.AccountService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings("MissortedModifiers")
public class ClientSnapService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnapService.class.getSimpleName());

    @NotNull
    private final Map<Long, List<ClientSnap>> userToSnaps = new HashMap<>();
    @NotNull
    private AccountService accountService;

    public ClientSnapService(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    public synchronized void pushClientSnap(long user, @NotNull ClientSnap snap) {
        final List<ClientSnap> userSnaps = userToSnaps.computeIfAbsent(user, u -> new ArrayList<>());
        userSnaps.add(snap);
    }

    @Nullable
    public synchronized List<ClientSnap> getSnapsForUser(long user) {
        return userToSnaps.get(user);
    }

    public void processSnapshotsFor(GameSession gameSession) {

    }

    public void clear() {
        userToSnaps.clear();
    }
}
