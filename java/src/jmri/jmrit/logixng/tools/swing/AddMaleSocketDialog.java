package jmri.jmrit.logixng.tools.swing;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    
    public final void init(int x, int y, Component c) {
        setTitle(Bundle.getMessage("AddMaleSocketDialogTitle", _femaleSocket.getLongDescription()));
        setModal(true);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
//        panel.add(new JLabel(Bundle.getMessage("InitExMessageListHeader")));
        panel.add(new JLabel("InitExMessageListHeader"));
        contentPanel.add(panel);
        
        
        
        
        setContentPane(contentPanel);
        pack();
        System.out.format("Component: %s%n", c.getClass().getName());
//        System.out.format("%d, %d%n", c.getLocationOnScreen().x, c.getLocationOnScreen().y);
        this.setLocationRelativeTo(c);
//        this.setLocation(x, y);
//        // Center dialog on screen
//        setLocationRelativeTo(null);
        setVisible(true);
    }
    
}
