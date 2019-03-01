package jmri.jmrit.logixng.digitalactions;

import jmri.jmrit.logixng.digitalactions.Turnout;
import jmri.jmrit.logixng.digitalactions.IfThen;
import jmri.jmrit.logixng.digitalactions.Many;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.jmrit.logixng.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionFactory;

/**
 * The factory for DigitalAction classes.
 */
@ServiceProvider(service = DigitalActionFactory.class)
public class Factory implements DigitalActionFactory {

    @Override
    public Set<Map.Entry<Category, Class<? extends DigitalAction>>> getActionClasses() {
        Set<Map.Entry<Category, Class<? extends DigitalAction>>> digitalActionClasses = new HashSet<>();
        digitalActionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, IfThen.class));
        digitalActionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, Many.class));
        digitalActionClasses.add(new AbstractMap.SimpleEntry<>(Category.ITEM, Turnout.class));
        return digitalActionClasses;
    }

}
