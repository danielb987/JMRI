package jmri.jmrit.logixng.analogexpressions;

import jmri.AnalogIO;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Reads an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogExpressionGetAnalogIO extends AbstractAnalogExpression {

    private AnalogIO _analogIO;
    
    public AnalogExpressionGetAnalogIO(Base parent, String sys) throws BadUserNameException,
            BadSystemNameException {
        super(parent, sys);
    }

    public AnalogExpressionGetAnalogIO(Base parent, String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        super(parent, sys, user);
    }

    public AnalogExpressionGetAnalogIO(
            Base parent,
            String sys,
            AnalogIO analogIO) {
        
        super(parent, sys);
        _analogIO = analogIO;
    }
    
    public AnalogExpressionGetAnalogIO(
            Base parent,
            String sys, String user,
            AnalogIO analogIO) {
        
        super(parent, sys, user);
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
