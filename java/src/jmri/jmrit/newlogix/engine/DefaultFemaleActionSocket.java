package jmri.jmrit.newlogix.engine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.newlogix.Action;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.FemaleActionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleActionSocket;
import jmri.jmrit.newlogix.MaleSocket;

/**
 *
 */
public class DefaultFemaleActionSocket
        extends AbstractFemaleSocket
        implements FemaleActionSocket {

    private Action _action;
    
    public DefaultFemaleActionSocket(FemaleSocketListener listener, String name) {
        super(listener, name);
    }
    
    @Override
    public boolean isCompatible(MaleSocket socket) {
        return socket instanceof MaleActionSocket;
    }
    
    @Override
    public Category getCategory() {
        if (_action != null) {
            return _action.getCategory();
        } else {
            return null;
        }
    }

    @Override
    public boolean isExternal() {
        if (_action != null) {
            return _action.isExternal();
        } else {
            return false;
        }
    }

    @Override
    public boolean executeStart() {
        if (_action != null) {
            return _action.executeStart();
        } else {
            return false;
        }
    }

    @Override
    public boolean executeContinue() {
        if (_action != null) {
            return _action.executeContinue();
        } else {
            return false;
        }
    }

    @Override
    public boolean executeRestart() {
        if (_action != null) {
            return _action.executeRestart();
        } else {
            return false;
        }
    }

    @Override
    public void abort() {
        if (_action != null) {
            _action.abort();
        }
    }

    @Override
    public String getShortDescription() {
        if (_action != null) {
            return _action.getShortDescription();
        } else {
            return null;
        }
    }

    @Override
    public String getLongDescription() {
        if (_action != null) {
            return _action.getLongDescription();
        } else {
            return null;
        }
    }

    @Override
    public FemaleSocket getChild(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
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
