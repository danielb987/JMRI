package jmri.jmrit.logixng.tools.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.util.JmriJFrame;

/**
 * Show a dialog that allows the user to add a male socket.
 * 
 * @author Daniel Bergqvist 2018
 */
public class AddMaleSocketDialog extends JDialog {

    private final FemaleSocket _femaleSocket;
    
    // Add LogixNG Variables
    JmriJFrame addLogixNGFrame = null;
    JTextField _systemName = new JTextField(20);
    JTextField _addUserName = new JTextField(20);
    JCheckBox _autoSystemName = new JCheckBox(Bundle.getMessage("LabelAutoSysName"));   // NOI18N
//    JCheckBox _autoSystemName = new JCheckBox("LabelAutoSysName");   // NOI18N
    JLabel _sysNameLabel = new JLabel(Bundle.getMessage("SystemName") + ":");  // NOI18N
//    JLabel _sysNameLabel = new JLabel("BeanNameLogixNG" + " " + "ColumnSystemName" + ":");  // NOI18N
    JLabel _userNameLabel = new JLabel(Bundle.getMessage("UserName") + ":");   // NOI18N
//    JLabel _userNameLabel = new JLabel("BeanNameLogixNG" + " " + "ColumnUserName" + ":");   // NOI18N
//    String systemNameAuto = this.getClass().getName() + ".AutoSystemName";      // NOI18N
    JButton create;
    
    
    AddMaleSocketDialog(FemaleSocket femaleSocket) {
        _femaleSocket = femaleSocket;
    }
    
    public final void init(int x, int y, Component component) {
        setTitle(Bundle.getMessage("AddMaleSocketDialogTitle", _femaleSocket.getLongDescription()));
        setModal(true);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//        JPanel panel = new JPanel();
//        panel.add(new JLabel(Bundle.getMessage("InitExMessageListHeader")));
//        panel.add(new JLabel("InitExMessageListHeader"));
//        contentPanel.add(panel);
        
        
        
        
        
//        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel p;
        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.EAST;
        p.add(_sysNameLabel, c);
        c.gridy = 1;
        p.add(_userNameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
        p.add(_systemName, c);
        c.gridy = 1;
        p.add(_addUserName, c);
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
        c.gridy = 0;
        p.add(_autoSystemName, c);
        _addUserName.setToolTipText(Bundle.getMessage("UserNameHint"));    // NOI18N
//        _addUserName.setToolTipText("LogixNGUserNameHint");    // NOI18N
        _systemName.setToolTipText(Bundle.getMessage("SystemNameHint", _femaleSocket.getExampleSystemName()));   // NOI18N
//        _systemName.setToolTipText("LogixNGSystemNameHint");   // NOI18N
        contentPanel.add(p);
        // set up message
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        JPanel panel31 = new JPanel();
        panel31.setLayout(new FlowLayout());
//        JLabel message1 = new JLabel(jmri.jmrit.beantable.Bundle.getMessage(messageId + "1"));  // NOI18N
        JLabel message1 = new JLabel("aaa1");  // NOI18N
        panel31.add(message1);
        JPanel panel32 = new JPanel();
//        JLabel message2 = new JLabel(jmri.jmrit.beantable.Bundle.getMessage(messageId + "2"));  // NOI18N
        JLabel message2 = new JLabel("bbb2");  // NOI18N
        panel32.add(message2);
        panel3.add(panel31);
        panel3.add(panel32);
        contentPanel.add(panel3);

        // set up create and cancel buttons
        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout());
        // Cancel
        JButton cancel = new JButton(Bundle.getMessage("ButtonCancel"));    // NOI18N
        panel5.add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAddPressed(e);
            }
        });
//        cancel.setToolTipText(Bundle.getMessage("CancelLogixButtonHint"));      // NOI18N
        cancel.setToolTipText("CancelLogixButtonHint");      // NOI18N
/*
        addLogixNGFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelAddPressed(null);
            }
        });
*/
        contentPanel.add(panel5);

        _autoSystemName.addItemListener(
                new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                autoSystemName();
            }
        });
//        return panel5;
        
//        if (1==1) return;
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        setContentPane(contentPanel);
        pack();
        System.out.format("Component: %s%n", c.getClass().getName());
//        System.out.format("%d, %d%n", c.getLocationOnScreen().x, c.getLocationOnScreen().y);
        this.setLocationRelativeTo(component);
//        this.setLocation(x, y);
//        // Center dialog on screen
//        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Enable/disable fields for data entry when user selects to have system
     * name automatically generated.
     */
    void autoSystemName() {
        if (_autoSystemName.isSelected()) {
            _systemName.setEnabled(false);
            _sysNameLabel.setEnabled(false);
        } else {
            _systemName.setEnabled(true);
            _sysNameLabel.setEnabled(true);
        }
    }

    /**
     * Respond to the Cancel button in Add LogixNG window.
     * <p>
     * Note: Also get there if the user closes the Add LogixNG window.
     *
     * @param e The event heard
     */
    void cancelAddPressed(ActionEvent e) {
        addLogixNGFrame.setVisible(false);
        addLogixNGFrame.dispose();
        addLogixNGFrame = null;
//        _inCopyMode = false;
//        if (f != null) {
//            f.setVisible(true);
//        }
    }

}
