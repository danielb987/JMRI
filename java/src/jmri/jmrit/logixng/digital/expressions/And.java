package jmri.jmrit.logixng.digital.expressions;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * Evaluates to True if all of the children expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class And extends AbstractDigitalExpression implements FemaleSocketListener {

    private And _template;
    List<String> _childrenSystemNames;
    private final List<ExpressionEntry> _expressionEntries = new ArrayList<>();
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     */
    public And(ConditionalNG conditionalNG) {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
        init();
    }
    
    public And(String sys) throws BadSystemNameException {
        super(sys);
        init();
    }

    public And(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        init();
    }
    
    public And(String sys, List<String> childrenSystemNames) throws BadSystemNameException {
        super(sys);
        _childrenSystemNames = childrenSystemNames;
    }

    public And(String sys, String user, List<String> childrenSystemNames)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        _childrenSystemNames = childrenSystemNames;
    }

    private And(And template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new And(this, sys);
    }
    
    private void init() {
        _expressionEntries
                .add(new ExpressionEntry(InstanceManager.getDefault(DigitalExpressionManager.class)
                        .createFemaleSocket(this, this, getNewSocketName())));
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.COMMON;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        for (ExpressionEntry e : _expressionEntries) {
            e._socket.initEvaluation();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        boolean result = true;
        for (ExpressionEntry e : _expressionEntries) {
            if (! e._socket.evaluate(isCompleted)) {
                result = false;
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (ExpressionEntry e : _expressionEntries) {
            e._socket.reset();
        }
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return _expressionEntries.get(index)._socket;
    }
    
    @Override
    public int getChildCount() {
        return _expressionEntries.size();
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("And_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("And_Long");
    }

    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (ExpressionEntry entry : _expressionEntries) {
            hasFreeSocket = !entry._socket.isConnected();
            if (hasFreeSocket) {
                break;
            }
        }
        if (!hasFreeSocket) {
            _expressionEntries
                    .add(new ExpressionEntry(
                            InstanceManager.getDefault(DigitalExpressionManager.class)
                                    .createFemaleSocket(this, this, getNewSocketName())));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        DigitalExpressionManager manager =
                InstanceManager.getDefault(DigitalExpressionManager.class);
        
        if (_childrenSystemNames != null) {
            if (!_expressionEntries.isEmpty()) {
                throw new RuntimeException("expression system names cannot be set more than once");
            }
            
            for (String systemName : _childrenSystemNames) {
                FemaleDigitalExpressionSocket femaleSocket =
                        manager.createFemaleSocket(this, this, getNewSocketName());

                try {
                    femaleSocket.connect(manager.getBeanBySystemName(systemName));
                } catch (SocketAlreadyConnectedException ex) {
                    // This shouldn't happen and is a runtime error if it does.
                    throw new RuntimeException("socket is already connected");
                }
            }
        }
        
        // Add one extra empty socket
        _expressionEntries
                .add(new ExpressionEntry(manager.createFemaleSocket(this, this, getNewSocketName())));
    }
    
    
    /* This class is public since ExpressionAndXml needs to access it. */
    private static class ExpressionEntry {
        private String _socketSystemName;
        private final FemaleDigitalExpressionSocket _socket;
        
        private ExpressionEntry(FemaleDigitalExpressionSocket socket, String socketSystemName) {
            _socketSystemName = socketSystemName;
            _socket = socket;
        }
        
        private ExpressionEntry(FemaleDigitalExpressionSocket socket) {
            this._socket = socket;
        }
    }
    
}
