package jmri.jmrit.logixng.analog.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Reads a Memory.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogExpressionMemory extends AbstractAnalogExpression {

    private AnalogExpressionMemory _template;
    private String _memorySystemName;
    private Memory _memory;
    
    public AnalogExpressionMemory(String sys) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
    }

    public AnalogExpressionMemory(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
    }

    public AnalogExpressionMemory(String sys, Memory memory)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys);
        _memory = memory;
    }
    
    public AnalogExpressionMemory(String sys, String user, Memory memory)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
        _memory = memory;
    }
    
    private AnalogExpressionMemory(AnalogExpressionMemory template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new AnalogExpressionMemory(this, sys);
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
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public double evaluate(@Nonnull AtomicBoolean isCompleted) {
        if (_memory != null) {
            return jmri.util.TypeConversionUtil.convertToDouble(_memory.getValue(), false);
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
        if (_memory != null) {
            return Bundle.getMessage("AnalogExpressionMemory", _memory.getDisplayName());
        } else {
            return Bundle.getMessage("AnalogExpressionMemory", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setAnalogIO_SystemName(String memorySystemName) {
        _memorySystemName = memorySystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_memory == null) && (_memorySystemName != null)) {
            System.out.format("Setup: %s%n", _memorySystemName);     // Temporary until the AnalogIOManager is implemented
            _memory = InstanceManager.getDefault(MemoryManager.class).getBeanBySystemName(_memorySystemName);
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
