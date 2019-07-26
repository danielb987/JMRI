package jmri.jmrit.logixng.analog.implementation.configurexml;

import jmri.InstanceManager;
import jmri.configurexml.JmriConfigureXmlException;
import jmri.jmrit.logixng.AnalogActionManager;
import jmri.jmrit.logixng.analog.actions.AnalogActionMemory;
import jmri.jmrit.logixng.analog.actions.configurexml.AnalogActionMemoryXml;
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
public class DefaultAnalogActionManagerXmlTest {

    @Test
    public void testCTor() {
        DefaultAnalogActionManagerXml b = new DefaultAnalogActionManagerXml();
        Assert.assertNotNull("exists", b);
    }

    @Test
    public void testLoad() {
        DefaultAnalogActionManagerXml b = new DefaultAnalogActionManagerXml();
        
        // Test the method load(Element element, Object o)
        b.load((Element)null, (Object)null);
        JUnitAppender.assertErrorMessage("Invalid method called");
        
        Element e = new Element("logixngAnalogExpressions");
        Element e2 = new Element("missing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.this.class.does.not.exist.TestClassXml");
        e.addContent(e2);
        b.loadActions(e);
        JUnitAppender.assertErrorMessage("cannot load class jmri.jmrit.logixng.this.class.does.not.exist.TestClassXml");
        
        // Test loading the same class twice, in order to check field "xmlClasses"
        e = new Element("logixngAnalogExpressions");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.actions.configurexml.AnalogActionMemoryXml");
        e.addContent(e2);
        e2.addContent(new Element("systemName").addContent("IQAA1"));
        b.loadActions(e);
        
        e = new Element("logixngAnalogExpressions");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.actions.configurexml.AnalogActionMemoryXml");
        e.addContent(e2);
        e2.addContent(new Element("systemName").addContent("IQAA2"));
        b.loadActions(e);
        
        // Test trying to load a class with private constructor
        e = new Element("logixngAnalogExpressions");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.implementation.configurexml.DefaultAnalogActionManagerXmlTest$PrivateConstructorXml");
        e.addContent(e2);
        b.loadActions(e);
        JUnitAppender.assertErrorMessage("cannot create constructor");
        
        // Test trying to load a class which throws an exception
        e = new Element("logixngAnalogExpressions");
        e2 = new Element("existing_class");
        e2.setAttribute("class", "jmri.jmrit.logixng.analog.implementation.configurexml.DefaultAnalogActionManagerXmlTest$ThrowExceptionXml");
        e.addContent(e2);
        b.loadActions(e);
        JUnitAppender.assertErrorMessage("cannot create constructor");
        
//        System.out.format("Class name: %s%n", PrivateConstructorXml.class.getName());
    }

    @Test
    public void testStore() {
        DefaultAnalogActionManagerXml b = new DefaultAnalogActionManagerXml();
        
        // If parameter is null, nothing should happen
        b.store(null);
        
        // Test store a named bean that has no configurexml class
        AnalogActionManager manager = InstanceManager.getDefault(AnalogActionManager.class);
        manager.registerAction(new MyAnalogAction());
        b.store(manager);
        JUnitAppender.assertErrorMessage("Cannot load configuration adapter for jmri.jmrit.logixng.analog.implementation.configurexml.DefaultAnalogActionManagerXmlTest$MyAnalogAction");
        JUnitAppender.assertErrorMessage("Cannot store configuration for jmri.jmrit.logixng.analog.implementation.configurexml.DefaultAnalogActionManagerXmlTest$MyAnalogAction");
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.initAnalogActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    
    private class MyAnalogAction extends AnalogActionMemory {
        
        MyAnalogAction() {
            super("IQAA9999");
        }
        
    }
    
    
    // This class is loaded by reflection
    private class PrivateConstructorXml extends AnalogActionMemoryXml {
        private PrivateConstructorXml() {
        }
    }
    
    // This class is loaded by reflection
    private class ThrowExceptionXml extends AnalogActionMemoryXml {
        @Override
        public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {
            throw new JmriConfigureXmlException();
        }
    }
}
