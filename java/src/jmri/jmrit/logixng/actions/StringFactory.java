package jmri.jmrit.logixng.actions;

import java.util.HashSet;
import java.util.Set;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.StringActionFactory;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for StringAction classes.
 */
@ServiceProvider(service = StringActionFactory.class)
public class StringFactory implements StringActionFactory {

    @Override
    public Set<ClassInfo> getClasses() {
        Set<ClassInfo> stringActionClasses = new HashSet<>();
        stringActionClasses.add(new ClassInfo(Category.ITEM, StringActionMemory.class));
        stringActionClasses.add(new ClassInfo(Category.ITEM, StringActionStringIO.class));
        stringActionClasses.add(new ClassInfo(Category.COMMON, StringMany.class));
        return stringActionClasses;
    }

}
