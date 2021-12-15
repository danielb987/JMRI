package jmri.jmrit.logixng;

import java.util.Set;

/**
 * Factory class for DigitalExpressionBean classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalExpressionFactory {

    /**
     * Init the factory, for example create categories.
     */
    public default void init() {}
    
    /**
     * Get a set of classes that implements the DigitalExpressionBean interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<ClassInfo> getExpressionClasses();
    
    
    public static class ClassInfo {
        
        public final Category category;
        public final Class<? extends DigitalExpressionBean> clazz;
        public final String description;
        
        public ClassInfo(
                Category category,
                Class<? extends DigitalExpressionBean> clazz,
                String description) {
            
            this.category = category;
            this.clazz = clazz;
            this.description = description;
        }
    }
    
}
