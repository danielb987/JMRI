package jmri.jmrit.newlogix.expressions;

import jmri.jmrit.newlogix.expressions.ExpressionAnd;
import jmri.jmrit.newlogix.expressions.ExpressionTriggerOnce;
import jmri.jmrit.newlogix.expressions.ExpressionTimer;
import jmri.jmrit.newlogix.expressions.ExpressionResetOnTrue;
import jmri.jmrit.newlogix.expressions.ExpressionTurnout;
import jmri.jmrit.newlogix.expressions.ExpressionHold;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.jmrit.newlogix.NewLogixExpressionFactory;
import jmri.jmrit.newlogix.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.newlogix.Expression;

/**
 * The factory for NewLogixAction classes.
 */
@ServiceProvider(service = NewLogixExpressionFactory.class)
public class Factory implements NewLogixExpressionFactory {

    @Override
    public Set<Map.Entry<Category, Class<? extends Expression>>> getExpressionClasses() {
        Set<Map.Entry<Category, Class<? extends Expression>>> expressionClasses = new HashSet<>();
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, ExpressionAnd.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.OTHER, ExpressionHold.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.OTHER, ExpressionResetOnTrue.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, ExpressionTimer.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.OTHER, ExpressionTriggerOnce.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.ITEM, ExpressionTurnout.class));
        return expressionClasses;
    }

}
