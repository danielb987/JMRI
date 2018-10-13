package jmri.jmrit.newlogix;

import jmri.Action;
import jmri.NewLogixCategory;
import jmri.Turnout;
import jmri.implementation.AbstractAction;

/**
 * This action sets the state of a turnout.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionTurnout extends AbstractAction {

//    private Turnout _turnout;
//    private int _newState;
    
    public ActionTurnout(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        throw new UnsupportedOperationException("Not supported yet.");
        // Do this on the LayoutThread
//        _turnout.setState(_newState);
//        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // The executeStart() metod never return True from this action and
        // therefore executeContinue() should never be called.
        throw new RuntimeException("ActionTurnout don't support executeContinue()");
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        // The executeStart() metod never return True from this action and
        // therefore executeRestart() should never be called.
        throw new RuntimeException("ActionTurnout don't support executeRestart()");
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // The executeStart() metod never return True from this action and
        // therefore abort() should never be called.
        throw new RuntimeException("ActionTurnout don't support abort()");
    }

}
