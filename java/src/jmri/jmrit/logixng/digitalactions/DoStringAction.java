package jmri.jmrit.logixng.digitalactions;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.StringExpressionManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleStringActionSocket;
import jmri.jmrit.logixng.FemaleStringExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleStringActionSocket;
import jmri.jmrit.logixng.MaleStringExpressionSocket;

/**
 * Executes an string action with the result of an string expression.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public class DoStringAction
        extends AbstractDigitalAction
        implements FemaleSocketListener {

    private final FemaleStringExpressionSocket _stringExpressionSocket;
    private final FemaleStringActionSocket _stringActionSocket;
    
    public DoStringAction(Base parent, String sys) {
        super(parent, sys);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleStringExpressionSocket(this, this, "E1");
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleStringActionSocket(this, this, "A1");
    }
    
    public DoStringAction(Base parent, String sys, String user) {
        super(parent, sys, user);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleStringExpressionSocket(this, this, "E1");
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleStringActionSocket(this, this, "A1");
    }
    
    public DoStringAction(
            Base parent,
            String sys,
            String expressionSocketName, String actionSocketName,
            MaleStringExpressionSocket expression, MaleStringActionSocket action) {
        
        super(parent, sys);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleStringExpressionSocket(this, this, expressionSocketName, expression);
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleStringActionSocket(this, this, actionSocketName, action);
    }
    
    public DoStringAction(
            Base parent,
            String sys, String user,
            String expressionSocketName, String actionSocketName, 
            MaleStringExpressionSocket expression, MaleStringActionSocket action) {
        
        super(parent, sys, user);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleStringExpressionSocket(this, this, expressionSocketName, expression);
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleStringActionSocket(this, this, actionSocketName, action);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        String result = _stringExpressionSocket.evaluate();
        
        _stringActionSocket.setValue(result);

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // We should never be here since executeStart() always return false.
        throw new UnsupportedOperationException("Not supported.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        return executeStart();
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // Do nothing
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _stringExpressionSocket;
                
            case 1:
                return _stringActionSocket;
                
            default:
                throw new IllegalArgumentException(
                        String.format("index has invalid value: %d", index));
        }
    }

    @Override
    public int getChildCount() {
        return 2;
    }

    @Override
    public void connected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("DoStringAction_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DoStringAction", _stringExpressionSocket.getName(), _stringActionSocket.getName());
    }

}
