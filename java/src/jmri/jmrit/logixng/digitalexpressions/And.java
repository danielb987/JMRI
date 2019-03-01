package jmri.jmrit.logixng.digitalexpressions;

import java.util.List;
import java.util.ArrayList;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class And extends AbstractDigitalExpression implements FemaleSocketListener {

    List<FemaleDigitalExpressionSocket> children = new ArrayList<>();
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     * @param newLogix the LogixNG that this action is related to
     */
    public And(LogixNG newLogix) {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(newLogix));
        init();
    }
    
    public And(String sys) throws BadUserNameException,
            BadSystemNameException {
        super(sys);
        init();
    }

    public And(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        super(sys, user);
        init();
    }

    private void init() {
        children.add(InstanceManager.getDefault(DigitalExpressionManager.class).createFemaleExpressionSocket(this, getNewSocketName()));
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
        for (DigitalExpression e : children) {
            if (! e.evaluate()) {
                result = false;
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (DigitalExpression e : children) {
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
        return Bundle.getMessage("And");
    }
    
    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (FemaleDigitalExpressionSocket child : children) {
            hasFreeSocket = !child.isConnected();
            if (hasFreeSocket) {
                break;
            }
        }
        if (!hasFreeSocket) {
            children.add(InstanceManager.getDefault(DigitalExpressionManager.class).createFemaleExpressionSocket(this, getNewSocketName()));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
