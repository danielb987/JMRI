package jmri.jmrit.ctc.editor;

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
public final class CtcEditorStartup extends AbstractStartupActionFactory {

    @Override
    public String getTitle(Class<?> clazz, @Nullable Locale locale) throws IllegalArgumentException {    // Spotbugs warns that Locale.ENGLISH may be null
        if (clazz.equals(CtcEditorAction.class)) {
            return Bundle.getMessage(locale, "CtcEditorAction"); // NOI18N
        }
        throw new IllegalArgumentException(clazz.getName() + " is not supported by " + this.getClass().getName());
    }

    @Override
    public Class<?>[] getActionClasses() {
        return new Class[]{CtcEditorAction.class};
    }

}
