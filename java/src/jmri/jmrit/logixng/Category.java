package jmri.jmrit.logixng;

/**
 * The category of expressions and actions.
 * 
 * It's used to group expressions or actions then the user creates a new
 * expression or action.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public enum Category {

    /**
     * A item on the layout, for example turnout, sensor and signal mast.
     */
    ITEM,
    
    /**
     * Common.
     */
    COMMON,
    
    /**
     * Other things.
     */
    OTHER,
    
    /**
     * Extravaganza. Things seldom used, included mostly for fun, but maybe
     * useful in some cases.
     */
    EXRAVAGANZA,
}
