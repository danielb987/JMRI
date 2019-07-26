package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.AnalogActionManager;
import jmri.jmrit.logixng.AnalogExpressionManager;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.analog.actions.AnalogActionMemory;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.MemoryManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.DigitalActionBean;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.digital.actions.DoAnalogAction;

/**
 * Test SetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogExpressionMemoryTest extends AbstractAnalogExpressionTestBase {

    protected Memory _memory;
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _base != null);
        
        AnalogExpressionMemory expression2;
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        
        expression2 = new AnalogExpressionMemory("IQAE11");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQAE11", "My memory");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQAE11");
        expression2.setMemory(_memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQAE11", "My memory");
        expression2.setMemory(_memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        // Test template
        expression2 = (AnalogExpressionMemory)_base.getNewObjectBasedOnTemplate("IQAE12");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertNull("Username is null", expression2.getUserName());
//        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        boolean thrown = false;
        try {
            // Illegal system name
            new AnalogExpressionMemory("IQA55:12:XY11");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        
        thrown = false;
        try {
            // Illegal system name
            new AnalogExpressionMemory("IQA55:12:XY11", "A name");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
    }
    
    @Test
    public void testEvaluate() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        AnalogExpressionMemory _expression = (AnalogExpressionMemory)_base;
        _memory.setValue(0.0d);
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate());
        _memory.setValue(10.0d);
        Assert.assertTrue("Evaluate matches", 10.0d == _expression.evaluate());
        _expression.setMemory((Memory)null);
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate());
        _expression.reset();
    }
    
    @Test
    public void testEvaluateAndAction() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        
        AnalogExpressionMemory _expression = (AnalogExpressionMemory)_base;
        
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        
        logixNG.addConditionalNG(conditionalNG);
        logixNG.activateLogixNG();
        
        DigitalActionBean actionDoAnalog = new DoAnalogAction();
        MaleSocket socketDoAnalog = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionDoAnalog);
        conditionalNG.getChild(0).connect(socketDoAnalog);
        
        MaleSocket socketExpression = InstanceManager.getDefault(AnalogExpressionManager.class).registerExpression(_expression);
        socketDoAnalog.getChild(0).connect(socketExpression);
        
        Memory _memoryOut = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        _memoryOut.setValue(0.0);
        AnalogActionMemory actionMemory = new AnalogActionMemory("IQAA1");
        actionMemory.setMemory(_memoryOut);
        MaleSocket socketAction = InstanceManager.getDefault(AnalogActionManager.class).registerAction(actionMemory);
        socketDoAnalog.getChild(1).connect(socketAction);
        
        // The action is not yet executed so the double should be 0.0
        Assert.assertTrue("memory is 0.0", 0.0 == (Double)_memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue(1.0);
        // The conditionalNG is not yet enabled so it shouldn't be executed.
        // So the memory should be 0.0
        Assert.assertTrue("memory is 0.0", 0.0 == (Double)_memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue(2.0);
        // Enable the conditionalNG and all its children.
        conditionalNG.setEnabled(true);
        // The action is not yet executed so the memory should be 0.0
        Assert.assertTrue("memory is 0.0", 0.0 == (Double)_memoryOut.getValue());
        // Set the value of the memory. This should execute the conditional.
        _memory.setValue(3.0);
        // The action should now be executed so the memory should be 3.0
        Assert.assertTrue("memory is 3.0", 3.0 == (Double)_memoryOut.getValue());
        // Disable the conditionalNG and all its children.
        conditionalNG.setEnabled(false);
        // The action is not yet executed so the memory should be 0.0
        Assert.assertTrue("memory is 0.0", 3.0 == (Double)_memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue(4.0);
        // The action should not be executed so the memory should still be 3.0
        Assert.assertTrue("memory is 3.0", 3.0 == (Double)_memoryOut.getValue());
        // Unregister listeners. This should do nothing since the listeners are
        // already unregistered.
        _expression.unregisterListeners();
        // The memory should be 3.0
        Assert.assertTrue("memory is 0.0", 3.0 == (Double)_memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue(5.0);
        // The action should not be executed so the memory should still be 3.0
        Assert.assertTrue("memory is 3.0", 3.0 == (Double)_memoryOut.getValue());
        
        // Test register listeners when there is no memory.
        _expression.setMemory((Memory)null);
        _expression.registerListeners();
    }
    
    @Test
    public void testMemory() {
        AnalogExpressionMemory _expression = (AnalogExpressionMemory)_base;
        _expression.setMemory((Memory)null);
        Assert.assertNull("Memory is null", _expression.getMemory());
        _expression.setMemory(_memory);
        Assert.assertTrue("Memory matches", _memory == _expression.getMemory().getBean());
        
        _expression.setMemory((NamedBeanHandle<Memory>)null);
        Assert.assertNull("Memory is null", _expression.getMemory());
        Memory otherMemory = InstanceManager.getDefault(MemoryManager.class).provide("IM99");
        Assert.assertNotNull("memory is not null", otherMemory);
        NamedBeanHandle<Memory> memoryHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(otherMemory.getDisplayName(), otherMemory);
        _expression.setMemory(memoryHandle);
        Assert.assertTrue("Memory matches", memoryHandle == _expression.getMemory());
        Assert.assertTrue("Memory matches", otherMemory == _expression.getMemory().getBean());
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _base.getCategory());
    }
    
    @Test
    public void testIsExternal() {
        Assert.assertTrue("is external", _base.isExternal());
    }
    
    @Test
    public void testShortDescription() {
//        System.err.format("aa: %s%n", _base.getShortDescription());
        Assert.assertTrue("String matches", "Get memory IM1".equals(_base.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Get memory IM1".equals(_base.getLongDescription()));
    }
    
    @Test
    public void testSetup() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        AnalogExpressionMemory expression2 = new AnalogExpressionMemory("IQAE321");
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        expression2.setMemoryName(_memory.getSystemName());
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        // Test running setup() again when it's already setup
        expression2.setup();
        
        // Test setup() with another memory
        Memory memory2 = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        expression2.setMemoryName(memory2.getSystemName());
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory IM2".equals(expression2.getLongDescription()));
        
        // Test none existing memory
        expression2.setMemoryName("IM999");
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        jmri.util.JUnitAppender.assertErrorMessage("Memory IM999 does not exists");
        
        // Test that action has a memory, but the wrong memory
        expression2.setMemoryName(_memory.getSystemName());
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        // We need to use reflection since setMemoryName will clear _memoryHandle
        jmri.util.ReflectionUtilScaffold.setField(expression2, "_memoryName", memory2.getSystemName());
        expression2.setup();
        Assert.assertTrue("String matches", "Get memory IM2".equals(expression2.getLongDescription()));
    }
    
    @Test
    public void testChild() {
        Assert.assertTrue("Num children is zero", 0 == _base.getChildCount());
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        try {
            _base.getChild(0);
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
        _base = new AnalogExpressionMemory("IQAE321", "AnalogIO_Memory");
        ((AnalogExpressionMemory)_base).setMemory(_memory);
    }

    @After
    public void tearDown() {
        _base.dispose();
        JUnitUtil.tearDown();
    }
    
}
