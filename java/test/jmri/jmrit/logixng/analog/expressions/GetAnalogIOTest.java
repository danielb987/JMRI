package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.AnalogIO;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.MemoryManager;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.analog.expressions.GetAnalogIO;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test GetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class GetAnalogIOTest extends AbstractAnalogExpressionTestBase {

    MyAnalogIO _analogIO = new MyAnalogIO();
    
    @Test
    public void testCtor() throws JmriException {
        Assert.assertTrue("object exists", _expression != null);
        
        GetAnalogIO expression2;
        Assert.assertNotNull("analog is not null", _analogIO);
        _analogIO.setCommandedAnalogValue(10.2);
        
        expression2 = new GetAnalogIO("IQA55:12:AE11");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get analog none".equals(expression2.getLongDescription()));
        
        expression2 = new GetAnalogIO("IQA55:12:AE11", "My analog");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My analog".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get analog none".equals(expression2.getLongDescription()));
        
        expression2 = new GetAnalogIO("IQA55:12:AE11", _analogIO);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        System.out.format("AA: %s%n", expression2.getLongDescription());
        Assert.assertTrue("String matches", "Get analog IV1".equals(expression2.getLongDescription()));
        
        expression2 = new GetAnalogIO("IQA55:12:AE11", "My analog", _analogIO);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My analog".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get analog IV1".equals(expression2.getLongDescription()));
        
        // Test template
        expression2 = (GetAnalogIO)_expression.getNewObjectBasedOnTemplate("IQA55:12:AE12");
        Assert.assertNotNull("object exists", expression2);
//        Assert.assertTrue("Username matches", "My analog".equals(expression2.getUserName()));
//        Assert.assertTrue("String matches", "Get analog IV1".equals(expression2.getLongDescription()));
    }
    
    @Test
    public void testEvaluate() throws JmriException {
        AtomicBoolean isCompleted = new AtomicBoolean();
        _analogIO.setCommandedAnalogValue(0.0d);
        _expression.initEvaluation();
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
        _expression.initEvaluation();
        _analogIO.setCommandedAnalogValue(10.0d);
        Assert.assertTrue("Evaluate matches", 10.0d == _expression.evaluate(isCompleted));
        _expression.initEvaluation();
        ((GetAnalogIO)_expression).setAnalogIO(null);
        Assert.assertTrue("Evaluate matches", 0.0d == _expression.evaluate(isCompleted));
        _expression.reset();
    }
    
    @Test
    public void testAnalogIO() {
        ((GetAnalogIO)_expression).setAnalogIO(null);
        Assert.assertTrue("AnalogIO matches", null == ((GetAnalogIO)_expression).getAnalogIO());
        ((GetAnalogIO)_expression).setAnalogIO(_analogIO);
        Assert.assertTrue("AnalogIO matches", _analogIO == ((GetAnalogIO)_expression).getAnalogIO());
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _expression.getCategory());
    }
/*    
    // GetAnalogIO.setup() is not yet properly implemented since there is no AnalogIO_Manager yet.
    @Test
    public void testSetup() throws JmriException {
        Assert.assertNotNull("analog is not null", _analogIO);
        _analogIO.setCommandedAnalogValue(10.2);
        GetAnalogIO expression2 = new GetAnalogIO("IQA55:12:AE321");
//        System.out.format("AA: %s%n", expression2.getLongDescription());
        Assert.assertTrue("String matches", "Get analog none".equals(expression2.getLongDescription()));
        expression2.setup();
        Assert.assertTrue("String matches", "Get analog none".equals(expression2.getLongDescription()));
        expression2.setAnalogIO_SystemName(_analogIO.getSystemName());
        expression2.setup();
        Assert.assertTrue("String matches", "Get analog none".equals(expression2.getLongDescription()));
//        Assert.assertTrue("String matches", "Get analog IV1".equals(expression2.getLongDescription()));
    }
*/    
    @Test
    public void testShortDescription() {
        Assert.assertTrue("String matches", "Get analog IV1".equals(_expression.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Get analog IV1".equals(_expression.getLongDescription()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        _expression = new GetAnalogIO("IQA55:12:AE321", "GetAnalogIO", _analogIO);
    }

    @After
    public void tearDown() {
        _expression.dispose();
        JUnitUtil.tearDown();
    }
    
    
    
    private class MyAnalogIO extends AbstractNamedBean implements AnalogIO {

        private double _value = 0.0;
        
        private MyAnalogIO() {
            super("IV1");
        }
        
        @Override
        public void setState(int s) throws JmriException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getState() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getBeanType() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setCommandedAnalogValue(double value) throws JmriException {
            _value = value;
        }

        @Override
        public double getCommandedAnalogValue() {
            return _value;
        }

        @Override
        public double getMin() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getMax() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getResolution() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public AbsoluteOrRelative getAbsoluteOrRelative() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
