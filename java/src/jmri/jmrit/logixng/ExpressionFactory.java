package jmri.jmrit.logixng;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for Expression classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ExpressionFactory {

    /**
     * Get a set of classes that implements the Expression interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends Expression>>> getExpressionClasses();
    
}
