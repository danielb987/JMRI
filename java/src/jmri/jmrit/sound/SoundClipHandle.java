package jmri.jmrit.sound;

import java.net.URL;
import javax.annotation.Nonnull;
import javax.sound.sampled.Clip;

/**
 * A handle for SoundClip
 */
public class SoundClipHandle {

    private final URL _url;
    private SoundClip _clip = null;
    
    public SoundClipHandle(@Nonnull URL url) {
        _url = url;
    }
    
    public void start() {
        _clip = SoundClipPool.instance().getClip(this, _url);
        if (_clip != null) {
            _clip.start();
        }
    }
    
    public void loop() {
        _clip = SoundClipPool.instance().getClip(this, _url);
        if (_clip != null) {
            _clip.loop();
        }
    }
    
    public void loop(int count) {
        _clip = SoundClipPool.instance().getClip(this, _url);
        if (_clip != null) {
            _clip.loop(count);
        }
    }
    
}
