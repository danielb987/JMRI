package jmri.jmrit.logixng.digital.implementation;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleSocketTestBase;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
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
public class DefaultFemaleDigitalActionSocketTest extends FemaleSocketTestBase {

    private MyActionTurnout _action;
    
    @Test
    public void testGetName() {
        Assert.assertTrue("String matches", "A1".equals(femaleSocket.getName()));
    }
    
    @Test
    public void testGetDescription() {
        Assert.assertTrue("String matches", "!".equals(femaleSocket.getShortDescription()));
        Assert.assertTrue("String matches", "! A1".equals(femaleSocket.getLongDescription()));
    }
    
    @Override
    protected boolean hasSocketBeenSetup() {
        return _action._hasBeenSetup;
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        
        flag = new AtomicBoolean();
        errorFlag = new AtomicBoolean();
        _action = new MyActionTurnout("IQDA321");
        ActionTurnout otherAction = new MyActionTurnout("IQDA322");
        maleSocket = new DefaultMaleDigitalActionSocket(_action);
        otherMaleSocket = new DefaultMaleDigitalActionSocket(otherAction);
        femaleSocket = new DefaultFemaleDigitalActionSocket(null, new FemaleSocketListener() {
            @Override
            public void connected(FemaleSocket socket) {
                flag.set(true);
            }

            @Override
            public void disconnected(FemaleSocket socket) {
                flag.set(true);
            }
        }, "A1");
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    
    private class MyActionTurnout extends ActionTurnout {
        
        private boolean _hasBeenSetup = false;
        
        public MyActionTurnout(String systemName) {
            super(systemName);
        }
        
        /** {@inheritDoc} */
        @Override
        public void setup() {
            _hasBeenSetup = true;
        }
    }
    
}
