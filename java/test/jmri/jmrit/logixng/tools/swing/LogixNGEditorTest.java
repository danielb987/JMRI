package jmri.jmrit.logixng.tools.swing;

import jmri.jmrit.logixng.tools.swing.LogixNGEditor;
import java.awt.GraphicsEnvironment;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Test LogixNGEditor
 * 
 * @author Daniel Bergqvist 2018
 */
public class LogixNGEditorTest {

    @Test
    public void testCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        new LogixNGEditor();
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
