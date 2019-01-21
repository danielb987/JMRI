package jmri.jmrit.newlogix.expressions;

import jmri.InstanceManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.ExpressionManager;
import jmri.jmrit.newlogix.FemaleExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.SocketAlreadyConnectedException;

/**
 * This Expression has two expressions, the primary expression and the secondary
 expression. When the primary expression becomes True after have been False,
 * the secondary expression is reset.
 * 
 * The result of the evaluation of this expression is True if both the
 * expressions evaluates to True.
 * 
 * This expression is used for example if one expression should trigger a timer.
 * If the primary expression is a sensor having a certain state and the secondary
 * expression is a timer, this expression will evaluate to True if the sensor
 * has had that state during the specified time.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionResetOnTrue extends AbstractExpression implements FemaleSocketListener {

    private final FemaleExpressionSocket _primaryExpressionSocket;
    private final FemaleExpressionSocket _secondaryExpressionSocket;
    private boolean _lastMainResult = false;
    
    public ExpressionResetOnTrue(String sys, String user,
            MaleExpressionSocket primaryExpression, MaleExpressionSocket secondaryExpression)
            throws BadUserNameException, BadSystemNameException, SocketAlreadyConnectedException {
        
        super(sys, user);
        
        _primaryExpressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _secondaryExpressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, "E2");
        
        _primaryExpressionSocket.connect(primaryExpression);
        _secondaryExpressionSocket.connect(secondaryExpression);
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
        boolean result = _primaryExpressionSocket.evaluate();
        if (!_lastMainResult && result) {
            _secondaryExpressionSocket.reset();
        }
        _lastMainResult = result;
        result |= _secondaryExpressionSocket.evaluate();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _primaryExpressionSocket.reset();
        _secondaryExpressionSocket.reset();
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
    public String getShortDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // This class doesn't care.
    }

}
