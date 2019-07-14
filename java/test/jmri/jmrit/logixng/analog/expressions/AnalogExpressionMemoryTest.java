package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.MemoryManager;
import jmri.jmrit.logixng.Category;

/**
 * Test SetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogExpressionMemoryTest extends AbstractAnalogExpressionTestBase {

    protected Memory _memory;
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _expression != null);
        
        AnalogExpressionMemory expression2;
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11", "My memory");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11", _memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11", "My memory", _memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        // Test template
        expression2 = (AnalogExpressionMemory)_expression.getNewObjectBasedOnTemplate("IQA55:12:AE12");
        Assert.assertNotNull("object exists", expression2);
//        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
//        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
    }
    
    @Test
    public void testEvaluate() {
        AtomicBoolean isCompleted = new AtomicBoolean();
        _memory.setValue(0.0d);
        _expression.initEvaluation();
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
        _expression.initEvaluation();
        _memory.setValue(10.0d);
        Assert.assertTrue("Evaluate matches", 10.0d == _expression.evaluate(isCompleted));
        _expression.initEvaluation();
        ((AnalogExpressionMemory)_expression).setMemory(null);
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
        _expression.reset();
    }
    
    @Test
    public void testMemory() {
        ((AnalogExpressionMemory)_expression).setMemory(null);
        Assert.assertTrue("Memory matches", null == ((AnalogExpressionMemory)_expression).getMemory());
        ((AnalogExpressionMemory)_expression).setMemory(_memory);
        Assert.assertTrue("Memory matches", _memory == ((AnalogExpressionMemory)_expression).getMemory());
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _expression.getCategory());
    }
    
    @Test
    public void testShortDescription() {
//        System.err.format("aa: %s%n", _expression.getShortDescription());
        Assert.assertTrue("String matches", "Get memory IM1".equals(_expression.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Get memory IM1".equals(_expression.getLongDescription()));
    }
    
    @Test
    public void testSetup() {
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        AnalogExpressionMemory expression2 = new AnalogExpressionMemory("IQA55:12:AE321");
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        expression2.setAnalogIO_SystemName(_memory.getSystemName());
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
    }
    
    @Test
    public void testChild() {
        Assert.assertTrue("Num children is zero", 0 == _expression.getChildCount());
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        try {
            _expression.getChild(0);
        } catch (UnsupportedOperationException ex) {
            hasThrown.set(true);
            Assert.assertTrue("Error message is correct", "Not supported.".equals(ex.getMessage()));
        }
        Assert.assertTrue("Exception is thrown", hasThrown.get());
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
        _expression = new AnalogExpressionMemory("IQA55:12:AE321", "AnalogIO_Memory", _memory);
    }

    @After
    public void tearDown() {
        _expression.dispose();
        JUnitUtil.tearDown();
    }
    
}
