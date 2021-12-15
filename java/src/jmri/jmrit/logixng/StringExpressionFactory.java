package jmri.jmrit.logixng;

import java.util.Set;

/**
 * Factory class for StringExpressionBean classes.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public interface StringExpressionFactory {

    /**
     * Init the factory, for example create categories.
     */
    public default void init() {}
    
    /**
     * Get a set of classes that implements the StringExpression interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<ClassInfo> getClasses();
    
    
    public static class ClassInfo {
        
        public final Category category;
        public final Class<? extends StringExpressionBean> clazz;
        public final String description;
        
        public ClassInfo(
                Category category,
                Class<? extends StringExpressionBean> clazz,
                String description) {
            
            this.category = category;
            this.clazz = clazz;
            this.description = description;
        }
    }
    
}
