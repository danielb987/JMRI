package jmri.jmrit.newlogix.analogexpressions;

import java.util.List;
import java.util.ArrayList;
import jmri.AnalogIO;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleSocket;

/**
 * Evaluates to True if all the child expressions evaluate to true.
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
        return Category.COMMON;
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

}
