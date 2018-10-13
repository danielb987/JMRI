package jmri.managers;

import jmri.InstanceManager;
import jmri.InstanceManagerAutoDefault;
import jmri.Logix;
import jmri.NewLogix;
import jmri.NewLogixManager;

/**
 * Class providing the basic logic of the NewLogixManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultNewLogixManager extends AbstractManager<NewLogix>
        implements NewLogixManager, InstanceManagerAutoDefault {

    public DefaultNewLogixManager() {
        super();
        InstanceManager.getDefault(NewLogixManager.class).addVetoableChangeListener(this);
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
        if (systemName.matches("IQ\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public NewLogix createNewNewLogix(String systemName, String userName) {
        apps.gui3.TabbedPreferencesAction a;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NewLogix createNewNewLogix(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NewLogix getNewLogix(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NewLogix getByUserName(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NewLogix getBySystemName(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void activateAllNewLogixs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteNewLogix(NewLogix x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLoadDisabled(boolean s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
