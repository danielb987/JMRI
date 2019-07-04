package jmri.jmrit.logixng;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;

/**
 * String expression is used in LogixNG to answer a question that can give
 * a string value as result.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface StringExpression extends Base {
    
    /**
     * Initialize evaluation.
     * Must be called before evaluation if isCompleted was false after the last
     * call to evaluate().
     */
    public void initEvaluation();
    
    /**
     * Evaluate this expression.
     * 
     * @param isCompleted true if the evaluation is completed. The caller must
     * ensure its initiated to true. If the evaluation is not completed, the
     * expression sets this to false.
     * @return the result of the evaluation
     */
    public String evaluate(@Nonnull AtomicBoolean isCompleted);
    
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
