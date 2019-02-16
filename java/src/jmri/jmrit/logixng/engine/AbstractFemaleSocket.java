package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Abstract female socket.
 */
public abstract class AbstractFemaleSocket implements FemaleSocket {
    
    private final FemaleSocketListener _listener;
    private MaleSocket _socket = null;
    private String _name = null;
    private Lock _lock = Lock.NONE;
    
    
    public AbstractFemaleSocket(FemaleSocketListener listener, String name) {
        _listener = listener;
        _name = name;
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

}
