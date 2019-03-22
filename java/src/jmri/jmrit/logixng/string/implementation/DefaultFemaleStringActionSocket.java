package jmri.jmrit.logixng.string.implementation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleStringActionSocket;
import jmri.jmrit.logixng.MaleStringActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.implementation.AbstractFemaleSocket;

/**
 *
 */
public final class DefaultFemaleStringActionSocket
        extends AbstractFemaleSocket
        implements FemaleStringActionSocket {

    public DefaultFemaleStringActionSocket(Base parent, FemaleSocketListener listener, String name) {
        super(parent, listener, name);
    }
    
    public DefaultFemaleStringActionSocket(
            Base parent,
            FemaleSocketListener listener,
            String name,
            MaleStringActionSocket maleSocket) {
        
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
        return socket instanceof MaleStringActionSocket;
    }
    
    @Override
    public void setValue(String value) {
        if (isConnected()) {
            ((MaleStringActionSocket)getConnectedSocket()).setValue(value);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("DefaultFemaleStringActionSocket_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DefaultFemaleStringActionSocket_Long", getName());
    }

    /** {@inheritDoc} */
    @Override
    public String getExampleSystemName() {
        return getLogixNG().getSystemName() + ":SA10";
    }

    /** {@inheritDoc} */
    @Override
    public String getNewSystemName() {
        return InstanceManager.getDefault(StringActionManager.class)
                .getNewSystemName(getLogixNG());
    }

    @Override
    public Map<Category, List<Class<? extends Base>>> getConnectableClasses() {
        return InstanceManager.getDefault(StringActionManager.class).getActionClasses();
    }

}
