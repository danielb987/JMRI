package jmri.jmrit.newlogix.engine;

import java.text.DecimalFormat;
import jmri.jmrit.newlogix.ActionManager;
import jmri.jmrit.newlogix.ExpressionManager;
import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.NewLogixManager;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.SocketListener;
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
    
    
    public DefaultNewLogixManager() {
        super();
        
        // For testing only.
        InstanceManager.getDefault(ExpressionManager.class);
        
        // For testing only.
        InstanceManager.getDefault(ActionManager.class);
        
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
        if (systemName.toUpperCase().matches("IQ\\:[AM]\\:\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public NewLogix createNewNewLogix(String systemName, String userName) {
        // Check that Logix does not already exist
        NewLogix x;
        if (userName != null && !userName.equals("")) {
            x = getByUserName(userName);
            if (x != null) {
                return null;
            }
        }
        x = getBySystemName(systemName);
        if (x != null) {
            return null;
        }
        // Check if system name is valid
        if (this.validSystemNameFormat(systemName) != NameValidity.VALID) {
            log.warn("SystemName " + systemName + " is not in the correct format");
            return null;
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
        return x;
    }

    @Override
    public NewLogix createNewNewLogix(String userName) {
        int nextAutoNewLogixRef = lastAutoNewLogixRef + 1;
        StringBuilder b = new StringBuilder("IQ:A:");
        String nextNumber = paddedNumber.format(nextAutoNewLogixRef);
        b.append(nextNumber);
        return createNewNewLogix(b.toString(), userName);
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

    @Override
    public FemaleSocket createFemaleActionSocket(SocketListener listener) {
        return null;
    }

    @Override
    public FemaleSocket createFemaleExpressionSocket(SocketListener listener) {
        return null;
    }
    
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
}
