package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.AnalogIO;
import jmri.JmriException;
import jmri.jmrit.logixng.AnalogExpressionBean;
import jmri.jmrit.logixng.Base;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test AbstractAnalogExpression
 * 
 * @author Daniel Bergqvist 2018
 */
public abstract class AbstractAnalogExpressionTestBase {

    protected AnalogExpressionBean _expression;
    
    abstract double expectedEvaluateValue();
    
    @Test
    public void testEvaluate() {
        AtomicBoolean isCompleted = new AtomicBoolean();
        Assert.assertTrue("Evaluate matches", expectedEvaluateValue() == _expression.evaluate(isCompleted));
    }
    
    @Test
    public void testGetBeanType() {
        Assert.assertTrue("String matches", "Analog expression".equals(_expression.getBeanType()));
    }
    
    @Test
    public void testLock() {
        _expression.setLock(Base.Lock.NONE);
        Assert.assertTrue("String matches", Base.Lock.NONE == _expression.getLock());
        _expression.setLock(Base.Lock.USER_LOCK);
        Assert.assertTrue("String matches", Base.Lock.USER_LOCK == _expression.getLock());
        _expression.setLock(Base.Lock.HARD_LOCK);
        Assert.assertTrue("String matches", Base.Lock.HARD_LOCK == _expression.getLock());
    }
    
    @Test
    public void testState() throws JmriException {
        _expression.setState(AnalogIO.INCONSISTENT);
        Assert.assertTrue("State matches", AnalogIO.INCONSISTENT == _expression.getState());
        jmri.util.JUnitAppender.assertWarnMessage("Unexpected call to getState in AbstractAnalogExpression.");
        _expression.setState(AnalogIO.UNKNOWN);
        Assert.assertTrue("State matches", AnalogIO.UNKNOWN == _expression.getState());
        jmri.util.JUnitAppender.assertWarnMessage("Unexpected call to getState in AbstractAnalogExpression.");
        _expression.setState(AnalogIO.INCONSISTENT);
        Assert.assertTrue("State matches", AnalogIO.INCONSISTENT == _expression.getState());
        jmri.util.JUnitAppender.assertWarnMessage("Unexpected call to getState in AbstractAnalogExpression.");
    }
    
    @Test
    public void testParent() {
        AnalogExpressionBean a = new AnalogExpressionMemory("IQA55:12:A321");
        _expression.setParent(null);
        Assert.assertTrue("Parent matches", null == _expression.getParent());
        _expression.setParent(a);
        Assert.assertTrue("Parent matches", a == _expression.getParent());
        _expression.setParent(null);
        Assert.assertTrue("Parent matches", null == _expression.getParent());
    }
    
}
