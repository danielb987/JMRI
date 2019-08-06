package jmri.jmrit.logixng.string.implementation;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.string.actions.StringActionMemory;
import jmri.jmrit.logixng.StringActionBean;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.MaleSocket;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleStringActionSocketTest {

    @Test
    public void testMaleSocket() {
        StringActionBean action = new StringActionMemory("IQSA321");
        Assert.assertNotNull("exists", action);
        MaleSocket maleSocket =
                InstanceManager.getDefault(StringActionManager.class)
                        .registerAction(action);
        Assert.assertNotNull("exists", maleSocket);
        
        String systemName =
                InstanceManager.getDefault(StringActionManager.class)
                        .getNewSystemName();
        Assert.assertNotNull("get object based on template is not null",
                maleSocket.getNewObjectBasedOnTemplate(systemName));
        
        maleSocket.setLock(Base.Lock.NONE);
        Assert.assertTrue("lock is correct",
                Base.Lock.NONE == maleSocket.getLock());
        maleSocket.setLock(Base.Lock.USER_LOCK);
        Assert.assertTrue("lock is correct",
                Base.Lock.USER_LOCK == maleSocket.getLock());
        
        Assert.assertEquals("category is correct",
                action.getCategory(), maleSocket.getCategory());
        
        Assert.assertTrue("isExternal is correct",
                action.isExternal() == maleSocket.isExternal());
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
