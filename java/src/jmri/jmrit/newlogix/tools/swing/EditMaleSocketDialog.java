package jmri.jmrit.newlogix.tools.swing;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    
    public final void init(int x, int y, Component c) {
        setTitle(Bundle.getMessage("EditMaleSocketDialogTitle", _femaleSocket.getLongDescription()));
        setModal(true);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
//        panel.add(new JLabel(Bundle.getMessage("InitExMessageListHeader")));
        panel.add(new JLabel("InitExMessageListHeader"));
        contentPanel.add(panel);
        
        
        
        
        setContentPane(contentPanel);
        pack();
        
        this.setLocationRelativeTo(c);
//        this.setLocation(x, y);
//        // Center dialog on screen
//        setLocationRelativeTo(null);
        setVisible(true);
    }
    
}
