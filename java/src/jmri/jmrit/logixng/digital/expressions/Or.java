package jmri.jmrit.logixng.digital.expressions;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evaluates to True if any of the children expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Or extends AbstractDigitalExpression implements FemaleSocketListener {

    private Or _template;
    List<FemaleDigitalExpressionSocket> _children = new ArrayList<>();
    private final List<ExpressionEntry> expressionEntries = new ArrayList<>();
    
    /**
     * Create a new instance of ExpressionIfThen and generate a new system name.
     */
    public Or(ConditionalNG conditionalNG) {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
        init();
    }
    
    public Or(String sys) throws BadSystemNameException {
        super(sys);
        init();
    }

    public Or(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        init();
    }
    
    public Or(String sys, List<Map.Entry<String, String>> expressionSystemNames) throws BadSystemNameException {
        super(sys);
        setExpressionSystemNames(expressionSystemNames);
    }

    public Or(String sys, String user, List<Map.Entry<String, String>> expressionSystemNames)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        setExpressionSystemNames(expressionSystemNames);
    }

    private Or(Or template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new Or(this, sys);
    }
    
    private void init() {
        _children.add(InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, getNewSocketName()));
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
        for (DigitalExpression e : _children) {
            e.initEvaluation();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        boolean result = false;
        for (DigitalExpression e : _children) {
            if (e.evaluate(isCompleted)) {
                result = true;
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (DigitalExpression e : _children) {
            e.reset();
        }
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return _children.get(index);
    }
    
    @Override
    public int getChildCount() {
        return _children.size();
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("Or_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("Or_Long");
    }

    private void setExpressionSystemNames(List<Map.Entry<String, String>> systemNames) {
        if (!expressionEntries.isEmpty()) {
            throw new RuntimeException("expression system names cannot be set more than once");
        }
        
        for (Map.Entry<String, String> entry : systemNames) {
//            System.out.format("Many: systemName: %s%n", entry);
            System.err.format("AAA Or: socketName: %s, systemName: %s%n", entry.getKey(), entry.getValue());
            FemaleDigitalExpressionSocket socket =
                    InstanceManager.getDefault(DigitalExpressionManager.class)
                            .createFemaleSocket(this, this, entry.getKey());
            
            expressionEntries.add(new ExpressionEntry(socket, entry.getValue()));
        }
    }
    
    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (FemaleDigitalExpressionSocket child : _children) {
            hasFreeSocket = !child.isConnected();
            if (hasFreeSocket) {
                break;
            }
        }
        if (!hasFreeSocket) {
            _children.add(InstanceManager.getDefault(DigitalExpressionManager.class).createFemaleSocket(this, this, getNewSocketName()));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        System.err.format("AAAA setup()%n");
        for (ExpressionEntry ee : expressionEntries) {
            if (ee._socketSystemName != null) {
                System.err.format("AA SocketName: %s, SystemName: %s%n", ee._socket.getName(), ee._socketSystemName);
                try {
                    MaleSocket maleSocket = InstanceManager.getDefault(DigitalExpressionManager.class).getBeanBySystemName(ee._socketSystemName);
                    if (maleSocket != null) {
                        ee._socket.connect(maleSocket);
                        maleSocket.setup();
                    } else {
                        log.error("cannot load digital expression " + ee._socketSystemName);
                    }
                } catch (SocketAlreadyConnectedException ex) {
                    // This shouldn't happen and is a runtime error if it does.
                    throw new RuntimeException("socket is already connected");
                }
            }
        }
    }
    
    
//    /* This class is public since ExpressionManyXml needs to access it. */
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
        
//        public DigitalExpression getExpression() {
//            return (MaleDigitalExpressionSocket) socket.getConnectedSocket();
//        }
        
    }
    
    private final static Logger log = LoggerFactory.getLogger(Or.class);
    
}
