package jmri.jmrit.logixng.expressions;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.Expression;
import jmri.jmrit.logixng.ExpressionManager;
import jmri.jmrit.logixng.FemaleExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;

/**
 * An Expression that returns True only once while its child expression returns
 True.
 * 
 * The first time the child expression returns True, this expression returns
 * True. After that, this expression returns False until the child expression
 * returns False and again returns True.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionTriggerOnce extends AbstractExpression implements FemaleSocketListener {

    private FemaleExpressionSocket _childExpression;
    private boolean _childLastState = false;
    
    public ExpressionTriggerOnce(String sys, String user, MaleExpressionSocket expression)
            throws BadUserNameException, BadSystemNameException, SocketAlreadyConnectedException {
        
        super(sys, user);
        
        _childExpression = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _childExpression.connect(expression);
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
    public boolean evaluate() {
        if (_childExpression.evaluate() && !_childLastState) {
            _childLastState = true;
            return true;
        }
        _childLastState = _childExpression.evaluate();
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _childLastState = false;
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
