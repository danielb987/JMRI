package jmri.jmrit.newlogix.analogactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import jmri.AnalogIO;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.newlogix.AnalogActionManager;
import jmri.jmrit.newlogix.AnalogExpressionManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.AnalogIO;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Category getCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isExternal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private final static Logger log = LoggerFactory.getLogger(AnalogActionSetAnalogIO.class);

}
