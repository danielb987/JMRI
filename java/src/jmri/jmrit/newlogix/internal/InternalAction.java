package jmri.jmrit.newlogix.internal;

import jmri.NewLogixCategory;
import jmri.implementation.AbstractAction;
import jmri.NewLogixAction;

/**
 * Every NewLogixAction has an InternalAction as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class InternalAction extends AbstractAction {

    private final NewLogixAction _action;
    private boolean _isActive = false;
    
    public InternalAction(String sys, NewLogixAction child) throws BadSystemNameException {
        super(sys);
        _action = child;
    }
    
    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return _action.getCategory();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        if (_isActive) {
            throw new RuntimeException("executeStart() must not be called on an active action");
        }
        _isActive = _action.executeStart();
        return _isActive;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        if (!_isActive) {
            return false;
        }
        _isActive = _action.executeContinue();
        return _isActive;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        if (_isActive) {
            _isActive = _action.executeRestart();
        } else {
            _isActive = _action.executeStart();
        }
        return _isActive;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        _action.abort();
    }

    @Override
    public NewLogixSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
