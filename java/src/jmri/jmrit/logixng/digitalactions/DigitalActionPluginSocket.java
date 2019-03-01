package jmri.jmrit.logixng.digitalactions;

import java.util.Map;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.DigitalActionPlugin;

/**
 * This class has a plugin class.
 */
public class DigitalActionPluginSocket extends AbstractDigitalAction {

    private final DigitalActionPlugin _actionPlugin;
    
    public DigitalActionPluginSocket(String sys, DigitalActionPlugin actionPlugin) {
        super(sys);
        _actionPlugin = actionPlugin;
    }
    
    public DigitalActionPluginSocket(String sys, String user, DigitalActionPlugin actionPlugin) {
        super(sys, user);
        _actionPlugin = actionPlugin;
    }
    
    @Override
    public Category getCategory() {
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
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("ActionPluginSocket_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("ActionPluginSocket_Long");
//        return Bundle.getMessage("ActionPluginSocket_Long", _analogExpressionSocket.getName(), _analogActionSocket.getName());
    }

}
