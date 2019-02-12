package jmri.jmrit.newlogix.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.newlogix.Action;
import jmri.jmrit.newlogix.ActionManager;
import jmri.jmrit.newlogix.Base;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.ExpressionManager;
import jmri.jmrit.newlogix.FemaleActionSocket;
import jmri.jmrit.newlogix.FemaleExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketFactory;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleActionSocket;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.NewLogixManager;
import jmri.jmrit.newlogix.SocketAlreadyConnectedException;
import jmri.jmrit.newlogix.actions.ActionHoldAnything;
import jmri.jmrit.newlogix.actions.ActionIfThenElse;
import jmri.jmrit.newlogix.actions.ActionMany;
import jmri.managers.AbstractManager;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class providing the basic logic of the NewLogixManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultNewLogixManager extends AbstractManager<NewLogix>
        implements NewLogixManager {

    DecimalFormat paddedNumber = new DecimalFormat("0000");

    int lastAutoNewLogixRef = 0;
    List<FemaleSocketFactory> _femaleSocketFactories = new ArrayList<>();
    
    
    public DefaultNewLogixManager() {
        super();
        
        // The NewLogixPreferences class may load plugins so we must ensure
        // it's loaded here.
        InstanceManager.getDefault(NewLogixPreferences.class);
    }

    @Override
    public int getXMLOrder() {
        return NEWLOGIXS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameNewLogix");
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
    public NewLogix createNewLogix(String systemName, String userName)
            throws IllegalArgumentException {
        
        // Check that Logix does not already exist
        NewLogix x;
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
        // NewLogix does not exist, create a new NewLogix
        x = new DefaultNewLogix(systemName, userName);
        // save in the maps
        register(x);

        /* The following keeps track of the last created auto system name.
         currently we do not reuse numbers, although there is nothing to stop the
         user from manually recreating them */
        if (systemName.startsWith("IQ:A:")) {
            try {
                int autoNumber = Integer.parseInt(systemName.substring(5));
                if (autoNumber > lastAutoNewLogixRef) {
                    lastAutoNewLogixRef = autoNumber;
                }
            } catch (NumberFormatException e) {
                log.warn("Auto generated SystemName " + systemName + " is not in the correct format");
            }
        }
        
        // Setup initial tree for the NewLogix
        setupInitialNewLogixTree(x);
        
        return x;
    }

    @Override
    public NewLogix createNewLogix(String userName) throws IllegalArgumentException {
        int nextAutoNewLogixRef = lastAutoNewLogixRef + 1;
        StringBuilder b = new StringBuilder("IQ:A:");
        String nextNumber = paddedNumber.format(nextAutoNewLogixRef);
        b.append(nextNumber);
        return createNewLogix(b.toString(), userName);
    }

    @Override
    public void setupInitialNewLogixTree(NewLogix newLogix) {
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
                            .register(new ActionIfThenElse(newLogix, ActionIfThenElse.Type.TRIGGER_ACTION));
            femaleSocket = actionManySocket.getChild(1);
            femaleSocket.connect(actionIfThenSocket);
            
        } catch (SocketAlreadyConnectedException e) {
            // This should never be able to happen.
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public NewLogix getNewLogix(String name) {
        NewLogix x = getByUserName(name);
        if (x != null) {
            return x;
        }
        return getBySystemName(name);
    }

    @Override
    public NewLogix getByUserName(String name) {
        return _tuser.get(name);
    }

    @Override
    public NewLogix getBySystemName(String name) {
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
    public void activateAllNewLogixs() {
//        jmri.configurexml.ConfigXmlManager a;
//        jmri.managers.configurexml.AbstractSignalHeadManagerXml b;
//        jmri.implementation.configurexml.SE8cSignalHeadXml c;
        for (NewLogix newLogix : _tsys.values()) {
            System.out.format("NewLogix loaded: %s, %s%n", newLogix.getSystemName(), newLogix.getUserName());
        }
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteNewLogix(NewLogix x) {
        // delete the NewLogix
        deregister(x);
        x.dispose();
    }

    @Override
    public void setLoadDisabled(boolean s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    static DefaultNewLogixManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultNewLogixManager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new DefaultNewLogixManager();
        }
        return (_instance);
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultNewLogixManager.class);

    @Override
    public void registerFemaleSocketFactory(FemaleSocketFactory factory) {
        _femaleSocketFactories.add(factory);
    }

    @Override
    public List<FemaleSocketFactory> getFemaleSocketFactories() {
        return new ArrayList<>(_femaleSocketFactories);
    }
}
