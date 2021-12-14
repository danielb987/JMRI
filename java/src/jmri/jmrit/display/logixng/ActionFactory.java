package jmri.jmrit.display.logixng;

import java.util.HashSet;
import java.util.Set;

import jmri.jmrit.logixng.DigitalActionFactory;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for LogixNG Display classes.
 * 
 * @author Daniel Bergqvist Copyright 2021
 */
@ServiceProvider(service = DigitalActionFactory.class)
public class ActionFactory implements DigitalActionFactory {

    @Override
    public void init() {
        CategoryDisplay.registerCategory();
    }
    
    @Override
    public Set<ClassInfo> getActionClasses() {
        Set<ClassInfo> actionClasses = new HashSet<>();
        
        actionClasses.add(new ClassInfo(CategoryDisplay.DISPLAY, ActionPositionable.class));
        
        return actionClasses;
    }
    
}
