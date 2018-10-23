package jmri.jmrit.newlogix.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.NewLogixCategory;
import jmri.NewLogixAction;

/**
 * Every NewLogixAction has an InternalAction as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class InternalAction implements NewLogixAction {

    private final NewLogixAction _action;
    private boolean _isActive = false;
    
    public InternalAction(NewLogixAction child) {
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
    public String getShortDescription() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public NewLogixSocket getChild(int index)
            throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getUserName() {
        if (_action != null) {
            return _action.getUserName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        if (_action != null) {
            _action.setUserName(s);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getSystemName() {
        if (_action != null) {
            return _action.getSystemName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getDisplayName() {
        if (_action != null) {
            return _action.getDisplayName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getFullyFormattedDisplayName() {
        if (_action != null) {
            return _action.getFullyFormattedDisplayName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        if (_action != null) {
            _action.addPropertyChangeListener(l, name, listenerRef);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (_action != null) {
            _action.addPropertyChangeListener(l);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (_action != null) {
            _action.removePropertyChangeListener(l);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        if (_action != null) {
            _action.updateListenerRef(l, newName);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (_action != null) {
            _action.vetoableChange(evt);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        if (_action != null) {
            return _action.getListenerRef(l);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        if (_action != null) {
            return _action.getListenerRefs();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public int getNumPropertyChangeListeners() {
        if (_action != null) {
            return _action.getNumPropertyChangeListeners();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        if (_action != null) {
            return _action.getPropertyChangeListenersByReference(name);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void dispose() {
        if (_action != null) {
            _action.dispose();
        }
    }

    @Override
    public void setState(int s) throws JmriException {
        if (_action != null) {
            _action.setState(s);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public int getState() {
        if (_action != null) {
            return _action.getState();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String describeState(int state) {
        if (_action != null) {
            return _action.describeState(state);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getComment() {
        if (_action != null) {
            return _action.getComment();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void setComment(String comment) {
        if (_action != null) {
            _action.setComment(comment);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void setProperty(String key, Object value) {
        if (_action != null) {
            _action.setProperty(key, value);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public Object getProperty(String key) {
        if (_action != null) {
            return _action.getProperty(key);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void removeProperty(String key) {
        if (_action != null) {
            _action.removeProperty(key);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public Set<String> getPropertyKeys() {
        if (_action != null) {
            return _action.getPropertyKeys();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getBeanType() {
        if (_action != null) {
            return _action.getBeanType();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        if (_action != null) {
            return _action.compareSystemNameSuffix(suffix1, suffix2, n2);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getConfiguratorClassName() {
        if (_action != null) {
            return _action.getConfiguratorClassName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

}
