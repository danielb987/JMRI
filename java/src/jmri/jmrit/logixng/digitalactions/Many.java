package jmri.jmrit.logixng.digitalactions;

import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;

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
    public Many(Base parent) throws BadUserNameException, BadSystemNameException {
        super(parent, InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(parent.getLogixNG()));
        init();
    }

    public Many(Base parent, String sys) throws BadUserNameException, BadSystemNameException {
        super(parent, sys);
        init();
    }

    public Many(Base parent, String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(parent, sys, user);
        init();
    }
    
    private void init() {
        actionEntries.add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class).createFemaleActionSocket(this, this, getNewSocketName())));
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
            actionEntry.status = actionEntry.socket.executeStart();
            state |= actionEntry.status;
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        boolean state = false;
        for (ActionEntry actionEntry : actionEntries) {
            actionEntry.status = actionEntry.socket.executeRestart();
            state |= actionEntry.status;
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        boolean state = false;
        for (ActionEntry actionEntry : actionEntries) {
            if (actionEntry.status) {
                actionEntry.status = actionEntry.socket.executeContinue();
                state |= actionEntry.status;
            }
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        for (ActionEntry actionEntry : actionEntries) {
            actionEntry.socket.abort();
            actionEntry.status = false;
        }
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return actionEntries.get(index).socket;
    }

    @Override
    public int getChildCount() {
        return actionEntries.size();
    }
    
    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (ActionEntry actionEntry : actionEntries) {
            hasFreeSocket = !actionEntry.socket.isConnected();
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


//    /* This class is public since ActionManyXml needs to access it. */
    private static class ActionEntry {
        private final FemaleDigitalActionSocket socket;
        private boolean status;
        
        private ActionEntry(FemaleDigitalActionSocket socket) {
            this.socket = socket;
        }
        
//        public DigitalAction getAction() {
//            return (MaleDigitalActionSocket) socket.getConnectedSocket();
//        }
        
    }

}
