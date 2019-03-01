package jmri.jmrit.logixng.engine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.engine.configurexml.PackageTest.class,
    DefaultFemaleDigitalExpressionSocketTest.class,
    DefaultMaleDigitalActionSocketTest.class,
    DefaultMaleAnalogActionSocketTest.class,
    DefaultMaleAnalogExpressionSocketTest.class,
    DefaultMaleDigitalExpressionSocketTest.class,
    DefaultMaleStringActionSocketTest.class,
    DefaultMaleStringExpressionSocketTest.class,
    DigitalExpressionPluginAdapterTest.class,
    DefaultLogixNGTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng.engine tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {

}
