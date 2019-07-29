package jmri.jmrit.logixng.analog.implementation;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleSocketTestBase;
import jmri.jmrit.logixng.analog.actions.AnalogActionMemory;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.AnalogActionBean;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultFemaleAnalogActionSocketTest extends FemaleSocketTestBase {

    private String _memorySystemName;
    private Memory _memory;
    private MyAnalogActionMemory _action;
    
    @Test
    public void testGetName() {
        Assert.assertTrue("String matches", "A1".equals(femaleSocket.getName()));
    }
    
    @Test
    public void testGetDescription() {
        Assert.assertTrue("String matches", "!~".equals(femaleSocket.getShortDescription()));
        Assert.assertTrue("String matches", "!~ A1".equals(femaleSocket.getLongDescription()));
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
        _memorySystemName = "IM1";
        _memory = InstanceManager.getDefault(MemoryManager.class).provide(_memorySystemName);
        _action = new MyAnalogActionMemory("IQAA321");
        _action.setMemory(_memory);
        AnalogActionBean otherAction = new AnalogActionMemory("IQAA322");
        maleSocket = new DefaultMaleAnalogActionSocket(_action);
        otherMaleSocket = new DefaultMaleAnalogActionSocket(otherAction);
        femaleSocket = new DefaultFemaleAnalogActionSocket(null, new FemaleSocketListener() {
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
    
    
    
    private class MyAnalogActionMemory extends AnalogActionMemory {
        
        private boolean _hasBeenSetup = false;
        
        public MyAnalogActionMemory(String systemName) {
            super(systemName);
        }
        
        /** {@inheritDoc} */
        @Override
        public void setup() {
            _hasBeenSetup = true;
        }
    }
    
}
