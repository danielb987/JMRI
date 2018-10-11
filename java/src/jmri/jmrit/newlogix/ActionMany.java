package jmri.jmrit.newlogix;

import java.util.List;
import java.util.ArrayList;
import jmri.Action;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractAction;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionMany extends AbstractAction {

    private final List<Action> actions = new ArrayList<>();
    
    public ActionMany(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.COMMON;
    }

    @Override
    public boolean executeStart() {
        boolean state = false;
        for (Action action : actions) {
            state |= action.executeStart();
        }
        return state;
    }

    @Override
    public boolean executeRestart() {
        boolean state = false;
        for (Action action : actions) {
            state |= action.executeRestart();
        }
        return state;
    }

    @Override
    public boolean executeContinue() {
        boolean state = false;
        for (Action action : actions) {
            state |= action.executeContinue();
        }
        return state;
    }

    @Override
    public void abort() {
        for (Action action : actions) {
            action.abort();
        }
    }

}
