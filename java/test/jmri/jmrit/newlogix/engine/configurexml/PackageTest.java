package jmri.jmrit.newlogix.engine.configurexml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    DefaultActionManagerXmlTest.class,
    DefaultExpressionManagerXmlTest.class,
    DefaultNewLogixManagerXmlTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.newlogix.internal tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {

}
