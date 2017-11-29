package application.mechanic.music;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Vector;

public class Music {
    //ToDo: сделать БД с возможностью добавления песен
    private final Vector<String> playList = new Vector<>();
    private final Random random = new Random();

    public Music() {
        playList.add("badtrip.mp3");
        playList.add("Владимирский_централ.mp3");
    }

    public @Nullable byte[] getSong(String name) {
        try {
            final Path path = Paths.get("/src/main/resources/music/" + name);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }

    public @NotNull String getSongName() {
        final int index = random.nextInt(playList.capacity() - 1);
        return playList.get(index);
    }

    public @NotNull byte[] reverseRecord(@Nullable byte[] record) {
        final int length = record.length;
        for (int i = 0; i < length / 2; ++i) {
            final byte tmp = record[i];
            record[i] = record[length - i - 1];
            record[length - i -1] = tmp;
        }
        return record;
    }
}
