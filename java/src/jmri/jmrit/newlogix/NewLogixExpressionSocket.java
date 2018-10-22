package jmri.jmrit.newlogix;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.NewLogixCategory;
import jmri.NewLogixExpression;
import jmri.NewLogixCommon.NewLogixSocket;

/**
 *
 */
public class NewLogixExpressionSocket implements NewLogixExpression, NewLogixSocket {

    private NewLogixExpression _expression;

    @Override
    public NewLogixCategory getCategory() {
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
    public NewLogixSocket getChild(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getShortDescription() {
        if (_expression != null) {
            return _expression.getShortDescription();
        } else {
            return null;
        }
    }

    @Override
    public String getLongDescription() {
        if (_expression != null) {
            return _expression.getLongDescription();
        } else {
            return null;
        }
    }

    @Override
    public String getUserName() {
        if (_expression != null) {
            return _expression.getUserName();
        } else {
            return null;
        }
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        if (_expression != null) {
            _expression.setUserName(s);
        }
    }

    @Override
    public String getSystemName() {
        if (_expression != null) {
            return _expression.getSystemName();
        } else {
            return null;
        }
    }

    @Override
    public String getDisplayName() {
        if (_expression != null) {
            return _expression.getDisplayName();
        } else {
            return null;
        }
    }

    @Override
    public String getFullyFormattedDisplayName() {
        if (_expression != null) {
            return _expression.getFullyFormattedDisplayName();
        } else {
            return null;
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        if (_expression != null) {
            _expression.addPropertyChangeListener(l, name, listenerRef);
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (_expression != null) {
            _expression.addPropertyChangeListener(l);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (_expression != null) {
            _expression.removePropertyChangeListener(l);
        }
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        if (_expression != null) {
            _expression.updateListenerRef(l, newName);
        }
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (_expression != null) {
            _expression.vetoableChange(evt);
        }
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        if (_expression != null) {
            return _expression.getListenerRef(l);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        if (_expression != null) {
            return _expression.getListenerRefs();
        } else {
            return null;
        }
    }

    @Override
    public int getNumPropertyChangeListeners() {
        if (_expression != null) {
            return _expression.getNumPropertyChangeListeners();
        } else {
            return 0;
        }
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        if (_expression != null) {
            return _expression.getPropertyChangeListenersByReference(name);
        } else {
            return null;
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
        }
    }

    @Override
    public int getState() {
        if (_expression != null) {
            return _expression.getState();
        } else {
            return NamedBean.UNKNOWN;
        }
    }

    @Override
    public String describeState(int state) {
        if (_expression != null) {
            return _expression.describeState(state);
        } else {
            return null;
        }
    }

    @Override
    public String getComment() {
        if (_expression != null) {
            return _expression.getComment();
        } else {
            return null;
        }
    }

    @Override
    public void setComment(String comment) {
        if (_expression != null) {
            _expression.setComment(comment);
        }
    }

    @Override
    public void setProperty(String key, Object value) {
        if (_expression != null) {
            _expression.setProperty(key, value);
        }
    }

    @Override
    public Object getProperty(String key) {
        if (_expression != null) {
            return _expression.getProperty(key);
        } else {
            return null;
        }
    }

    @Override
    public void removeProperty(String key) {
        if (_expression != null) {
            _expression.removeProperty(key);
        }
    }

    @Override
    public Set<String> getPropertyKeys() {
        if (_expression != null) {
            return _expression.getPropertyKeys();
        } else {
            return null;
        }
    }

    @Override
    public String getBeanType() {
        if (_expression != null) {
            return _expression.getBeanType();
        } else {
            return null;
        }
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        if (_expression != null) {
            return _expression.compareSystemNameSuffix(suffix1, suffix2, n2);
        } else {
            return 0;
        }
    }

    @Override
    public String getConfiguratorClassName() {
        if (_expression != null) {
            return _expression.getConfiguratorClassName();
        } else {
            return null;
        }
    }

}
