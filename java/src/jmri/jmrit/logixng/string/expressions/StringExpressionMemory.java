package jmri.jmrit.logixng.string.expressions;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads a Memory.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class StringExpressionMemory extends AbstractStringExpression implements PropertyChangeListener {

    private StringExpressionMemory _template;
    private String _memoryName;
//    private Memory _memory;
    private NamedBeanHandle<Memory> _memoryHandle;
    private boolean _listenersAreRegistered = false;
    
    public StringExpressionMemory(String sys) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
    }

    public StringExpressionMemory(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
    }

    private StringExpressionMemory(StringExpressionMemory template, String sys) {
        super(sys);
        _template = template;
        _memoryHandle = _template._memoryHandle;
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new StringExpressionMemory(this, sys);
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
    public String evaluate(@Nonnull AtomicBoolean isCompleted) {
        if (_memoryHandle != null) {
            return jmri.util.TypeConversionUtil.convertToString(_memoryHandle.getBean().getValue(), false);
        } else {
            return "";
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
            return Bundle.getMessage("StringExpressionMemory", _memoryHandle.getBean().getDisplayName());
        } else {
            return Bundle.getMessage("StringExpressionMemory", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setMemoryName(String memoryName) {
        _memoryName = memoryName;
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
        if ((! _listenersAreRegistered) && (_memoryHandle != null)) {
            _memoryHandle.getBean().addPropertyChangeListener("value", this);
            _listenersAreRegistered = true;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        if (_listenersAreRegistered) {
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
    
    
    private final static Logger log = LoggerFactory.getLogger(StringExpressionMemory.class);
    
}
