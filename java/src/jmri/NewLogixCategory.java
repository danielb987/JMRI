package jmri;

/**
 * The category of expressions and actions.
 * 
 * It's used to group expressions or actions then the user creates a new
 * expression or action.
 * 
 * @author Daniel Bergqvist 2018
 */
public enum NewLogixCategory {

    /**
     * A item on the layout, for example turnout, sensor and signal mast.
     */
    ITEM,
    
    /**
     * Common.
     */
    COMMON,
    
    /**
     * Everything else.
     */
    OTHER,
}
