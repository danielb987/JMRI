package jmri.jmrit.newlogix.engine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.MaleSocket;

/**
 *
 */
public class DefaultFemaleExpressionSocket extends AbstractFemaleSocket
        implements FemaleExpressionSocket {

    private Expression _expression;
    
    public DefaultFemaleExpressionSocket(FemaleSocketListener listener, String name) {
        super(listener, name);
    }
    
    @Override
    public boolean isCompatible(MaleSocket socket) {
        return socket instanceof MaleExpressionSocket;
    }
    
    @Override
    public Category getCategory() {
        if (_expression != null) {
            return _expression.getCategory();
        } else {
            return null;
        }
    }

    @Override
    public boolean isExternal() {
        if (_expression != null) {
            return _expression.isExternal();
        } else {
            return false;
        }
    }

    @Override
    public boolean evaluate() {
        if (_expression != null) {
            return _expression.evaluate();
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        if (_expression != null) {
            _expression.reset();
        }
    }

    @Override
    public FemaleSocket getChild(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported.");
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
    public int getState() {
        if (_expression != null) {
            return _expression.getState();
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
