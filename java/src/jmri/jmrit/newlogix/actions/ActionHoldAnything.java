package jmri.jmrit.newlogix.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.newlogix.ActionManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.NewLogix;

/**
 * An action that can hold everything but doesn't do anything.
 * It is used to hold parts of the NewLogix tree that is not currently in use.
 * This allows moving operations in the tree.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionHoldAnything extends AbstractAction {

    private final Map<Class, List<? extends FemaleSocket>> sockets = new HashMap<>();
    
    /**
     * Create a new instance of ActionMany and generate a new system name.
     * @param newLogix the NewLogix that this action is related to
     */
    public ActionHoldAnything(NewLogix newLogix)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
        super(InstanceManager.getDefault(ActionManager.class).getNewSystemName(newLogix));
    }

    public ActionHoldAnything(String sys)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
        super(sys);
    }

    public ActionHoldAnything(String sys, String user)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
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
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // Do nothing
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
