package jmri.jmrit.logixng.analog.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Sets a Memory.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogActionMemory extends AbstractAnalogAction {

    private AnalogActionMemory _template;
    private String _memory_SystemName;
    private Memory _memory;
    
    public AnalogActionMemory(String sys) {
        super(sys);
    }
    
    public AnalogActionMemory(String sys, String user) {
        super(sys, user);
    }
    
    public AnalogActionMemory(String sys, Memory memory) {
        
        super(sys);
        _memory = memory;
    }
    
    public AnalogActionMemory(String sys, String user, Memory memory) {
        
        super(sys, user);
        _memory = memory;
    }
    
    private AnalogActionMemory(AnalogActionMemory template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new AnalogActionMemory(this, sys);
    }
    
    @Override
    public void setValue(double value) {
        if (_memory != null) {
            _memory.setValue(value);
        }
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
    public Category getCategory() {
        return Category.ITEM;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    public String getShortDescription() {
        if (_memory != null) {
            return Bundle.getMessage("AnalogActionSetAnalogIO", _memory.getDisplayName());
        } else {
            return Bundle.getMessage("AnalogActionSetAnalogIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    public void setMemorySystemName(String analogIO_SystemName) {
        _memory_SystemName = analogIO_SystemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_memory == null) && (_memory_SystemName != null)) {
            System.out.format("Setup: %s%n", _memory_SystemName);     // Temporary until the AnalogIOManager is implemented
            _memory = InstanceManager.getDefault(MemoryManager.class).getBeanBySystemName(_memory_SystemName);
        }
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(AnalogActionMemory.class);

}
