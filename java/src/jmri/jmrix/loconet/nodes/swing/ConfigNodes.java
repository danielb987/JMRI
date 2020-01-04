package jmri.jmrix.loconet.nodes.swing;

import javax.swing.JButton;

/**
 * Configure nodes on the LocoNet
 */
public class ConfigNodes {

    public JButton getButton() {
        JButton b = new JButton("Daniel2");
//        JButton b = new JButton(Bundle.getMessage("ConfigureNodesTitle"));
//        b.addActionListener(new NodeConfigManagerAction((CMRISystemConnectionMemo)adapter.getSystemConnectionMemo()));
        return b;
    }
    
}
