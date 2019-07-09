package jmri.jmrit.logixng.digital.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Many extends AbstractDigitalAction implements FemaleSocketListener {

    private Many _template;
    private boolean _enableExecution;
    private final List<ActionEntry> actionEntries = new ArrayList<>();
    
    /**
     * Create a new instance of ActionMany and generate a new system name.
     */
    public Many(ConditionalNG conditionalNG) {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(conditionalNG));
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

    public Many(String sys, List<Map.Entry<String, String>> actionSystemNames) throws BadSystemNameException {
        super(sys);
        setActionSystemNames(actionSystemNames);
    }

    public Many(String sys, String user, List<Map.Entry<String, String>> actionSystemNames)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        setActionSystemNames(actionSystemNames);
    }
    
    private Many(Many template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    private void init() {
        actionEntries
                .add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class)
                        .createFemaleSocket(this, this, getNewSocketName())));
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new Many(this, sys);
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsEnableExecution() {
        
        // This action does not support EnableExecution if the user may add or
        // remove child actions.
        if (getLock().isChangeableByUser()) {
            return false;
        }
        
        // This action supports EnableExecution if all the children supports it.
        boolean support = true;
        for (ActionEntry actionEntry : actionEntries) {
            if (actionEntry._socket.isConnected()) {
                support &= actionEntry._socket.supportsEnableExecution();
            }
        }
        return support;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setEnableExecution(boolean b) {
        if (supportsEnableExecution()) {
            _enableExecution = b;
        } else {
            log.error("This digital action does not supports the method setEnableExecution()");
            throw new UnsupportedOperationException("This digital action does not supports the method setEnableExecution()");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExecutionEnabled() {
        if (supportsEnableExecution()) {
            return _enableExecution;
        } else {
            log.error("This digital action does not supports the method setEnableExecution()");
            throw new UnsupportedOperationException("This digital action does not supports the method isExecutionEnabled()");
        }
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
            actionEntries.add(
                    new ActionEntry(
                            InstanceManager.getDefault(DigitalActionManager.class)
                                    .createFemaleSocket(this, this, getNewSocketName())));
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

    private void setActionSystemNames(List<Map.Entry<String, String>> systemNames) {
        if (!actionEntries.isEmpty()) {
            throw new RuntimeException("action system names cannot be set more than once");
        }
        
        for (Map.Entry<String, String> entry : systemNames) {
//            System.out.format("Many: systemName: %s%n", entry);
            System.err.format("AAA Many: socketName: %s, systemName: %s%n", entry.getKey(), entry.getValue());
            FemaleDigitalActionSocket socket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .createFemaleSocket(this, this, entry.getKey());
            
            actionEntries.add(new ActionEntry(socket, entry.getValue()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        System.err.format("AAAA setup()%n");
        for (ActionEntry ae : actionEntries) {
            if (ae._socketSystemName != null) {
                System.err.format("AA SocketName: %s, SystemName: %s%n", ae._socket.getName(), ae._socketSystemName);
                try {
                    MaleSocket maleSocket = InstanceManager.getDefault(DigitalActionManager.class).getBeanBySystemName(ae._socketSystemName);
                    if (maleSocket != null) {
                        ae._socket.connect(maleSocket);
                        maleSocket.setup();
                    } else {
                        log.error("cannot load digital action " + ae._socketSystemName);
                    }
                } catch (SocketAlreadyConnectedException ex) {
                    // This shouldn't happen and is a runtime error if it does.
                    throw new RuntimeException("socket is already connected");
                }
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

    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(Many.class);

}
