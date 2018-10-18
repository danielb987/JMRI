package jmri;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for NewLogixAction classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixActionFactory {

    /**
     * Get a set of classes that implements the NewLogixAction interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixAction>>> getActionClasses();
    
}
