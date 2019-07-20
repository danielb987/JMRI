package jmri.jmrit.logixng.implementation;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleSocketTestBase;
import jmri.jmrit.logixng.FemaleGenericExpressionSocket;
import jmri.jmrit.logixng.FemaleGenericExpressionSocket.SocketType;
import jmri.jmrit.logixng.digital.implementation.DefaultMaleDigitalExpressionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout;

/**
 * Test DefaultFemaleGenericExpressionSocket
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultFemaleGenericExpressionSocketTest extends FemaleSocketTestBase {

    private String _turnoutSystemName;
    private Turnout _turnout;
    private ExpressionTurnout _expression;
    private FemaleGenericExpressionSocket femaleGenericSocket;
    
    @Test
    public void testSocketType() {
        Assert.assertEquals("strings are equal",
                Bundle.getMessage("SocketTypeDigital"),
                FemaleGenericExpressionSocket.SocketType.DIGITAL.toString());
        Assert.assertEquals("strings are equal",
                Bundle.getMessage("SocketTypeAnalog"),
                FemaleGenericExpressionSocket.SocketType.ANALOG.toString());
        Assert.assertEquals("strings are equal",
                Bundle.getMessage("SocketTypeString"),
                FemaleGenericExpressionSocket.SocketType.STRING.toString());
        Assert.assertEquals("strings are equal",
                Bundle.getMessage("SocketTypeGeneric"),
                FemaleGenericExpressionSocket.SocketType.GENERIC.toString());
    }
    
    @Test
    public void testGetName() {
        Assert.assertTrue("String matches", "E1".equals(femaleSocket.getName()));
    }
    
    @Test
    public void testGetDescription() {
        Assert.assertTrue("String matches", "?".equals(femaleSocket.getShortDescription()));
        Assert.assertTrue("String matches", "? E1".equals(femaleSocket.getLongDescription()));
    }
    
    @Override
    protected boolean hasSocketBeenSetup() {
        if (_expression.getTurnout() == null) {
            return false;
        }
        return _turnout == _expression.getTurnout().getBean();
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        
        flag = new AtomicBoolean();
        errorFlag = new AtomicBoolean();
        _turnoutSystemName = "IT1";
        _turnout = InstanceManager.getDefault(TurnoutManager.class).provide(_turnoutSystemName);
        _expression = new ExpressionTurnout("IQA55:1:DE321");
        _expression.setTurnoutName(_turnoutSystemName);
        ExpressionTurnout otherExpression = new ExpressionTurnout("IQA55:1:DE322");
        maleSocket = new DefaultMaleDigitalExpressionSocket(_expression);
        otherMaleSocket = new DefaultMaleDigitalExpressionSocket(otherExpression);
        femaleGenericSocket = new DefaultFemaleGenericExpressionSocket(SocketType.GENERIC, null, new FemaleSocketListener() {
            @Override
            public void connected(FemaleSocket socket) {
                flag.set(true);
            }

            @Override
            public void disconnected(FemaleSocket socket) {
                flag.set(true);
            }
        }, "E1");
        femaleSocket = femaleGenericSocket;
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
