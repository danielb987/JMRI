package jmri.jmrit.newlogix;

import java.util.List;
import java.util.ArrayList;
import jmri.Action;
import jmri.NewLogixCategory;

/**
 * Execute many Actions in a specific order.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionMany implements Action {

    private final List<Action> actions = new ArrayList<>();
    
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
