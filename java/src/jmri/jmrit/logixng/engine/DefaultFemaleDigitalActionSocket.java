package jmri.jmrit.logixng.engine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;

/**
 *
 */
public final class DefaultFemaleDigitalActionSocket
        extends AbstractFemaleSocket
        implements FemaleDigitalActionSocket {

    public DefaultFemaleDigitalActionSocket(FemaleSocketListener listener, String name) {
        super(listener, name);
    }
    
    public DefaultFemaleDigitalActionSocket(
            FemaleSocketListener listener,
            String name,
            MaleDigitalActionSocket maleSocket) {
        super(listener, name);
        
        try {
            connect(maleSocket);
        } catch (SocketAlreadyConnectedException e) {
            // This should never be able to happen since a newly created
            // socket is not connected.
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean isCompatible(MaleSocket socket) {
        return socket instanceof MaleDigitalActionSocket;
    }
    
    @Override
    public boolean executeStart() {
        if (isConnected()) {
            return ((MaleDigitalActionSocket)getConnectedSocket()).executeStart();
        } else {
            return false;
        }
    }

    @Override
    public boolean executeContinue() {
        if (isConnected()) {
            return ((MaleDigitalActionSocket)getConnectedSocket()).executeContinue();
        } else {
            return false;
        }
    }

    @Override
    public boolean executeRestart() {
        if (isConnected()) {
            return ((MaleDigitalActionSocket)getConnectedSocket()).executeRestart();
        } else {
            return false;
        }
    }

    @Override
    public void abort() {
        if (isConnected()) {
            ((MaleDigitalActionSocket)getConnectedSocket()).abort();
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
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getSystemName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getFullyFormattedDisplayName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumPropertyChangeListeners() {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        // Implement this!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setState(int s) throws JmriException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getState() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String describeState(int state) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getComment() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setComment(String comment) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setProperty(String key, Object value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Object getProperty(String key) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void removeProperty(String key) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Set<String> getPropertyKeys() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getBeanType() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getConfiguratorClassName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("DefaultFemaleActionSocket_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DefaultFemaleActionSocket_Long", getName());
    }

}
