package jmri.jmrit.logixng;

import java.util.Set;

/**
 * Factory class for AnalogExpressionBean classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogExpressionFactory {

    /**
     * Init the factory, for example create categories.
     */
    public default void init() {}
    
    /**
     * Get a set of classes that implements the AnalogExpression interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<ClassInfo> getClasses();
    
    
    public static class ClassInfo {
        
        public final Category category;
        public final Class<? extends AnalogExpressionBean> clazz;
        public final String description;
        public final String englishDescription;
        
        public ClassInfo(
                Category category,
                Class<? extends AnalogExpressionBean> clazz,
                String description,
                String englishDescription) {
            
            this.category = category;
            this.clazz = clazz;
            this.description = description;
            this.englishDescription = englishDescription;
        }
    }
    
}
