package application.mechanic.music;

import java.io.IOException;
import javax.sound.sampled.*;

public class FrameBuffer {

    private byte[][] frames;
    private int frameSize;

    public FrameBuffer(AudioInputStream stream) throws IOException {
        readFrames(stream);
    }

    public byte[] getFrame(int i) {
        return frames[i];
    }

    public int numberFrames()
    {
        return frames.length;
    }

    public int frameSize()
    {
        return frameSize;
    }

    private void readFrames(AudioInputStream stream) throws IOException {
        frameSize = stream.getFormat().getFrameSize();
        frames = new byte[stream.available() / frameSize][frameSize];
        int i = 0;
        for (; i < frames.length; i++)
        {
            byte[] frame = new byte[frameSize];
            int numBytes = stream.read(frame, 0, frameSize);
            if (numBytes == -1)
            {
                break;
            }
            frames[i] = frame;
        }
        System.out.println("FrameSize = " + frameSize);
        System.out.println("Number frames = " + frames.length);
        System.out.println("Number frames read = " + i);
    }
}
