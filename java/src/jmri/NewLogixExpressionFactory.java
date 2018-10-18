package jmri;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for NewLogixExpression classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixExpressionFactory {

    /**
     * Get a set of classes that implements the NewLogixExpression interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<NewLogixCategory, Class<? extends NewLogixExpression>>> getExpressionClasses();
    
}
