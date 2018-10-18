package jmri.jmrit.newlogix;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.NewLogixExpression;
import jmri.NewLogixExpressionFactory;
import jmri.NewLogixCategory;
import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for NewLogixAction classes.
 */
@ServiceProvider(service = NewLogixExpressionFactory.class)
public class DefaultExpressionFactory implements NewLogixExpressionFactory {

    @Override
    public Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixExpression>>> getExpressionClasses() {
        Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixExpression>>> actionClasses = new HashSet<>();
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.COMMON, ExpressionAnd.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.OTHER, ExpressionHold.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.OTHER, ExpressionResetOnTrue.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.COMMON, ExpressionTimer.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.OTHER, ExpressionTriggerOnce.class));
        actionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.ITEM, ExpressionTurnout.class));
        return actionClasses;
    }

}
