package jmri.jmrit.newlogix.actions;

import java.util.List;
import java.util.ArrayList;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.Action;
import jmri.jmrit.newlogix.FemaleSocket;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionMany extends AbstractAction {

    private final List<Action> actions = new ArrayList<>();
    
    public ActionMany(String sys) throws BadUserNameException, BadSystemNameException {
        super(sys);
    }

    public ActionMany(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
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
        for (Action action : actions) {
            state |= action.executeStart();
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        boolean state = false;
        for (Action action : actions) {
            state |= action.executeRestart();
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        boolean state = false;
        for (Action action : actions) {
            state |= action.executeContinue();
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        for (Action action : actions) {
            action.abort();
        }
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
