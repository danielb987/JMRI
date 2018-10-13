package jmri.jmrit.newlogix;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jmri.InstanceManager;
import jmri.NewLogix;
import jmri.Expression;
import jmri.Action;

/**
 * Test NewLogix
 * 
 * @author Daniel Bergqvist 2018
 */
public class NewLogixTest {

    @Test
    public void testManagers() {
        NewLogix newLogix = InstanceManager.getDefault(jmri.NewLogixManager.class).createNewNewLogix("IQ102", "NewLogix 102");  // NOI18N
        Expression expression = InstanceManager.getDefault(jmri.ExpressionManager.class).createNewExpression("IQ102:E26", "NewLogix 102, Expression 26");  // NOI18N
        Action action = InstanceManager.getDefault(jmri.ActionManager.class).createNewAction("IQ102:A52", "NewLogix 102, Action 52");  // NOI18N
    }
    
    @Test
    public void testBundle() {
        Assert.assertTrue("bean type is correct", "New Logix".equals(new DefaultNewLogix("IQA55", null, null).getBeanType()));
        Assert.assertTrue("bean type is correct", "Action".equals(new ActionDoIf("IQA55:A321", null, ActionDoIf.Type.TRIGGER_ACTION, null, null).getBeanType()));
        Assert.assertTrue("bean type is correct", "Expression".equals(new ExpressionAnd("IQA55:E321", null).getBeanType()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initNewLogixManager();
        JUnitUtil.initExpressionManager();
        JUnitUtil.initActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
