package jmri.jmrit.logixng.implementation;

import java.awt.GraphicsEnvironment;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
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
import jmri.jmrit.logixng.zTest.TestLogixNG;

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
    public LogixNG createLogixNG(String systemName, String userName, boolean setupTree)
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
        
        if (setupTree) {
            // Setup initial tree for the LogixNG
            setupInitialLogixNGTree(x);
        }
        
        return x;
    }

    @Override
    public LogixNG createLogixNG(String userName, boolean setupTree) throws IllegalArgumentException {
        int nextAutoLogixNGRef = lastAutoLogixNGRef + 1;
        StringBuilder b = new StringBuilder("IQA");
        String nextNumber = paddedNumber.format(nextAutoLogixNGRef);
        b.append(nextNumber);
        return createLogixNG(b.toString(), userName, setupTree);
    }

    @Override
    public void setupInitialLogixNGTree(LogixNG logixNG) {
        try {
            FemaleSocket femaleSocket = logixNG.getFemaleSocket();
            MaleDigitalActionSocket actionManySocket =
                    InstanceManager.getDefault(DigitalActionManager.class).registerAction(new Many(femaleSocket.getLogixNG()));
            femaleSocket.connect(actionManySocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            femaleSocket = actionManySocket.getChild(0);
            MaleDigitalActionSocket actionHoldAnythingSocket =
                    InstanceManager.getDefault(DigitalActionManager.class).registerAction(new HoldAnything(femaleSocket.getLogixNG()));
            femaleSocket.connect(actionHoldAnythingSocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            femaleSocket = actionManySocket.getChild(1);
            MaleDigitalActionSocket actionIfThenSocket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .registerAction(new IfThen(femaleSocket.getLogixNG(), IfThen.Type.TRIGGER_ACTION));
            femaleSocket.connect(actionIfThenSocket);

            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            /* FOR TESTING ONLY */
            femaleSocket = actionIfThenSocket.getChild(0);
            MaleDigitalExpressionSocket expressionAndSocket =
                    InstanceManager.getDefault(DigitalExpressionManager.class)
                            .registerExpression(new And(femaleSocket.getLogixNG()));
            femaleSocket.connect(expressionAndSocket);
            
            femaleSocket = actionIfThenSocket.getChild(1);
            MaleDigitalActionSocket actionIfThenSocket2 =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .registerAction(new IfThen(femaleSocket.getLogixNG(), IfThen.Type.CONTINOUS_ACTION));
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
    
    /** {@inheritDoc} */
    @Override
    public void activateAllLogixNGs() {
//        jmri.configurexml.ConfigXmlManager a;
//        jmri.managers.configurexml.AbstractSignalHeadManagerXml b;
//        jmri.implementation.configurexml.SE8cSignalHeadXml c;
        for (LogixNG logixNG : _tsys.values()) {
            System.out.format("LogixNG loaded: %s, %s%n", logixNG.getSystemName(), logixNG.getUserName());
        }
//        throw new UnsupportedOperationException("Not supported yet.");
        
        
        // FOR TESTING ONLY. REMOVE LATER.
        if ((dialog == null) && (!GraphicsEnvironment.isHeadless())) {
            dialog = new TestLogixNG(new javax.swing.JFrame(), false);
            dialog.setVisible(true);
        }
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
