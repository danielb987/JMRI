package jmri.jmrit.logixng.analog.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.AnalogIO;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Sets an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class SetAnalogIO extends AbstractAnalogAction {

    private SetAnalogIO _template;
    private String _analogIO_SystemName;
    private AnalogIO _analogIO;
    
    public SetAnalogIO(String sys) {
        super(sys);
    }
    
    public SetAnalogIO(String sys, String user) {
        super(sys, user);
    }
    
    public SetAnalogIO(String sys, AnalogIO analogIO) {
        
        super(sys);
        _analogIO = analogIO;
    }
    
    public SetAnalogIO(String sys, String user, AnalogIO analogIO) {
        
        super(sys, user);
        _analogIO = analogIO;
    }
    
    private SetAnalogIO(SetAnalogIO template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new SetAnalogIO(this, sys);
    }
    
    @Override
    public void setValue(double value) {
        if (_analogIO != null) {
            try {
                _analogIO.setCommandedAnalogValue((float)value);
            } catch (JmriException ex) {
                log.warn("AnalogActionSetAnalogIO", ex);
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
        if (_analogIO != null) {
            return Bundle.getMessage("AnalogActionSetAnalogIO", _analogIO.getDisplayName());
        } else {
            return Bundle.getMessage("AnalogActionSetAnalogIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setAnalogIO_SystemName(String analogIO_SystemName) {
        _analogIO_SystemName = analogIO_SystemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_analogIO == null) && (_analogIO_SystemName != null)) {
            System.out.format("Setup: %s%n", _analogIO_SystemName);     // Temporary until the AnalogIOManager is implemented
//            _analogIO = InstanceManager.getDefault(AnalogIOManager.class).getBeanBySystemName(_analogIO_SystemName);
        }
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(SetAnalogIO.class);

}
