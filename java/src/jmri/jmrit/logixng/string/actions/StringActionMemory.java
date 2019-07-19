package jmri.jmrit.logixng.string.actions;

import javax.annotation.CheckForNull;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sets a Memory.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class StringActionMemory extends AbstractStringAction {

    private StringActionMemory _template;
    private String _memoryName;
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

    public void setMemoryName(String memoryName) {
        _memoryName = memoryName;
        // setup() must be run in order to use the new memory
        _memoryHandle = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Don't setup again if we already has the correct memory
        if ((_memoryHandle != null) && (_memoryHandle.getName().equals(_memoryName))) {
            return;
        }
        
        // Remove the old _memoryHandle if it exists
        _memoryHandle = null;
        
        if (_memoryName != null) {
            Memory m = InstanceManager.getDefault(MemoryManager.class).getMemory(_memoryName);
            if (m != null) {
                _memoryHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_memoryName, m);
            } else {
                log.error("Memory {} does not exists", _memoryName);
            }
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
    
    
    private final static Logger log = LoggerFactory.getLogger(StringActionMemory.class);
    
}
