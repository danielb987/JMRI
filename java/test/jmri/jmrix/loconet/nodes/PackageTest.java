package jmri.jmrix.loconet.nodes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    jmri.jmrix.loconet.nodes.swing.PackageTest.class,
    BundleTest.class,
    LnAnalogIOTest.class,
    LnNodeManagerTest.class,
    LnStringIOTest.class,
})
/**
 * Tests for the jmri.jmrix.loconet.node package.
 *
 * @author Paul Bender Copyright (C) 2016
 */
public class PackageTest {
}
