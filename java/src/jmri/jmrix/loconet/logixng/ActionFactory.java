package jmri.jmrix.loconet.logixng;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import jmri.jmrit.logixng.DigitalActionFactory;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for LogixNG LocoNet classes.
 */
@ServiceProvider(service = DigitalActionFactory.class)
public class ActionFactory implements DigitalActionFactory {

    @Override
    public void init() {
        CategoryLocoNet.registerCategory();
    }
    
    @Override
    public Set<ClassInfo> getActionClasses() {
        Set<ClassInfo> actionClasses = new HashSet<>();
        
        // We don't want to add these classes if we don't have a LocoNet connection
        if (CategoryLocoNet.hasLocoNet()) {
            actionClasses.add(new ClassInfo(CategoryLocoNet.LOCONET, ActionClearSlots.class, Bundle.getMessage("ActionClearSlots_Description"), Bundle.getMessage("ActionClearSlots_Description", Locale.US)));
            actionClasses.add(new ClassInfo(CategoryLocoNet.LOCONET, ActionUpdateSlots.class, Bundle.getMessage("ActionUpdateSlots_Description"), Bundle.getMessage("ActionUpdateSlots_Description", Locale.US)));
        }
        
        return actionClasses;
    }
    
}
