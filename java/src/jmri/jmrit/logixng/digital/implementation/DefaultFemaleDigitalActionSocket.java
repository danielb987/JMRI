package jmri.jmrit.logixng.digital.implementation;

import java.util.List;
import java.util.Map;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;
import jmri.jmrit.logixng.implementation.AbstractFemaleSocket;

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
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        // Female sockets have special handling
        throw new UnsupportedOperationException();
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
    public String getShortDescription() {
        return Bundle.getMessage("DefaultFemaleDigitalActionSocket_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DefaultFemaleDigitalActionSocket_Long", getName());
    }

    /** {@inheritDoc} */
    @Override
    public String getExampleSystemName() {
        return getConditionalNG().getSystemName() + ":DA10";
    }

    /** {@inheritDoc} */
    @Override
    public String getNewSystemName() {
        return InstanceManager.getDefault(DigitalActionManager.class)
                .getNewSystemName(getConditionalNG());
    }

    @Override
    public Map<Category, List<Class<? extends Base>>> getConnectableClasses() {
        return InstanceManager.getDefault(DigitalActionManager.class).getActionClasses();
    }

}
