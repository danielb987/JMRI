package jmri.jmrit.logixng.implementation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import jmri.NamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Abstract female socket.
 * 
 * @author Daniel Bergqvist 2019
 */
public abstract class AbstractFemaleSocket implements FemaleSocket {
    
    private Base _parent;
    protected final FemaleSocketListener _listener;
    private MaleSocket _socket = null;
    private String _name = null;
    private Lock _lock = Lock.NONE;
    
    
    public AbstractFemaleSocket(Base parent, FemaleSocketListener listener, String name) {
        _listener = listener;
//        _name = name;
        _parent = parent;
        setName(name);
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
    public void setParentForAllChildren() {
        if (_socket != null) {
            _socket.setParent(this);
            _socket.setParentForAllChildren();
        }
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
        if (socket == null) {
            throw new NullPointerException("socket cannot be null");
        }
        
        if (_socket != null) {
            throw new SocketAlreadyConnectedException("Socket is already connected");
        }
        
        if (!isCompatible(socket)) {
            throw new UnsupportedOperationException("Socket is not compatible");
        }
        
        _socket = socket;
        _socket.setParent(this);
        _listener.connected(this);
    }

    /** {@inheritDoc} */
    @Override
    public void disconnect() {
        if (_socket == null) {
            return;
        }
        
        _socket.setParent(null);
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
    public final boolean validateName(String name) {
        for (int i=0; i < name.length(); i++) {
            if ((i == 0) && !Character.isLetter(name.charAt(i))) {
                return false;
            } else if (!Character.isLetterOrDigit(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setName(String name) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("the name is not valid: " + name);
        }
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return _name;
    }

    public void disposeMe() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public void dispose() {
        if (_socket != null) {
            MaleSocket aSocket = _socket;
            disconnect();
            aSocket.dispose();
        }
        disposeMe();
    }

    /**
     * Register listeners if this object needs that.
     * <P>
     * Important: This method may be called more than once. Methods overriding
     * this method must ensure that listeners are not registered more than once.
     */
    protected void registerListenersForThisClass() {
        // Do nothing
    }
    
    /**
     * Unregister listeners if this object needs that.
     * <P>
     * Important: This method may be called more than once. Methods overriding
     * this method must ensure that listeners are not unregistered more than once.
     */
    protected void unregisterListenersForThisClass() {
        // Do nothing
    }
    
    /**
     * Register listeners if this object needs that.
     */
    @Override
    public void registerListeners() {
        registerListenersForThisClass();
        if (_socket != null) {
            _socket.registerListeners();
        }
    }
    
    /**
     * Register listeners if this object needs that.
     */
    @Override
    public void unregisterListeners() {
        unregisterListenersForThisClass();
        if (_socket != null) {
            _socket.unregisterListeners();
        }
    }
    
    @Override
    public Category getCategory() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isExternal() {
        throw new UnsupportedOperationException("Not supported.");
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
    
    /** {@inheritDoc} */
    @Override
    public final ConditionalNG getConditionalNG() {
        if (this instanceof ConditionalNG) {
            return (ConditionalNG) this;
        } else {
            Base parent = getParent();
            while (! (parent instanceof ConditionalNG)) {
                parent = parent.getParent();
            }
            return (ConditionalNG) parent;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public final LogixNG getLogixNG() {
        if (this instanceof LogixNG) {
            return (LogixNG) this;
        } else {
            Base parent = getParent();
            while (! (parent instanceof LogixNG)) {
                parent = parent.getParent();
            }
            return (LogixNG) parent;
        }
    }
    
}
