package jmri.jmrit.logixng.analog.expressions;

import jmri.AnalogIO;
import jmri.JmriException;
import jmri.jmrit.logixng.AnalogExpressionBean;
import jmri.jmrit.logixng.AbstractBaseTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test AbstractAnalogExpression
 * 
 * @author Daniel Bergqvist 2018
 */
public abstract class AbstractAnalogExpressionTestBase extends AbstractBaseTestBase {

    @Test
    public void testGetBeanType() {
        Assert.assertTrue("String matches", "Analog expression".equals(((AnalogExpressionBean)_base).getBeanType()));
    }
    
    @Test
    public void testState() throws JmriException {
        AnalogExpressionBean _expression = (AnalogExpressionBean)_base;
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
/*    
    @Test
    public void testParent() {
        AnalogExpressionBean _expression = (AnalogExpressionBean)_base;
        AnalogExpressionBean a = new AnalogExpressionMemory("IQA55:12:AE321");
        _expression.setParent(null);
        Assert.assertTrue("Parent matches", null == _expression.getParent());
        _expression.setParent(a);
        Assert.assertTrue("Parent matches", a == _expression.getParent());
        _expression.setParent(null);
        Assert.assertTrue("Parent matches", null == _expression.getParent());
    }
*/    
}
