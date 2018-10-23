package jmri;

/**
 * The common interface for NewLogix expressions and actions.
 * Used to simplify the user interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixCommon {

    /**
     * Return the class name of the configurator class.
     * Returns the fully qualified class name of the class that is used to
     * configurate this class. That class needs to implement the
     * jmri.jmrit.newlogix.swing.NewLogixConfigurator interface.
     * 
     * @return the fully qualified name of the configurator class
     */
    public String getConfiguratorClassName();
    
    /**
     * Get a short description of this item.
     * @return a short description
     */
    public String getShortDescription();
    
    /**
     * Get a long description of this item.
     * @return a long description
     */
    public String getLongDescription();
    
    /**
     * Get a child of this item
     * @param index the index of the child to get
     * @return the child
     * @throws IllegalArgumentException if the index is less than 0 or greater
     * or equal with the value returned by getChildCount()
     */
    public NewLogixSocket getChild(int index)
            throws IllegalArgumentException, UnsupportedOperationException;

    /**
     * Get the number of children.
     * @return the number of children
     */
    public int getChildCount();
    
    
    /**
     * A NewLogix socket.
     * A NewLogixExpression or a NewLogixAction that has children must not
     * use these directly but instead use a NewLogixExpressionSocket or a
     * NewLogixActionSocket.
     */
    public interface NewLogixSocket extends NewLogixCommon {}
    
}
