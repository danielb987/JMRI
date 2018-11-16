package jmri.jmrit.newlogix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.newlogix.actions.PackageTest.class,
    jmri.jmrit.newlogix.analogactions.PackageTest.class,
    jmri.jmrit.newlogix.analogexpressions.PackageTest.class,
    jmri.jmrit.newlogix.engine.PackageTest.class,
    jmri.jmrit.newlogix.expressions.PackageTest.class,
    jmri.jmrit.newlogix.log.PackageTest.class,
    jmri.jmrit.newlogix.tools.swing.PackageTest.class,
    NewLogixTest.class,
    ExpressionTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.newlogix tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
