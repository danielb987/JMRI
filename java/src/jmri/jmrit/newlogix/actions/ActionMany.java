package jmri.jmrit.newlogix.actions;

import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.ActionManager;
import jmri.jmrit.newlogix.FemaleActionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.NewLogix;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionMany extends AbstractAction implements FemaleSocketListener {

    private final List<ActionEntry> actionEntries = new ArrayList<>();
    
    /**
     * Create a new instance of ActionMany and generate a new system name.
     * @param newLogix the NewLogix that this action is related to
     */
    public ActionMany(NewLogix newLogix) throws BadUserNameException, BadSystemNameException {
        super(InstanceManager.getDefault(ActionManager.class).getNewSystemName(newLogix));
        init();
    }

    public ActionMany(String sys) throws BadUserNameException, BadSystemNameException {
        super(sys);
        init();
    }

    public ActionMany(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        init();
    }
    
    private void init() {
        actionEntries.add(new ActionEntry(InstanceManager.getDefault(ActionManager.class).createFemaleActionSocket(this, getNewSocketName())));
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
            actionEntries.add(new ActionEntry(InstanceManager.getDefault(ActionManager.class).createFemaleActionSocket(this, getNewSocketName())));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private static class ActionEntry {
        private final FemaleActionSocket socket;
        private boolean status;
        
        private ActionEntry(FemaleActionSocket socket) {
            this.socket = socket;
        }
        
    }
    
}
