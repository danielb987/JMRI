package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.MemoryManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.analog.expressions.GetAnalogIO;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test GetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogExpressionGetAnalogIOTest extends AbstractAnalogExpressionTestBase {

    @Test
    public void testEvaluate() {
        AtomicBoolean isCompleted = new AtomicBoolean();
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _expression.getCategory());
    }
    
    @Test
    public void testShortDescription() {
        Assert.assertTrue("String matches", "Get analog none".equals(_expression.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Get analog none".equals(_expression.getLongDescription()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        _expression = new GetAnalogIO("IQA55:12:AE321", "AnalogIO_Memory");
    }

    @After
    public void tearDown() {
        _expression.dispose();
        JUnitUtil.tearDown();
    }
    
}
