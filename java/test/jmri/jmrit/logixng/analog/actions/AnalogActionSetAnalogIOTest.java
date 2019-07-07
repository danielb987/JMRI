package jmri.jmrit.logixng.analog.actions;

import jmri.InstanceManager;
import jmri.Memory;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.MemoryManager;

/**
 * Test SetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogActionSetAnalogIOTest extends AbstractAnalogActionTestBase {

    protected Memory _memory;
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _action != null);
    }
    
    @Test
    public void testShortDescription() {
        Assert.assertTrue("String matches", "Set analog none".equals(_action.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Set analog none".equals(_action.getShortDescription()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initMemoryManager();
        _memory = InstanceManager.getDefault(MemoryManager.class).provide("IM1");
        _action = new SetAnalogIO("IQA55:A321");
    }

    @After
    public void tearDown() {
        _action.dispose();
        JUnitUtil.tearDown();
    }
    
}
