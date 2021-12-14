package jmri.jmrit.logixng.expressions;

import java.util.HashSet;
import java.util.Set;
import jmri.jmrit.logixng.Category;
import org.openide.util.lookup.ServiceProvider;
import jmri.jmrit.logixng.DigitalExpressionFactory;

/**
 * The factory for DigitalExpressionBean classes.
 */
@ServiceProvider(service = DigitalExpressionFactory.class)
public class DigitalFactory implements DigitalExpressionFactory {

    @Override
    public Set<ClassInfo> getExpressionClasses() {
        Set<ClassInfo> expressionClasses = new HashSet<>();
        expressionClasses.add(new ClassInfo(Category.COMMON, And.class));
        expressionClasses.add(new ClassInfo(Category.COMMON, Antecedent.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, DigitalCallModule.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionBlock.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionClock.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionConditional.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionDispatcher.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionEntryExit.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionLight.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionLocalVariable.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionMemory.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionOBlock.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionPower.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionReference.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionReporter.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionScript.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSensor.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSignalHead.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSignalMast.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionTurnout.class));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionWarrant.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, False.class));
        expressionClasses.add(new ClassInfo(Category.COMMON, DigitalFormula.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, Hold.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, LastResultOfDigitalExpression.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, LogData.class));
        expressionClasses.add(new ClassInfo(Category.COMMON, Not.class));
        expressionClasses.add(new ClassInfo(Category.COMMON, Or.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, TriggerOnce.class));
        expressionClasses.add(new ClassInfo(Category.OTHER, True.class));
        return expressionClasses;
    }

}
