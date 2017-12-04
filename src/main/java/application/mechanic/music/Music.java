package application.mechanic.music;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

//ToDo: сделать нормальный класс
public class Music {
    private final Vector<String> playList = new Vector<>();
    private final Random random = new Random();
    private static final int WAV_HEADER_SIZE = 78;

    public Music() {
        playList.add("badtrip.wav");
        playList.add("Владимирский_централ.wav");
    }

    @Nullable
    public byte[] getSong(String name) {
        try {
            final Path path = Paths.get("./src/main/resources/music/" + name);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }

    @NotNull
    public String getSongName() {
        final int index = random.nextInt(playList.size());
        return playList.get(index);
    }

    //ToDo: переписать без костылей и говнокода
    @Nullable
    public byte[] reverseRecord(@NotNull byte[] record) {
        try {
            System.out.println("bytearraatbtbh = " + record.length);
            final AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(record));
            final int frameSize = stream.getFormat().getFrameSize();
            final byte[][] frames = new byte[stream.available() / frameSize][frameSize];
            for (int i = 0; i < frames.length; i++) {
                final byte[] frame = new byte[frameSize];
                final int numBytes = stream.read(frame, 0, frameSize);
                if (numBytes == -1) {
                    break;
                }
                frames[i] = frame;
            }
            System.out.println("FrameSize = " + frameSize);
            System.out.println("Number frames = " + frames.length);
            System.out.println(record.length - frameSize * frames.length);
            final byte[] result = new byte[record.length];
            for (int i = 0; i < WAV_HEADER_SIZE; ++i) {
                result[i] = record[i];
            }
            for (int i = 0; i < frames.length / 2; ++i) {
                final byte[] tmp = frames[i];
                frames[i] = frames[frames.length - i - 1];
                frames[frames.length - i - 1] = tmp;
            }
            for (int i = 0; i < frames.length; i++) {
                for (int j = 0; j < frameSize; j++) {
                    result[i * frameSize + j + WAV_HEADER_SIZE] = frames[i][j];
                }
            }
            return result;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        final Music music = new Music();
//        final Path path = Paths.get("./src/main/resources/music/hh.wav");
//        try {
//            Files.createFile(path);
//            Files.write(path, music.reverseRecord(music.getSong("Владимирский_централ.wav")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}


