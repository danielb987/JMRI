package jmri.jmrit.logixng.expressions;

import jmri.jmrit.logixng.expressions.ExpressionAnd;
import jmri.jmrit.logixng.expressions.ExpressionTriggerOnce;
import jmri.jmrit.logixng.expressions.ExpressionTimer;
import jmri.jmrit.logixng.expressions.ExpressionResetOnTrue;
import jmri.jmrit.logixng.expressions.ExpressionTurnout;
import jmri.jmrit.logixng.expressions.ExpressionHold;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.jmrit.logixng.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.logixng.Expression;
import jmri.jmrit.logixng.ExpressionFactory;

/**
 * The factory for Expression classes.
 */
@ServiceProvider(service = ExpressionFactory.class)
public class Factory implements ExpressionFactory {

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
