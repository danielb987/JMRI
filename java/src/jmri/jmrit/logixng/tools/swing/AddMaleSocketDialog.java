package jmri.jmrit.logixng.tools.swing;

import javax.swing.JDialog;
import jmri.jmrit.logixng.FemaleSocket;

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
    
    public final void init() {
        setTitle(Bundle.getMessage("AddMaleSocketDialogTitle", _femaleSocket.getLongDescription()));
        setModal(true);
    }
    
}
