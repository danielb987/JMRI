package jmri.jmrix.loconet.nodes.swing;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
// import jmri.jmrix.cmri.CMRISystemConnectionMemo;
// import jmri.jmrix.cmri.serial.nodeconfigmanager.NodeConfigManagerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configure nodes on the LocoNet
 */
public class ConfigNodes {

    public JButton getButton(final LocoNetSystemConnectionMemo lnMemo) {
        JButton b = new JButton(Bundle.getMessage("ConfigureNodesButton"));
        b.addActionListener((ActionEvent e) -> {
            NodeConfigManagerFrame f = new NodeConfigManagerFrame(lnMemo);
            try {
                f.initComponents();
            } catch (Exception ex) {
                log.error("NodeConfigManagerAction Exception-C2: "+ex.toString());
            }
            f.setLocation(20,40);
            f.setVisible(true);
        });
        return b;
    }
    
    private final static Logger log = LoggerFactory.getLogger(ConfigNodes.class);
}
