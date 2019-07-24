package jmri.jmrit.logixng.ztest;

import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.implementation.DefaultLogixNGManager;
import jmri.util.JUnitUtil;
import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 * Tests for the LogixNG_Startup Class
 * @author Dave Sand Copyright (C) 2018
 */
public class LogixNG_StartupTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreate() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertNotNull("exists", new LogixNG_Startup());
    }

//    @Ignore("For testing only. This method should be removed before merging into master.")
    @Test
    public void testAction() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        new LogixNG_StartupAction().actionPerformed(null);
        
        // Test that actionPerformed() throws PropertyVetoException
        InstanceManager.setDefault(LogixNG_Manager.class, new MyLogixNG_Manager());
        new LogixNG_StartupAction().actionPerformed(null);
        if (1==1)
            jmri.util.JUnitAppender.assertWarnMessage("exception thrown");
        else
            jmri.util.JUnitAppender.assertErrorMessage("exception thrown");
//        jmri.util.JUnitAppender.assertErrorMessageStartsWith("exception thrown");
    }

    @Test
    public void testGetTitle() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertEquals("LogixNG test action", new LogixNG_Startup().getTitle(LogixNG_StartupAction.class, Locale.US));  // NOI18N
        
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        try {
            new LogixNG_Startup().getTitle(String.class, Locale.US);
        } catch (IllegalArgumentException ex) {
            hasThrown.set(true);
        }
        Assert.assertTrue("Exception is thrown", hasThrown.get());
    }

    @Test
    public void testGetClass() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertNotNull(new LogixNG_Startup().getActionClasses());
    }
    
    @Test
    public void testBundle() {
        Assert.assertEquals(
                "strings matches",
                "LogixNG test action",
                Bundle.getMessage("LogixNG_TestAction"));
        
        Assert.assertEquals(
                "strings matches",
                "Test bundle a1 b2",
                Bundle.getMessage("TestBundle", "a1", "b2"));
        
        Assert.assertEquals(
                "strings matches",
                "LogixNG test action",
                Bundle.getMessage(Locale.US, "LogixNG_TestAction"));
        
        Assert.assertEquals(
                "strings matches",
                "Test bundle a1 b2",
                Bundle.getMessage(Locale.US, "TestBundle", "a1", "b2"));
        
        // Test Bundle.retry(Locale, String)
        Assert.assertEquals(
                "strings matches",
                "Item",
                Bundle.getMessage("CategoryItem"));
    }

    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initInternalSensorManager();
        
        JUnitUtil.initInternalSignalHeadManager();
        JUnitUtil.initDefaultSignalMastManager();
        JUnitUtil.initSignalMastLogicManager();
        JUnitUtil.initOBlockManager();
        JUnitUtil.initWarrantManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    
    private class MyLogixNG_Manager extends DefaultLogixNGManager {
        
        @Override
        public void testLogixNGs() throws PropertyVetoException {
            throw new PropertyVetoException("exception", null);
//            throw new PropertyVetoException("exception", new PropertyChangeEvent("The object", "property", "old value", "new value"));
        }
        
    }
    
}