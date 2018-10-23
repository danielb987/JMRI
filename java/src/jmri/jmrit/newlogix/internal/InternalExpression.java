package jmri.jmrit.newlogix.internal;

import jmri.NewLogixCategory;
import jmri.NewLogixExpression;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;

/**
 * Every NewLogixExpression has an InternalExpression as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class InternalExpression implements NewLogixExpression {

    private final NewLogixExpression _expression;
    private boolean lastEvaluationResult = false;
    
    public InternalExpression(NewLogixExpression expression) {
        _expression = expression;
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return _expression.getCategory();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        lastEvaluationResult = _expression.evaluate();
        return lastEvaluationResult;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _expression.reset();
    }

    @Override
    public int getState() {
        return lastEvaluationResult ? NewLogixExpression.TRUE : NewLogixExpression.FALSE;
    }

    @Override
    public NewLogixSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getShortDescription() {
        if (_expression != null) {
            return _expression.getShortDescription();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getLongDescription() {
        if (_expression != null) {
            return _expression.getLongDescription();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getUserName() {
        if (_expression != null) {
            return _expression.getUserName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        if (_expression != null) {
            _expression.setUserName(s);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getSystemName() {
        if (_expression != null) {
            return _expression.getSystemName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getDisplayName() {
        if (_expression != null) {
            return _expression.getDisplayName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getFullyFormattedDisplayName() {
        if (_expression != null) {
            return _expression.getFullyFormattedDisplayName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        if (_expression != null) {
            _expression.addPropertyChangeListener(l, name, listenerRef);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (_expression != null) {
            _expression.addPropertyChangeListener(l);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (_expression != null) {
            _expression.removePropertyChangeListener(l);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        if (_expression != null) {
            _expression.updateListenerRef(l, newName);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (_expression != null) {
            _expression.vetoableChange(evt);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        if (_expression != null) {
            return _expression.getListenerRef(l);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        if (_expression != null) {
            return _expression.getListenerRefs();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public int getNumPropertyChangeListeners() {
        if (_expression != null) {
            return _expression.getNumPropertyChangeListeners();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        if (_expression != null) {
            return _expression.getPropertyChangeListenersByReference(name);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void dispose() {
        if (_expression != null) {
            _expression.dispose();
        }
    }

    @Override
    public void setState(int s) throws JmriException {
        if (_expression != null) {
            _expression.setState(s);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String describeState(int state) {
        if (_expression != null) {
            return _expression.describeState(state);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getComment() {
        if (_expression != null) {
            return _expression.getComment();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void setComment(String comment) {
        if (_expression != null) {
            _expression.setComment(comment);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void setProperty(String key, Object value) {
        if (_expression != null) {
            _expression.setProperty(key, value);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public Object getProperty(String key) {
        if (_expression != null) {
            return _expression.getProperty(key);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public void removeProperty(String key) {
        if (_expression != null) {
            _expression.removeProperty(key);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public Set<String> getPropertyKeys() {
        if (_expression != null) {
            return _expression.getPropertyKeys();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getBeanType() {
        if (_expression != null) {
            return _expression.getBeanType();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        if (_expression != null) {
            return _expression.compareSystemNameSuffix(suffix1, suffix2, n2);
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public String getConfiguratorClassName() {
        if (_expression != null) {
            return _expression.getConfiguratorClassName();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

}
