package jmri.jmrit.logixng.expressions;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.DigitalExpressionFactory;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for DigitalExpressionBean classes.
 */
@ServiceProvider(service = DigitalExpressionFactory.class)
public class DigitalFactory implements DigitalExpressionFactory {

    @Override
    public Set<ClassInfo> getExpressionClasses() {
        Set<ClassInfo> expressionClasses = new HashSet<>();
        expressionClasses.add(new ClassInfo(Category.COMMON, And.class, Bundle.getMessage("And_Description"), Bundle.getMessage("And_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.COMMON, Antecedent.class, Bundle.getMessage("Antecedent_Description"), Bundle.getMessage("Antecedent_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, DigitalCallModule.class, Bundle.getMessage("DigitalCallModule_Description"), Bundle.getMessage("DigitalCallModule_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionBlock.class, Bundle.getMessage("Block_Description"), Bundle.getMessage("Block_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionClock.class, Bundle.getMessage("Clock_Long_Description"), Bundle.getMessage("Clock_Long_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionConditional.class, Bundle.getMessage("Conditional_Description"), Bundle.getMessage("Conditional_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionDispatcher.class, Bundle.getMessage("Dispatcher_Description"), Bundle.getMessage("Dispatcher_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionEntryExit.class, Bundle.getMessage("EntryExit_Description"), Bundle.getMessage("EntryExit_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionLight.class, Bundle.getMessage("Light_Description"), Bundle.getMessage("Light_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionLocalVariable.class, Bundle.getMessage("LocalVariable_Description"), Bundle.getMessage("LocalVariable_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionMemory.class, Bundle.getMessage("Memory_Description"), Bundle.getMessage("Memory_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionOBlock.class, Bundle.getMessage("OBlock_Description"), Bundle.getMessage("OBlock_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionPower.class, Bundle.getMessage("Power_Description"), Bundle.getMessage("Power_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionReference.class, Bundle.getMessage("Reference_Description"), Bundle.getMessage("Reference_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionReporter.class, Bundle.getMessage("Reporter_Description"), Bundle.getMessage("Reporter_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionScript.class, Bundle.getMessage("ExpressionScript_Description"), Bundle.getMessage("ExpressionScript_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSensor.class, Bundle.getMessage("Sensor_Description"), Bundle.getMessage("Sensor_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSignalHead.class, Bundle.getMessage("SignalHead_Description"), Bundle.getMessage("SignalHead_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionSignalMast.class, Bundle.getMessage("SignalMast_Description"), Bundle.getMessage("SignalMast_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionTurnout.class, Bundle.getMessage("Turnout_Description"), Bundle.getMessage("Turnout_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.ITEM, ExpressionWarrant.class, Bundle.getMessage("Warrant_Description"), Bundle.getMessage("Warrant_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, False.class, Bundle.getMessage("False_Description"), Bundle.getMessage("False_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.COMMON, DigitalFormula.class, Bundle.getMessage("DigitalFormula_Description"), Bundle.getMessage("DigitalFormula_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, Hold.class, Bundle.getMessage("Hold_Description"), Bundle.getMessage("Hold_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, LastResultOfDigitalExpression.class, Bundle.getMessage("LastResultOfDigitalExpression_Description"), Bundle.getMessage("LastResultOfDigitalExpression_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, LogData.class, Bundle.getMessage("LogData_Description"), Bundle.getMessage("LogData_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.COMMON, Not.class, Bundle.getMessage("Not_Description"), Bundle.getMessage("Not_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.COMMON, Or.class, Bundle.getMessage("Or_Description"), Bundle.getMessage("Or_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, TriggerOnce.class, Bundle.getMessage("TriggerOnce_Description"), Bundle.getMessage("TriggerOnce_Description", Locale.US)));
        expressionClasses.add(new ClassInfo(Category.OTHER, True.class, Bundle.getMessage("True_Description"), Bundle.getMessage("True_Description", Locale.US)));
        return expressionClasses;
    }

}
