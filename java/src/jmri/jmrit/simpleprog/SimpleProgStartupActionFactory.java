package jmri.jmrit.simpleprog;

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
public final class SimpleProgStartupActionFactory extends AbstractStartupActionFactory {

    @Override
    public String getTitle(Class<?> clazz, @Nullable Locale locale) throws IllegalArgumentException {    // Spotbugs warns that Locale.ENGLISH may be null
        if (clazz.equals(SimpleProgAction.class)) {
            return Bundle.getMessage(locale, "StartupSimpleProgAction");
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Class<?>[] getActionClasses() {
        return new Class[]{SimpleProgAction.class};
    }
    
}
