package jmri.jmrit.logixng.string.implementation;

import jmri.jmrit.logixng.FemaleSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleStringActionSocket;
import jmri.jmrit.logixng.MaleStringActionSocket;
import jmri.jmrit.logixng.StringAction;
import jmri.jmrit.logixng.string.actions.StringActionSetStringIO;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultFemaleStringActionSocketTest {

    @Test
    public void testFemaleSocket() {
        StringAction action = new StringActionSetStringIO("IQA55:A321");
        MaleStringActionSocket maleSocket = new DefaultMaleStringActionSocket(action);
        FemaleStringActionSocket femaleSocket = new DefaultFemaleStringActionSocket(null, new FemaleSocketListener() {
            @Override
            public void connected(FemaleSocket socket) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void disconnected(FemaleSocket socket) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }, "A1");
        
        Assert.assertTrue("String matches", "A1".equals(femaleSocket.getName()));
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
