package jmri.jmrit.logixng.string.actions;

import jmri.jmrit.logixng.analog.actions.*;
import javax.annotation.CheckForNull;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Sets a Memory.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class StringActionMemory extends AbstractStringAction {

    private StringActionMemory _template;
    private String _memorySystemName;
    private NamedBeanHandle<Memory> _memoryHandle;
//    private Memory _memory;
    
    public StringActionMemory(String sys) {
        super(sys);
    }
    
    public StringActionMemory(String sys, String user) {
        super(sys, user);
    }
    
    private StringActionMemory(StringActionMemory template, String sys) {
        super(sys);
        _template = template;
        _memoryHandle = _template._memoryHandle;
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new StringActionMemory(this, sys);
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
    public void setValue(String value) {
        if (_memoryHandle != null) {
            _memoryHandle.getBean().setValue(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    /** {@inheritDoc} */
    @Override
    public int getChildCount() {
        return 0;
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
    public String getShortDescription() {
        if (_memoryHandle != null) {
            return Bundle.getMessage("StringActionMemory", _memoryHandle.getBean().getDisplayName());
        } else {
            return Bundle.getMessage("StringActionMemory", "none");
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setMemorySystemName(String analogIO_SystemName) {
        _memorySystemName = analogIO_SystemName;
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
