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
        Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixExpression>>> expressionClasses = new HashSet<>();
        expressionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.COMMON, ExpressionAnd.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.OTHER, ExpressionHold.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.OTHER, ExpressionResetOnTrue.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.COMMON, ExpressionTimer.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.OTHER, ExpressionTriggerOnce.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(NewLogixCategory.ITEM, ExpressionTurnout.class));
        return expressionClasses;
    }

}
