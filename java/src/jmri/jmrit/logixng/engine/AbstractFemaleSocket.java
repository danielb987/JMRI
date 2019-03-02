package jmri.jmrit.logixng.engine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Abstract female socket.
 */
public abstract class AbstractFemaleSocket implements FemaleSocket, NamedBean{
    
    // The reason AbstractFemaleSocket implements NamedBean is that it's
    // implementation classes implements NamedBean. And since there is a lot
    // of methods in NamedBean that isn't useful for female sockets, it's
    // better to let AbstractFemaleSocket implement these.
    
    private Base _parent;
    private final FemaleSocketListener _listener;
    private MaleSocket _socket = null;
    private String _name = null;
    private Lock _lock = Lock.NONE;
    
    
    public AbstractFemaleSocket(Base parent, FemaleSocketListener listener, String name) {
        _listener = listener;
        _name = name;
        _parent = parent;
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getParent() {
        return _parent;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setParent(Base parent) {
        _parent = parent;
    }
    
    /** {@inheritDoc} */
    @Override
    public Lock getLock() {
        return _lock;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setLock(Lock lock) {
        _lock = lock;
    }
    
    /** {@inheritDoc} */
    @Override
    public void connect(MaleSocket socket) throws SocketAlreadyConnectedException {
        if (_socket != null) {
            throw new SocketAlreadyConnectedException("Socket is already connected");
        }
        
        if (!isCompatible(socket)) {
            throw new UnsupportedOperationException("Socket is not compatible");
        }
        
        _socket = socket;
        _listener.connected(this);
    }

    /** {@inheritDoc} */
    @Override
    public void disconnect() {
        _socket = null;
        _listener.disconnected(this);
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket getConnectedSocket() {
        return _socket;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isConnected() {
        return _socket != null;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setName(String name) {
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Category getCategory() {
        if (_socket != null) {
            return _socket.getCategory();
        } else {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    @Override
    public boolean isExternal() {
        if (_socket != null) {
            return _socket.isExternal();
        } else {
            throw new UnsupportedOperationException("Not supported.");
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
    public void setUserName(String s) throws NamedBean.BadUserNameException {
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

}
