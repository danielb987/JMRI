package jmri.time.implementation;

import java.time.*;

import jmri.implementation.*;

import javax.annotation.Nonnull;

import jmri.NamedBean;
import jmri.time.TimeProvider;

/**
 * Abstract class providing the basic logic of the TimeProvider interface.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2009
 */
public abstract class AbstractTimeProvider extends AbstractNamedBean implements TimeProvider {

    public AbstractTimeProvider(String systemName) {
        super(systemName);
    }

    public AbstractTimeProvider(String systemName, String userName) {
        super(systemName, userName);
    }

    /** {@inheritDoc} */
    @Override
    @Nonnull
    public String getBeanType() {
        return Bundle.getMessage("BeanNameTimeProvider");
    }

    /** {@inheritDoc} */
    @Override
    public void setState(int s) throws jmri.JmriException {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public int getState() {
        return NamedBean.UNKNOWN;
    }
/*
    private static final int SECONDS_PER_DAY = 24 * 60 * 60;

    private long getSeconds(long sec) {
        long seconds = sec % SECONDS_PER_DAY;
        if (seconds < 0) {
            seconds += SECONDS_PER_DAY;
        }
        return seconds;
    }
*/
    protected void timeIsUpdated(LocalDateTime oldTime) {
        LocalDateTime newTime = getTime();
        long oldSeconds = oldTime.toEpochSecond(ZoneOffset.UTC);
        long newSeconds = newTime.toEpochSecond(ZoneOffset.UTC);
//        System.out.format("AAA timeIsUpdated: %d, %d, %b, %b, %s, %s%n", oldSeconds, newSeconds, oldSeconds >= 0, newSeconds >= 0, oldTime.toString(), newTime.toString());
        System.out.format("timeIsUpdated: sec: %d, %d%n", oldSeconds, newSeconds);
        if (newSeconds != oldSeconds) {
            long oldMinutes = oldSeconds / 60;
            long newMinutes = newSeconds / 60;
            System.out.format("timeIsUpdated: firePropChange: %s, %d, %d%n", TimeProvider.PROPERTY_CHANGE_SECONDS, oldSeconds, newSeconds);
            firePropertyChange(TimeProvider.PROPERTY_CHANGE_SECONDS, oldSeconds, newSeconds);
            System.out.format("timeIsUpdated: min: %d, %d%n", oldMinutes, newMinutes);
            if (newMinutes != oldMinutes) {
                System.out.format("timeIsUpdated: firePropChange: %s, %d, %d%n", TimeProvider.PROPERTY_CHANGE_MINUTES, oldMinutes, newMinutes);
                System.out.format("timeIsUpdated: firePropChange: %s, %s, %s%n", TimeProvider.PROPERTY_CHANGE_DATETIME, oldTime, newTime);
                firePropertyChange(TimeProvider.PROPERTY_CHANGE_MINUTES, oldMinutes, newMinutes);
                firePropertyChange(TimeProvider.PROPERTY_CHANGE_DATETIME, oldTime, newTime);
            }
        }
    }


//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AbstractClock.class);

}
