package jmri.jmrix.loconet.logixng.configureswing;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import jmri.NamedBean;
import jmri.jmrit.logixng.Base;

/**
 * Show a dialog that lets the user discover devices on the LocoNet bus.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public class DiscoverDialog extends JDialog {

    private final JLabel _tableLabel = new JLabel(Bundle.getMessage("Devices") + ":");  // NOI18N
    private final JTable _deviceTable = new JTable();
    
    
    public DiscoverDialog(Frame owner) {
        super(owner, true);
    }
    
    public void initComponents() {
//        jmri.jmrix.loconet.locobuffer.ConnectionConfig;
        
        
        Container contentPanel = getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        JButton _buttonOK = new JButton(Bundle.getMessage("ButtonOK"));  // NOI18N
        panel.add(_buttonOK);
        _buttonOK.addActionListener((ActionEvent e) -> {
/*            
            List<String> errorMessages = new ArrayList<>();
            if (editSwingConfiguratorInterface.validate(errorMessages)) {
                Base object = femaleSocket.getConnectedSocket().getObject();
                ((NamedBean)object).setUserName(_addUserName.getText());
                editSwingConfiguratorInterface.updateObject(femaleSocket.getConnectedSocket().getObject());
                editSwingConfiguratorInterface.dispose();
                editLogixNGFrame.dispose();
                editLogixNGFrame = null;
                for (TreeModelListener l : _femaleSocketTreeModel.listeners) {
                    TreeModelEvent tme = new TreeModelEvent(
                            femaleSocket,
                            path.getPath()
                    );
                    l.treeNodesChanged(tme);
                }
            } else {
                StringBuilder errorMsg = new StringBuilder();
                for (String s : errorMessages) {
                    if (errorMsg.length() > 0) errorMsg.append("<br>");
                    errorMsg.append(s);
                }
                JOptionPane.showMessageDialog(null,
                        jmri.jmrit.logixng.tools.swing.Bundle.getMessage("ValidateErrorMessage", errorMsg),
                        jmri.jmrit.logixng.tools.swing.Bundle.getMessage("ValidateErrorTitle"),
                        JOptionPane.ERROR_MESSAGE);
            }
*/
        });
        
        contentPanel.add(panel);
    }
    
}
