package jmri.jmrit.logixng;

/**
 * A LogixNG male AnalogExpression socket.
 */
public interface MaleAnalogExpressionSocket
        extends MaleSocket, AnalogExpression {

    /**
     * {@inheritDoc}
     * <P>
     * This method must ensure that the result is not a Float.NaN, negative
     * infinity or positive infinity. If that is the case, it must throw an
     * IllegalArgumentException before checking if an error has occured.
     * <P>
     * If an error occurs and are handled, the method returns the value 0.0f.
     */
    @Override
    public float evaluate();

}
