package jmri.jmrit.logixng.implementation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;

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
public class DefaultScriptTypeTest {

    private static final String MY_SCRIPT = "" +
            "import java.lang\n" +
            "\n" +
            "class MyScriptClass(java.lang.Object):\n" +
            "\n" +
            "        myIntField = 12\n" +
            "        myStringField = \"Hello World!\"\n" +
            "\n" +
            "theClass.set(MyScriptClass())";
    
    private static final String MY_OTHER_SCRIPT = "" +
            "import java.lang\n" +
            "\n" +
            "class AnotherScriptClass(java.lang.Object):\n" +
            "\n" +
            "        myOtherIntField = 32\n" +
            "        myOtherStringField = \"Hello today!\"\n" +
            "\n" +
            "        def hello(self):\n" +
            "              return \"Hello World\"" +
            "\n" +
            "theClass.set(AnotherScriptClass())";
    
    @Test
    public void testScriptInString() throws IOException, JmriException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        System.out.format("MyScript:%n%n%s%n%n", MY_SCRIPT);
        DefaultScriptType myScriptType=  new DefaultScriptType("IQY1", "My script");
        myScriptType.setScript(MY_SCRIPT);
//        Assert.assertEquals("org.python.proxies.__builtin__$MyScriptClass$", myScriptType.getNewInstance().getClass().getName());
        Object myScriptInstance = myScriptType.getNewInstance();
        java.lang.reflect.Field myIntField = myScriptInstance.getClass().getDeclaredField("myIntField");
        Assert.assertEquals((int)1, (int)(Integer)myIntField.get(myScriptInstance));
        
        DefaultScriptType myOtherScriptType = new DefaultScriptType("IQY2", "My other script");
        myOtherScriptType.setScript(MY_OTHER_SCRIPT);
//        Assert.assertTrue(myOtherScriptType.getNewInstance().getClass().getName().startsWith("org.python.proxies.__builtin__$AnotherScriptClass$"));
        Object myOtherScriptInstance = myOtherScriptType.getNewInstance();
        java.lang.reflect.Field myOtherIntField = myScriptInstance.getClass().getDeclaredField("myOtherIntField");
        Assert.assertEquals((int)1, (int)(Integer)myOtherIntField.get(myOtherScriptInstance));
    }
    
    @Test
    public void testScriptOnFile() throws IOException, JmriException {
        DefaultExistingClassType arrayListType = new DefaultExistingClassType("IQY1", "ArrayList");
        arrayListType.setClassName("java.util.ArrayList");
        Assert.assertEquals(java.util.ArrayList.class.getName(), arrayListType.getNewInstance().getClass().getName());
        
        DefaultExistingClassType hashMapType = new DefaultExistingClassType("IQY2", "HashMap");
        hashMapType.setClassName("java.util.HashMap");
        Assert.assertEquals(java.util.HashMap.class.getName(), hashMapType.getNewInstance().getClass().getName());
        
/*        
        NamedTable table = AbstractNamedTable.loadTableFromCSV_File(
                "IQT1", null,
                new File("java/test/jmri/jmrit/logixng/panel_and_data_files/turnout_and_signals.csv"),
                true);
        
        FileUtil.createDirectory(FileUtil.getUserFilesPath() + "temp");
        File file = new File(FileUtil.getUserFilesPath() + "temp/" + "turnout_and_signals.csv");
//        System.out.format("Temporary file: %s%n", file.getAbsoluteFile());
        table.storeTableAsCSV(file);
        Assert.assertNotNull("exists", table);
*/
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
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        JUnitUtil.clearShutDownManager();
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        // REMOVE THIS!!!
        
        
        
        
        
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.tearDown();
    }
    
}
