package jmri.jmrit.logixng.actions;

import java.util.HashSet;
import java.util.Set;
import jmri.jmrit.logixng.AnalogActionFactory;
import jmri.jmrit.logixng.Category;
import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for AnalogAction classes.
 */
@ServiceProvider(service = AnalogActionFactory.class)
public class AnalogFactory implements AnalogActionFactory {

    @Override
    public Set<ClassInfo> getClasses() {
        Set<ClassInfo> analogActionClasses = new HashSet<>();
        analogActionClasses.add(new ClassInfo(Category.ITEM, AnalogActionLightIntensity.class));
        analogActionClasses.add(new ClassInfo(Category.ITEM, AnalogActionMemory.class));
        analogActionClasses.add(new ClassInfo(Category.COMMON, AnalogMany.class));
        return analogActionClasses;
    }

}
