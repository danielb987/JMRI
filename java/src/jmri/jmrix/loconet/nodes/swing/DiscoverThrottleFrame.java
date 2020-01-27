package jmri.jmrix.loconet.nodes.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import jmri.DccLocoAddress;
import jmri.DccThrottle;
import jmri.InstanceManager;
import jmri.LocoAddress;
import jmri.ThrottleListener;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrit.roster.Roster;
import jmri.jmrit.roster.RosterEntry;
import jmri.jmrit.symbolicprog.ProgDefault;
import jmri.jmrit.symbolicprog.SymbolicProgBundle;
import jmri.jmrit.symbolicprog.tabbedframe.PaneOpsProgFrame;
import jmri.jmrit.throttle.AddressListener;
import jmri.jmrit.throttle.ThrottleFrameManager;
import jmri.jmrix.loconet.LocoNetListener;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.Sv2Command;
import jmri.jmrix.loconet.nodes.LnNode;
import jmri.jmrix.loconet.nodes.LnNodeManager;
import jmri.util.ThreadingUtil;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Frame for discovering throttle on the LocoNet.
 * This is useful when programming FREDi throttles.
 * <p>
 * Note: This code uses the decoder definitions in the xml/decoders folder
 * to find manufacturer and developer. If a decoder has a manufacturer
 * of "Public-domain and DIY", it's important that that decoder, in its
 * family definition, has a developerID.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class DiscoverThrottleFrame extends jmri.util.JmriJFrame implements LocoNetListener, ThrottleListener {

    private static final ResourceBundle rbx = ResourceBundle.getBundle("jmri.jmrit.throttle.ThrottleBundle");
    
    private LocoNetSystemConnectionMemo _memo = null;
    private LnTrafficController _tc;
    private final List<LnNode> lnNodes = new ArrayList<>();
    
    protected JPanel nodeTablePanel = null;
    protected Border inputBorder = BorderFactory.createEtchedBorder();
    
    protected JTable nodeTable = null;
    
    JButton throttleIdButton = new JButton(Bundle.getMessage("ButtonThrottleId"));
    JButton doneButton = new JButton(Bundle.getMessage("ButtonDone"));
    
    private int currentAddress;
    private DccThrottle throttle;
    
    /**
     * Constructor method
     * 
     * @param memo the LocoNet system connection memo
     */
    public DiscoverThrottleFrame(LocoNetSystemConnectionMemo memo) {
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
//        setSize(500, 150);
        setSize(700, 300);
        
        GridBagConstraints c = new GridBagConstraints();
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        
        JPanel throttlePanel = new JPanel();
//        throttlePanel.setLayout(new BoxLayout(nodeTablePanel, BoxLayout.Y_AXIS));
//        throttlePanel.setLayout(new GridLayout(5, 3));
        throttlePanel.setLayout(new GridBagLayout());
        Border throttleBorderTitled = BorderFactory.createTitledBorder(inputBorder,
                Bundle.getMessage("TitleThrottle"),
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        throttlePanel.setBorder(throttleBorderTitled);
        
        c.insets = new Insets(2,2,2,2);
        
        JLabel infoThrottle1 = new JLabel(Bundle.getMessage("ThrottleInfoMessage1"));
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(infoThrottle1, c);
        
        JLabel labelLocoAddress = new JLabel(Bundle.getMessage("LocoAddress"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelLocoAddress, c);
        
        JTextField locoAddress = new JTextField("123");
        locoAddress.setEditable(false);
        locoAddress.setColumns(5);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(locoAddress, c);
        
        JLabel labelThrottleId = new JLabel(Bundle.getMessage("ThrottleId"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelThrottleId, c);
        
        JTextField throttleId = new JTextField("456");
        throttleId.setEditable(false);
        throttleId.setColumns(10);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(throttleId, c);
        
        JLabel labelManufacturer = new JLabel(Bundle.getMessage("Manufacturer"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelManufacturer, c);
        
        JTextField manufacturer = new JTextField("789");
        manufacturer.setEditable(false);
        manufacturer.setColumns(30);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(manufacturer, c);
        
        JLabel labelDeveloper = new JLabel(Bundle.getMessage("Developer"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelDeveloper, c);
        
        JTextField developer = new JTextField("Abc");
        developer.setEditable(false);
        developer.setColumns(30);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(developer, c);
        
        JLabel labelProduct = new JLabel(Bundle.getMessage("Product"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelProduct, c);
        
        JTextField product = new JTextField("Def");
        product.setEditable(false);
        product.setColumns(30);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(product, c);
        
        JButton programmerButton = new JButton(Bundle.getMessage("ButtonOpenProgrammer"));
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        throttlePanel.add(programmerButton, c);
        
        contentPane.add(throttlePanel);
        
        
        
        
        JPanel dispatchPanel = new JPanel();
        dispatchPanel.setLayout(new GridBagLayout());
        Border dispatchBorderTitled = BorderFactory.createTitledBorder(inputBorder,
                Bundle.getMessage("TitleDispatch"),
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        dispatchPanel.setBorder(dispatchBorderTitled);
        
        c.insets = new Insets(2,2,2,2);
        
        JLabel infoDispatch1 = new JLabel(Bundle.getMessage("DispatchInfoMessage1"));
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.anchor = java.awt.GridBagConstraints.WEST;
        dispatchPanel.add(infoDispatch1, c);
        
        JLabel infoDispatch2 = new JLabel(Bundle.getMessage("DispatchInfoMessage2"));
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.anchor = java.awt.GridBagConstraints.WEST;
        dispatchPanel.add(infoDispatch2, c);
        
        JLabel labelAddress = new JLabel(Bundle.getMessage("LocoAddress"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.anchor = java.awt.GridBagConstraints.EAST;
        dispatchPanel.add(labelAddress, c);
        
//        JTextField address = new JTextField("Daniel LocoAddress");
        JTextField address = new JTextField();
        address.setColumns(5);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.WEST;
        dispatchPanel.add(address, c);
        
        JButton dispatchButton = new JButton(Bundle.getMessage("ButtonDispatch"));
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        dispatchButton.addActionListener((java.awt.event.ActionEvent e) -> {
            currentAddress = 0;
            try {
                currentAddress = Integer.parseInt(address.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, rbx.getString("AddressIsInvalid"));
                return;
            }
            boolean requestOK
                    = InstanceManager.throttleManagerInstance().requestThrottle(currentAddress, this, true);
            if (!requestOK) {
                JOptionPane.showMessageDialog(this, rbx.getString("AddressInUse"));
            }
        });
        dispatchPanel.add(dispatchButton, c);
        
        contentPane.add(dispatchPanel);
        
        
        
/*        
        _tc = _memo.getLnTrafficController();
        _tc.addLocoNetListener(~0, this);
        
        _tc.sendLocoNetMessage(LnSv2MessageContents.createSvDiscoverQueryMessage());
*/        
    }
    
    /**
     * Handle the done button click.
     */
    public void throttleIdButtonActionPerformed() {
        DiscoverThrottleFrame f = new DiscoverThrottleFrame(_memo);
        try {
            f.initComponents();
        } catch (Exception ex) {
            log.error("NodeConfigManagerAction Exception-C2: "+ex.toString());
        }
        f.setLocation(20,40);
        f.setVisible(true);
    }
    
    /**
     * Handle the done button click.
     */
    public void doneButtonActionPerformed() {
//        changedNode = false;
        setVisible(false);
        dispose();
    }
    
    @Override
    public void message(LocoNetMessage msg) {
        // Return if the message is not a SV2 message
        if (!LnSv2MessageContents.isSupportedSv2Message(msg)) return;
        LnSv2MessageContents contents = new LnSv2MessageContents(msg);
        
        Sv2Command command = LnSv2MessageContents.extractMessageType(msg);
        
        if (command == Sv2Command.SV2_DISCOVER_DEVICE_REPORT) {
            LnNode node = new LnNode(contents, _tc);
//DANIEL            addNode(node);
        }
    }
    
    /*.*
     * Get the selected node address from the node table.
     *./
    private LnNode getSelectedNode() {
        int addr = (Integer)nodeTable.getValueAt(
                nodeTable.getSelectedRow(), NodeTableModel.ADDRESS_COLUMN);
        
        for (LnNode node : lnNodes) {
            if (node.getAddress() == addr) return node;
        }
        // If here, the node is not found
        return null;
    }
    
    /**
     * Handle the open programmer button click
     */
    public void openProgrammerActionSelected() {
        
//        LnNode selectedNode = getSelectedNode();
        LnNode selectedNode = null;
        log.debug(String.format("LnNode: Mfg: %s, Dev: %s, Prod: %s, decoderFile: %s%n",
                selectedNode.getManufacturer(), selectedNode.getDeveloper(), selectedNode.getProduct(), selectedNode.getDecoderFile()));
        
        String programmerFilename;
        
        if (ProgDefault.getDefaultProgFile() != null) {
            programmerFilename = ProgDefault.getDefaultProgFile();
        } else {
            programmerFilename = ProgDefault.findListOfProgFiles()[0];
        }
        
//        DccLocoAddress addr = new DccLocoAddress(selectedNode.getAddress(), true);
        DccLocoAddress addr = new DccLocoAddress(selectedNode.getAddress(), false);
        
        RosterEntry re = new RosterEntry();
//        re.setLongAddress(true);
//        System.out.format("DccAddress: %d%n", selectedNode.getAddress());
        re.setDccAddress(Integer.toString(selectedNode.getAddress()));
        re.setMfg(selectedNode.getDecoderFile().getMfg());
        re.setDecoderFamily(selectedNode.getDecoderFile().getFamily());
//        re.setModel(selectedNode.getDecoderFile().getModel());
        re.setDecoderModel(selectedNode.getDecoderFile().getModel());
        re.setId(SymbolicProgBundle.getMessage("LabelNewDecoder")); // NOI18N
        // note that we're leaving the filename null
        // add the new roster entry to the in-memory roster
        Roster.getDefault().addEntry(re);
        
        String title = java.text.MessageFormat.format(SymbolicProgBundle.getMessage("FrameServiceProgrammerTitle"),
                new Object[]{selectedNode.getProduct()});
//        String title = java.text.MessageFormat.format(SymbolicProgBundle.getMessage("FrameServiceProgrammerTitle"),
//                new Object[]{"new decoder"});
//        title = java.text.MessageFormat.format(SymbolicProgBundle.getMessage("FrameServiceProgrammerTitle"),
//                new Object[]{re.getId()});
        PaneOpsProgFrame p = new PaneOpsProgFrame(selectedNode.getDecoderFile(), re,
                title, "programmers" + File.separator + programmerFilename + ".xml",
                _memo.getProgrammerManager().getAddressedProgrammer(addr));
        p.pack();
        p.setVisible(true);
        ThreadingUtil.runOnGUIEventually(p::clickButtonReadAll);
        
        
        
/*
        NodeConfigManagerFrame f = new NodeConfigManagerFrame(_memo);
        f.nodeTableModel = nodeTableModel;
        f.selectedTableRow = nodeTable.convertRowIndexToModel(nodeTable.getSelectedRow());
        try {
            f.initNodeConfigWindow();
            f.deleteNodeButtonActionPerformed(selectedNodeAddr);
        } catch (Exception ex) {
            log.info("deleteActionSelected", ex);

        }
        f.setLocation(200, 200);
        f.buttonSet_DELETE();
        f.setVisible(true);
*/        
    }
    
    
    /**
     * Get notification that a throttle has been found as we requested.
     *
     * @param t An instantiation of the DccThrottle with the address requested.
     */
    @Override
    public void notifyThrottleFound(DccThrottle t) {
        log.debug("Asked for " + currentAddress + " got " + t.getLocoAddress());
        if (((DccLocoAddress) t.getLocoAddress()).getNumber() != currentAddress) {
            log.warn("Not correct address, asked for " + currentAddress + " got " + t.getLocoAddress() + ", requesting again...");
            boolean requestOK
                    = InstanceManager.throttleManagerInstance().requestThrottle(currentAddress, this, true);
            if (!requestOK) {
                JOptionPane.showMessageDialog(this, rbx.getString("AddressInUse"));
//                JOptionPane.showMessageDialog(mainPanel, rbx.getMessage("AddressInUse"));
            }
            return;
        }

        throttle = t;
        currentAddress = t.getLocoAddress().getNumber();
//DANIEL        throttle.addPropertyChangeListener(this);

        if (InstanceManager.throttleManagerInstance().hasDispatchFunction()) {
            dispatchAddress();
        } else {
            JOptionPane.showMessageDialog(this, Bundle.getMessage("NoDispatchFunction"));
            releaseAddress();
        }
    }

    /**
     * Dispatch the current address for use by other throttles
     */
    public void dispatchAddress() {
        final ThrottleListener listener = this;
        if (throttle != null) {
            int usageCount  = InstanceManager.throttleManagerInstance().getThrottleUsageCount(throttle.getLocoAddress()) - 1;
//            int usageCount  = InstanceManager.throttleManagerInstance().getThrottleUsageCount(throttle.getLocoAddress());

            if ( usageCount != 0 ) {
                JOptionPane.showMessageDialog(this, Bundle.getMessage("CannotDispatch", usageCount));
                return;
            }
            jmri.util.ThreadingUtil.runOnGUIEventually(() -> {
                InstanceManager.throttleManagerInstance().dispatchThrottle(throttle, listener);
            });
//            InstanceManager.throttleManagerInstance().dispatchThrottle(throttle, this);
        }
    }

    /**
     * Release the current address.
     */
    public void releaseAddress() {
        InstanceManager.throttleManagerInstance().releaseThrottle(throttle, this);
    }

    @Override
    public void notifyFailedThrottleRequest(LocoAddress address, String reason) {
        javax.swing.JOptionPane.showMessageDialog(null, reason, rbx.getString("FailedSetupRequestTitle"), javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * {@inheritDoc}
     * @deprecated since 4.15.7; use #notifyDecisionRequired
     */
    @Override
    @Deprecated
    public void notifyStealThrottleRequired(jmri.LocoAddress address) {
        notifyDecisionRequired(address, ThrottleListener.DecisionType.STEAL);
    }

    /**
    * A decision is required for Throttle creation to continue.
    * <p>
    * Steal / Cancel, Share / Cancel, or Steal / Share Cancel
    */
    @Override
    public void notifyDecisionRequired(LocoAddress address, ThrottleListener.DecisionType question) {
        if ( question == ThrottleListener.DecisionType.STEAL
                || question == ThrottleListener.DecisionType.STEAL_OR_SHARE ){
            InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.STEAL );
        } else {
            InstanceManager.throttleManagerInstance().cancelThrottleRequest(address, this);
        }
/*        
        if ( question == ThrottleListener.DecisionType.STEAL ){
            if ( InstanceManager.getDefault(ThrottleFrameManager.class).getThrottlesPreferences().isSilentSteal() ){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.STEAL );
                return;
            }
            jmri.util.ThreadingUtil.runOnGUI(() -> {
                if ( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog(
                    this, rbx.getMessage("StealQuestionText",address.toString()), 
                    rbx.getMessage("StealRequestTitle"), javax.swing.JOptionPane.YES_NO_OPTION)) {
                        InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.STEAL );
                } else {
                    InstanceManager.throttleManagerInstance().cancelThrottleRequest(address, this);
                }
            });
        }
        else if ( question == ThrottleListener.DecisionType.SHARE ){
            if ( InstanceManager.getDefault(ThrottleFrameManager.class).getThrottlesPreferences().isSilentShare() ){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.SHARE );
                return;
            }
            jmri.util.ThreadingUtil.runOnGUI(() -> {
                if ( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog(
                    this, rbx.getMessage("ShareQuestionText",address.toString()), 
                    rbx.getMessage("ShareRequestTitle"), javax.swing.JOptionPane.YES_NO_OPTION)) {
                        InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.SHARE );
                } else {
                    InstanceManager.throttleManagerInstance().cancelThrottleRequest(address, this);
                }
            });
        }
        else if ( question == ThrottleListener.DecisionType.STEAL_OR_SHARE ){
            
            if ( InstanceManager.getDefault(ThrottleFrameManager.class).getThrottlesPreferences().isSilentSteal() ){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.STEAL );
                return;
            }
            if ( InstanceManager.getDefault(ThrottleFrameManager.class).getThrottlesPreferences().isSilentShare() ){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.SHARE );
                return;
            }
            
            String[] options = new String[] {rbx.getMessage("StealButton"), 
                rbx.getMessage("ShareButton"), rbx.getMessage("CancelButton")};
            jmri.util.ThreadingUtil.runOnGUI(() -> {
                int response = javax.swing.JOptionPane.showOptionDialog(
                    this, rbx.getMessage("StealShareQuestionText",address.toString()),
                    rbx.getMessage("StealShareRequestTitle"),
                    javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE,
                    null, options, options[1]);
            
                if (response == 0){
                    log.debug("steal clicked");
                    InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.STEAL );
                } else if ( response == 1 ) {
                    log.debug("share clicked");
                    InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, ThrottleListener.DecisionType.SHARE );
                }
                else {
                    log.debug("cancel clicked");
                    InstanceManager.throttleManagerInstance().cancelThrottleRequest(address, this);
                }
            });
        }
*/
    }
    
    
    
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DiscoverThrottleFrame.class);
    
}


// NOTE: If a card with manufacturerID, productID and serialNo has a new address,
// warn about the existing card. Maybe red color?
