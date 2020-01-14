package jmri.jmrix.loconet.nodes.swing;

import static jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.isSupportedSv2Message;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import jmri.jmrix.loconet.LocoNetListener;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.Sv2Command;
import jmri.jmrix.loconet.nodes.LnNode;

/**
 * Frame for discover nodes on the LocoNet.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class DiscoverNodesFrame extends jmri.util.JmriJFrame implements LocoNetListener {

    private LocoNetSystemConnectionMemo _memo = null;
    private LnTrafficController _tc;
    private List<LnNode> lnNode = new ArrayList<>();
    
    /**
     * Constructor method
     * 
     * @param memo the LocoNet system connection memo
     */
    public DiscoverNodesFrame(LocoNetSystemConnectionMemo memo) {
        super();
        _memo = memo;
        
        // addHelpMenu("package.jmri.jmrix.cmri.serial.nodeconfigmanager.NodeConfigManagerFrame", true); // NOI18N duplicate, see initComponents
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponents() {
        // set the frame's initial state
        setTitle(Bundle.getMessage("WindowTitle") + Bundle.getMessage("WindowConnectionMemo")+_memo.getUserName());  // NOI18N
        setSize(500, 150);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        
        
        _tc = _memo.getLnTrafficController();
        _tc.addLocoNetListener(~0, this);
        
        _tc.sendLocoNetMessage(LnSv2MessageContents.createSvDiscoverQueryMessage());
    }
    
    // jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.createSvDiscoverQueryMessage();

    @Override
    public void message(LocoNetMessage msg) {
        // Return if the message is not a SV2 message
        if (!isSupportedSv2Message(msg)) return;
        LnSv2MessageContents contents = new LnSv2MessageContents(msg);
        
        Sv2Command command = LnSv2MessageContents.extractMessageType(msg);
        
        if (command == Sv2Command.SV2_DISCOVER_DEVICE_REPORT) {
            LnNode node = new LnNode(contents, _tc);
            jmri.util.ThreadingUtil.runOnGUIEventually(() -> {System.out.println(node.toString()); });
        }
    }
}


// NOTE: If a card with manufacturerID, productID and serialNo has a new address,
// warn about the existing card. Maybe red color?
