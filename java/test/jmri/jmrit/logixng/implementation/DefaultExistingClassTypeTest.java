package jmri.jmrit.logixng.implementation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import jmri.JmriException;
import jmri.jmrit.logixng.NamedTable;
import jmri.util.FileUtil;
import jmri.util.JUnitUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test DefaultExistingClassType
 * 
 * @author Daniel Bergqvist 2021
 */
public class DefaultExistingClassTypeTest {

    @Test
    public void testExistingClass() throws IOException, JmriException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        DefaultExistingClassType arrayListType = new DefaultExistingClassType("IQY1", "ArrayList");
        arrayListType.setClassName("java.util.ArrayList");
        Assert.assertEquals(java.util.ArrayList.class.getName(), arrayListType.getNewInstance().getClass().getName());
        java.util.ArrayList theList = (java.util.ArrayList) arrayListType.getNewInstance();
        Assert.assertEquals((Class<?>)java.util.ArrayList.class, (Class<?>)theList.getClass());
        
        DefaultExistingClassType hashMapType = new DefaultExistingClassType("IQY2", "HashMap");
        hashMapType.setClassName("java.util.HashMap");
        Assert.assertEquals(java.util.HashMap.class.getName(), hashMapType.getNewInstance().getClass().getName());
        java.util.HashMap theMap = (java.util.HashMap) hashMapType.getNewInstance();
        Assert.assertEquals((Class<?>)java.util.HashMap.class, theMap.getClass());
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
    }

    @After
    public void tearDown() {
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.tearDown();
    }
    
}
