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
        expressionClasses.add(new ClassInfo(Category.COMMON, And.class, Bundle.getMessage("And_Description")));
        expressionClasses.add(new ClassInfo(Category.COMMON, Antecedent.class, Bundle.getMessage("Antecedent_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, DigitalCallModule.class, Bundle.getMessage("DigitalCallModule_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionBlock.class, Bundle.getMessage("Block_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionClock.class, Bundle.getMessage("Clock_Long_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionConditional.class, Bundle.getMessage("Conditional_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionDispatcher.class, Bundle.getMessage("Dispatcher_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionEntryExit.class, Bundle.getMessage("EntryExit_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionLight.class, Bundle.getMessage("Light_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionLocalVariable.class, Bundle.getMessage("LocalVariable_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionMemory.class, Bundle.getMessage("Memory_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionOBlock.class, Bundle.getMessage("OBlock_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionPower.class, Bundle.getMessage("Power_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionReference.class, Bundle.getMessage("Reference_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionReporter.class, Bundle.getMessage("Reporter_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionScript.class, Bundle.getMessage("ExpressionScript_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSensor.class, Bundle.getMessage("Sensor_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSignalHead.class, Bundle.getMessage("SignalHead_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSignalMast.class, Bundle.getMessage("SignalMast_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionTurnout.class, Bundle.getMessage("Turnout_Description")));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionWarrant.class, Bundle.getMessage("Warrant_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, False.class, Bundle.getMessage("False_Description")));
        expressionClasses.add(new ClassInfo(Category.COMMON, DigitalFormula.class, Bundle.getMessage("DigitalFormula_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, Hold.class, Bundle.getMessage("Hold_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, LastResultOfDigitalExpression.class, Bundle.getMessage("LastResultOfDigitalExpression_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, LogData.class, Bundle.getMessage("LogData_Description")));
        expressionClasses.add(new ClassInfo(Category.COMMON, Not.class, Bundle.getMessage("Not_Description")));
        expressionClasses.add(new ClassInfo(Category.COMMON, Or.class, Bundle.getMessage("Or_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, TriggerOnce.class, Bundle.getMessage("TriggerOnce_Description")));
        expressionClasses.add(new ClassInfo(Category.OTHER, True.class, Bundle.getMessage("True_Description")));
        return expressionClasses;
    }

}
