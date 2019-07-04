package jmri.jmrit.logixng.string.implementation;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleSocketTestBase;
import jmri.jmrit.logixng.string.expressions.GetStringIO;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.StringExpressionBean;

/**
 * Test DefaultFemaleStringExpressionSocket
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultFemaleStringExpressionSocketTest extends FemaleSocketTestBase {

    @Test
    public void testGetName() {
        Assert.assertTrue("String matches", "E1".equals(femaleSocket.getName()));
    }
    
    @Test
    public void testGetDescription() {
        Assert.assertTrue("String matches", "?s".equals(femaleSocket.getShortDescription()));
        Assert.assertTrue("String matches", "?s E1".equals(femaleSocket.getLongDescription()));
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
        StringExpressionBean expression = new GetStringIO("IQA55:A321");
        StringExpressionBean otherExpression = new GetStringIO("IQA55:A322");
        maleSocket = new DefaultMaleStringExpressionSocket(expression);
        otherMaleSocket = new DefaultMaleStringExpressionSocket(otherExpression);
        femaleSocket = new DefaultFemaleStringExpressionSocket(null, new FemaleSocketListener() {
            @Override
            public void connected(FemaleSocket socket) {
                flag.set(true);
            }

            @Override
            public void disconnected(FemaleSocket socket) {
                flag.set(true);
            }
        }, "E1");
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
