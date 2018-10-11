package jmri.jmrit.newlogix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   jmri.jmrit.newlogix.internal.PackageTest.class,
   jmri.jmrit.newlogix.log.PackageTest.class,
   ActionDoIfTest.class,
   ActionManyTest.class,
   ActionTurnoutTest.class,
   DefaultNewLogixTest.class,
   ExpressionAndTest.class,
   ExpressionHoldTest.class,
   ExpressionResetOnTrueTest.class,
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
