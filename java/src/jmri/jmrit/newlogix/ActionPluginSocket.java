package jmri.jmrit.newlogix;

import jmri.NewLogixActionPlugin;
import java.util.Map;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractAction;

/**
 * This class has a plugin class.
 */
public class ActionPluginSocket extends AbstractAction {

    private final NewLogixActionPlugin _actionPlugin;
    
    public ActionPluginSocket(String sys, NewLogixActionPlugin actionPlugin) {
        super(sys);
        _actionPlugin = actionPlugin;
    }
    
    public ActionPluginSocket(String sys, String user, NewLogixActionPlugin action) {
        super(sys, user);
        _actionPlugin = action;
    }
    
    @Override
    public NewLogixCategory getCategory() {
        return _actionPlugin.getCategory();
    }

    @Override
    public boolean isExternal() {
        return _actionPlugin.isExternal();
    }

    @Override
    public boolean executeStart() {
        return _actionPlugin.executeStart();
    }

    @Override
    public boolean executeContinue() {
        return _actionPlugin.executeContinue();
    }

    @Override
    public boolean executeRestart() {
        return _actionPlugin.executeRestart();
    }

    @Override
    public void abort() {
        _actionPlugin.abort();
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
