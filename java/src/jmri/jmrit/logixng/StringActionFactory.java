package jmri.jmrit.logixng;

import java.util.Set;

/**
 * Factory class for StringActionBean classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface StringActionFactory {

    /**
     * Init the factory, for example create categories.
     */
    public default void init() {}
    
    /**
     * Get a set of classes that implements the StringActionBean interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<ClassInfo> getClasses();
    
    
    public static class ClassInfo {
        
        public final Category category;
        public final Class<? extends StringActionBean> clazz;
        public final String description;
        
        public ClassInfo(
                Category category,
                Class<? extends StringActionBean> clazz,
                String description) {
            
            this.category = category;
            this.clazz = clazz;
            this.description = description;
        }
    }
    
}
