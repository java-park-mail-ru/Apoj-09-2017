package application.mechanic.music;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Vector;

@SuppressWarnings("MissortedModifiers")
@Service
public class Music {
    private static final Logger LOGGER = LoggerFactory.getLogger(Music.class);
    private final Vector<String> playList = new Vector<>();
    private final Random random = new Random();

    public Music() {
        playList.add("badtrip");
        playList.add("Владимирский централ");
    }

    @Nullable
    public byte[] getSong(String name) {
        try {
            final Path path = Paths.get("./src/main/resources/music/" + name + ".wav");
            return Files.readAllBytes(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @NotNull
    public String getSongName() {
        final int index = random.nextInt(playList.size());
        return playList.get(index);
    }

    @Nullable
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
            //System.out.println(headerSize);
            System.arraycopy(record, 0, result, 0, headerSize);
            for (int i = 0; i < frames.length; i++) {
                for (int j = 0; j < frameSize; j++) {
                    result[i * frameSize + j + headerSize] = frames[i][j];
                }
            }
            return result;
        } catch (UnsupportedAudioFileException e) {
            LOGGER.error("Unsuported audio file");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

//    public static void main(String[] args) {
//        final Music music = new Music();
//        final Path path = Paths.get("./src/main/resources/music/hh2.wav");
//        try {
//            Files.createFile(path);
//            Files.write(path, music.reverseRecord(music.getSong("badtrip.wav")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}


