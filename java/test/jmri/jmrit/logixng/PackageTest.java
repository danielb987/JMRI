package jmri.jmrit.logixng;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.analogactions.PackageTest.class,
    jmri.jmrit.logixng.analog.expressions.PackageTest.class,
    jmri.jmrit.logixng.implementation.PackageTest.class,
    jmri.jmrit.logixng.digital.actions.PackageTest.class,
    jmri.jmrit.logixng.digital.expressions.PackageTest.class,
    jmri.jmrit.logixng.log.digital.PackageTest.class,
    jmri.jmrit.logixng.stringactions.PackageTest.class,
    jmri.jmrit.logixng.stringexpressions.PackageTest.class,
    jmri.jmrit.logixng.tools.swing.PackageTest.class,
    DigitalExpressionTest.class,
    LogixNGCategoryTest.class,
    LogixNGTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
