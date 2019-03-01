package jmri.jmrit.logixng.digitalactions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.digitalactions.configurexml.PackageTest.class,
    DigitalActionPluginSocketTest.class,
    DoAnalogActionTest.class,
    DoStringActionTest.class,
    IfThenElseTest.class,
    ManyTest.class,
    SocketTest.class,
    ActionTurnoutTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng.actions tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
