package jmri.jmrit.logixng.actions;

import java.util.HashSet;
import java.util.Set;

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
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionAudio.class, Bundle.getMessage("ActionAudio_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionBlock.class, Bundle.getMessage("ActionBlock_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionClock.class, Bundle.getMessage("ActionClock_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionDispatcher.class, Bundle.getMessage("ActionDispatcher_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionEntryExit.class, Bundle.getMessage("ActionEntryExit_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLight.class, Bundle.getMessage("Light_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLightIntensity.class, Bundle.getMessage("ActionLightIntensity_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ActionListenOnBeans.class, Bundle.getMessage("ActionListenOnBeans_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ActionListenOnBeansTable.class, Bundle.getMessage("ActionListenOnBeansTable_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLocalVariable.class, Bundle.getMessage("ActionLocalVariable_Long_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionMemory.class, Bundle.getMessage("ActionMemory_Long_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionOBlock.class, Bundle.getMessage("ActionOBlock_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionPower.class, Bundle.getMessage("Power_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionReporter.class, Bundle.getMessage("ActionReporter_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionScript.class, Bundle.getMessage("ActionScript_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSensor.class, Bundle.getMessage("Sensor_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSignalHead.class, Bundle.getMessage("SignalHead_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSignalMast.class, Bundle.getMessage("SignalMast_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSound.class, Bundle.getMessage("ActionSound_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionThrottle.class, Bundle.getMessage("ActionThrottle_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, ActionTimer.class, Bundle.getMessage("ActionTimer_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionTurnout.class, Bundle.getMessage("Turnout_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionTurnoutLock.class, Bundle.getMessage("TurnoutLock_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionWarrant.class, Bundle.getMessage("ActionWarrant_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, DigitalCallModule.class, Bundle.getMessage("DigitalCallModule_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DigitalFormula.class, Bundle.getMessage("DigitalFormula_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DoAnalogAction.class, Bundle.getMessage("DoAnalogAction_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DoStringAction.class, Bundle.getMessage("DoStringAction_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, EnableLogix.class, Bundle.getMessage("EnableLogix_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, ExecuteDelayed.class, Bundle.getMessage("ExecuteDelayed_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, For.class, Bundle.getMessage("For_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, IfThenElse.class, Bundle.getMessage("IfThenElse_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, Logix.class, Bundle.getMessage("Logix_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, LogData.class, Bundle.getMessage("LogData_Long_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, LogLocalVariables.class, Bundle.getMessage("LogLocalVariables_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DigitalMany.class, Bundle.getMessage("DigitalMany_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, Sequence.class, Bundle.getMessage("Sequence_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ShowDialog.class, Bundle.getMessage("ShowDialog_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ShutdownComputer.class, Bundle.getMessage("ShutdownComputer_Description")));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, TableForEach.class, Bundle.getMessage("TableForEach_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, Timeout.class, Bundle.getMessage("Timeout_Description")));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, TriggerRoute.class, Bundle.getMessage("TriggerRoute_Description")));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, WebBrowser.class, Bundle.getMessage("WebBrowser_Description")));
        return digitalActionClasses;
    }

}
