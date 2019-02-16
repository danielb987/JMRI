package jmri.jmrit.logixng.expressions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.expressions.configurexml.PackageTest.class,
    ExpressionAndTest.class,
    ExpressionBufferedSensorTest.class,
    ExpressionHoldTest.class,
    ExpressionResetOnTrueTest.class,
    ExpressionSocketTest.class,
    ExpressionTimerTest.class,
    ExpressionTriggerOnceTest.class,
    ExpressionTurnoutTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.newlogix tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
