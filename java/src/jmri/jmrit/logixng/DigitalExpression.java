package jmri.jmrit.logixng;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;

/**
 * DigitalExpressionBean is used in LogixNG to answer a question that can give the
 answers 'true' or 'false'.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalExpression extends Base {
    
    public enum TriggerCondition {
        TRUE,
        FALSE,
        CHANGE
    }
    
    /**
     * Initialize evaluation.
     * Must be called before evaluation if isCompleted was false after the last
     * call to evaluate().
     */
    public void initEvaluation();
    
    /**
     * Evaluate this expression.
     * <P>
     * The parameter isCompleted is used if the expression should be evaluated
     * more than once. For example, the Count expression is not completed until
     * its child expression has been true and false a number of times.
     * 
     * @param isCompleted true if the evaluation is completed. The caller must
     * ensure its initiated to true. If the evaluation is not completed, the
     * expression sets this to false.
     * @return the result of the evaluation
     */
    public boolean evaluate(@Nonnull AtomicBoolean isCompleted);
    
    /**
     * Reset the evaluation.
     * This method is called when the closest ancestor Action is activated. An
     * example is a timer who is used to delay the execution of an action's
     * child action.
     * 
     * A parent expression must to call reset() on its child when the parent
     * is reset().
     */
    public void reset();
    
}
