package application.mechanic;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("MissortedModifiers")
@Service
public class MechanicsExecutor {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(MechanicsExecutor.class);
    private static final long STEP_TIME = 500;

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

        private final Clock clock = Clock.systemDefaultZone();

        @Override
        public void run() {
            while (true) {
                try {
                    final long before = clock.millis();

                    gameMechanics.gmStep();

                    final long after = clock.millis();
                    try {
                        final long sleepingTime = Math.max(0, STEP_TIME - (after - before));
                        Thread.sleep(sleepingTime);
                    } catch (InterruptedException e) {
                        LOGGER.error("Mechanics thread was interrupted", e);
                    }

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
