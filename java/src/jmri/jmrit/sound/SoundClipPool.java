package jmri.jmrit.sound;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.sound.sampled.LineUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pool of SoundClip's.
 */
class SoundClipPool {

    private static final int MAX_POOL_SIZE = 1000;
    private static final SoundClipPool _instance = new SoundClipPool();
    
    private final List<SoundClip> _pool = new ArrayList<>();
    private final Object _lock = new Object();
    
    public static SoundClipPool instance() {
        return _instance;
    }
    
    public SoundClipPool() {
    }
    
    @CheckForNull
    public SoundClip getClip(SoundClipHandle handle, @Nonnull URL url) {
        SoundClip _clip = null;
        synchronized(_lock) {
            if (_pool.size() < MAX_POOL_SIZE) {
                try {
                    clip = new SoundClip();
                } catch (IllegalArgumentException | LineUnavailableException e) {
                    // Do nothing
                }
                // Do nothing
                
            }
            
            if (clip == null) {
                long timeFinished = -1;
                for (int i=0; i < _pool.size(); i++) {
                    SoundClip c = _pool.get(i);
                    long time = c.getTimeFinished();
                    if (c.isAvailable() && (time < timeFinished)) {
                        clip = _pool.get(i);
                    }
                }
                
                if (clip != null) {
                    clip.init(url);
                } else {
                    log.error("Unable to get a SoundClip. Number of clips in pool: {}", _pool.size());
                }
            }
        }
        
        return clip;
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(SoundClipPool.class);
    
}
