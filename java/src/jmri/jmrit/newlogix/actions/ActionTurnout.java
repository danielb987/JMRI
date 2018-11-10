package jmri.jmrit.newlogix.actions;

import jmri.jmrit.newlogix.Category;
import jmri.Turnout;
import jmri.jmrit.newlogix.Action;
import jmri.jmrit.newlogix.FemaleSocket;

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
//        jmri.InstanceManager.turnoutManagerInstance().addVetoableChangeListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
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

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

}
