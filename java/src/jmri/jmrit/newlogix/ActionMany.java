package jmri.jmrit.newlogix;

import java.util.List;
import java.util.ArrayList;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractAction;
import jmri.NewLogixAction;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionMany extends AbstractAction {

    private final List<NewLogixAction> actions = new ArrayList<>();
    
    public ActionMany(String sys) throws BadUserNameException, BadSystemNameException {
        super(sys);
    }

    public ActionMany(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.COMMON;
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
        for (NewLogixAction action : actions) {
            state |= action.executeStart();
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        boolean state = false;
        for (NewLogixAction action : actions) {
            state |= action.executeRestart();
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        boolean state = false;
        for (NewLogixAction action : actions) {
            state |= action.executeContinue();
        }
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        for (NewLogixAction action : actions) {
            action.abort();
        }
    }

    @Override
    public NewLogixSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
