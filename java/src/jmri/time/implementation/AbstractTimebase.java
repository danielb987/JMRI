package jmri.time.implementation;

import java.time.*;

import java.util.*;

import jmri.*;
import jmri.implementation.AbstractNamedBean;
import jmri.time.*;

/**
 * Abstract implementation of Timebase.
 *
 * This class contains all the code that relates to TimeProvider.
 *
 * @author Bob Jacobsen Copyright (C) 2004, 2007
 * @author Dave Duchamp Copyright (C) 2007. additions/revisions for handling one hardware clock
 * @author Daniel Bergqvist Copyright (C) 2025
 */
public abstract class AbstractTimebase extends AbstractNamedBean implements Timebase {

    public AbstractTimebase(String sysName) {
        super(sysName);
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private LocalTime convertToLocalTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


    protected final void setTimeIfPossible(Date d) {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        if (tp instanceof TimeSetter) {
            setTime(d);
        }
    }

    @Override
    public final void setTime(Date d) {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        if (tp instanceof TimeSetter) {
            LocalDateTime dateTime = convertToLocalDateTime(d);
            ((TimeSetter) tp).setDateTime(dateTime);
        } else {
            throw new UnsupportedOperationException("The current TimeProvider is not a TimeSetter: "
                    + (tp != null ? tp.getClass().getName() : null));
        }
    }

    @Override
    public final void setTime(Instant i) {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        if (tp instanceof TimeSetter) {
            TimeSetter ts = (TimeSetter)tp;
            LocalTime time = LocalTime.ofInstant(i, ZoneId.systemDefault());
            ts.setTime(time);
        } else {
            throw new UnsupportedOperationException("The current TimeProvider is not a TimeSetter: "
                    + (tp != null ? tp.getClass().getName() : null));
        }
    }

    @Override
    public final void userSetTime(Date d) {
        setTime(d);
    }

    @Override
    public final Date getTime() {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        return convertToDate(tp.getTime());
    }

    @Override
    public final void setRun(boolean y) {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        if (tp instanceof StartStopTimeProvider) {
            StartStopTimeProvider sstp = (StartStopTimeProvider)tp;
            if (sstp.canStartAndStop()) {
                if (y) {
                    sstp.start();
                } else {
                    sstp.stop();
                }
            } else {
                throw new UnsupportedOperationException("The current TimeProvider can not start/stop time");
            }
        } else {
            throw new UnsupportedOperationException("The current TimeProvider is not a StartStopTimeProvider: "
                    + (tp != null ? tp.getClass().getName() : null));
        }
    }

    @Override
    public final boolean getRun() {
        return InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider().isRunning();
    }

    @Override
    public final void setRate(double factor) throws TimebaseRateException {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        if (tp instanceof CanSetRate) {
            if (tp.getRate() instanceof RateSetter) {
                CanSetRate csr = (CanSetRate)tp;
                RateSetter rs = (RateSetter)tp.getRate();
                if (csr.canSetRate()) {
                    rs.setRate(factor);
                } else {
                    throw new UnsupportedOperationException("The current TimeProvider can not start/stop time");
                }
            } else {
                throw new UnsupportedOperationException("The current TimeProvider rate is not a RateSetter: "
                        + (tp.getRate() != null ? tp.getRate().getClass().getName() : null));
            }
        } else {
            throw new UnsupportedOperationException("The current TimeProvider is not a CanSetRate: "
                    + (tp != null ? tp.getClass().getName() : null));
        }
    }

    @Override
    public final void userSetRate(double factor) throws TimebaseRateException {
        TimeProvider tp = InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider();
        if (tp instanceof CanSetRate) {
            if (tp.getRate() instanceof RateSetter) {
                CanSetRate csr = (CanSetRate)tp;
                RateSetter rs = (RateSetter)tp.getRate();
                if (csr.canSetRate()) {
                    rs.setRate(factor);
                } else {
                    throw new UnsupportedOperationException("The current TimeProvider can not start/stop time");
                }
            } else {
                throw new UnsupportedOperationException("The current TimeProvider rate is not a RateSetter: "
                        + (tp.getRate() != null ? tp.getRate().getClass().getName() : null));
            }
        } else {
            throw new UnsupportedOperationException("The current TimeProvider is not a CanSetRate: "
                    + (tp != null ? tp.getClass().getName() : null));
        }
    }

    @Override
    public final double getRate() {
        return InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider().getRate().getRate();
    }

    @Override
    public final double userGetRate() {
        return InstanceManager.getDefault(TimeProviderManager.class)
                .getCurrentTimeProvider().getRate().getRate();
    }
/*
    @Override
    @SuppressFBWarnings(value = "OVERRIDING_METHODS_MUST_INVOKE_SUPER",
            justification = "This class is an adapter of the main time provider")
    public final void addPropertyChangeListener(PropertyChangeListener l) {
        InstanceManager.getDefault(TimeProviderManager.class)
                .getMainTimeProviderHandler().addPropertyChangeListener(l);
    }

    @Override
    @SuppressFBWarnings(value = "OVERRIDING_METHODS_MUST_INVOKE_SUPER",
            justification = "This class is an adapter of the main time provider")
    public final void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
        InstanceManager.getDefault(TimeProviderManager.class)
                .getMainTimeProviderHandler().addPropertyChangeListener(propertyName, l);
    }

    @Override
    @SuppressFBWarnings(value = "OVERRIDING_METHODS_MUST_INVOKE_SUPER",
            justification = "This class is an adapter of the main time provider")
    public final void removePropertyChangeListener(PropertyChangeListener l) {
        InstanceManager.getDefault(TimeProviderManager.class)
                .getMainTimeProviderHandler().removePropertyChangeListener(l);
    }

    @Override
    @SuppressFBWarnings(value = "OVERRIDING_METHODS_MUST_INVOKE_SUPER",
            justification = "This class is an adapter of the main time provider")
    public final void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
        InstanceManager.getDefault(TimeProviderManager.class)
                .getMainTimeProviderHandler().removePropertyChangeListener(l);
    }

    @Override
    public final PropertyChangeListener[] getPropertyChangeListeners() {
        return InstanceManager.getDefault(TimeProviderManager.class)
                .getMainTimeProviderHandler().getPropertyChangeListeners();
    }

    @Override
    public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return InstanceManager.getDefault(TimeProviderManager.class)
                .getMainTimeProviderHandler().getPropertyChangeListeners(propertyName);
    }
/*
    private final Map<PropertyChangeListener, PropertyChangeListener> _pclMap = new HashMap<>();

    @Override
    public final void addMinuteChangeListener(PropertyChangeListener l) {
        System.out.format("AbstractTimeBase.addMinuteChangeListener%n");
        PropertyChangeListener adapter = new PropertyChangeListenerAdapter(l);
        _pclMap.put(l, adapter);
        addPropertyChangeListener(Timebase.PROPERTY_CHANGE_MINUTES, adapter);
    }

    @Override
    public final void removeMinuteChangeListener(PropertyChangeListener l) {
        PropertyChangeListener adapter = _pclMap.remove(l);
        if (adapter != null) {
            removePropertyChangeListener(Timebase.PROPERTY_CHANGE_MINUTES, l);
        }
    }

    @Override
    public final PropertyChangeListener[] getMinuteChangeListeners() {
        return _pclMap.keySet().toArray(PropertyChangeListener[]::new);
//        return getPropertyChangeListeners(Timebase.PROPERTY_CHANGE_MINUTES);
    }
*/


//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AbstractTimebase.class);

}
