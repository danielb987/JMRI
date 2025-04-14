package jmri.jmrit.logixng.configurexml2;


import jmri.*;
import jmri.jmrit.logixng.*;
import jmri.util.JUnitUtil;

import org.junit.jupiter.api.*;

/**
 * Test.
 *
 * @author Daniel Bergqvist 2025
 */
public class TestTest {

    @Test
    public void testFibonacci() {
    }

    // The minimal setup for log4J
    @BeforeEach
    public void setUp() throws SocketAlreadyConnectedException, JmriException {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        NamedBeanType.Audio.name();
    }

    @AfterEach
    public void tearDown() {
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }

}
