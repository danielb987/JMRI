package jmri.jmrit.newlogix.actions;

import jmri.jmrit.newlogix.actions.ActionTurnout;
import jmri.jmrit.newlogix.actions.ActionIfThenElse;
import jmri.jmrit.newlogix.actions.ActionMany;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.jmrit.newlogix.NewLogixActionFactory;
import jmri.jmrit.newlogix.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.newlogix.Action;

/**
 * The factory for Action classes.
 */
@ServiceProvider(service = NewLogixActionFactory.class)
public class Factory implements NewLogixActionFactory {

    @Override
    public Set<Map.Entry<Category, Class<? extends Action>>> getActionClasses() {
        Set<Map.Entry<Category, Class<? extends Action>>> actionClasses = new HashSet<>();
        actionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, ActionIfThenElse.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, ActionMany.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(Category.ITEM, ActionTurnout.class));
        return actionClasses;
    }

}
