package jmri.jmrit.newlogix.engine;

import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleSocket;

/**
 * Abstract female socket.
 */
public abstract class AbstractFemaleSocket implements FemaleSocket {
    
    private final FemaleSocketListener _listener;
    private MaleSocket _socket = null;
    private String _name = null;
    
    public AbstractFemaleSocket(FemaleSocketListener listener) {
        _listener = listener;
    }
    
    @Override
    public void connect(MaleSocket socket) {
        if (!isCompatible(socket)) {
            throw new UnsupportedOperationException("Socket is not compatible");
        }
        
        if (_socket != null) {
            disconnect();
        }
        
        _socket = socket;
        _listener.connected(this);
    }

    @Override
    public void disconnect() {
        _socket = null;
        _listener.disconnected(this);
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

}
