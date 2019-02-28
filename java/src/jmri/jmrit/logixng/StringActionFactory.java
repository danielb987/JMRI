package jmri.jmrit.logixng;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for StringAction classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface StringActionFactory {

    /**
     * Get a set of classes that implements the StringAction interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends StringAction>>> getStringActionClasses();
    
}
