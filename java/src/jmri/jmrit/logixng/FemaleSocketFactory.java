package jmri.jmrit.logixng;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * A factory for creation of FemaleSockets.
 */
public interface FemaleSocketFactory {
    
    /**
     * Create a FemaleSocket.
     */
    public FemaleSocket create();
    
    /**
     * Get a named bean by system name.
     * The bean must be wrapped in a male socket.
     */
    @CheckReturnValue
    @CheckForNull
    public MaleSocket getBeanBySystemName(@Nonnull String systemName);
    
}
