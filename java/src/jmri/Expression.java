package jmri;

/**
 * Expression is used in NewLogix to answer a question that can give the answers
 * 'true' or 'false'.
 * 
 * @author Daniel Bergqvist 2018
 */
public interface Expression {
    
    public enum TriggerCondition {
        TRUE,
        FALSE,
        CHANGE
    }
    
    /**
     * Get the category of this expression.
     * @return the category
     */
    public NewLogixCategory getCategory();

    /**
     * Evaluate this expression.
     * 
     * @return the result of the evaluation
     */
    public boolean evaluate();
    
    /**
     * Reset the evaluation.
     * This method is called when the closest ancestor Action is activated, if
     * this Expression is used by an Action. An example is a timer who is used
     * to delay the execution of an action's child action.
     * 
     * A parent expression needs to call reset() on its child when the parent
     * is reset().
     */
    public void reset();
    
}
