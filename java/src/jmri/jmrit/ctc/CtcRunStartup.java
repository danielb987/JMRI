package jmri.jmrit.ctc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jmri.util.startup.AbstractStartupActionFactory;
import jmri.util.startup.StartupActionFactory;
import java.util.Locale;
import org.openide.util.lookup.ServiceProvider;

/**
 * {@link jmri.util.startup.StartupActionFactory} for the
 * {@link jmri.jmrit.ctc.editor.CtcEditorAction}.
 *
 * @author Dave Sand Copyright (C) 2018
 */
@ServiceProvider(service = StartupActionFactory.class)
public class CtcRunStartup extends AbstractStartupActionFactory {

    @Override
    public String getTitle(Class<?> clazz, @Nullable Locale locale) throws IllegalArgumentException {    // Spotbugs warns that Locale.ENGLISH may be null
        if (!clazz.equals(CtcRunAction.class)) {
            throw new IllegalArgumentException();
        }
        return Bundle.getMessage(locale, "CtcRunAction"); // NOI18N
    }

    @Override
    public Class<?>[] getActionClasses() {
        return new Class[]{CtcRunAction.class};
    }

}
