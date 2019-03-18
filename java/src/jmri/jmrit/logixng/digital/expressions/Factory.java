package jmri.jmrit.logixng.digital.expressions;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmri.jmrit.logixng.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionFactory;

/**
 * The factory for DigitalExpression classes.
 */
@ServiceProvider(service = DigitalExpressionFactory.class)
public class Factory implements DigitalExpressionFactory {

    @Override
    public Set<Map.Entry<Category, Class<? extends DigitalExpression>>> getExpressionClasses() {
        Set<Map.Entry<Category, Class<? extends DigitalExpression>>> expressionClasses = new HashSet<>();
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, And.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.OTHER, Hold.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.OTHER, ResetOnTrue.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.COMMON, Timer.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.OTHER, TriggerOnce.class));
        expressionClasses.add(new AbstractMap.SimpleEntry<>(Category.ITEM, ExpressionTurnout.class));
        return expressionClasses;
    }

}
