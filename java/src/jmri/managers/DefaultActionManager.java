package jmri.managers;

import jmri.Action;
import jmri.ActionManager;
import jmri.InstanceManager;
import jmri.InstanceManagerAutoDefault;

/**
 * Class providing the basic logic of the ActionManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultActionManager extends AbstractManager<Action>
        implements ActionManager, InstanceManagerAutoDefault {

    public DefaultActionManager() {
        super();
        InstanceManager.getDefault(DefaultActionManager.class).addVetoableChangeListener(this);
//        InstanceManager.getDefault(DefaultNewLogixManager.class).addVetoableChangeListener(this);
    }

    @Override
    public int getXMLOrder() {
        return NEWLOGIXS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameAction");
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
        if (systemName.matches("IQ\\d+\\:A\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public Action createNewAction(String systemName, String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action createNewExpression(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action getExpression(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action getByUserName(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action getBySystemName(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAction(Action x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
