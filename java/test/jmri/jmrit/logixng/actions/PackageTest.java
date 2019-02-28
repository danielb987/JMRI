package jmri.jmrit.logixng.actions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.actions.configurexml.PackageTest.class,
    ActionDoAnalogActionTest.class,
    ActionDoStringActionTest.class,
    ActionIfThenElseTest.class,
    ActionManyTest.class,
    ActionPluginSocketTest.class,
    ActionSocketTest.class,
    ActionTurnoutTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng.actions tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
