package jmri.jmrit.logixng.actions;

import java.util.HashSet;
import java.util.Set;

import jmri.jmrit.logixng.Category;

import org.openide.util.lookup.ServiceProvider;

import jmri.jmrit.logixng.DigitalBooleanActionFactory;

/**
 * The factory for DigitalAction classes.
 */
@ServiceProvider(service = DigitalBooleanActionFactory.class)
public class DigitalBooleanFactory implements DigitalBooleanActionFactory {

    @Override
    public Set<ClassInfo> getClasses() {
        Set<ClassInfo> digitalBooleanActionClasses = new HashSet<>();
        digitalBooleanActionClasses.add(new ClassInfo(Category.COMMON, DigitalBooleanMany.class));
        digitalBooleanActionClasses.add(new ClassInfo(Category.COMMON, DigitalBooleanOnChange.class));
        return digitalBooleanActionClasses;
    }

}
