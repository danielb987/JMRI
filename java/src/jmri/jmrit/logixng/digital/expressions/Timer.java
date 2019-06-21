package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;

/**
 * This expression is a timer that evaluates to true then a certain time has passed.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Timer extends AbstractDigitalExpression {

    private Timer _template;
    
    public Timer(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    private Timer(Timer template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new Timer(this, sys);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        // Has timer completed?
        
        return true;    // Mockup code for now.
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Reset timer.
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
        return Bundle.getMessage("Timer_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("Timer_Long");
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
    }

}
