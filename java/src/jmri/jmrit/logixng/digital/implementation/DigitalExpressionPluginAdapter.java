package jmri.jmrit.logixng.digital.implementation;

import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.digital.expressions.AbstractDigitalExpression;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.DigitalExpression;

/**
 * Adapter for expression plugins.
 * Every expression needs to have a configurator class that delivers a JPanel
 * used for configuration. Since plugin expressions has 
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DigitalExpressionPluginAdapter extends AbstractDigitalExpression {

    private final DigitalExpression _pluginExpression;
    
    public DigitalExpressionPluginAdapter(String sys, DigitalExpression pluginExpression)
            throws BadSystemNameException {
        
        super(sys);
        
        _pluginExpression = pluginExpression;
    }

    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return _pluginExpression.getNewObjectBasedOnTemplate(sys);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
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
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getShortDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        _pluginExpression.dispose();
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        _pluginExpression.setup();
    }

}
