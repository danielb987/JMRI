package jmri.daniel;

import java.util.Locale;
import jmri.util.startup.AbstractStartupActionFactory;
import jmri.util.startup.StartupActionFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author daniel
 */
@ServiceProvider(service = StartupActionFactory.class)
public class StartupFactory extends AbstractStartupActionFactory {

    @Override
    public String getTitle(Class<?> clazz, Locale locale) throws IllegalArgumentException {
        if (clazz.equals(DanielAction.class)) {
            return "Daniel";
        }
        throw new IllegalArgumentException(clazz.getName() + " is not supported by " + this.getClass().getName());
    }

    @Override
    public Class<?>[] getActionClasses() {
        return new Class[]{DanielAction.class};
    }
    
    
    public static class DanielAction extends javax.swing.AbstractAction {
        
        public void actionPerformed(java.awt.event.ActionEvent e) {
            new TreeListener();
        }
    }

}
