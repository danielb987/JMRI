package jmri.jmrit.newlogix;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.NewLogixAction;
import jmri.NewLogixActionFactory;
import jmri.NewLogixCategory;
import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for NewLogixAction classes.
 */
@ServiceProvider(service = NewLogixActionFactory.class)
public class DefaultActionFactory implements NewLogixActionFactory {

    @Override
    public Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixAction>>> getActionClasses() {
        Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixAction>>> actionClasses = new HashSet<>();
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.COMMON, ActionDoIf.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.COMMON, ActionMany.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.ITEM, ActionTurnout.class));
        return actionClasses;
    }

}
