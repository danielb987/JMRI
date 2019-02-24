package jmri.jmrit.logixng.expressions;

import java.util.List;
import java.util.ArrayList;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.Expression;
import jmri.jmrit.logixng.ExpressionManager;
import jmri.jmrit.logixng.FemaleExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.LogixNG;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionAnd extends AbstractExpression implements FemaleSocketListener {

    List<FemaleExpressionSocket> children = new ArrayList<>();
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     * @param newLogix the LogixNG that this action is related to
     */
    public ExpressionAnd(LogixNG newLogix) {
        super(InstanceManager.getDefault(ExpressionManager.class).getNewSystemName(newLogix));
        init();
    }
    
    public ExpressionAnd(String sys) throws BadUserNameException,
            BadSystemNameException {
        super(sys);
        init();
    }

    public ExpressionAnd(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        super(sys, user);
        init();
    }

    private void init() {
        children.add(InstanceManager.getDefault(ExpressionManager.class).createFemaleExpressionSocket(this, getNewSocketName()));
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
    public boolean evaluate() {
        boolean result = true;
        for (Expression e : children) {
            if (! e.evaluate()) {
                result = false;
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (Expression e : children) {
            e.reset();
        }
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return children.get(index);
    }
    
    @Override
    public int getChildCount() {
        return children.size();
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("ExpressionAnd");
//        return Bundle.getMessage("ExpressionAnd",
//                _holdExpressionSocket.getName(),
//                _triggerExpressionSocket.getName());
    }
    
    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (FemaleExpressionSocket child : children) {
            hasFreeSocket = !child.isConnected();
            if (hasFreeSocket) {
                break;
            }
        }
        if (!hasFreeSocket) {
            children.add(InstanceManager.getDefault(ExpressionManager.class).createFemaleExpressionSocket(this, getNewSocketName()));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
