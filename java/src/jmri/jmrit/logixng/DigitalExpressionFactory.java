package jmri.jmrit.logixng;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for DigitalExpression classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalExpressionFactory {

    /**
     * Get a set of classes that implements the DigitalExpression interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends DigitalExpression>>> getExpressionClasses();
    
}
