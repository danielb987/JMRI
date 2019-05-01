package jmri.jmrit.logixng.string.expressions;

import jmri.InstanceManager;
import jmri.StringIO;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;

/**
 * Reads an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class GetStringIO extends AbstractStringExpression {

    private GetStringIO _template;
    private String _stringIO_SystemName;
    private StringIO _stringIO;
    
    public GetStringIO(String sys)
            throws BadUserNameException, BadSystemNameException {
        super(sys);
    }

    public GetStringIO(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    public GetStringIO(String sys, StringIO stringIO)
            throws BadUserNameException, BadSystemNameException {
        super(sys);
        _stringIO = stringIO;
    }
    
    public GetStringIO(String sys, String user, StringIO stringIO)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        _stringIO = stringIO;
    }
    
    private GetStringIO(GetStringIO template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new GetStringIO(this, sys);
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

    public void setAnalogIO_SystemName(String stringIO_SystemName) {
        _stringIO_SystemName = stringIO_SystemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_stringIO == null) && (_stringIO_SystemName != null)) {
            System.out.format("Setup: %s%n", _stringIO_SystemName);     // Temporary until the StringIOManager is implemented
//            _stringIO = InstanceManager.getDefault(StringIOManager.class).getBeanBySystemName(_stringIO_SystemName);
        }
    }

}
