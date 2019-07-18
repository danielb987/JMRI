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
import jmri.jmrit.logixng.AnalogExpressionBean;
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
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11", "My memory");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11");
        expression2.setMemory(_memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        expression2 = new AnalogExpressionMemory("IQA55:12:AE11", "My memory");
        expression2.setMemory(_memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        // Test template
        expression2 = (AnalogExpressionMemory)_base.getNewObjectBasedOnTemplate("IQA55:12:AE12");
        Assert.assertNotNull("object exists", expression2);
//        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
//        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
    }
    
    @Test
    public void testEvaluate() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        AnalogExpressionMemory _expression = (AnalogExpressionMemory)_base;
        AtomicBoolean isCompleted = new AtomicBoolean();
        _memory.setValue(0.0d);
        _expression.initEvaluation();
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
        _expression.initEvaluation();
        _memory.setValue(10.0d);
        Assert.assertTrue("Evaluate matches", 10.0d == _expression.evaluate(isCompleted));
        _expression.initEvaluation();
        _expression.setMemory((Memory)null);
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
        _expression.reset();
    }
    
    @Test
    public void testEvaluateAndAction() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        
        AnalogExpressionMemory _expression = (AnalogExpressionMemory)_base;
        
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        
        logixNG.addConditionalNG(conditionalNG);
        logixNG.activateLogixNG();
        
        DigitalActionBean actionDoAnalog = new DoAnalogAction(conditionalNG);
        MaleSocket socketDoAnalog = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionDoAnalog);
        conditionalNG.getChild(0).connect(socketDoAnalog);
        
//        ExpressionTurnout expressionTurnout = new ExpressionTurnout(conditionalNG);
//        expressionTurnout.setTurnout(turnout);
//        expressionTurnout.set_Is_IsNot(Is_IsNot_Enum.IS);
//        expressionTurnout.setTurnoutState(ExpressionTurnout.TurnoutState.THROWN);
        MaleSocket socketExpression = InstanceManager.getDefault(AnalogExpressionManager.class).registerExpression(_expression);
        socketDoAnalog.getChild(0).connect(socketExpression);
        
        Memory _memoryOut = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        _memoryOut.setValue(0.0);
        AnalogActionMemory actionMemory = new AnalogActionMemory("IQA55:12:AA1", _memoryOut);
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
        // The action is not yet executed so the memory should be 0.0
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
        ((AnalogExpressionMemory)_base).setMemory((Memory)null);
        Assert.assertTrue("Memory matches", null == ((AnalogExpressionMemory)_base).getMemory());
        ((AnalogExpressionMemory)_base).setMemory(_memory);
        Assert.assertTrue("Memory matches", _memory == ((AnalogExpressionMemory)_base).getMemory().getBean());
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
        _base = new AnalogExpressionMemory("IQA55:12:AE321", "AnalogIO_Memory");
        ((AnalogExpressionMemory)_base).setMemory(_memory);
    }

    @After
    public void tearDown() {
        _base.dispose();
        JUnitUtil.tearDown();
    }
    
}
