package jmri.jmrit.logixng.template;

import jmri.managers.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import jmri.AudioManager;
import jmri.BlockManager;
import jmri.ClockControl;
import jmri.ConditionalManager;
import jmri.IdTagManager;
import jmri.InstanceInitializer;
import jmri.InstanceManager;
import jmri.LightManager;
import jmri.LogixManager;
import jmri.MemoryManager;
import jmri.NamedBean;
import jmri.RailComManager;
import jmri.ReporterManager;
import jmri.RouteManager;
import jmri.SensorManager;
import jmri.SignalGroupManager;
import jmri.SignalHeadManager;
import jmri.SignalMastLogicManager;
import jmri.SignalMastManager;
import jmri.SignalSystemManager;
import jmri.Timebase;
import jmri.TurnoutManager;
import jmri.implementation.AbstractInstanceInitializer;
import jmri.implementation.DefaultClockControl;
import jmri.jmrit.audio.DefaultAudioManager;
import jmri.jmrit.logixng.NullNamedBeanInitializer;
import jmri.jmrit.vsdecoder.VSDecoderManager;
import org.openide.util.lookup.ServiceProvider;

/**
 * Provide the usual default implementations for the
 * {@link jmri.jmrit.logixng.template.TemplateInstanceManager}.
 * <P>
 * @author Daniel Bergqvist Copyright (C) 2019
 */
@ServiceProvider(service = NullNamedBeanInitializer.class)
public class DefaultNullNamedBeanInitializer implements NullNamedBeanInitializer {

    @Override
    public <T> Object create(Class<T> type, @Nonnull String name) {

        // In order for getDefault() to create a new object, the type also
        // needs to be added to the method getInitalizes() below.

        if (type == NullTurnout.class) {
            return new NullTurnout(name);
        }

        if (type == NullSensor.class) {
            return new NullSensor(name);
        }

        if (type == ConditionalManager.class) {
            return new DefaultConditionalManager();
        }

        if (type == LightManager.class) {
            return new jmri.managers.ProxyLightManager();
        }

        if (type == LogixManager.class) {
            return new DefaultLogixManager();
        }

        if (type == MemoryManager.class) {
            return new DefaultMemoryManager();
        }

        if (type == RailComManager.class) {
            return new DefaultRailComManager();
        }

        if (type == ReporterManager.class) {
            return new jmri.managers.ProxyReporterManager();
        }

        if (type == RouteManager.class) {
            return new DefaultRouteManager();
        }

        if (type == SensorManager.class) {
            return new jmri.managers.ProxySensorManager();
        }

        if (type == SignalGroupManager.class) {
            // ensure signal mast manager exists first
            InstanceManager.getDefault(jmri.SignalMastManager.class);
            return new DefaultSignalGroupManager();
        }

        if (type == SignalHeadManager.class) {
            return new AbstractSignalHeadManager();
        }

        if (type == SignalMastLogicManager.class) {
            return new DefaultSignalMastLogicManager();
        }

        if (type == SignalMastManager.class) {
            // ensure signal head manager exists first
            InstanceManager.getDefault(jmri.SignalHeadManager.class);
            return new DefaultSignalMastManager();
        }

        if (type == SignalSystemManager.class) {
            return new DefaultSignalSystemManager();
        }

        if (type == Timebase.class) {
            Timebase timebase = new jmri.jmrit.simpleclock.SimpleTimebase();
            if (InstanceManager.getNullableDefault(jmri.ConfigureManager.class) != null) {
                InstanceManager.getDefault(jmri.ConfigureManager.class).registerConfig(timebase, jmri.Manager.TIMEBASE);
            }
            return timebase;
        }

        if (type == TurnoutManager.class) {
            return new jmri.managers.ProxyTurnoutManager();
        }

        if (type == VSDecoderManager.class) {
            return VSDecoderManager.instance();
        }

        if (type == IdTagManager.class) {
            return new jmri.managers.ProxyIdTagManager();
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Set<Class<?>> getInitalizes() {
        Set<Class<?>> set = new HashSet<>();
        set.addAll(Arrays.asList(
                AudioManager.class,
                BlockManager.class,
                ClockControl.class,
                ConditionalManager.class,
                IdTagManager.class,
                LightManager.class,
                LogixManager.class,
                MemoryManager.class,
                RailComManager.class,
                ReporterManager.class,
                RouteManager.class,
                SensorManager.class,
                SignalGroupManager.class,
                SignalHeadManager.class,
                SignalMastLogicManager.class,
                SignalMastManager.class,
                SignalSystemManager.class,
                Timebase.class,
                TurnoutManager.class,
                VSDecoderManager.class
        ));
        return set;
    }

}
