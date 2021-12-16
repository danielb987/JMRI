package jmri.jmrit.logixng;

import java.util.Set;

/**
 * Factory class for DigitalAction classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalActionFactory {

    /**
     * Init the factory, for example create categories.
     */
    public default void init() {}
    
    /**
     * Get a set of classes that implements the DigitalAction interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<ClassInfo> getActionClasses();
    
    
    public static class ClassInfo {
        
        public final Category category;
        public final Class<? extends DigitalActionBean> clazz;
        public final String description;
        public final String englishDescription;
        
        public ClassInfo(
                Category category,
                Class<? extends DigitalActionBean> clazz,
                String description,
                String englishDescription) {
            
            this.category = category;
            this.clazz = clazz;
            this.description = description;
            this.englishDescription = englishDescription;
        }
    }
    
}
