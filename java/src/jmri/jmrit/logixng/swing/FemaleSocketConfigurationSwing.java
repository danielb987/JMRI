package jmri.jmrit.logixng.swing;

import java.util.List;

import javax.annotation.Nonnull;
import javax.swing.JPanel;

import jmri.jmrit.logixng.FemaleSocketConfiguration;

/**
 * The parent interface for configuring FemaleSocketConfigurations with Swing.
 * @author Daniel Bergqvist (C) 2023
 */
public interface FemaleSocketConfigurationSwing {

    /**
     * Get a configuration panel for an object.
     * This method initializes the panel with the configuration of the object.
     *
     * @param config the configuration for which to return a configuration panel
     * @param buttonPanel panel with the buttons
     * @return a panel that configures this object
     */
    public JPanel getConfigPanel(@Nonnull FemaleSocketConfiguration config, JPanel buttonPanel) throws IllegalArgumentException;

    /**
     * Validate the form.
     * <P>
     * The parameter errorMessage is used to give the error message in case of
     * an error. If there are errors, the error messages is added to the list
     * errorMessage.
     *
     * @param errorMessages the error messages in case of an error
     * @return true if data in the form is valid, false otherwise
     */
    public boolean validate(@Nonnull List<String> errorMessages);

}
