package jmri.jmrit.logixng.implementation;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.logixng.*;

/**
 * Default implementation of the Female Digital Action socket
 */
public class DefaultFemaleDigitalActionSocket
        extends AbstractFemaleSocket
        implements FemaleDigitalActionSocket {


    public DefaultFemaleDigitalActionSocket(
            Base parent, FemaleSocketListener listener, String name) {
        super(parent, listener, name);
    }

    public DefaultFemaleDigitalActionSocket(
            Base parent, FemaleSocketListener listener, String name,
            FemaleSocketConfiguration config) {
        super(parent, listener, name, config);
    }

    @Override
    public boolean isCompatible(MaleSocket socket) {
        return socket instanceof MaleDigitalActionSocket;
    }

    @Override
    public void execute() throws JmriException {
        if (isConnected()) {
            ((MaleDigitalActionSocket)getConnectedSocket()).execute();
        }
    }

    @Override
    public String getShortDescription(Locale locale) {
        return Bundle.getMessage(locale, "DefaultFemaleDigitalActionSocket_Short");
    }

    @Override
    public String getLongDescription(Locale locale) {
        return Bundle.getMessage(locale, "DefaultFemaleDigitalActionSocket_Long", getName());
    }

    @Override
    public Map<Category, List<Class<? extends Base>>> getConnectableClasses() {
        return InstanceManager.getDefault(DigitalActionManager.class).getActionClasses();
    }

    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
        // Do nothing
    }

}
