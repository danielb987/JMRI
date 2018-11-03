package jmri.jmrit.newlogix.expressions;

import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.engine.DefaultFemaleExpressionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionSocketTest {

    @Test
    public void testCtor() {
        FemaleSocketListener listener = new FemaleSocketListener() {
            @Override
            public void connected(FemaleSocket socket) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void disconnected(FemaleSocket socket) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        new DefaultFemaleExpressionSocket(listener, "E1");
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
