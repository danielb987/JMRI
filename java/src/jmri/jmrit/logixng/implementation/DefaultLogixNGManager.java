package jmri.jmrit.logixng.implementation;

import java.awt.GraphicsEnvironment;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
import jmri.JmriException;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketFactory;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.digital.actions.HoldAnything;
import jmri.jmrit.logixng.digital.actions.IfThen;
import jmri.jmrit.logixng.digital.actions.Many;
import jmri.jmrit.logixng.digital.expressions.And;
import jmri.managers.AbstractManager;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.digital.actions.ActionAtomicBoolean;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;
import jmri.jmrit.logixng.zTest.TestLogixNG;
import org.junit.Assert;

/**
 * Class providing the basic logic of the LogixNG_Manager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultLogixNGManager extends AbstractManager<LogixNG>
        implements LogixNG_Manager {

    // FOR TESTING ONLY. REMOVE LATER.
    private TestLogixNG dialog;
    
    DecimalFormat paddedNumber = new DecimalFormat("0000");

    int lastAutoLogixNGRef = 0;
    List<FemaleSocketFactory> _femaleSocketFactories = new ArrayList<>();
    
    
    public DefaultLogixNGManager() {
        super();
        
        // The LogixNGPreferences class may load plugins so we must ensure
        // it's loaded here.
        InstanceManager.getDefault(LogixNGPreferences.class);
    }

    @Override
    public int getXMLOrder() {
        return LOGIXNGS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameLogixNG");
    }

    @Override
    public String getSystemPrefix() {
        return "I";
    }

    @Override
    public char typeLetter() {
        return 'Q';
    }

    /**
     * Test if parameter is a properly formatted system name.
     *
     * @param systemName the system name
     * @return enum indicating current validity, which might be just as a prefix
     */
    @Override
    public NameValidity validSystemNameFormat(String systemName) {
        if (systemName.toUpperCase().matches("IQA?\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public LogixNG createLogixNG(String systemName, String userName)
            throws IllegalArgumentException {
        
        // Check that Logix does not already exist
        LogixNG x;
        if (userName != null && !userName.equals("")) {
            x = getByUserName(userName);
            if (x != null) {
                throw new IllegalArgumentException("UserName " + userName + " already exists");
            }
        }
        x = getBySystemName(systemName);
        if (x != null) {
            return null;
        }
        // Check if system name is valid
        if (this.validSystemNameFormat(systemName) != NameValidity.VALID) {
            throw new IllegalArgumentException("SystemName " + systemName + " is not in the correct format");
//            log.warn("SystemName " + systemName + " is not in the correct format");
//            return null;
        }
        // LogixNG does not exist, create a new LogixNG
        x = new DefaultLogixNG(systemName, userName);
        // save in the maps
        register(x);

        /* The following keeps track of the last created auto system name.
         currently we do not reuse numbers, although there is nothing to stop the
         user from manually recreating them */
        if (systemName.startsWith("IQA")) {
            try {
                int autoNumber = Integer.parseInt(systemName.substring(5));
                if (autoNumber > lastAutoLogixNGRef) {
                    lastAutoLogixNGRef = autoNumber;
                }
            } catch (NumberFormatException e) {
                log.warn("Auto generated SystemName " + systemName + " is not in the correct format");
            }
        }
        
//        if (setupTree) {
            // Setup initial tree for the LogixNG
//            setupInitialConditionalNGTree(x);
//            throw new UnsupportedOperationException("Throw exception for now until this is fixed");
//        }
        
        return x;
    }

    @Override
    public LogixNG createLogixNG(String userName) throws IllegalArgumentException {
        int nextAutoLogixNGRef = lastAutoLogixNGRef + 1;
        StringBuilder b = new StringBuilder("IQA");
        String nextNumber = paddedNumber.format(nextAutoLogixNGRef);
        b.append(nextNumber);
        return createLogixNG(b.toString(), userName);
    }

    @Override
    public void setupInitialConditionalNGTree(ConditionalNG conditionalNG) {
        try {
            FemaleSocket femaleSocket = conditionalNG.getFemaleSocket();
            MaleDigitalActionSocket actionManySocket =
                    InstanceManager.getDefault(DigitalActionManager.class).registerAction(new Many(femaleSocket.getConditionalNG()));
            femaleSocket.connect(actionManySocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            femaleSocket = actionManySocket.getChild(0);
            MaleDigitalActionSocket actionHoldAnythingSocket =
                    InstanceManager.getDefault(DigitalActionManager.class).registerAction(new HoldAnything(femaleSocket.getConditionalNG()));
            femaleSocket.connect(actionHoldAnythingSocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            femaleSocket = actionManySocket.getChild(1);
            MaleDigitalActionSocket actionIfThenSocket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .registerAction(new IfThen(femaleSocket.getConditionalNG(), IfThen.Type.TRIGGER_ACTION));
            femaleSocket.connect(actionIfThenSocket);

            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            femaleSocket = actionIfThenSocket.getChild(0);
            MaleDigitalExpressionSocket expressionAndSocket =
                    InstanceManager.getDefault(DigitalExpressionManager.class)
                            .registerExpression(new And(femaleSocket.getConditionalNG()));
            femaleSocket.connect(expressionAndSocket);
            
            femaleSocket = actionIfThenSocket.getChild(1);
            MaleDigitalActionSocket actionIfThenSocket2 =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .registerAction(new IfThen(femaleSocket.getConditionalNG(), IfThen.Type.CONTINOUS_ACTION));
            femaleSocket.connect(actionIfThenSocket2);
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */

        } catch (SocketAlreadyConnectedException e) {
            // This should never be able to happen.
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public LogixNG getLogixNG(String name) {
        LogixNG x = getByUserName(name);
        if (x != null) {
            return x;
        }
        return getBySystemName(name);
    }

    @Override
    public LogixNG getByUserName(String name) {
        return _tuser.get(name);
    }

    @Override
    public LogixNG getBySystemName(String name) {
        return _tsys.get(name);
    }

//    @Override
//    public MaleDigitalActionSocket createMaleActionSocket(DigitalAction action) {
//        return new DefaultMaleActionSocket(action);
//    }

//    @Override
//    public MaleDigitalExpressionSocket createMaleExpressionSocket(DigitalExpression expression) {
//        return new DefaultMaleExpressionSocket(expression);
//    }

    /** {@inheritDoc} */
    @Override
    public void resolveAllTrees() {
        for (LogixNG logixNG : _tsys.values()) {
//            System.out.format("LogixNG loaded: %s, %s%n", logixNG.getSystemName(), logixNG.getUserName());
            logixNG.setParentForAllChildren();
        }
    }
    
    public void testLogixNGs() {
        
        // FOR TESTING ONLY. REMOVE LATER.
        if (1==0) {
            if ((dialog == null) && (!GraphicsEnvironment.isHeadless())) {
                dialog = new TestLogixNG(new javax.swing.JFrame(), false);
                dialog.setVisible(true);

                for (jmri.Logix l : InstanceManager.getDefault(jmri.LogixManager.class).getNamedBeanSet()) {
                    String sysName = l.getSystemName();
                    if (!sysName.equals("SYS") && !sysName.startsWith("RTX")) {
                        jmri.jmrit.logixng.tools.ImportLogix il = new jmri.jmrit.logixng.tools.ImportLogix(l);
                        il.doImport();
                    }
                }
            }
        }
        
        // FOR TESTING ONLY. REMOVE LATER.
        if (1==1) {
            int store = 0;
            try {
                if (store == 1) {
                    Turnout turnout = InstanceManager.getDefault(TurnoutManager.class).provide("IT1_Daniel");
                    turnout.setCommandedState(Turnout.CLOSED);
    //                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                    LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
                    ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
                    logixNG.addConditionalNG(conditionalNG);
                    logixNG.activateLogixNG();

                    DigitalAction actionIfThen = new IfThen(conditionalNG, IfThen.Type.TRIGGER_ACTION);
                    MaleSocket socketIfThen = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionIfThen);
                    conditionalNG.getChild(0).connect(socketIfThen);

                    ExpressionTurnout expressionTurnout = new ExpressionTurnout(conditionalNG);
                    expressionTurnout.setTurnout(turnout);
                    expressionTurnout.set_Is_IsNot(Is_IsNot_Enum.IS);
                    expressionTurnout.setTurnoutState(ExpressionTurnout.TurnoutState.THROWN);
                    MaleSocket socketTurnout = InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expressionTurnout);
                    socketIfThen.getChild(0).connect(socketTurnout);

    //                DigitalAction actionAtomicBoolean = new ActionAtomicBoolean(conditionalNG, atomicBoolean, true);
    //                MaleSocket socketAtomicBoolean = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionAtomicBoolean);
    //                socketIfThen.getChild(1).connect(socketAtomicBoolean);

                    // The action is not yet executed so the atomic boolean should be false
    //                Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
                    // Throw the switch. This should not execute the conditional.
                    turnout.setCommandedState(Turnout.THROWN);
                    // The conditionalNG is not yet enabled so it shouldn't be executed.
                    // So the atomic boolean should be false
    //                Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
                    // Close the switch. This should not execute the conditional.
                    turnout.setCommandedState(Turnout.CLOSED);
                    // Enable the conditionalNG and all its children.
                    conditionalNG.setEnabled(true);
                    // The action is not yet executed so the atomic boolean should be false
    //                Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
                    // Throw the switch. This should execute the conditional.
                    turnout.setCommandedState(Turnout.THROWN);
                    // The action should now be executed so the atomic boolean should be true
    //                Assert.assertTrue("atomicBoolean is true",atomicBoolean.get());
                }
                
                // Store panels
                jmri.ConfigureManager cm = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
                if (cm == null) {
                    log.error("Failed to make backup due to unable to get default configure manager");
                } else {
                    java.io.File file = new java.io.File("F:\\temp\\DanielTestarLogixNG.xml");
//                    cm.makeBackup(file);
                    // and finally store
                    
                    if (store == 1) {
                        boolean results = cm.storeUser(file);
                        log.debug(results ? "store was successful" : "store failed");
                        if (!results) {
                            log.error("Failed to store panel");
                            System.exit(-1);
                        }
                    } else {
                        boolean results = cm.load(file);
                        log.debug(results ? "load was successful" : "store failed");
                        if (results) {
                            resolveAllTrees();
                            setupAllLogixNGs();
                        } else {
                            log.error("Failed to load panel");
                            System.exit(-1);
                        }
                    }
                }
                
            } catch (JmriException ex) {
                log.error("Failed to store panel", ex);
                System.exit(-1);
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void setupAllLogixNGs() {
//        jmri.configurexml.ConfigXmlManager a;
//        jmri.managers.configurexml.AbstractSignalHeadManagerXml b;
//        jmri.implementation.configurexml.SE8cSignalHeadXml c;
        for (LogixNG logixNG : _tsys.values()) {
            System.err.format("LogixNG loaded: %s, %s%n", logixNG.getSystemName(), logixNG.getUserName());
            logixNG.setup();
        }
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteLogixNG(LogixNG x) {
        // delete the LogixNG
        deregister(x);
        x.dispose();
    }

    @Override
    public void setLoadDisabled(boolean s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    static DefaultLogixNGManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultLogixNGManager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new DefaultLogixNGManager();
        }
        return (_instance);
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultLogixNGManager.class);

    @Override
    public void registerFemaleSocketFactory(FemaleSocketFactory factory) {
        _femaleSocketFactories.add(factory);
    }

    @Override
    public List<FemaleSocketFactory> getFemaleSocketFactories() {
        return new ArrayList<>(_femaleSocketFactories);
    }
}
