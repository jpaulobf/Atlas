package audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

    private Clip clip;
    private FloatControl volumeControl;

    /**
     * Loads an audio file from the classpath.
     * Supports WAV natively and OGG (via VorbisSPI).
     * 
     * @param path The path to the audio file (ex: "/sounds/music.ogg").
     */
    public Audio(String path) {
        load(path);
    }

    private void load(String path) {
        try {
            // Loads the resource (works inside JAR and IDE)
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Audio not found: " + path);
                return;
            }

            // Obtains the original audio stream (can be encoded/compressed)
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            AudioFormat baseFormat = audioInput.getFormat();

            // Defines the decoded format (PCM) required for the Clip to play
            // Transforms from OGG/Vorbis to raw data (PCM 16-bit)
            AudioFormat decodeFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16, // 16 bits
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2, // Frame size: channels * 2 bytes
                baseFormat.getSampleRate(),
                false // Little Endian
            );

            // Creates the decoded stream
            AudioInputStream decodedInput = AudioSystem.getAudioInputStream(decodeFormat, audioInput);

            // Obtains a Clip from the system and opens the decoded stream
            clip = AudioSystem.getClip();
            clip.open(decodedInput);

            // Tries to obtain the volume control (Master Gain)
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading audio: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Starts playback. If paused, resumes. If finished, restarts.
     */
    public void play() {
        if (clip == null) return;
        
        // If reached the end, rewind to start before playing
        if (clip.getFramePosition() >= clip.getFrameLength()) {
            clip.setFramePosition(0);
        }
        clip.start();
    }

    /**
     * Stops playback (Pause).
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    /**
     * Stops playback, rewinds to the beginning, and plays again.
     */
    public void restart() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Plays in an infinite loop (good for background music).
     */
    public void loop() {
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Sets the audio volume.
     * @param volume Value between 0.0 (mute) and 1.0 (max volume).
     */
    public void setVolume(float volume) {
        if (volumeControl == null) return;

        // Clamps values between 0 and 1
        if (volume < 0f) volume = 0f;
        if (volume > 1f) volume = 1f;

        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        
        // The gain control uses Decibels (logarithmic scale)
        // Convert linear (0-1) to dB
        float dB;
        if (volume == 0) {
            dB = min; // Total silence
        } else {
            dB = (float) (Math.log10(volume) * 20.0);
        }

        // Ensures dB value is within hardware limits
        if (dB < min) dB = min;
        if (dB > max) dB = max;

        volumeControl.setValue(dB);
    }
    
    /**
     * Closes the clip and releases system resources.
     */
    public void close() {
        stop();
        if (clip != null) {
            clip.close();
        }
    }
}
