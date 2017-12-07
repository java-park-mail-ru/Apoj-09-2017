package application.mechanic;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("MissortedModifiers")
@Service
public class MechanicsExecutor {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(MechanicsExecutor.class);
    private Executor tickExecutor = Executors.newSingleThreadExecutor();
    private final GameMechanics mechanic;

    @Autowired
    public MechanicsExecutor(GameMechanics mechanic) {
        this.mechanic = mechanic;
    }


    @PostConstruct
    public void initAfterStartup() {
        final Runnable worker = new Worker(mechanic);
        tickExecutor.execute(worker);
    }


    private static class Worker implements Runnable {

        private final GameMechanics gameMechanics;

        Worker(GameMechanics gameMechanics) {
            this.gameMechanics = gameMechanics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    gameMechanics.gmStep();
                    if (Thread.currentThread().isInterrupted()) {
                        gameMechanics.reset();
                        return;
                    }
                } catch (RuntimeException e) {
                    LOGGER.error("Mechanics executor was reseted due to exception", e);
                    gameMechanics.reset();
                }
            }
        }
    }

}
