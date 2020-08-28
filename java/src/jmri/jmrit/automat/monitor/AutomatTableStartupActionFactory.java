package jmri.jmrit.automat.monitor;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jmri.util.startup.AbstractStartupActionFactory;
import jmri.util.startup.StartupActionFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Randall Wood Copyright 2020
 */
@ServiceProvider(service = StartupActionFactory.class)
public final class AutomatTableStartupActionFactory extends AbstractStartupActionFactory {

    @Override
    public String getTitle(Class<?> clazz, @Nullable Locale locale) throws IllegalArgumentException {    // Spotbugs warns that Locale.ENGLISH may be null
        if (clazz.equals(AutomatTableAction.class)) {
            return Bundle.getMessage(locale, "StartupAutomatTableAction");
        }
        throw new IllegalArgumentException(clazz.getName() + " is not supported by " + this.getClass().getName());
    }

    @Override
    public Class<?>[] getActionClasses() {
        return new Class[]{AutomatTableAction.class};
    }
    
}
