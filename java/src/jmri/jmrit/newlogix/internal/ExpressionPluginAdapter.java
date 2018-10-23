package jmri.jmrit.newlogix.internal;

import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;
import jmri.NewLogixExpression;

/**
 * Adapter for expression plugins.
 * Every expression needs to have a configurator class that delivers a JPanel
 * used for configuration. Since plugin expressions has 
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionPluginAdapter extends AbstractExpression {

    private NewLogixExpression _pluginExpression;
    
    public ExpressionPluginAdapter(String sys, NewLogixExpression pluginExpression)
            throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
//        jmri.jmrix.ConnectionConfig cc;
        _pluginExpression = pluginExpression;
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return _pluginExpression.getCategory();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        return _pluginExpression.evaluate();
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        _pluginExpression.reset();
    }

    @Override
    public NewLogixSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
