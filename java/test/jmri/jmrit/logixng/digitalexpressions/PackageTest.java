package jmri.jmrit.logixng.digitalexpressions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.digitalexpressions.configurexml.PackageTest.class,
    AndTest.class,
    BufferedSensorTest.class,
    HoldTest.class,
    ResetOnTrueTest.class,
    TimerTest.class,
    TriggerOnceTest.class,
    ExpressionTurnoutTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng.expressions tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
