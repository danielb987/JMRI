package jmri.jmrit.newlogix.tools.swing;

import javax.swing.JDialog;
import jmri.jmrit.newlogix.FemaleSocket;

/**
 * Show a dialog that allows the user to edit the connected socket.
 * 
 * @author Daniel Bergqvist 2018
 */
public class EditMaleSocketDialog extends JDialog {

    private final FemaleSocket _femaleSocket;
    
    EditMaleSocketDialog(FemaleSocket femaleSocket) {
        _femaleSocket = femaleSocket;
    }
    
    public final void init() {
        setTitle(Bundle.getMessage("EditMaleSocketDialogTitle", _femaleSocket.getLongDescription()));
        setModal(true);
    }
    
}
