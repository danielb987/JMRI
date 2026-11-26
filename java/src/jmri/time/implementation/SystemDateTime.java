package jmri.time.implementation;

import java.time.ZonedDateTime;
import java.util.TimerTask;

import jmri.time.Rate;
import jmri.time.rate.IntegerRate;
import jmri.time.DateProvider;
import jmri.util.TimerUtil;

/**
 * The system date and time.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class SystemDateTime extends AbstractTimeProvider implements DateProvider {

    // System date and time always has the rate 1:1.
    private static final Rate RATE = new IntegerRate(1);
    private static final int _100_MILLISECONDS = 100;

    private final TimerTask _timerTask;
    private TimerTask _scheduledTask = null;
    private ZonedDateTime _lastDateTime;
    private long _lastSec;
    private long _lastMin;


    /**
     * Creates an instance of SystemDateTime.
     * This constructor must only be called from ProxyTimeProviderManager and
     * the caller must call {@link #init()} afterwards.
     * @param systemName the system name
     */
    public SystemDateTime(String systemName) {
        super(systemName);

        _timerTask = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
    }

    // package-private so tests can drive the clock without waiting
    void tick() {
        var newDateTime = now();
        // count from the epoch, as timeIsUpdated() does, so a tick exactly 60 s later still fires
        long sec = newDateTime.toEpochSecond();
        if (sec != _lastSec) {
            long min = sec / 60;
            firePropertyChange(PROPERTY_CHANGE_SECONDS, _lastSec, sec);
            if (min != _lastMin) {
                firePropertyChange(PROPERTY_CHANGE_MINUTES, _lastMin, min);
            }
            firePropertyChange(PROPERTY_CHANGE_DATETIME, _lastDateTime, newDateTime);
            _lastDateTime = newDateTime;
            _lastSec = sec;
            _lastMin = min;
        }
    }

    // the computer's time; tests override it
    ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public SystemDateTime init() {
        _lastDateTime = now();
        _lastSec = _lastDateTime.toEpochSecond();
        _lastMin = _lastSec / 60;
        // tick on the GUI thread, so listeners and panels update there
        _scheduledTask = TimerUtil.scheduleOnGUIThread(_timerTask, _100_MILLISECONDS, _100_MILLISECONDS);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ZonedDateTime getTime() {
        return now();
    }

    /** {@inheritDoc} */
    @Override
    public Rate getRate() {
        return RATE;    // System date and time always has the rate 1:1.
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        return true;    // System date and time is always running.
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasWeekday() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasDayOfMonth() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasMonth() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasYear() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        super.dispose();
        // cancel the GUI wrapper; cancelling _timerTask alone wouldn't stop it
        if (_scheduledTask != null) _scheduledTask.cancel();
    }

}
