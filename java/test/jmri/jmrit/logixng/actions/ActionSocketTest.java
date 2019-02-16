package jmri.jmrit.logixng.actions;

import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.engine.DefaultFemaleActionSocket;
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
public class ActionSocketTest {

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
        
        new DefaultFemaleActionSocket(listener, "A1");
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
