package jmri.jmrit.logixng;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.implementation.DefaultLogixNG;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import jmri.jmrit.logixng.digital.actions.IfThen;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.digital.expressions.And;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test LogixNG
 * 
 * @author Daniel Bergqvist 2018
 */
public class LogixNGTest {
    
    @Test
    public void testBaseLock() {
        Assert.assertTrue("isChangeableByUser is correct", Base.Lock.NONE.isChangeableByUser());
        Assert.assertTrue("isChangeableByUser is correct", Base.Lock.USER_LOCK.isChangeableByUser());
        Assert.assertFalse("isChangeableByUser is correct", Base.Lock.HARD_LOCK.isChangeableByUser());
        Assert.assertFalse("isChangeableByUser is correct", Base.Lock.TEMPLATE_LOCK.isChangeableByUser());
    }
    
    @Test
    public void testBundleClass() {
        Assert.assertTrue("bundle is correct", "Test Bundle bb aa cc".equals(Bundle.getMessage("TestBundle", "aa", "bb", "cc")));
        Assert.assertTrue("bundle is correct", "Generic".equals(Bundle.getMessage(Locale.US, "SocketTypeGeneric")));
        Assert.assertTrue("bundle is correct", "Test Bundle bb aa cc".equals(Bundle.getMessage(Locale.US, "TestBundle", "aa", "bb", "cc")));
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("isChangeableByUser is correct", "Item".equals(Category.ITEM.toString()));
        Assert.assertTrue("isChangeableByUser is correct", "Common".equals(Category.COMMON.toString()));
        Assert.assertTrue("isChangeableByUser is correct", "Other".equals(Category.OTHER.toString()));
        Assert.assertTrue("isChangeableByUser is correct", "Extravaganza".equals(Category.EXRAVAGANZA.toString()));
    }
    
    @Test
    public void testDigitalAction() {
        AtomicBoolean exceptionIsThrown = new AtomicBoolean();
        DigitalAction a = new MyDigitalAction();
        
        try {
            a.setEnableExecution(true);
        } catch (UnsupportedOperationException e) {
//            System.err.format("Error message: %s%n", e.getMessage());
            Assert.assertTrue("exception message matches", "This digital action does not supports the method setEnableExecution()".equals(e.getMessage()));
            exceptionIsThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", exceptionIsThrown.get());
        jmri.util.JUnitAppender.assertErrorMessageStartsWith("This digital action does not supports the method setEnableExecution()");
        
        exceptionIsThrown.set(false);
        try {
            a.isExecutionEnabled();
        } catch (UnsupportedOperationException e) {
//            System.err.format("Error message: %s%n", e.getMessage());
            Assert.assertTrue("exception message matches", "This digital action does not supports the method isExecutionEnabled()".equals(e.getMessage()));
            exceptionIsThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", exceptionIsThrown.get());
        jmri.util.JUnitAppender.assertErrorMessageStartsWith("This digital action does not supports the method isExecutionEnabled()");
    }
    
    @Test
    public void testManagers() throws SocketAlreadyConnectedException {
        String systemName;
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        logixNG.addConditionalNG(conditionalNG);
        InstanceManager.getDefault(LogixNG_Manager.class).setupInitialConditionalNGTree(conditionalNG);
        MaleSocket many = conditionalNG.getChild(0).getConnectedSocket();
//        System.err.format("aa: %s%n", many.getLongDescription());
        Assert.assertTrue("description is correct", "Many".equals(many.getLongDescription()));
        MaleSocket ifThen = many.getChild(1).getConnectedSocket();
//        System.err.format("aa: %s%n", ifThen.getLongDescription());
        Assert.assertTrue("description is correct", "If E then A".equals(ifThen.getLongDescription()));
        systemName = InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG);
        DigitalExpressionBean expression = new ExpressionTurnout(systemName, "An expression for test");  // NOI18N
        MaleSocket digitalExpressionBean = InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
        ifThen.getChild(0).connect(digitalExpressionBean);
//        InstanceManager.getDefault(jmri.DigitalExpressionManager.class).addExpression(new ExpressionTurnout(systemName, "LogixNG 102, DigitalExpressionBean 26"));  // NOI18N
        systemName = InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(conditionalNG);
        DigitalActionBean action = new ActionTurnout(systemName, "An action for test");  // NOI18N
        MaleSocket digitalActionBean = InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
        ifThen.getChild(1).connect(digitalActionBean);
        
        logixNG.setParentForAllChildren();
        
        Assert.assertTrue("conditionalng is correct", conditionalNG == digitalActionBean.getConditionalNG());
        Assert.assertTrue("conditionalng is correct", conditionalNG == conditionalNG.getConditionalNG());
        Assert.assertTrue("logixlng is correct", logixNG == digitalActionBean.getLogixNG());
        Assert.assertTrue("logixlng is correct", logixNG == logixNG.getLogixNG());
        
        Assert.assertTrue("instance manager is correct", Base.InstanceManagerContainer.defaultInstanceManager == digitalActionBean.getInstanceManager());
        Assert.assertTrue("instance manager is correct", Base.InstanceManagerContainer.defaultInstanceManager == conditionalNG.getInstanceManager());
        Assert.assertTrue("instance manager is correct", Base.InstanceManagerContainer.defaultInstanceManager == logixNG.getInstanceManager());
    }
    
    @Test
    public void testSetup() throws SocketAlreadyConnectedException {
        
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        DefaultConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        logixNG.addConditionalNG(conditionalNG);
        
        String systemName = InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(conditionalNG);
        DigitalActionBean action = new ActionTurnout(systemName, "An action for test");  // NOI18N
        MaleSocket digitalActionBean = InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
        
        conditionalNG.setSocketSystemName(systemName);
        logixNG.setup();
        
        logixNG.setParentForAllChildren();
        
//        System.err.format("%s%n", conditionalNG.getChild(0).getConnectedSocket().getLongDescription());
        Assert.assertTrue("conditionalng child is correct",
                "Set turnout '' to Thrown"
                        .equals(conditionalNG.getChild(0).getConnectedSocket().getLongDescription()));
        Assert.assertTrue("conditionalng is correct", conditionalNG == digitalActionBean.getConditionalNG());
        Assert.assertTrue("logixlng is correct", logixNG == digitalActionBean.getLogixNG());
    }
    
    @Test
    public void testExceptions() {
        new SocketAlreadyConnectedException().getMessage();
    }
    
    @Test
    public void testBundle() {
        Assert.assertTrue("bean type is correct", "LogixNG".equals(new DefaultLogixNG("IQA55").getBeanType()));
        Assert.assertTrue("bean type is correct", "Action".equals(new IfThen("IQA55:A321", IfThen.Type.TRIGGER_ACTION).getBeanType()));
        Assert.assertTrue("bean type is correct", "Expression".equals(new And("IQA55:E321").getBeanType()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        JUnitUtil.initDigitalExpressionManager();
        JUnitUtil.initDigitalActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    
    private class MyBase implements Base {

        @Override
        public String getSystemName() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public String getUserName() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void setUserName(String s) throws NamedBean.BadUserNameException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public String getShortDescription() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public String getLongDescription() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Base getNewObjectBasedOnTemplate(String sys) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Base getParent() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void setParent(Base parent) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int getChildCount() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Category getCategory() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean isExternal() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Lock getLock() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void setLock(Lock lock) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void setup() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void dispose() {
            throw new UnsupportedOperationException("Not supported.");
        }
    
    }
    
    private class MyDigitalAction extends MyBase implements DigitalAction {

        @Override
        public boolean executeStart() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean executeContinue() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean executeRestart() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void abort() {
            throw new UnsupportedOperationException("Not supported.");
        }
        
    }
    
}
