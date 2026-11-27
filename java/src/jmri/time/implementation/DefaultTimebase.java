package jmri.time.implementation;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.*;

import jmri.*;
import jmri.jmrix.internal.InternalSystemConnectionMemo;
import jmri.time.*;

/**
 * Default implementation of Timebase.
 *
 * This code relates to all the code that don't relate to TimeProvider.
 *
 * @author Bob Jacobsen Copyright (C) 2004, 2007
 * @author Dave Duchamp Copyright (C) 2007. additions/revisions for handling one hardware clock
 * @author Daniel Bergqvist Copyright (C) 2025
 */
public class DefaultTimebase extends AbstractTimebase {

    private final SystemConnectionMemo memo;

    /**
     * InstanceManager.getDefault(jmri.Timebase.class) variables and options
     */
    private double mFactor = 1.0; // this is the rate factor for the JMRI fast clock
    private double hardwareFactor = 1.0; // this is the rate factor for the hardware clock
    //  The above is necessary to support hardware clock Time Sources that fiddle with mFactor to
    //      synchronize, instead of sending over a new time to synchronize.
    private double startupFactor = 1.0; // this is the rate requested at startup
    private boolean startSetRate = true; // if true, the hardware rate will be set to
    private boolean haveStartupFactor = false; // true if startup factor was ever set.
    // startupFactor at startup.

//    private Date startAtTime;
//    private Date setTimeValue;
    private Sensor clockSensor = null; // active when clock is running, inactive when stopped
    private Memory clockMemory = null; // contains current time on each tick
    private Memory factorMemory = null; // contains the rate factor for the fast clock

    private boolean internalMaster = true; // false indicates a hardware clock is the master
    private String masterName = ""; // name of hardware time source, if not internal master
    private ClockControl hardwareTimeSource = null; // ClockControl instance of hardware time source
    private boolean synchronizeWithHardware = false; // true indicates need to synchronize
    private boolean correctHardware = false; // true indicates hardware correction requested
    private boolean display12HourClock = false; // true if 12-hour clock display is requested
    private ClockInitialRunState initialState = ClockInitialRunState.DO_START; // what to do with the clock running state at startup
    private boolean startSetTime = false; // true indicates set fast clock to specified time at
    //start up requested
    private Date startTime = new Date(); // specified time for setting fast clock at start up
    private int startClockOption = NONE; // request start of a clock at start up
    private boolean notInitialized = true; // true before initialization received from start up
    private boolean showStopButton = false; // true indicates start up with start/stop button displayed

    private java.text.SimpleDateFormat timeStorageFormat = null;

    private final PropertyChangeListener timeProviderListener = this::timeProviderChanged;
    private final MainTimeProviderHandler timeProviderHandler;
    private int oldMinutes = -1;
    private Date oldDate = null;

    private javax.swing.Timer timer = null;


    public DefaultTimebase(InternalSystemConnectionMemo memo) {
        super("SIMPLECLOCK");
        this.memo = memo;
        // initialize time-containing memory
        try {
            clockMemory = InstanceManager.memoryManagerInstance().provideMemory(memo.getSystemPrefix()+"MCURRENTTIME");
            clockMemory.setValue("--");
        } catch (IllegalArgumentException ex) {
            log.warn("Unable to create CURRENTTIME time memory variable");
        }

        init();

        timeProviderHandler = InstanceManager.getDefault(TimeProviderManager.class).getMainTimeProviderHandler();
        timeProviderHandler.addPropertyChangeListener(timeProviderListener);
        // init() set the time before this listener was added, so fill in the memory now
        updateMemory(getTime());
    }

    private void init(){

        // set to start counting from now
        setTimeIfPossible(new Date());
        // initialize start/stop sensor for time running
        try {
            clockSensor = InstanceManager.sensorManagerInstance().provideSensor(memo.getSystemPrefix()+"SCLOCKRUNNING");
            // match the clock, which the time provider manager starts and a panel file may stop
            clockSensor.setKnownState(getRun() ? Sensor.ACTIVE : Sensor.INACTIVE);
            clockSensor.addPropertyChangeListener(this::clockSensorChanged);
        } catch (JmriException e) {
            log.warn("Exception setting CLOCKRUNNING sensor", e);
        }
        // initialize rate factor-containing memory
        if (InstanceManager.getNullableDefault(MemoryManager.class) != null) {
            // only try to create memory if memories are supported
            try {
                factorMemory = InstanceManager.memoryManagerInstance()
                    .provideMemory(memo.getSystemPrefix()+"MRATEFACTOR");
                factorMemory.setValue(userGetRate());
            } catch (IllegalArgumentException ex) {
                log.warn("Unable to create RATEFACTOR time memory variable");
            }
        }

    }

    @Override
    public void setTime(Date d) {
        super.setTime(d);
        if (synchronizeWithHardware && InstanceManager.getDefault(ClockControl.class) != hardwareTimeSource) {
            // send new time to all hardware clocks, except the hardware time source if there is one
            InstanceManager.getDefault(ClockControl.class).setTime(d);
        }
    }

    @Override
    public void setTime(java.time.Instant i) {
        // same as SimpleTimebase; the inherited version drops the date and treats the time as UTC
        setTime(Date.from(i));
    }

    @Override
    public void userSetTime(Date d) {
        // this call only results from user changing fast clock time in Setup Fast Clock
        super.setTime(d);
        if (synchronizeWithHardware) {
            // send new time to all hardware clocks, including the hardware time source if there is one
            InstanceManager.getDefault(ClockControl.class).setTime(d);
        } else if (!internalMaster && (hardwareTimeSource != null)) {
            // if not synchronizing, send to the hardware time source if there is one
            hardwareTimeSource.setTime(d);
        }
    }

    @Override
    public void setRun(boolean run) {
        boolean wasRunning = getRun();
        super.setRun(run);
        if (run && !wasRunning) {
            if (synchronizeWithHardware) {
                InstanceManager.getDefault(ClockControl.class).startHardwareClock(getTime());
            } else if (!internalMaster && hardwareTimeSource != null) {
                hardwareTimeSource.startHardwareClock(getTime());
            }
        } else if (!run && wasRunning) {
            if (synchronizeWithHardware) {
                InstanceManager.getDefault(ClockControl.class).stopHardwareClock();
            } else if (!internalMaster && hardwareTimeSource != null) {
                hardwareTimeSource.stopHardwareClock();
            }
        }
    }

    @Override
    public void setRate(double factor) throws TimebaseRateException {
        double oldFactor = mFactor;
        super.setRate(factor);
        // initializeHardwareClock() sends these, so keep them current as SimpleTimebase does
        mFactor = factor;
        if (internalMaster || notInitialized) {
            hardwareFactor = factor;
        }
        if (internalMaster || (synchronizeWithHardware && notInitialized)) {
            // send new rate to all hardware clocks, except the hardware time source if there is one
            if (InstanceManager.getDefault(ClockControl.class) != hardwareTimeSource) {
                InstanceManager.getDefault(ClockControl.class).setRate(factor);
            }
        }
        // like SimpleTimebase, fire only as internal master; with a hardware master, its rate updates don't fire
        if (internalMaster) {
            firePropertyChange(PROPERTY_CHANGE_RATE, oldFactor, factor);
        }
    }

    @Override
    public void userSetRate(double factor) throws TimebaseRateException {
        // this call is used when user changes fast clock rate either in Setup Fast Clock or via a ClockControl
        double oldFactor = hardwareFactor;
        super.userSetRate(factor);
        mFactor = factor;
        hardwareFactor = factor;
        if (synchronizeWithHardware) {
            // send new rate to all hardware clocks, including the hardware time source if there is one
            InstanceManager.getDefault(ClockControl.class).setRate(factor);
        } else if (!internalMaster && (hardwareTimeSource != null)) {
            // if not synchronizing, send to the hardware time source if there is one
            hardwareTimeSource.setRate(factor);
        }
        // a user's change always updates the memory and fires, as in SimpleTimebase
        if (factorMemory != null) {
            factorMemory.setValue(factor);
        }
        firePropertyChange(PROPERTY_CHANGE_RATE, oldFactor, factor);
    }

    @Override
    public void setState(int s) throws JmriException {
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public String getBeanType() {
        return Bundle.getMessage("BeanNameTime");
    }

    @Override
    public void setInternalMaster(boolean master, boolean update) {
        if (master != internalMaster) {
            internalMaster = master;
            if (internalMaster) {
                mFactor = hardwareFactor; // get rid of any fiddled rate present
            }
            if (update) {
                // Note if there are multiple hardware clocks, this should be a loop over all hardware clocks
                InstanceManager.getDefault(ClockControl.class).initializeHardwareClock(userGetRate(),
                        getTime(), false);
            }

            if (internalMaster) {
                masterName = "";
                hardwareTimeSource = null;
            } else {
                // Note if there are multiple hardware clocks, this should be changed to correctly
                // identify which hardware clock has been chosen-currently assumes only one
                hardwareTimeSource = InstanceManager.getDefault(ClockControl.class);
                masterName = hardwareTimeSource.getHardwareClockName();
            }
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public boolean getInternalMaster() {
        return internalMaster;
    }

    @Override
    public void setMasterName(String name) {
        if (!internalMaster) {
            masterName = name;
            // if multiple clocks, this must be replaced by a loop over all hardware clocks to identify
            // the one that is the hardware time source
            hardwareTimeSource = InstanceManager.getDefault(ClockControl.class);
        } else {
            masterName = "";
            hardwareTimeSource = null;
        }
    }

    @Override
    public String getMasterName() {
        return masterName;
    }

    @Override
    public void setSynchronize(boolean synchronize, boolean update) {
        if (synchronizeWithHardware != synchronize) {
            synchronizeWithHardware = synchronize;
            if (update) {
                // Note if there are multiple hardware clocks, this should be a loop over all hardware clocks
                InstanceManager.getDefault(ClockControl.class).initializeHardwareClock(
                   userGetRate(), getTime(), false);
            }
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public boolean getSynchronize() {
        return synchronizeWithHardware;
    }

    @Override
    public void setCorrectHardware(boolean correct, boolean update) {
        if (correctHardware != correct) {
            correctHardware = correct;
            if (update) {
                // Note if there are multiple hardware clocks, this should be a loop over all hardware clocks
                InstanceManager.getDefault(ClockControl.class).initializeHardwareClock(
                userGetRate(), getTime(), false);
            }
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public boolean getCorrectHardware() {
        return correctHardware;
    }

    @Override
    public void set12HourDisplay(boolean display, boolean update) {
        if (display != display12HourClock) {
            display12HourClock = display;
            if (update) {
                // Note if there are multiple hardware clocks, this should be a loop over all hardware clocks
                InstanceManager.getDefault(ClockControl.class).initializeHardwareClock(
                userGetRate(), getTime(), false);
            }
        }
    }

    @Override
    public boolean use12HourDisplay() {
        return display12HourClock;
    }

    @Override
    public void setClockInitialRunState(ClockInitialRunState state) {
        if (initialState != state) {
            initialState = state;
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public ClockInitialRunState getClockInitialRunState() {
        return initialState;
    }

    @Override
    public void setShowStopButton(boolean displayed) {
        if (showStopButton != displayed) {
            showStopButton = displayed;
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public boolean getShowStopButton() {
        return showStopButton;
    }

    @Override
    public void setStartSetTime(boolean set, Date time) {
        if (startSetTime!=set || startTime!=new Date(time.getTime())) {
            startSetTime = set;
            startTime = new Date(time.getTime());
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public boolean getStartSetTime() {
        return startSetTime;
    }

    @Override
    public void setStartRate(double factor) {
        if (Math.abs(startupFactor - factor) > 0.0001) { //avoid possible float precision errors
            startupFactor = factor;
            haveStartupFactor = true;
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public double getStartRate() {
        if (haveStartupFactor) {
            return startupFactor;
        } else {
            return userGetRate();
        }
    }

    @Override
    public void setSetRateAtStart(boolean set) {
        if (startSetRate != set) {
            startSetRate = set;
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public boolean getSetRateAtStart() {
        return startSetRate;
    }

    @Override
    public Date getStartTime() {
        return new Date(startTime.getTime());
    }

    @Override
    public void setStartClockOption(int option) {
        if (startClockOption != option) {
            startClockOption = option;
            firePropertyChange("config", 0, 1); // inform listeners that the clock config has changed
        }
    }

    @Override
    public int getStartClockOption() {
        return startClockOption;
    }

    @Override
    public void initializeClock() {
        switch (startClockOption) {
            case NIXIE_CLOCK:
                jmri.jmrit.nixieclock.NixieClockFrame f = new jmri.jmrit.nixieclock.NixieClockFrame();
                f.setVisible(true);
                break;
            case ANALOG_CLOCK:
                jmri.jmrit.analogclock.AnalogClockFrame g = new jmri.jmrit.analogclock.AnalogClockFrame();
                g.setVisible(true);
                break;
            case LCD_CLOCK:
                jmri.jmrit.lcdclock.LcdClockFrame h = new jmri.jmrit.lcdclock.LcdClockFrame();
                h.setVisible(true);
                break;
            case PRAGOTRON_CLOCK:
                jmri.jmrit.pragotronclock.PragotronClockFrame p = new jmri.jmrit.pragotronclock.PragotronClockFrame();
                p.setVisible(true);
                break;
            default:
                log.debug("initializeClock() called with invalid startClockOption: {}", startClockOption);
        }
    }

    @Override
    public void initializeHardwareClock() {
        boolean startStopped = (initialState == ClockInitialRunState.DO_STOP);
        if (synchronizeWithHardware || correctHardware) {
            if (startStopped) {
                InstanceManager.getList(ClockControl.class).forEach( cc ->
                    cc.initializeHardwareClock( 0, getTime(), (!internalMaster && !startSetTime)) );
            } else {
                InstanceManager.getList(ClockControl.class).forEach( cc ->
                    cc.initializeHardwareClock( mFactor, getTime(), (!internalMaster && !startSetTime)) );
            }
        } else if (!internalMaster) {
            if (startStopped) {
                hardwareTimeSource.initializeHardwareClock(0, getTime(), (!startSetTime));
            } else {
                hardwareTimeSource.initializeHardwareClock(hardwareFactor, getTime(), (!startSetTime));
            }
        }
        notInitialized = false;
    }

    @Override
    public boolean getIsInitialized() {
        return (!notInitialized);
    }

    @Override
    public void addMinuteChangeListener(PropertyChangeListener l) {
        addPropertyChangeListener(PROPERTY_CHANGE_MINUTES, l);
    }

    @Override
    public void removeMinuteChangeListener(PropertyChangeListener l) {
        removePropertyChangeListener(PROPERTY_CHANGE_MINUTES, l);
    }

    @Override
    public PropertyChangeListener[] getMinuteChangeListeners() {
        return getPropertyChangeListeners(PROPERTY_CHANGE_MINUTES);
    }

    // pass the current time provider's events on under the old Timebase names
    private void timeProviderChanged(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if (name == null) {
            return;
        }
        switch (name) {
            case TimeProvider.PROPERTY_CHANGE_MINUTES:
                minuteChanged();
                break;
            case TimeProvider.PROPERTY_CHANGE_RATE:
                // only a change made on the clock itself; the rate setters here fire their own event
                if (factorMemory != null) {
                    factorMemory.setValue(userGetRate());
                }
                firePropertyChange(PROPERTY_CHANGE_RATE, evt.getOldValue(), evt.getNewValue());
                break;
            case TimeProvider.PROPERTY_CHANGE_RUN:
                updateClockSensor();
                firePropertyChange(PROPERTY_CHANGE_RUN, evt.getOldValue(), evt.getNewValue());
                break;
            default:
                // the old Timebase has no seconds or datetime events
                break;
        }
    }

    // fire the minutes and time events the way SimpleTimebase does
    private void minuteChanged() {
        Date date = getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minutes = calendar.get(Calendar.MINUTE);
        updateMemory(date);
        firePropertyChange(PROPERTY_CHANGE_MINUTES, Double.valueOf(oldMinutes), Double.valueOf(minutes));
        firePropertyChange(PROPERTY_CHANGE_TIME, oldDate != null ? new Date(oldDate.getTime()) : null,
                new Date(date.getTime())); // to ensure not modified outside
        oldDate = date;
        oldMinutes = minutes;
    }

    private void updateMemory(Date date) {
        if (clockMemory == null) {
            return;
        }
        if (timeStorageFormat == null) {
            String pattern = java.util.ResourceBundle.getBundle("jmri.jmrit.simpleclock.SimpleClockBundle")
                .getString("TimeStorageFormat");
            try {
                timeStorageFormat = new java.text.SimpleDateFormat(pattern);
            } catch (IllegalArgumentException e) {
                log.info("Unable to parse date / time format: {}",pattern);
                log.info("For supported formats see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html");
                log.info("Dropping back to default time format (h:mm a) 4:56 PM, due to exception", e);
                timeStorageFormat = new java.text.SimpleDateFormat("h:mm a");
            }
        }
        clockMemory.setValue(timeStorageFormat.format(date));
    }

    // keep the clock running sensor in step with the clock
    private void updateClockSensor() {
        if (clockSensor == null) {
            return;
        }
        int state = getRun() ? Sensor.ACTIVE : Sensor.INACTIVE;
        if (clockSensor.getKnownState() != state) {
            try {
                clockSensor.setKnownState(state);
            } catch (JmriException e) {
                log.warn("Exception setting ISClockRunning sensor", e);
            }
        }
    }

    /**
     * Handle a change in the clock running sensor
     */
    private void clockSensorChanged(java.beans.PropertyChangeEvent e) {
        if (clockSensor.getKnownState() == Sensor.ACTIVE) {
            // simply return if clock is already running
            if (getRun()) {
                return;
            }
            setRun(true);
        } else {
            // simply return if clock is already stopped
            if (!getRun()) {
                return;
            }
            setRun(false);
        }
    }

    /**
     * Stops Timer.
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        if (timer != null) {
            // end this timer
            timer.setRepeats(false); // just in case
            timer.stop();

            ActionListener[] listeners = timer.getListeners(ActionListener.class);
            for (ActionListener listener : listeners) {
                timer.removeActionListener(listener);
            }
            timer = null;
        }
        if ( clockSensor != null ) {
            clockSensor.removePropertyChangeListener(this::clockSensorChanged);
        }
        timeProviderHandler.removePropertyChangeListener(timeProviderListener);
        super.dispose(); // remove standard property change listeners
    }


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DefaultTimebase.class);

}
