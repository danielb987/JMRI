package jmri.jmrit.logixng.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.ActionManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Expression;
import jmri.jmrit.logixng.ExpressionManager;
import jmri.jmrit.logixng.FemaleActionSocket;
import jmri.jmrit.logixng.FemaleExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketFactory;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleActionSocket;
import jmri.jmrit.logixng.MaleExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.actions.ActionHoldAnything;
import jmri.jmrit.logixng.actions.ActionIfThen;
import jmri.jmrit.logixng.actions.ActionMany;
import jmri.managers.AbstractManager;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;

/**
 * Class providing the basic logic of the LogixNG_Manager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Default_LogixNG_Manager extends AbstractManager<LogixNG>
        implements LogixNG_Manager {

    DecimalFormat paddedNumber = new DecimalFormat("0000");

    int lastAutoLogixNGRef = 0;
    List<FemaleSocketFactory> _femaleSocketFactories = new ArrayList<>();
    
    
    public Default_LogixNG_Manager() {
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
        if (systemName.toUpperCase().matches("IQ\\:(A\\:)?\\d+")) {
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
        if (systemName.startsWith("IQ:A:")) {
            try {
                int autoNumber = Integer.parseInt(systemName.substring(5));
                if (autoNumber > lastAutoLogixNGRef) {
                    lastAutoLogixNGRef = autoNumber;
                }
            } catch (NumberFormatException e) {
                log.warn("Auto generated SystemName " + systemName + " is not in the correct format");
            }
        }
        
        // Setup initial tree for the LogixNG
        setupInitialLogixNGTree(x);
        
        return x;
    }

    @Override
    public LogixNG createLogixNG(String userName) throws IllegalArgumentException {
        int nextAutoLogixNGRef = lastAutoLogixNGRef + 1;
        StringBuilder b = new StringBuilder("IQ:A:");
        String nextNumber = paddedNumber.format(nextAutoLogixNGRef);
        b.append(nextNumber);
        return createLogixNG(b.toString(), userName);
    }

    @Override
    public void setupInitialLogixNGTree(LogixNG newLogix) {
        try {
            MaleActionSocket actionManySocket =
                    InstanceManager.getDefault(ActionManager.class).register(new ActionMany(newLogix));
            FemaleSocket femaleSocket = newLogix.getFemaleSocket();
            femaleSocket.connect(actionManySocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            MaleActionSocket actionHoldAnythingSocket =
                    InstanceManager.getDefault(ActionManager.class).register(new ActionHoldAnything(newLogix));
            femaleSocket = actionManySocket.getChild(0);
            femaleSocket.connect(actionHoldAnythingSocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            MaleActionSocket actionIfThenSocket =
                    InstanceManager.getDefault(ActionManager.class)
                            .register(new ActionIfThen(newLogix, ActionIfThen.Type.TRIGGER_ACTION));
            femaleSocket = actionManySocket.getChild(1);
            femaleSocket.connect(actionIfThenSocket);
            
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
//    public MaleActionSocket createMaleActionSocket(Action action) {
//        return new DefaultMaleActionSocket(action);
//    }

//    @Override
//    public MaleExpressionSocket createMaleExpressionSocket(Expression expression) {
//        return new DefaultMaleExpressionSocket(expression);
//    }

    @Override
    public void activateAllLogixNGs() {
//        jmri.configurexml.ConfigXmlManager a;
//        jmri.managers.configurexml.AbstractSignalHeadManagerXml b;
//        jmri.implementation.configurexml.SE8cSignalHeadXml c;
        for (LogixNG newLogix : _tsys.values()) {
            System.out.format("LogixNG loaded: %s, %s%n", newLogix.getSystemName(), newLogix.getUserName());
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
    
    static Default_LogixNG_Manager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public Default_LogixNG_Manager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new Default_LogixNG_Manager();
        }
        return (_instance);
    }

    private final static Logger log = LoggerFactory.getLogger(Default_LogixNG_Manager.class);

    @Override
    public void registerFemaleSocketFactory(FemaleSocketFactory factory) {
        _femaleSocketFactories.add(factory);
    }

    @Override
    public List<FemaleSocketFactory> getFemaleSocketFactories() {
        return new ArrayList<>(_femaleSocketFactories);
    }
}
