package jmri.jmrit.logixng.engine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.engine.configurexml.PackageTest.class,
    DefaultLogixNGTest.class,
    DefaultMaleActionSocketTest.class,
    DefaultMaleAnalogActionSocketTest.class,
    DefaultMaleAnalogExpressionSocketTest.class,
    DefaultMaleExpressionSocketTest.class,
    DefaultMaleStringActionSocketTest.class,
    DefaultMaleStringExpressionSocketTest.class,
    ExpressionPluginAdapterTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng.engine tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {

}
