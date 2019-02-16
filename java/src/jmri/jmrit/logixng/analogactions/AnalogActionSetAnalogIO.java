package jmri.jmrit.logixng.analogactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.AnalogIO;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.logixng.AnalogActionManager;
import jmri.jmrit.logixng.AnalogExpressionManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Sets an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogActionSetAnalogIO extends AbstractAnalogAction {

    private AnalogIO _analogIO;
    
    public AnalogActionSetAnalogIO(String sys) {
        super(sys);
    }
    
    public AnalogActionSetAnalogIO(String sys, String user) {
        super(sys, user);
    }
    
    public AnalogActionSetAnalogIO(
            String sys,
            AnalogIO analogIO) {
        
        super(sys);
        _analogIO = analogIO;
    }
    
    public AnalogActionSetAnalogIO(
            String sys, String user,
            AnalogIO analogIO) {
        
        super(sys, user);
        _analogIO = analogIO;
    }
    
    @Override
    public void setValue(float value) {
        if (_analogIO != null) {
            try {
                _analogIO.setCommandedAnalogValue(value);
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

    private final static Logger log = LoggerFactory.getLogger(AnalogActionSetAnalogIO.class);

}
