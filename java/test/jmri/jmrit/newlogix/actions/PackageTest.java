package jmri.jmrit.newlogix.actions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.newlogix.actions.configurexml.PackageTest.class,
    ActionDoIfTest.class,
    ActionManyTest.class,
    ActionSocketTest.class,
    ActionTurnoutTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.newlogix tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
