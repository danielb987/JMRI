package jmri.jmrit.logixng.string.implementation;

import jmri.jmrit.logixng.implementation.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrit.logixng.string.implementation.configurexml.PackageTest.class,
    DefaultMaleStringActionSocketTest.class,
    DefaultMaleStringExpressionSocketTest.class,
    DefaultLogixNGTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.logixng.engine tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {

}
