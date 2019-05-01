package jmri.jmrit.logixng.template;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import jmri.ConditionalManager;
import jmri.LightManager;
import jmri.LogixManager;
import jmri.MemoryManager;
import jmri.RailComManager;
import jmri.ReporterManager;
import jmri.RouteManager;
import jmri.SensorManager;
import jmri.jmrit.logixng.NullNamedBeanInitializer;
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
            return new NullAudio(name);
        }

        if (type == NullSensor.class) {
            return new NullIdTag(name);
        }

        if (type == ConditionalManager.class) {
            return new NullLight(name);
        }

        if (type == LightManager.class) {
            return new NullLogix(name);
        }

        if (type == LogixManager.class) {
            return new NullMemory(name);
        }

        if (type == MemoryManager.class) {
            return new NullOBlock(name);
        }

        if (type == RailComManager.class) {
            return new NullReporter(name);
        }

        if (type == ReporterManager.class) {
            return new NullSensor(name);
        }

        if (type == RouteManager.class) {
            return new NullSignalHead(name);
        }

        if (type == SensorManager.class) {
            return new NullSignalMast(name);
        }

        if (type == NullTurnout.class) {
            return new NullTurnout(name);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Set<Class<?>> getInitalizes() {
        Set<Class<?>> set = new HashSet<>();
        set.addAll(Arrays.asList(
                NullAudio.class,
                NullIdTag.class,
                NullLight.class,
                NullLogix.class,
                NullMemory.class,
                NullOBlock.class,
                NullReporter.class,
                NullSensor.class,
                NullSignalHead.class,
                NullSignalMast.class,
                NullTurnout.class
        ));
        return set;
    }

}
