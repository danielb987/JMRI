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
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;

/**
 *
 */
public final class DefaultFemaleDigitalActionSocket
        extends AbstractFemaleSocket
        implements FemaleDigitalActionSocket {

    public DefaultFemaleDigitalActionSocket(Base parent, FemaleSocketListener listener, String name) {
        super(parent, listener, name);
    }
    
    public DefaultFemaleDigitalActionSocket(
            Base parent,
            FemaleSocketListener listener,
            String name,
            MaleDigitalActionSocket maleSocket) {
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
    public void dispose() {
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("DefaultFemaleActionSocket_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DefaultFemaleActionSocket_Long", getName());
    }

    /** {@inheritDoc} */
    @Override
    public String getExampleSystemName() {
        return getLogixNG().getSystemName() + ":DA10";
    }

}
