package jmri.jmrit.logixng.implementation.configurexml;

import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.configurexml.JmriConfigureXmlException;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.implementation.DefaultLogixNGManager;
import jmri.jmrix.internal.InternalSystemConnectionMemo;
import jmri.util.JUnitAppender;
import jmri.util.JUnitUtil;
import org.jdom2.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class DefaultLogixNGManagerXmlTest {

    @Test
    public void testCTor() {
        DefaultLogixNGManagerXml b = new DefaultLogixNGManagerXml();
        Assert.assertNotNull("exists", b);
    }

    @Test
    public void testLoad() {
        DefaultLogixNGManagerXml b = new DefaultLogixNGManagerXml();
        Assert.assertNotNull("exists", b);
        b.load((Element)null, (Object)null);
        JUnitAppender.assertErrorMessage("Invalid method called");
    }

    @Test
    public void testStore() {
        DefaultLogixNGManagerXml b = new DefaultLogixNGManagerXml();
        Assert.assertNotNull("exists", b);
        // Calling store() with null is OK.
        b.store((Object)null);
    }

    @Test
    public void testReplaceActionManagerWithoutConfigManager() {
        
        // if old manager exists, remove it from configuration process
        if (InstanceManager.getNullableDefault(jmri.jmrit.logixng.AnalogActionManager.class) != null) {
            ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
            if (cmOD != null) {
                cmOD.deregister(InstanceManager.getDefault(jmri.jmrit.logixng.AnalogActionManager.class));
            }

        }

        // register new one with InstanceManager
        MyManager pManager = new MyManager();
        InstanceManager.store(pManager, LogixNG_Manager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.LOGIXNGS);
        }
        
        Assert.assertTrue("manager is a MyManager",
                InstanceManager.getDefault(LogixNG_Manager.class)
                        instanceof MyManager);
        
        // Test replacing the manager
        DefaultLogixNGManagerXml b = new DefaultLogixNGManagerXml();
        b.replaceLogixNGManager();
        
        Assert.assertFalse("manager is not a MyManager",
                InstanceManager.getDefault(LogixNG_Manager.class)
                        instanceof MyManager);
    }
    
    @Test
    public void testReplaceActionManagerWithConfigManager() {
        
        JUnitUtil.initConfigureManager();
        
        // if old manager exists, remove it from configuration process
        if (InstanceManager.getNullableDefault(jmri.jmrit.logixng.AnalogActionManager.class) != null) {
            ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
            if (cmOD != null) {
                cmOD.deregister(InstanceManager.getDefault(jmri.jmrit.logixng.AnalogActionManager.class));
            }

        }

        // register new one with InstanceManager
        MyManager pManager = new MyManager();
        InstanceManager.store(pManager, LogixNG_Manager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.LOGIXNGS);
        }
        
        Assert.assertTrue("manager is a MyManager",
                InstanceManager.getDefault(LogixNG_Manager.class)
                        instanceof MyManager);
        
        // Test replacing the manager
        DefaultLogixNGManagerXml b = new DefaultLogixNGManagerXml();
        b.replaceLogixNGManager();
        
        Assert.assertFalse("manager is not a MyManager",
                InstanceManager.getDefault(LogixNG_Manager.class)
                        instanceof MyManager);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    class MyManager extends DefaultLogixNGManager {
        MyManager() {
            super(InstanceManager.getDefault(InternalSystemConnectionMemo.class));
        }
    }
    
}
