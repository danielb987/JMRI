package jmri.jmrit.logixng.analog.actions;

import jmri.InstanceManager;
import jmri.Memory;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.MemoryManager;
import jmri.jmrit.logixng.Category;

/**
 * Test SetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogActionMemoryTest extends AbstractAnalogActionTestBase {

    protected Memory _memory;
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _base != null);
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _base.getCategory());
    }
    
    @Test
    public void testShortDescription() {
        Assert.assertTrue("String matches", "Set memory IM1".equals(_base.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Set memory IM1".equals(_base.getLongDescription()));
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
        _base = new AnalogActionMemory("IQA55:12:AA321", "AnalogIO_Memory", _memory);
    }

    @After
    public void tearDown() {
        _base.dispose();
        JUnitUtil.tearDown();
    }
    
}
