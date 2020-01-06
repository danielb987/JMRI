package jmri.managers;

import java.beans.PropertyChangeListener;
import javax.annotation.Nonnull;
import jmri.StringIO;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrix.internal.InternalStringIOManager;
import jmri.jmrix.internal.InternalSystemConnectionMemo;
import jmri.util.JUnitAppender;
import jmri.util.JUnitUtil;
import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import jmri.StringIOManager;

/**
 * Test the ProxyStringIOManager.
 *
 * @author	Bob Jacobsen 2003, 2006, 2008
 * @author      Daniel Bergqvist Copyright (C) 2020
 */
public class ProxyStringIOManagerTest {

    public String getSystemName(int i) {
        return "JC" + i;
    }

    protected StringIOManager l = null;	// holds objects under test

    static protected boolean listenerResult = false;

    protected class Listen implements PropertyChangeListener {

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent e) {
            listenerResult = true;
        }
    }

    private StringIO newStringIO(String sysName, String userName) {
        return new MyStringIO(sysName, userName);
    }
    
    @Test
    public void testDispose() {
        l.dispose();  // all we're really doing here is making sure the method exists
    }

    @Test
    public void testStringIOPutGet() {
        // create
        StringIO t = newStringIO(getSystemName(getNumToTest1()), "mine");
        l.register(t);
        // check
        Assert.assertTrue("real object returned ", t != null);
        Assert.assertTrue("user name correct ", t == l.getByUserName("mine"));
        Assert.assertTrue("system name correct ", t == l.getBySystemName(getSystemName(getNumToTest1())));
    }
/*
    @Test
    public void testDefaultSystemName() {
        // create
        StringIO t = l.provideStringIO("" + getNumToTest1());
        // check
        Assert.assertTrue("real object returned ", t != null);
        Assert.assertTrue("system name correct ", t == l.getBySystemName(getSystemName(getNumToTest1())));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testProvideFailure() {
        try {
            l.provideStringIO("");
            Assert.fail("didn't throw");
        } catch (IllegalArgumentException ex) {
            JUnitAppender.assertErrorMessage("Invalid system name for StringIO: System name must start with \"" + l.getSystemNamePrefix() + "\".");
            throw ex;
        }
    }
*/
    @Test
    public void testSingleObject() {
        // test that you always get the same representation
        StringIO t1 = newStringIO(getSystemName(getNumToTest1()), "mine");
        l.register(t1);
        Assert.assertTrue("t1 real object returned ", t1 != null);
        Assert.assertTrue("same by user ", t1 == l.getByUserName("mine"));
        Assert.assertTrue("same by system ", t1 == l.getBySystemName(getSystemName(getNumToTest1())));

//        StringIO t2 = newStringIO(getSystemName(getNumToTest1()), "mine");
//        Assert.assertTrue("t2 real object returned ", t2 != null);
        // check
//        Assert.assertTrue("same new ", t1 == t2);
    }

    @Test
    public void testMisses() {
        // try to get nonexistant lights
        Assert.assertTrue(null == l.getByUserName("foo"));
        Assert.assertTrue(null == l.getBySystemName("bar"));
    }
/*
    @Test
    public void testUpperLower() {
        StringIO t = l.provideStringIO("" + getNumToTest2());
        String name = t.getSystemName();
        Assert.assertNull(l.getStringIO(name.toLowerCase()));
    }
*/
    @Test
    public void testRename() {
        // get light
        StringIO t1 = newStringIO(getSystemName(getNumToTest1()), "before");
        Assert.assertNotNull("t1 real object ", t1);
        l.register(t1);
        t1.setUserName("after");
        StringIO t2 = l.getByUserName("after");
        Assert.assertEquals("same object", t1, t2);
        Assert.assertEquals("no old object", null, l.getByUserName("before"));
    }
/*
    @Test
    public void testTwoNames() {
        StringIO il211 = l.provideStringIO("IL211");
        StringIO jl211 = l.provideStringIO("JL211");

        Assert.assertNotNull(il211);
        Assert.assertNotNull(jl211);
        Assert.assertTrue(il211 != jl211);
    }

    @Test
    public void testDefaultNotInternal() {
        StringIO lut = l.provideStringIO("211");

        Assert.assertNotNull(lut);
        Assert.assertEquals("JL211", lut.getSystemName());
    }

    @Test
    public void testProvideUser() {
        StringIO l1 = l.provideStringIO("211");
        l1.setUserName("user 1");
        StringIO l2 = l.provideStringIO("user 1");
        StringIO l3 = l.getStringIO("user 1");

        Assert.assertNotNull(l1);
        Assert.assertNotNull(l2);
        Assert.assertNotNull(l3);
        Assert.assertEquals(l1, l2);
        Assert.assertEquals(l3, l2);
        Assert.assertEquals(l1, l3);

        StringIO l4 = l.getStringIO("JLuser 1");
        Assert.assertNull(l4);
    }
*/
    @Test
    public void testInstanceManagerIntegration() {
        jmri.util.JUnitUtil.resetInstanceManager();
        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class));

//        jmri.util.JUnitUtil.initInternalStringIOManager();

        Assert.assertTrue(InstanceManager.getDefault(StringIOManager.class) instanceof ProxyStringIOManager);

        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class));
        StringIO b = newStringIO("IC1", null);
        InstanceManager.getDefault(StringIOManager.class).register(b);
        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class).getBySystemName("IC1"));
//        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class).provideStringIO("IL1"));

        InternalStringIOManager m = new InternalStringIOManager(new InternalSystemConnectionMemo("J", "Juliet"));
        InstanceManager.setStringIOManager(m);

        b = newStringIO("IC2", null);
        InstanceManager.getDefault(StringIOManager.class).register(b);
        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class).getBySystemName("IC1"));
//        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class).provideStringIO("JL1"));
        b = newStringIO("IC3", null);
        InstanceManager.getDefault(StringIOManager.class).register(b);
        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class).getBySystemName("IC1"));
//        Assert.assertNotNull(InstanceManager.getDefault(StringIOManager.class).provideStringIO("IL2"));
    }

    /**
     * Number of light to test. Made a separate method so it can be overridden
     * in subclasses that do or don't support various numbers.
     * 
     * @return the number to test
     */
    protected int getNumToTest1() {
        return 9;
    }

    protected int getNumToTest2() {
        return 7;
    }

    @Before
    public void setUp() {
        JUnitUtil.setUp();
        // create and register the manager object
        l = new InternalStringIOManager(new InternalSystemConnectionMemo("J", "Juliet"));
        jmri.InstanceManager.setStringIOManager(l);
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    
    private class MyStringIO extends AbstractNamedBean implements StringIO {

        String _value = "";
        
        public MyStringIO(String sys, String userName) {
            super(sys, userName);
        }
        
        @Override
        public void setState(int s) throws JmriException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int getState() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public String getBeanType() {
            return "StringIO";
        }

        @Override
        public void setCommandedStringValue(@Nonnull String value) throws JmriException {
            _value = value;
        }

        @Override
        public String getCommandedStringValue() {
            return _value;
        }

    }
    
}
