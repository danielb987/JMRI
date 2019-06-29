package jmri.jmrit.logixng.implementation;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleSocketTestBase;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.FemaleGenericExpressionSocket;
import jmri.jmrit.logixng.FemaleGenericExpressionSocket.SocketType;
import jmri.jmrit.logixng.digital.expressions.And;
import jmri.jmrit.logixng.digital.implementation.DefaultMaleDigitalExpressionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test DefaultFemaleGenericExpressionSocket
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultFemaleGenericExpressionSocketTest extends FemaleSocketTestBase {

    private FemaleGenericExpressionSocket femaleGenericSocket;
    
    @Test
    public void testGetName() {
        Assert.assertTrue("String matches", "E1".equals(femaleSocket.getName()));
    }
    
    @Test
    public void testGetDescription() {
        Assert.assertTrue("String matches", "?".equals(femaleSocket.getShortDescription()));
        Assert.assertTrue("String matches", "? E1".equals(femaleSocket.getLongDescription()));
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
        DigitalExpression expression = new And("IQA55:E321");
        DigitalExpression otherExpression = new And("IQA55:E322");
        maleSocket = new DefaultMaleDigitalExpressionSocket(expression);
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
