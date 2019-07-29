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
    private String _stringExpressionSocket_SocketSystemName;
    private String _stringActionSocket_SocketSystemName;
    private final FemaleStringExpressionSocket _stringExpressionSocket;
    private final FemaleStringActionSocket _stringActionSocket;
    
    public DoStringAction() {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName());
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
    public void execute() {
        String result = _stringExpressionSocket.evaluate();
        
        _stringActionSocket.setValue(result);
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

    public FemaleStringActionSocket getStringActionSocket() {
        return _stringActionSocket;
    }

    public String getStringActionSocketSystemName() {
        return _stringActionSocket_SocketSystemName;
    }

    public void setStringActionSocketSystemName(String systemName) {
        _stringActionSocket_SocketSystemName = systemName;
    }

    public FemaleStringExpressionSocket getStringExpressionSocket() {
        return _stringExpressionSocket;
    }

    public String getStringExpressionSocketSystemName() {
        return _stringExpressionSocket_SocketSystemName;
    }

    public void setStringExpressionSocketSystemName(String systemName) {
        _stringExpressionSocket_SocketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ((!_stringActionSocket.isConnected()) && (_stringActionSocket_SocketSystemName != null)) {
                _stringActionSocket.connect(
                        InstanceManager.getDefault(StringActionManager.class)
                                .getBeanBySystemName(_stringActionSocket_SocketSystemName));
            }
            if ((!_stringExpressionSocket.isConnected()) && (_stringExpressionSocket_SocketSystemName != null)) {
                _stringExpressionSocket.connect(
                        InstanceManager.getDefault(StringExpressionManager.class)
                                .getBeanBySystemName(_stringExpressionSocket_SocketSystemName));
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }

}
