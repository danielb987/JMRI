package jmri.jmrit.logixng;

/**
 * A LogixNG male AnalogAction socket.
 */
public interface MaleAnalogActionSocket
        extends MaleSocket, AnalogAction {

    /**
     * {@inheritDoc}
     * 
     * 
     */
    @Override
    public void setValue(float value);

}
