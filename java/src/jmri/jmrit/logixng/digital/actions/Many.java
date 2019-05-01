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
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Many extends AbstractDigitalAction implements FemaleSocketListener {

    private Many _template;
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
    
    private void init() {
        actionEntries
                .add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class)
                        .createFemaleSocket(this, this, getNewSocketName())));
    }
    
    private Many(Many template, String sys) {
        super(sys);
        _template = template;
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new Many(this, sys);
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
            actionEntries.add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class).createFemaleSocket(this, this, getNewSocketName())));
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

    private final static Logger log = LoggerFactory.getLogger(Many.class);

}
