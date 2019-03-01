package jmri.managers;

import java.util.Arrays;
import java.util.Set;
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
import jmri.jmrit.logixng.ActionManager;
import jmri.jmrit.logixng.AnalogActionManager;
import jmri.jmrit.logixng.AnalogExpressionManager;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.StringExpressionManager;
import jmri.jmrit.logixng.engine.DefaultActionManager;
import jmri.jmrit.logixng.engine.DefaultAnalogActionManager;
import jmri.jmrit.logixng.engine.DefaultAnalogExpressionManager;
import jmri.jmrit.logixng.engine.DefaultDigitalExpressionManager;
import jmri.jmrit.logixng.engine.DefaultLogixNGManager;
import jmri.jmrit.logixng.engine.DefaultStringActionManager;
import jmri.jmrit.logixng.engine.DefaultStringExpressionManager;
import jmri.jmrit.logixng.engine.LogixNGPreferences;
import jmri.jmrit.vsdecoder.VSDecoderManager;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.DigitalExpressionManager;

/**
 * Provide the usual default implementations for the
 * {@link jmri.InstanceManager}.
 * <P>
 * Not all {@link jmri.InstanceManager} related classes are provided by this
 * class. See the discussion in {@link jmri.InstanceManager} of initialization
 * methods.
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under the
 * terms of version 2 of the GNU General Public License as published by the Free
 * Software Foundation. See the "COPYING" file for a copy of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <P>
 * @author Bob Jacobsen Copyright (C) 2001, 2008, 2014
 * @since 2.9.4
 */
@ServiceProvider(service = InstanceInitializer.class)
public class DefaultInstanceInitializer extends AbstractInstanceInitializer {

    @Override
    public <T> Object getDefault(Class<T> type) {
        
        // In order for getDefault() to be called for a particular manager,
        // the manager also needs to be added to the method getInitalizes()
        // below.
        
        if (type == ActionManager.class) {
            return new DefaultActionManager();
        }

        if (type == AnalogActionManager.class) {
            return new DefaultAnalogActionManager();
        }

        if (type == AnalogExpressionManager.class) {
            return new DefaultAnalogExpressionManager();
        }

        if (type == AudioManager.class) {
            return DefaultAudioManager.instance();
        }

        if (type == ClockControl.class) {
            return new DefaultClockControl();
        }

        if (type == ConditionalManager.class) {
            return new DefaultConditionalManager();
        }

        if (type == DigitalExpressionManager.class) {
            return new DefaultDigitalExpressionManager();
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

        if (type == LogixNG_Manager.class) {
            return new DefaultLogixNGManager();
        }

        if (type == LogixNGPreferences.class) {
            return new LogixNGPreferences();
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

        if (type == StringActionManager.class) {
            return new DefaultStringActionManager();
        }

        if (type == StringExpressionManager.class) {
            return new DefaultStringExpressionManager();
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

        return super.getDefault(type);
    }

    @Override
    public Set<Class<?>> getInitalizes() {
        Set<Class<?>> set = super.getInitalizes();
        set.addAll(Arrays.asList(ActionManager.class,
                AnalogActionManager.class,
                AnalogExpressionManager.class,
                AudioManager.class,
                BlockManager.class,
                ClockControl.class,
                ConditionalManager.class,
                DigitalExpressionManager.class,
                IdTagManager.class,
                LightManager.class,
                LogixManager.class,
                MemoryManager.class,
                LogixNG_Manager.class,
                LogixNGPreferences.class,
                RailComManager.class,
                ReporterManager.class,
                RouteManager.class,
                SensorManager.class,
                SignalGroupManager.class,
                SignalHeadManager.class,
                SignalMastLogicManager.class,
                SignalMastManager.class,
                SignalSystemManager.class,
                StringActionManager.class,
                StringExpressionManager.class,
                Timebase.class,
                TurnoutManager.class,
                VSDecoderManager.class
        ));
        return set;
    }

}
