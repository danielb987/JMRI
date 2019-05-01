package jmri.jmrit.logixng.digital.actions;

import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.DigitalActionPlugin;

/**
 * This class has a plugin class.
 */
public class DigitalActionPluginSocket extends AbstractDigitalAction {

    private DigitalActionPluginSocket _template;
    private final DigitalActionPlugin _actionPlugin;
    
    public DigitalActionPluginSocket(String sys, DigitalActionPlugin actionPlugin) {
        super(sys);
        _actionPlugin = actionPlugin;
    }
    
    public DigitalActionPluginSocket(
            String sys, String user, DigitalActionPlugin actionPlugin) {
        super(sys, user);
        _actionPlugin = actionPlugin;
    }
    
    private DigitalActionPluginSocket(DigitalActionPluginSocket template, String sys) {
        super(sys);
        _actionPlugin = null;
        _template = template;
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new DigitalActionPluginSocket(this, sys);
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

    /** {@inheritDoc} */
    @Override
    public void setup() {
        _actionPlugin.setup();
    }

}
