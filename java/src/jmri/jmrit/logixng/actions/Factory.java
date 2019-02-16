package jmri.jmrit.logixng.actions;

import jmri.jmrit.logixng.actions.ActionTurnout;
import jmri.jmrit.logixng.actions.ActionIfThenElse;
import jmri.jmrit.logixng.actions.ActionMany;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.jmrit.logixng.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.ActionFactory;

/**
 * The factory for Action classes.
 */
@ServiceProvider(service = ActionFactory.class)
public class Factory implements ActionFactory {

    @Override
    public Set<Map.Entry<Category, Class<? extends Action>>> getActionClasses() {
        Set<Map.Entry<Category, Class<? extends Action>>> actionClasses = new HashSet<>();
        actionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, ActionIfThenElse.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, ActionMany.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(Category.ITEM, ActionTurnout.class));
        return actionClasses;
    }

}
