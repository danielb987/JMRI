package jmri.jmrit.logixng.digital.actions;

import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Many extends AbstractDigitalAction implements FemaleSocketListener {

    private final List<ActionEntry> actionEntries = new ArrayList<>();
    
    /**
     * Create a new instance of ActionMany and generate a new system name.
     */
    public Many(LogixNG logixNG) {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(logixNG));
        init();
    }

    public Many(String sys) throws BadSystemNameException {
        super(sys);
        init();
    }

    public Many(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        init();
    }

    public Many(String sys, List<String> actionSystemNames) throws BadSystemNameException {
        super(sys);
        setActionSystemNames(actionSystemNames);
    }

    public Many(String sys, String user, List<String> actionSystemNames)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        setActionSystemNames(actionSystemNames);
    }
    
    private void init() {
        actionEntries
                .add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class)
                        .createFemaleActionSocket(this, this, getNewSocketName())));
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.COMMON;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        boolean state = false;
        for (ActionEntry actionEntry : actionEntries) {
            actionEntry._status = actionEntry._socket.executeStart();
            state |= actionEntry._status;
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        boolean state = false;
        for (ActionEntry actionEntry : actionEntries) {
            actionEntry._status = actionEntry._socket.executeRestart();
            state |= actionEntry._status;
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        boolean state = false;
        for (ActionEntry actionEntry : actionEntries) {
            if (actionEntry._status) {
                actionEntry._status = actionEntry._socket.executeContinue();
                state |= actionEntry._status;
            }
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        for (ActionEntry actionEntry : actionEntries) {
            actionEntry._socket.abort();
            actionEntry._status = false;
        }
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return actionEntries.get(index)._socket;
    }

    @Override
    public int getChildCount() {
        return actionEntries.size();
    }
    
    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (ActionEntry actionEntry : actionEntries) {
            hasFreeSocket = !actionEntry._socket.isConnected();
            if (hasFreeSocket) {
                break;
            }
        }
        if (!hasFreeSocket) {
            actionEntries.add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class).createFemaleActionSocket(this, this, getNewSocketName())));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("Many_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("Many_Long");
    }

    private void setActionSystemNames(List<String> systemNames) {
        if (!actionEntries.isEmpty()) {
            throw new RuntimeException("action system names cannot be set more than once");
        }
        
        for (String systemName : systemNames) {
            FemaleDigitalActionSocket socket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .createFemaleActionSocket(this, this, getNewSocketName());
            
            actionEntries.add(new ActionEntry(socket, systemName));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        for (ActionEntry ae : actionEntries) {
            try {
                ae._socket.connect(InstanceManager.getDefault(DigitalActionManager.class).getBeanBySystemName(ae._socketSystemName));
            } catch (SocketAlreadyConnectedException ex) {
                // This shouldn't happen and is a runtime error if it does.
                throw new RuntimeException("socket is already connected");
            }
        }
    }


//    /* This class is public since ActionManyXml needs to access it. */
    private static class ActionEntry {
        private String _socketSystemName;
        private final FemaleDigitalActionSocket _socket;
        private boolean _status;
        
        private ActionEntry(FemaleDigitalActionSocket socket, String socketSystemName) {
            _socketSystemName = socketSystemName;
            _socket = socket;
        }
        
        private ActionEntry(FemaleDigitalActionSocket socket) {
            this._socket = socket;
        }
        
//        public DigitalAction getAction() {
//            return (MaleDigitalActionSocket) socket.getConnectedSocket();
//        }
        
    }

}
