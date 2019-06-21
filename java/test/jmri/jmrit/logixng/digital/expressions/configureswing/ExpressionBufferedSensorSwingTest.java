package jmri.jmrit.logixng.digital.expressions.configureswing;

import java.awt.GraphicsEnvironment;
import javax.swing.JPanel;
import jmri.util.JUnitUtil;
import jmri.jmrit.logixng.digital.expressions.ExpressionBufferedSensor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionSensor
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionBufferedSensorSwingTest {

    @Test
    public void testCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        ExpressionBufferedSensorSwing t = new ExpressionBufferedSensorSwing();
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testPanel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        ExpressionBufferedSensorSwing t = new ExpressionBufferedSensorSwing();
        JPanel panel = t.getConfigPanel();
        Assert.assertNotNull("exists",panel);
    }
    
    @Test
    public void testCreatePanel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        Assert.assertTrue("panel is not null",
            null != new ExpressionBufferedSensorSwing().getConfigPanel());
        Assert.assertTrue("panel is not null",
            null != new ExpressionBufferedSensorSwing().getConfigPanel(new ExpressionBufferedSensor("IQ1:DA1")));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
