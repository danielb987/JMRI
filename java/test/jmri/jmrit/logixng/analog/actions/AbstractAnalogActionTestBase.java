package jmri.jmrit.logixng.analog.actions;

import jmri.AnalogIO;
import jmri.JmriException;
import jmri.jmrit.logixng.AbstractBaseTestBase;
import jmri.jmrit.logixng.AnalogActionBean;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test AbstractAnalogAction
 * 
 * @author Daniel Bergqvist 2018
 */
public class AbstractAnalogActionTestBase extends AbstractBaseTestBase {

    @Test
    public void testGetBeanType() {
        Assert.assertTrue("String matches", "Analog action".equals(((AnalogActionBean)_base).getBeanType()));
    }
    
    @Test
    public void testState() throws JmriException {
        AnalogActionBean _action = (AnalogActionBean)_base;
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
    
}
