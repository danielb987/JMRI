package jmri.jmrit.logixng;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public interface FemaleGenericExpressionSocket
        extends FemaleSocket {

    public enum SocketType {
        DIGITAL("SocketTypeDigital"),
        ANALOG("SocketTypeDigital"),
        STRING("SocketTypeDigital"),
        GENERIC("SocketTypeDigital");
        
        private final String _bundleName;
        
        private SocketType(String bundleName) {
            _bundleName = bundleName;
        }
        
        @Override
        public String toString() {
            return Bundle.getMessage(_bundleName);
        }
        
    }
    
    /**
     * Set the type of the socket.
     * 
     * @param socketType the type of socket.
     * @throws SocketAlreadyConnectedException if the socket is already
     * connected and if the new type doesn't match the currently connected
     * socket.
     */
    public void setSocketType(SocketType socketType)
            throws SocketAlreadyConnectedException;
    
    /**
     * Get the type of the socket.
     * 
     * @return the type of socket
     */
    public SocketType getSocketType();
    
    /**
     * Initialize evaluation.
     * Must be called before evaluation if isCompleted was false after the last
     * call to evaluateGeneric().
     */
    public void initEvaluation();
    
    /**
     * Evaluate this expression.
     * <P>
     * The return value of the evaluation is converted to a boolean if necessary.
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
    public boolean evaluateBoolean(@Nonnull AtomicBoolean isCompleted);
    
    /**
     * Evaluate this expression.
     * <P>
     * The return value of the evaluation is converted to a double if necessary.
     * 
     * @param isCompleted true if the evaluation is completed. The caller must
     * ensure its initiated to true. If the evaluation is not completed, the
     * expression sets this to false.
     * @return the result of the evaluation. The male socket that holds this
     * expression throws an exception if this value is a Double.NaN or an
     * infinite number.
     */
    public double evaluateDouble(@Nonnull AtomicBoolean isCompleted);
    
    /**
     * Evaluate this expression.
     * <P>
     * The return value of the evaluation is converted to a String if necessary.
     * 
     * @param isCompleted true if the evaluation is completed. The caller must
     * ensure its initiated to true. If the evaluation is not completed, the
     * expression sets this to false.
     * @return the result of the evaluation
     */
    public String evaluateString(@Nonnull AtomicBoolean isCompleted);
    
    /**
     * Evaluate this expression.
     * <P>
     * This method validates the expression without doing any convertation of
     * the return value.
     * <P>
     * The parameter isCompleted is used if the expression should be evaluated
     * more than once. For example, the Count expression is not completed until
     * its child expression has been true and false a number of times.
     * 
     * @param isCompleted true if the evaluation is completed. The caller must
     * ensure its initiated to true. If the evaluation is not completed, the
     * expression sets this to false.
     * @return the result of the evaluation. This is of the same class as
     * parentValue.
     */
    @CheckForNull
    public Object evaluateGeneric(@Nonnull AtomicBoolean isCompleted);
    
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
