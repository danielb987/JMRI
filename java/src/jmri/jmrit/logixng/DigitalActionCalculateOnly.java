package jmri.jmrit.logixng;

/**
 * A LogixNG DigitalAction that supports EnableExecution.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalActionCalculateOnly extends Base {

    /**
     * Evaluate the action without execution.
     * <p>
     * Note that enable execution for LogixNG is the equivalent of enable for Logix.
     */
    void evaluateOnly();
    
}
