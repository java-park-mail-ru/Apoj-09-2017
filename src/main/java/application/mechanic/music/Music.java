package application.mechanic.music;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

@Service
public class Music {
    private static final Logger LOGGER = LoggerFactory.getLogger(Music.class);
    private final ArrayList<String> playList = new ArrayList<>();
    private final Random random = new Random();
    private static final int SIZE = 16384;

    public Music() {
        playList.add("badtrip");
        playList.add("А мы не ангелы");
        playList.add("Белые розы");
        playList.add("В Питере пить");
        playList.add("Звезда по имени солнце");
        playList.add("Капитал");
        playList.add("Стрекоза любви");
        playList.add("Три полоски");
        playList.add("Я верю");
        playList.add("Я свободен");
        playList.add("Я солдат");
    }

    @NotNull
    public byte[] getSong(String name) {
        final ClassLoader cl = this.getClass().getClassLoader();
        try (InputStream is = cl.getResourceAsStream("music/" + name + ".wav");
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int readed;
            final byte[] data = new byte[SIZE];
            while ((readed = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, readed);
            }
            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Music Error", e);
        }
    }

    @NotNull
    public String getSongName() {
        final int index = random.nextInt(playList.size());
        return playList.get(index);
    }

    @NotNull
    public byte[] reverseRecord(@NotNull byte[] record) {
        try {
            final AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(record));
            final int frameSize = stream.getFormat().getFrameSize();
            final byte[][] frames = new byte[stream.available() / frameSize][frameSize];
            for (int i = 0; i < frames.length; i++) {
                final byte[] frame = new byte[frameSize];
                final int numBytes = stream.read(frame, 0, frameSize);
                if (numBytes == -1) {
                    break;
                }
                frames[frames.length - i - 1] = frame;
            }
            final byte[] result = new byte[record.length];
            final int headerSize = record.length - frames.length * frameSize;
            System.arraycopy(record, 0, result, 0, headerSize);
            for (int i = 0; i < frames.length; i++) {
                for (int j = 0; j < frameSize; j++) {
                    result[i * frameSize + j + headerSize] = frames[i][j];
                }
            }
            return result;
        } catch (UnsupportedAudioFileException | IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("Music Error", e);
        }
    }

}



