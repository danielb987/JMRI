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
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionAudio.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionBlock.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionClock.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionDispatcher.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionEntryExit.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLight.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLightIntensity.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ActionListenOnBeans.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ActionListenOnBeansTable.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionLocalVariable.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionMemory.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionOBlock.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionPower.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionReporter.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionScript.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSensor.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSignalHead.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSignalMast.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionSound.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionThrottle.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, ActionTimer.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionTurnout.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionTurnoutLock.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, ActionWarrant.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, DigitalCallModule.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DigitalFormula.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DoAnalogAction.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DoStringAction.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, EnableLogix.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, ExecuteDelayed.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, For.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, IfThenElse.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, Logix.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, LogData.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, LogLocalVariables.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, DigitalMany.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, Sequence.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ShowDialog.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, ShutdownComputer.class));
        digitalActionClasses.add(new ClassInfo(Category.COMMON, TableForEach.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, Timeout.class));
        digitalActionClasses.add(new ClassInfo(Category.ITEM, TriggerRoute.class));
        digitalActionClasses.add(new ClassInfo(Category.OTHER, WebBrowser.class));
        return digitalActionClasses;
    }

}
