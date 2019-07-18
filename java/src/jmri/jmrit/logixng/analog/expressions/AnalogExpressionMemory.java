package jmri.jmrit.logixng.analog.expressions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Reads a Memory.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogExpressionMemory extends AbstractAnalogExpression implements PropertyChangeListener {

    private AnalogExpressionMemory _template;
    private String _memorySystemName;
//    private Memory _memory;
    private NamedBeanHandle<Memory> _memoryHandle;
    private boolean _listenersAreRegistered = false;
    
    public AnalogExpressionMemory(String sys) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
    }

    public AnalogExpressionMemory(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
    }

    private AnalogExpressionMemory(AnalogExpressionMemory template, String sys) {
        super(sys);
        _template = template;
        _memoryHandle = _template._memoryHandle;
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
    
    public void setMemory(NamedBeanHandle<Memory> handle) {
        _memoryHandle = handle;
    }
    
    public void setMemory(@CheckForNull Memory memory) {
        if (memory != null) {
            _memoryHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                    .getNamedBeanHandle(memory.getDisplayName(), memory);
        } else {
            _memoryHandle = null;
        }
    }
    
    public NamedBeanHandle<Memory> getMemory() {
        return _memoryHandle;
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public double evaluate(@Nonnull AtomicBoolean isCompleted) {
        if (_memoryHandle != null) {
            return jmri.util.TypeConversionUtil.convertToDouble(_memoryHandle.getBean().getValue(), false);
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
        if (_memoryHandle != null) {
            return Bundle.getMessage("AnalogExpressionMemory", _memoryHandle.getBean().getDisplayName());
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
        if ((_memoryHandle == null) && (_memorySystemName != null)) {
            System.out.format("Setup: %s%n", _memorySystemName);
            setMemory(InstanceManager.getDefault(MemoryManager.class).getBeanBySystemName(_memorySystemName));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
        if ((! _listenersAreRegistered) && (_memoryHandle != null)) {
            _memoryHandle.getBean().addPropertyChangeListener("value", this);
            _listenersAreRegistered = true;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        if (! _listenersAreRegistered) {
            _memoryHandle.getBean().removePropertyChangeListener("value", this);
            _listenersAreRegistered = false;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getConditionalNG().execute();
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }

}
