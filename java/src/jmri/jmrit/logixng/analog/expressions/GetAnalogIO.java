package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import jmri.AnalogIO;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Reads an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class GetAnalogIO extends AbstractAnalogExpression {

    private GetAnalogIO _template;
    private String _analogIO_SystemName;
    private AnalogIO _analogIO;
    
    public GetAnalogIO(String sys) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
    }

    public GetAnalogIO(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
    }

    public GetAnalogIO(String sys, AnalogIO analogIO)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys);
        _analogIO = analogIO;
    }
    
    public GetAnalogIO(String sys, String user, AnalogIO analogIO)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
        _analogIO = analogIO;
    }
    
    private GetAnalogIO(GetAnalogIO template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new GetAnalogIO(this, sys);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return true;
    }
    
    public void setAnalogIO(AnalogIO analogIO) {
        _analogIO = analogIO;
    }
    
    public AnalogIO getAnalogIO() {
        return _analogIO;
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public double evaluate(@Nonnull AtomicBoolean isCompleted) {
        if (_analogIO != null) {
            return _analogIO.getKnownAnalogValue();
        } else {
            return 0.0;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Do nothing
    }
    
    @Override
    public FemaleSocket getChild(int index)
            throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription() {
        if (_analogIO != null) {
            return Bundle.getMessage("AnalogExpressionAnalogIO", _analogIO.getDisplayName());
        } else {
            return Bundle.getMessage("AnalogExpressionAnalogIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setAnalogIO_SystemName(String analogIO_SystemName) {
        _analogIO_SystemName = analogIO_SystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_analogIO == null) && (_analogIO_SystemName != null)) {
            System.out.format("Setup: %s%n", _analogIO_SystemName);     // Temporary until the AnalogIOManager is implemented
//            _analogIO = InstanceManager.getDefault(AnalogIOManager.class).getBeanBySystemName(_analogIO_SystemName);
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
