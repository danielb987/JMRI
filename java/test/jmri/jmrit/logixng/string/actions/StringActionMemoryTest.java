package jmri.jmrit.logixng.string.actions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.MemoryManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Test SetStringIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class StringActionMemoryTest extends AbstractStringActionTestBase {

    protected Memory _memory;
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _base != null);
        
        StringActionMemory action2;
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        
        action2 = new StringActionMemory("IQA55:12:SA11");
        Assert.assertNotNull("object exists", action2);
        Assert.assertTrue("Username matches", null == action2.getUserName());
        Assert.assertTrue("String matches", "Set memory none".equals(action2.getLongDescription()));
        
        action2 = new StringActionMemory("IQA55:12:SA11", "My memory");
        Assert.assertNotNull("object exists", action2);
        Assert.assertTrue("Username matches", "My memory".equals(action2.getUserName()));
        Assert.assertTrue("String matches", "Set memory none".equals(action2.getLongDescription()));
        
        action2 = new StringActionMemory("IQA55:12:SA11");
        action2.setMemory(_memory);
        Assert.assertNotNull("object exists", action2);
        Assert.assertTrue("Username matches", null == action2.getUserName());
        Assert.assertTrue("String matches", "Set memory IM1".equals(action2.getLongDescription()));
        
        action2 = new StringActionMemory("IQA55:12:SA11", "My memory");
        action2.setMemory(_memory);
        Assert.assertNotNull("object exists", action2);
        Assert.assertTrue("Username matches", "My memory".equals(action2.getUserName()));
        Assert.assertTrue("String matches", "Set memory IM1".equals(action2.getLongDescription()));
        
        // Test template
        action2 = (StringActionMemory)_base.getNewObjectBasedOnTemplate("IQA55:12:SA12");
        Assert.assertNotNull("object exists", action2);
        Assert.assertNull("Username is null", action2.getUserName());
        Assert.assertTrue("String matches", "Set memory IM1".equals(action2.getLongDescription()));
        
        boolean thrown = false;
        try {
            // Illegal system name
            new StringActionMemory("IQA55:12:XY11");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        
        thrown = false;
        try {
            // Illegal system name
            new StringActionMemory("IQA55:12:XY11", "A name");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
    }
    
    @Test
    public void testAction() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        StringActionMemory _action = (StringActionMemory)_base;
        _action.setValue("");
        Assert.assertEquals("Memory has correct value", "", _memory.getValue());
        _action.setValue("Test");
        Assert.assertEquals("Memory has correct value", "Test", _memory.getValue());
        _action.setMemory((Memory)null);
        _action.setValue("Other test");
        Assert.assertEquals("Memory has correct value", "Test", _memory.getValue());
    }
    
    @Test
    public void testMemory() {
        StringActionMemory _action = (StringActionMemory)_base;
        _action.setMemory((Memory)null);
        Assert.assertNull("Memory is null", _action.getMemory());
        ((StringActionMemory)_base).setMemory(_memory);
        Assert.assertTrue("Memory matches", _memory == _action.getMemory().getBean());
        
        _action.setMemory((NamedBeanHandle<Memory>)null);
        Assert.assertNull("Memory is null", _action.getMemory());
        Memory otherMemory = InstanceManager.getDefault(MemoryManager.class).provide("IM99");
        Assert.assertNotNull("memory is not null", otherMemory);
        NamedBeanHandle<Memory> memoryHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(otherMemory.getDisplayName(), otherMemory);
        ((StringActionMemory)_base).setMemory(memoryHandle);
        Assert.assertTrue("Memory matches", memoryHandle == _action.getMemory());
        Assert.assertTrue("Memory matches", otherMemory == _action.getMemory().getBean());
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _base.getCategory());
    }
    
    @Test
    public void testIsExternal() {
        Assert.assertTrue("is external", _base.isExternal());
    }
    
    @Test
    public void testShortDescription() {
        Assert.assertTrue("String matches", "Set memory IM1".equals(_base.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Set memory IM1".equals(_base.getLongDescription()));
    }
    
    @Test
    public void testSetup() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        StringActionMemory action2 = new StringActionMemory("IQA55:12:SA321");
        Assert.assertTrue("String matches", "Set memory none".equals(action2.getLongDescription()));
        action2.setup();
        Assert.assertTrue("String matches", "Set memory none".equals(action2.getLongDescription()));
        action2.setMemoryName(_memory.getSystemName());
        action2.setup();
        Assert.assertTrue("String matches", "Set memory IM1".equals(action2.getLongDescription()));
        
        // Test running setup() again when it's already setup
        action2.setup();
        
        // Test setup() with another memory
        Memory memory2 = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        action2.setMemoryName(memory2.getSystemName());
        Assert.assertTrue("String matches", "Set memory none".equals(action2.getLongDescription()));
        action2.setup();
        Assert.assertTrue("String matches", "Set memory IM2".equals(action2.getLongDescription()));
        
        // Test none existing memory
        action2.setMemoryName("IM999");
        action2.setup();
        Assert.assertTrue("String matches", "Set memory none".equals(action2.getLongDescription()));
        jmri.util.JUnitAppender.assertErrorMessage("Memory IM999 does not exists");
        
        // Test that action has a memory, but the wrong memory
        action2.setMemoryName(_memory.getSystemName());
        action2.setup();
        Assert.assertTrue("String matches", "Set memory IM1".equals(action2.getLongDescription()));
        // We need to use reflection since setMemoryName will clear _memoryHandle
        jmri.util.ReflectionUtilScaffold.setField(action2, "_memoryName", memory2.getSystemName());
        action2.setup();
        Assert.assertTrue("String matches", "Set memory IM2".equals(action2.getLongDescription()));
    }
    
    @Test
    public void testChild() {
        Assert.assertTrue("Num children is zero", 0 == _base.getChildCount());
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        try {
            _base.getChild(0);
        } catch (UnsupportedOperationException ex) {
            hasThrown.set(true);
            Assert.assertTrue("Error message is correct", "Not supported.".equals(ex.getMessage()));
        }
        Assert.assertTrue("Exception is thrown", hasThrown.get());
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
        _base = new StringActionMemory("IQA55:12:SA321", "StringIO_Memory");
        ((StringActionMemory)_base).setMemory(_memory);
    }

    @After
    public void tearDown() {
        _base.dispose();
        JUnitUtil.tearDown();
    }
    
}
