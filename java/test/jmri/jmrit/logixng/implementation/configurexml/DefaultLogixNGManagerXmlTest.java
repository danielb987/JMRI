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
        
        // Test the method load(Element element, Object o)
        b.load((Element)null, (Object)null);
        JUnitAppender.assertErrorMessage("Invalid method called");
        
        // Test the method load(Element element, Object o)
        b.load((Element)null, (Object)null);
        JUnitAppender.assertErrorMessage("Invalid method called");
        
        // Test loading a logixng without system name
        Element e = new Element("logixngs");
        Element e2 = new Element("logixng");
        e.addContent(e2);
        b.loadLogixNGs(e);
        JUnitAppender.assertWarnMessage("unexpected null in systemName [Element: <logixng/>]");
/*        
        // Test loading the same class twice, in order to check field "xmlClasses"
        e = new Element("logixngs");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.actions.configurexml.AnalogActionMemoryXml");
        e.addContent(e2);
        e2.addContent(new Element("systemName").addContent("IQAA1"));
        b.loadLogixNGs(e);
        
        e = new Element("logixngs");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.actions.configurexml.AnalogActionMemoryXml");
        e.addContent(e2);
        e2.addContent(new Element("systemName").addContent("IQAA2"));
        b.loadLogixNGs(e);
/*        
        // Test trying to load a class with private constructor
        e = new Element("logixngs");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.implementation.configurexml.DefaultAnalogActionManagerXmlTest$PrivateConstructorXml");
        e.addContent(e2);
        b.loadLogixNGs(e);
        JUnitAppender.assertErrorMessage("cannot create constructor");
        
        // Test trying to load a class which throws an exception
        e = new Element("logixngs");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.implementation.configurexml.DefaultAnalogActionManagerXmlTest$ThrowExceptionXml");
        e.addContent(e2);
        b.loadLogixNGs(e);
        JUnitAppender.assertErrorMessage("cannot create constructor");
*/
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
    
    
    private class MyLogixNG extends jmri.jmrit.logixng.implementation.DefaultLogixNG {
        
        MyLogixNG() {
            super("IQ9999");
        }
        
    }
    
/*    
    // This class is loaded by reflection. The class cannot be private since
    // Spotbugs will in that case flag it as "is never used locally"
    class PrivateConstructorXml extends DefaultLogixNGXml {
        private PrivateConstructorXml() {
        }
    }
    
    // This class is loaded by reflection. The class cannot be private since
    // Spotbugs will in that case flag it as "is never used locally"
    class ThrowExceptionXml extends DefaultLogixNGXml {
        @Override
        public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {
            throw new JmriConfigureXmlException();
        }
    }
*/    
    class MyManager extends DefaultLogixNGManager {
        MyManager() {
            super(InstanceManager.getDefault(InternalSystemConnectionMemo.class));
        }
    }
    
}
