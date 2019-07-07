package jmri.jmrit.logixng.analog.expressions;

import jmri.InstanceManager;
import jmri.Memory;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.MemoryManager;

/**
 * Test SetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogExpressionMemoryTest extends AbstractAnalogExpressionTestBase {

    protected Memory _memory;
    
    @Override
    double expectedEvaluateValue() {
        return (double) _memory.getValue();
    }
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _expression != null);
    }
    
    @Test
    public void testShortDescription() {
        System.err.format("aa: %s%n", _expression.getShortDescription());
        Assert.assertTrue("String matches", "Get memory IM1".equals(_expression.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Get memory IM1".equals(_expression.getLongDescription()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initMemoryManager();
        _memory = InstanceManager.getDefault(MemoryManager.class).provide("IM1");
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        _expression = new AnalogExpressionMemory("IQA55:12:A321", "AnalogIO_Memory", _memory);
    }

    @After
    public void tearDown() {
        _expression.dispose();
        JUnitUtil.tearDown();
    }
    
}
