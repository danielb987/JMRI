package jmri.jmrit.newlogix;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for AnalogExpression classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixAnalogExpressionFactory {

    /**
     * Get a set of classes that implements the AnalogExpression interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends AnalogExpression>>> getAnalogExpressionClasses();
    
}
