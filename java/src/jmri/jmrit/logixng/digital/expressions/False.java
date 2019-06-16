package jmri.jmrit.logixng.digital.expressions;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.DigitalExpressionManager;

/**
 * Always evaluates to False.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class False extends AbstractDigitalExpression {

    private False _template;
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     */
    public False(ConditionalNG conditionalNG) {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
    }
    
    public False(String sys) throws BadSystemNameException {
        super(sys);
    }

    public False(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    public False(String sys, List<String> childrenSystemNames) throws BadSystemNameException {
        super(sys);
    }

    public False(String sys, String user, List<String> childrenSystemNames)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    private False(False template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new False(this, sys);
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
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Do nothing
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("True");
    }
    
    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Do nothing
    }

}
