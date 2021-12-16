package jmri.jmrit.logixng.actions;

import java.util.*;

import jmri.jmrit.logixng.*;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for DigitalAction classes.
 */
@ServiceProvider(service = DigitalActionFactory.class)
public class DigitalFactory implements DigitalActionFactory {

    @Override
    public Set<ClassInfo> getActionClasses() {
        Set<ClassInfo> digitalActionClasses = new HashSet<>();
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionAudio.class, Bundle.getMessage("ActionAudio_Description"), Bundle.getMessage("ActionAudio_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionBlock.class, Bundle.getMessage("ActionBlock_Description"), Bundle.getMessage("ActionBlock_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionClock.class, Bundle.getMessage("ActionClock_Description"), Bundle.getMessage("ActionClock_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionDispatcher.class, Bundle.getMessage("ActionDispatcher_Description"), Bundle.getMessage("ActionDispatcher_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionEntryExit.class, Bundle.getMessage("ActionEntryExit_Description"), Bundle.getMessage("ActionEntryExit_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLight.class, Bundle.getMessage("Light_Description"), Bundle.getMessage("Light_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLightIntensity.class, Bundle.getMessage("ActionLightIntensity_Description"), Bundle.getMessage("ActionLightIntensity_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ActionListenOnBeans.class, Bundle.getMessage("ActionListenOnBeans_Description"), Bundle.getMessage("ActionListenOnBeans_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ActionListenOnBeansTable.class, Bundle.getMessage("ActionListenOnBeansTable_Description"), Bundle.getMessage("ActionListenOnBeansTable_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLocalVariable.class, Bundle.getMessage("ActionLocalVariable_Long_Description"), Bundle.getMessage("ActionLocalVariable_Long_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionMemory.class, Bundle.getMessage("ActionMemory_Long_Description"), Bundle.getMessage("ActionMemory_Long_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionOBlock.class, Bundle.getMessage("ActionOBlock_Description"), Bundle.getMessage("ActionOBlock_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionPower.class, Bundle.getMessage("Power_Description"), Bundle.getMessage("Power_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionReporter.class, Bundle.getMessage("ActionReporter_Description"), Bundle.getMessage("ActionReporter_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionScript.class, Bundle.getMessage("ActionScript_Description"), Bundle.getMessage("ActionScript_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSensor.class, Bundle.getMessage("Sensor_Description"), Bundle.getMessage("Sensor_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSignalHead.class, Bundle.getMessage("SignalHead_Description"), Bundle.getMessage("SignalHead_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSignalMast.class, Bundle.getMessage("SignalMast_Description"), Bundle.getMessage("SignalMast_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSound.class, Bundle.getMessage("ActionSound_Description"), Bundle.getMessage("ActionSound_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionThrottle.class, Bundle.getMessage("ActionThrottle_Description"), Bundle.getMessage("ActionThrottle_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, ActionTimer.class, Bundle.getMessage("ActionTimer_Description"), Bundle.getMessage("ActionTimer_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionTurnout.class, Bundle.getMessage("Turnout_Description"), Bundle.getMessage("Turnout_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionTurnoutLock.class, Bundle.getMessage("TurnoutLock_Description"), Bundle.getMessage("TurnoutLock_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionWarrant.class, Bundle.getMessage("ActionWarrant_Description"), Bundle.getMessage("ActionWarrant_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, DigitalCallModule.class, Bundle.getMessage("DigitalCallModule_Description"), Bundle.getMessage("DigitalCallModule_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DigitalFormula.class, Bundle.getMessage("DigitalFormula_Description"), Bundle.getMessage("DigitalFormula_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DoAnalogAction.class, Bundle.getMessage("DoAnalogAction_Description"), Bundle.getMessage("DoAnalogAction_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DoStringAction.class, Bundle.getMessage("DoStringAction_Description"), Bundle.getMessage("DoStringAction_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, EnableLogix.class, Bundle.getMessage("EnableLogix_Description"), Bundle.getMessage("EnableLogix_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, ExecuteDelayed.class, Bundle.getMessage("ExecuteDelayed_Description"), Bundle.getMessage("ExecuteDelayed_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, For.class, Bundle.getMessage("For_Description"), Bundle.getMessage("For_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, IfThenElse.class, Bundle.getMessage("IfThenElse_Description"), Bundle.getMessage("IfThenElse_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, Logix.class, Bundle.getMessage("Logix_Description"), Bundle.getMessage("Logix_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, LogData.class, Bundle.getMessage("LogData_Long_Description"), Bundle.getMessage("LogData_Long_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, LogLocalVariables.class, Bundle.getMessage("LogLocalVariables_Description"), Bundle.getMessage("LogLocalVariables_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DigitalMany.class, Bundle.getMessage("DigitalMany_Description"), Bundle.getMessage("DigitalMany_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, Sequence.class, Bundle.getMessage("Sequence_Description"), Bundle.getMessage("Sequence_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ShowDialog.class, Bundle.getMessage("ShowDialog_Description"), Bundle.getMessage("ShowDialog_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ShutdownComputer.class, Bundle.getMessage("ShutdownComputer_Description"), Bundle.getMessage("ShutdownComputer_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, TableForEach.class, Bundle.getMessage("TableForEach_Description"), Bundle.getMessage("TableForEach_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, Timeout.class, Bundle.getMessage("Timeout_Description"), Bundle.getMessage("Timeout_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, TriggerRoute.class, Bundle.getMessage("TriggerRoute_Description"), Bundle.getMessage("TriggerRoute_Description", Locale.US)));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, WebBrowser.class, Bundle.getMessage("WebBrowser_Description"), Bundle.getMessage("WebBrowser_Description", Locale.US)));
        return digitalActionClasses;
    }

}
