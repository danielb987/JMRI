package jmri.jmrit.logixng.analogactions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.actions.configurexml.PackageTest.class,
    AnalogActionSetAnalogIOTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.newlogix tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
