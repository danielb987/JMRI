package jmri.jmrit.sound;

import java.io.IOException;
import java.net.URL;
import javax.annotation.Nonnull;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sound clip.
 */
class SoundClip {

    private boolean _available = false;
    private long _timeFinished = -1;
    private final Clip _clip;
    
    SoundClip() throws IllegalArgumentException, LineUnavailableException {
        _clip = AudioSystem.getClip();
    }
    
    boolean isAvailable() {
        return _available;
    }
    
    long getTimeFinished() {
        return _timeFinished;
    }
    
    void init(@Nonnull URL url) {
        try {
            _clip.open(AudioSystem.getAudioInputStream(url));
        } catch (IOException ex) {
            log.error("Unable to open {}", url);
        } catch (LineUnavailableException ex) {
            log.error("Unable to provide audio playback", ex);
        } catch (UnsupportedAudioFileException ex) {
            log.error("{} is not a recognised audio format", url);
        }
    }
    
    public void start() {
        _clip.start();
    }
    
    public void loop() {
        _clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void loop(int count) {
        _clip.loop(count);
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(SoundClip.class);
    
}
