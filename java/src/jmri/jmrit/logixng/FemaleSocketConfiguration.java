package jmri.jmrit.logixng;

import java.util.Locale;

/**
 * An optional configuration of a FemaleSocket
 * @author Daniel Bergqvist (C) 2023
 */
public interface FemaleSocketConfiguration {

    /**
     * Copy the configuration from a source configuration.
     * This and the source configuration must be of the same type.
     * @param config the configuration
     */
    void copyConfiguration(FemaleSocketConfiguration config);

    /**
     * Get a description of this configuration.
     * @return a description
     */
    default String getDescription() {
        return getDescription(Locale.getDefault());
    }

    /**
     * Get a description of this configuration.
     * @param locale The locale to be used
     * @return a description
     */
    String getDescription(Locale locale);

}
