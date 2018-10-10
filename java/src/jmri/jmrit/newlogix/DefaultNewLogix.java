package jmri.jmrit.newlogix;

import jmri.NewLogix;
import jmri.Action;

/**
 * The default implementation of NewLogix.
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultNewLogix implements NewLogix {
    
    private Action _action;
    private boolean _isActive = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        if (_isActive) {
            _isActive = _action.executeStart();
        } else {
            _isActive = _action.executeContinue();
        }
    }
    
}
