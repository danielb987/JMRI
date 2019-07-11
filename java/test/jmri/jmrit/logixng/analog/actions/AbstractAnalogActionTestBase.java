package jmri.jmrit.logixng.analog.actions;

import jmri.AnalogIO;
import jmri.JmriException;
import org.junit.Assert;
import org.junit.Test;
import jmri.jmrit.logixng.AnalogActionBean;
import jmri.jmrit.logixng.Base;

/**
 * Test AbstractAnalogAction
 * 
 * @author Daniel Bergqvist 2018
 */
public class AbstractAnalogActionTestBase {

    protected AnalogActionBean _action;
    
    @Test
    public void testGetBeanType() {
        Assert.assertTrue("String matches", "Analog action".equals(_action.getBeanType()));
    }
    
    @Test
    public void testLock() {
        _action.setLock(Base.Lock.NONE);
        Assert.assertTrue("String matches", Base.Lock.NONE == _action.getLock());
        _action.setLock(Base.Lock.USER_LOCK);
        Assert.assertTrue("String matches", Base.Lock.USER_LOCK == _action.getLock());
        _action.setLock(Base.Lock.HARD_LOCK);
        Assert.assertTrue("String matches", Base.Lock.HARD_LOCK == _action.getLock());
    }
    
    @Test
    public void testState() throws JmriException {
        _action.setState(AnalogIO.INCONSISTENT);
        Assert.assertTrue("State matches", AnalogIO.INCONSISTENT == _action.getState());
        jmri.util.JUnitAppender.assertWarnMessage("Unexpected call to getState in AbstractAnalogAction.");
        _action.setState(AnalogIO.UNKNOWN);
        Assert.assertTrue("State matches", AnalogIO.UNKNOWN == _action.getState());
        jmri.util.JUnitAppender.assertWarnMessage("Unexpected call to getState in AbstractAnalogAction.");
        _action.setState(AnalogIO.INCONSISTENT);
        Assert.assertTrue("State matches", AnalogIO.INCONSISTENT == _action.getState());
        jmri.util.JUnitAppender.assertWarnMessage("Unexpected call to getState in AbstractAnalogAction.");
    }
    
    @Test
    public void testParent() {
        AnalogActionBean a = new AnalogActionMemory("IQA55:12:AA321");
        _action.setParent(null);
        Assert.assertTrue("Parent matches", null == _action.getParent());
        _action.setParent(a);
        Assert.assertTrue("Parent matches", a == _action.getParent());
        _action.setParent(null);
        Assert.assertTrue("Parent matches", null == _action.getParent());
    }
    
}
