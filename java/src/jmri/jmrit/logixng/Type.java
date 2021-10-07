package jmri.jmrit.logixng;

import jmri.JmriException;
import jmri.NamedBean;

/**
 * Represent a LogixNG type.
 * When a Local variable or a Module parameter is defined, the user select the
 * type of that local variable or module parameter.
 *
 * @author Daniel Bergqvist Copyright (C) 2021
 */
public interface Type extends NamedBean {
    
    Object getNewInstance() throws JmriException;
    
    String getClassName();
    
    void setClassName(String name) throws JmriException;
    
    TypeEnum getTypeEnum();
    
    
    public enum TypeEnum {
        ExistingClass(Bundle.getMessage("Type_TypeEnum_ExistingClass")),
        Script(Bundle.getMessage("Type_TypeEnum_Script"));
        
        private final String _descr;
        
        private TypeEnum(String descr) {
            this._descr = descr;
        }
        
        @Override
        public String toString() {
            return _descr;
        }
    }
    
}
