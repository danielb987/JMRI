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
import jmri.jmrit.logixng.FemaleAnalogActionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleAnalogActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 *
 */
public final class DefaultFemaleAnalogActionSocket
        extends AbstractFemaleSocket
        implements FemaleAnalogActionSocket {

    public DefaultFemaleAnalogActionSocket(Base parent, FemaleSocketListener listener, String name) {
        super(parent, listener, name);
    }
    
    public DefaultFemaleAnalogActionSocket(
            Base parent,
            FemaleSocketListener listener,
            String name,
            MaleAnalogActionSocket maleSocket) {
        
        super(parent, listener, name);
        
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
        return socket instanceof MaleAnalogActionSocket;
    }
    
    @Override
    public void setValue(float value) {
        if (isConnected()) {
            ((MaleAnalogActionSocket)getConnectedSocket()).setValue(value);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("DefaultFemaleAnalogActionSocket_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DefaultFemaleAnalogActionSocket_Long", getName());
    }

    /** {@inheritDoc} */
    @Override
    public String getExampleSystemName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
