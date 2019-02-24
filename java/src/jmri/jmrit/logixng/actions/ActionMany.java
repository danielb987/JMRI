package jmri.jmrit.logixng.actions;

import java.util.ArrayList;
import java.util.List;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ActionManager;
import jmri.jmrit.logixng.FemaleActionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleActionSocket;
import jmri.jmrit.logixng.LogixNG;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionMany extends AbstractAction implements FemaleSocketListener {

    private final List<ActionEntry> actionEntries = new ArrayList<>();
    
    /**
     * Create a new instance of ActionMany and generate a new system name.
     * @param newLogix the LogixNG that this action is related to
     */
    public ActionMany(LogixNG newLogix) throws BadUserNameException, BadSystemNameException {
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
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("ActionMany_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("ActionMany_Long");
//        return Bundle.getMessage("ActionMany_Long", _analogExpressionSocket.getName(), _analogActionSocket.getName());
    }


//    /* This class is public since ActionManyXml needs to access it. */
    private static class ActionEntry {
        private final FemaleActionSocket socket;
        private boolean status;
        
        private ActionEntry(FemaleActionSocket socket) {
            this.socket = socket;
        }
        
//        public Action getAction() {
//            return (MaleActionSocket) socket.getConnectedSocket();
//        }
        
    }

}
