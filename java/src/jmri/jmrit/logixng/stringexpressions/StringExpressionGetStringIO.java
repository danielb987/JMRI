package jmri.jmrit.logixng.stringexpressions;

import jmri.StringIO;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Reads an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class StringExpressionGetStringIO extends AbstractStringExpression {

    private StringIO _stringIO;
    
    public StringExpressionGetStringIO(String sys) throws BadUserNameException,
            BadSystemNameException {
        super(sys);
    }

    public StringExpressionGetStringIO(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        super(sys, user);
    }

    public StringExpressionGetStringIO(
            String sys,
            StringIO stringIO) {
        
        super(sys);
        _stringIO = stringIO;
    }
    
    public StringExpressionGetStringIO(
            String sys, String user,
            StringIO stringIO) {
        
        super(sys, user);
        _stringIO = stringIO;
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
    public String evaluate() {
        if (_stringIO != null) {
            return _stringIO.getKnownStringValue();
        } else {
            return "";
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
        if (_stringIO != null) {
            return Bundle.getMessage("StringExpressionStringIO", _stringIO.getDisplayName());
        } else {
            return Bundle.getMessage("StringExpressionStringIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

}
