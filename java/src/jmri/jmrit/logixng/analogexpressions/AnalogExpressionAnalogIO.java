package jmri.jmrit.logixng.analogexpressions;

import jmri.AnalogIO;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Reads an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogExpressionAnalogIO extends AbstractAnalogExpression {

    private AnalogIO _analogIO;
    
    public AnalogExpressionAnalogIO(String sys) throws BadUserNameException,
            BadSystemNameException {
        super(sys);
    }

    public AnalogExpressionAnalogIO(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        super(sys, user);
    }

    public AnalogExpressionAnalogIO(
            String sys,
            AnalogIO analogIO) {
        
        super(sys);
        _analogIO = analogIO;
    }
    
    public AnalogExpressionAnalogIO(
            String sys, String user,
            AnalogIO analogIO) {
        
        super(sys, user);
        _analogIO = analogIO;
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
    public float evaluate() {
        if (_analogIO != null) {
            return _analogIO.getKnownAnalogValue();
        } else {
            return (float) 0.0;
        }
    }
    
    @Override
    public FemaleSocket getChild(int index)
            throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription() {
        if (_analogIO != null) {
            return Bundle.getMessage("AnalogExpressionAnalogIO", _analogIO.getDisplayName());
        } else {
            return Bundle.getMessage("AnalogExpressionAnalogIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

}
