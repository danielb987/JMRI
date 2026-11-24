package jmri.time.implementation;

import java.time.*;
import java.time.temporal.*;
import java.util.TimerTask;

import jmri.time.*;
import jmri.time.rate.ChangeableDoubleRate;
import jmri.util.TimerUtil;

/**
 * The system date and time.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class InternalDateTime extends AbstractTimeProvider
        implements TimeSetter, DateProvider, CanSetRate, StartStopTimeProvider, RateSetter {

    private static final int _100_MILLISECONDS = 100;

    // We cannot have _time synchronized with _lock since that could lead to deadlock.
    // So _time is volatile instead. This works since ZonedDateTime is immutable.
    private volatile ZonedDateTime _time = ZonedDateTime.now();

    private final ChangeableDoubleRate _rate = new ChangeableDoubleRate(1.0);
    private boolean _isRunning;
    private boolean _isLockedFromRunning;
    private final TimerTask _timerTask;
    private ZonedDateTime _lastDateTime;
    private ZonedDateTime _lastUpdatedDateTime;
    private int _lastSec;
    private int _lastMin;
    private long _startTimeMillisec;
    private final Object _lock = new Object();
    private boolean _isTimerTaskStarted;
    private TimerTask _scheduledTask = null;


    private TimerTask getTimerTask() {
        /*
        Lägg till starta/stoppa klockan.
        Lägg till ändra hastighet (rate) på klockan.
        */

        return new TimerTask() {
            @Override
            public void run() {
                int oldSec;
                int newSec;
                int oldMin;
                int newMin;
                ZonedDateTime oldDateTime;
                ZonedDateTime newDateTime;
                synchronized(_lock) {
                    if (!_isRunning || _isLockedFromRunning) return;
                    if (_rate.getRate() < 0.0001) return;   // We don't want to divide by zero later

                    long time = System.currentTimeMillis() - _startTimeMillisec;
                    long diff = Math.round(time * _rate.getRate());
                    _time = _lastDateTime.plus(diff, ChronoUnit.MILLIS);
                    int sec = _time.getSecond();
                    if (sec == _lastSec) return;
                    oldSec = _lastSec;
                    newSec = sec;
                    oldMin = _lastMin;
                    newMin = _time.getMinute();
                    oldDateTime = _lastUpdatedDateTime;
                    newDateTime = _time;
                    _lastSec = newSec;
                    _lastMin = newMin;
                    _lastUpdatedDateTime = newDateTime;
                }
                // fire outside the lock, as start(), stop() and setRate() do
                InternalDateTime.this.firePropertyChange(PROPERTY_CHANGE_SECONDS, oldSec, newSec);
                if (newMin != oldMin) {
                    InternalDateTime.this.firePropertyChange(PROPERTY_CHANGE_MINUTES, oldMin, newMin);
                }
                InternalDateTime.this.firePropertyChange(PROPERTY_CHANGE_DATETIME, oldDateTime, newDateTime);
            }
        };
    }

    /**
     * Creates an instance of SystemDateTime.
     * the caller must call {@link #init()} afterwards.
     * @param systemName the system name
     */
    public InternalDateTime(String systemName) {
        super(systemName);
        _timerTask = getTimerTask();
    }

    /**
     * Creates an instance of SystemDateTime.
     * the caller must call {@link #init()} afterwards.
     * @param systemName  the system name
     * @param userName    the user name
     */
    public InternalDateTime(String systemName, String userName) {
        super(systemName, userName);
        _timerTask = getTimerTask();
    }

    public InternalDateTime init() {
        return this;
    }

    /**
     * Lock the clock from running.
     * Used by LoadAndStore tests.
     */
    public void lockFromRunning() {
        synchronized(_lock) {
            _isLockedFromRunning = true;
        }
    }

    private void resetLast() {
        _lastDateTime = _time;
        _lastUpdatedDateTime = _time;
        _lastSec = _lastDateTime.getSecond();
        _lastMin = _lastDateTime.getMinute();
    }

    /** {@inheritDoc} */
    @Override
    public ZonedDateTime getTime() {
        return _time;
    }

    /** {@inheritDoc} */
    @Override
    public Rate getRate() {
        synchronized(_lock) {
            return _rate;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        synchronized(_lock) {
            return _isRunning;
        }
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

    @Override
    public void start() throws UnsupportedOperationException {
        boolean oldRunning;
        synchronized(_lock) {
            oldRunning = _isRunning;
            if (!_isTimerTaskStarted && !_isLockedFromRunning) {
                _time = ZonedDateTime.now();
                resetLast();
                _startTimeMillisec = System.currentTimeMillis();
                // tick on the GUI thread, as SimpleTimebase does, so listeners and panels update there
                _scheduledTask = TimerUtil.scheduleOnGUIThread(_timerTask, _100_MILLISECONDS, _100_MILLISECONDS);
               _isTimerTaskStarted = true;
            }
            _isRunning = true;
        }
        // fire outside the lock, so a listener can't deadlock with a caller of isRunning()
        firePropertyChange(PROPERTY_CHANGE_RUN, oldRunning, true);
    }

    @Override
    public void stop() throws UnsupportedOperationException {
        boolean oldRunning;
        synchronized(_lock) {
            oldRunning = _isRunning;
            _isRunning = false;
        }
        firePropertyChange(PROPERTY_CHANGE_RUN, oldRunning, false);
    }

    @Override
    public boolean canStartAndStop() {
        return true;
    }

    @Override
    public void setTime(LocalTime time) throws UnsupportedOperationException {
        ZonedDateTime oldTime;
        synchronized(_lock) {
            oldTime = this._time;
            _time = ZonedDateTime.of(_time.toLocalDate(), time, ZoneOffset.UTC);
            resetLast();
        }
        timeIsUpdated(oldTime);
    }

    @Override
    public void setDateTime(ZonedDateTime time) throws UnsupportedOperationException {
        ZonedDateTime oldTime;
        synchronized(_lock) {
            oldTime = this._time;
            this._time = time;
            resetLast();
        }
        timeIsUpdated(oldTime);
    }

    @Override
    public void dispose() {
        super.dispose();
        // cancel the GUI wrapper; cancelling _timerTask alone wouldn't stop it
        if (_scheduledTask != null) _scheduledTask.cancel();
    }

    @Override
    public boolean canSetDateTime() {
        return true;
    }

    @Override
    public boolean canSetRate() {
        return true;
    }

    @Override
    public void setRate(double rate) {
        double oldRate;
        double newRate;
        synchronized(_lock) {
            oldRate = _rate.getRate();
            _rate.setRate(rate);
            newRate = _rate.getRate();
        }
        // fire outside the lock, as start() and stop() do
        firePropertyChange(PROPERTY_CHANGE_RATE, oldRate, newRate);
    }

}
