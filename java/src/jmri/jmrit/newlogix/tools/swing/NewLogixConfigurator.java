package jmri.jmrit.newlogix.tools.swing;

import javax.swing.JPanel;

/**
 * Interface for the classes that configures NewLogix expression and action
 * classes.
 * 
 * The constructor of the classes that implements this interface must take
 * exactly one parameter which is the class that is to be configured.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixConfigurator {

    public JPanel getConfigPanel();
    
}
