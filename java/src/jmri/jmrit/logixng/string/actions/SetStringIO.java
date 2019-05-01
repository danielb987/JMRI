package jmri.jmrit.logixng.string.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.InstanceManager;
import jmri.StringIO;
import jmri.JmriException;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;

/**
 * Sets an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class SetStringIO extends AbstractStringAction {

    private SetStringIO _template;
    private String _stringIO_SystemName;
    private StringIO _stringIO;
    
    public SetStringIO(String sys) {
        super(sys);
    }
    
    public SetStringIO(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    public SetStringIO(String sys, StringIO stringIO)
            throws BadSystemNameException {
        
        super(sys);
        _stringIO = stringIO;
    }
    
    public SetStringIO(String sys, String user, StringIO stringIO)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        _stringIO = stringIO;
    }
    
    private SetStringIO(SetStringIO template, String sys) {
        super(sys);
        _template = template;
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new SetStringIO(this, sys);
    }
    
    @Override
    public void setValue(String value) {
        if (_stringIO != null) {
            try {
                _stringIO.setCommandedStringValue(value);
            } catch (JmriException ex) {
                log.warn("StringActionSetStringIO", ex);
            }
        }
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    public String getShortDescription() {
        if (_stringIO != null) {
            return Bundle.getMessage("StringActionSetStringIO", _stringIO.getDisplayName());
        } else {
            return Bundle.getMessage("StringActionSetStringIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setStringIO_SystemName(String stringIO_SystemName) {
        _stringIO_SystemName = stringIO_SystemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_stringIO == null) && (_stringIO_SystemName != null)) {
            System.out.format("Setup: %s%n", _stringIO_SystemName);     // Temporary until the StringIOManager is implemented
//            _stringIO = InstanceManager.getDefault(StringIOManager.class).getBeanBySystemName(_stringIO_SystemName);
        }
    }

    private final static Logger log = LoggerFactory.getLogger(SetStringIO.class);

}
