package jmri.jmrit.newlogix.tools.swing;

import javax.swing.JDialog;
import jmri.jmrit.newlogix.FemaleSocket;

/**
 * Show a dialog that allows the user to add a male socket.
 * 
 * @author Daniel Bergqvist 2018
 */
public class AddMaleSocketDialog extends JDialog {

    private final FemaleSocket _femaleSocket;
    
    AddMaleSocketDialog(FemaleSocket femaleSocket) {
        _femaleSocket = femaleSocket;
    }
    
}
