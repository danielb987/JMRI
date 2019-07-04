package jmri.jmrit.logixng.digital.actions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.StringExpressionManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleStringActionSocket;
import jmri.jmrit.logixng.FemaleStringExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleStringActionSocket;
import jmri.jmrit.logixng.MaleStringExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Executes an string action with the result of an string expression.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public class DoStringAction
        extends AbstractDigitalAction
        implements FemaleSocketListener {

    private DoStringAction _template;
    private final AtomicBoolean _isExpressionCompleted = new AtomicBoolean(true);
    private String _stringExpressionSocketSocketSystemName;
    private String _stringActionSocketSocketSystemName;
    private final FemaleStringExpressionSocket _stringExpressionSocket;
    private final FemaleStringActionSocket _stringActionSocket;
    
    public DoStringAction(ConditionalNG conditionalNG) {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(conditionalNG));
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleSocket(this, this, "A1");
    }
    
    public DoStringAction(String sys) {
        super(sys);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleSocket(this, this, "A1");
    }
    
    public DoStringAction(String sys, String user) {
        super(sys, user);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleSocket(this, this, "A1");
    }
/*    
    public DoStringAction(
            String sys,
            String expressionSocketName, String actionSocketName,
            MaleStringExpressionSocket expression, MaleStringActionSocket action) {
        
        super(sys);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleStringExpressionSocket(this, this, expressionSocketName, expression);
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleStringActionSocket(this, this, actionSocketName, action);
    }
    
    public DoStringAction(
            String sys, String user,
            String expressionSocketName, String actionSocketName, 
            MaleStringExpressionSocket expression, MaleStringActionSocket action) {
        
        super(sys, user);
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleStringExpressionSocket(this, this, expressionSocketName, expression);
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleStringActionSocket(this, this, actionSocketName, action);
    }
*/    
    private DoStringAction(DoStringAction template, String sys) {
        super(sys);
        _template = template;
        _stringExpressionSocket = InstanceManager.getDefault(StringExpressionManager.class)
                .createFemaleSocket(this, this, _template._stringExpressionSocket.getName());
        _stringActionSocket = InstanceManager.getDefault(StringActionManager.class)
                .createFemaleSocket(this, this, _template._stringActionSocket.getName());
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new DoStringAction(this, sys);
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
        _isExpressionCompleted.set(true);
        
        String result = _stringExpressionSocket.evaluate(_isExpressionCompleted);
        
        _stringActionSocket.setValue(result);

        return !_isExpressionCompleted.get();
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        _isExpressionCompleted.set(true);
        
        String result = _stringExpressionSocket.evaluate(_isExpressionCompleted);
        
        _stringActionSocket.setValue(result);

        return !_isExpressionCompleted.get();
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        _isExpressionCompleted.set(true);
        
        String result = _stringExpressionSocket.evaluate(_isExpressionCompleted);
        
        _stringActionSocket.setValue(result);

        return !_isExpressionCompleted.get();
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
        return Bundle.getMessage("DoStringAction_Long", _stringExpressionSocket.getName(), _stringActionSocket.getName());
    }

    public void setAnalogActionSocketSystemName(String systemName) {
        _stringActionSocketSocketSystemName = systemName;
    }

    public void setAnalogExpressionSocketSystemName(String systemName) {
        _stringExpressionSocketSocketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ((!_stringActionSocket.isConnected()) && (_stringActionSocketSocketSystemName != null)) {
                _stringActionSocket.connect(
                        InstanceManager.getDefault(StringActionManager.class)
                                .getBeanBySystemName(_stringActionSocketSocketSystemName));
            }
            if ((!_stringExpressionSocket.isConnected()) && (_stringExpressionSocketSocketSystemName != null)) {
                _stringExpressionSocket.connect(
                        InstanceManager.getDefault(StringExpressionManager.class)
                                .getBeanBySystemName(_stringExpressionSocketSocketSystemName));
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }

}
