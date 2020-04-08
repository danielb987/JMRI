package jmri.jmrix.loconet.nodes.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.CheckForNull;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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
import jmri.jmrix.loconet.LnConstants;
import jmri.jmrix.loconet.LocoNetListener;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.Sv2Command;
import jmri.jmrix.loconet.nodes.LnNodeManager;
import jmri.util.ThreadingUtil;

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

    private static final ResourceBundle _rbx =
            ResourceBundle.getBundle("jmri.jmrit.throttle.ThrottleBundle");
    
    private final LnNodeManager _lnNodeManager;
    
    private LocoNetSystemConnectionMemo _memo = null;
    private LnTrafficController _tc;
    
    private final Border _inputBorder = BorderFactory.createEtchedBorder();
    
    private JTextField _locoAddress;
    private JTextField _throttleId;
    private JTextField _throttleId_Hex;
    private JTextField _manufacturer;
    private JTextField _developer;
    private JTextField _product;
    private JComboBox<RosterEntryItem> _rosterEntryItem;
    private JButton _programmerButton;
    
    private int _currentDispatchAddress;
    private DccThrottle _throttle;
    
    private GetLocoState _getLocoState = GetLocoState.NONE;
    private int _requestThrottleAddress;
    
    private int _discoverLocoAddress;
    
    private int _discoverThrottle_DeviceID;
    
    private int _manufacturerID;
    private int _developerID;
    private int _productID;
    
    /**
     * Constructor method
     * 
     * @param memo the LocoNet system connection memo
     */
    public DiscoverThrottleFrame(LocoNetSystemConnectionMemo memo) {
        super();
        _memo = memo;
        
        _lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        
        // addHelpMenu("package.jmri.jmrix.cmri.serial.nodeconfigmanager.NodeConfigManagerFrame", true); // NOI18N duplicate, see initComponents
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponents() {
        // set the frame's initial state
        setTitle(Bundle.getMessage("DiscoverThrottleWindowTitle"));  // NOI18N
        setSize(800, 300);
        
        GridBagConstraints c = new GridBagConstraints();
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        
        JPanel throttlePanel = new JPanel();
        throttlePanel.setLayout(new GridBagLayout());
        Border throttleBorderTitled = BorderFactory.createTitledBorder(_inputBorder,
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
        
        _locoAddress = new JTextField();
        _locoAddress.setEditable(false);
        _locoAddress.setColumns(5);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_locoAddress, c);
        
        JLabel labelThrottleId = new JLabel(Bundle.getMessage("ThrottleId"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelThrottleId, c);
        
        _throttleId = new JTextField();
        _throttleId.setEditable(false);
        _throttleId.setColumns(10);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_throttleId, c);
        
        _throttleId_Hex = new JTextField();
        _throttleId_Hex.setEditable(false);
        _throttleId_Hex.setColumns(10);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_throttleId_Hex, c);
        
        JLabel labelManufacturer = new JLabel(Bundle.getMessage("Manufacturer"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelManufacturer, c);
        
        _manufacturer = new JTextField();
        _manufacturer.setEditable(false);
        _manufacturer.setColumns(30);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_manufacturer, c);
        
        JLabel labelDeveloper = new JLabel(Bundle.getMessage("Developer"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelDeveloper, c);
        
        _developer = new JTextField();
        _developer.setEditable(false);
        _developer.setColumns(30);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_developer, c);
        
        JLabel labelProduct = new JLabel(Bundle.getMessage("Product"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelProduct, c);
        
        _product = new JTextField();
        _product.setEditable(false);
        _product.setColumns(30);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_product, c);
        
        JLabel labelRosterEntry = new JLabel(Bundle.getMessage("RosterEntry"));
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_END;
        throttlePanel.add(labelRosterEntry, c);
        
        _rosterEntryItem = new JComboBox<>();
        _rosterEntryItem.addItem(new RosterEntryItem(null));
        labelRosterEntry.setLabelFor(_rosterEntryItem);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 6;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        throttlePanel.add(_rosterEntryItem, c);
        
        JPanel buttonPanel = new JPanel();
        
        _programmerButton = new JButton(Bundle.getMessage("ButtonOpenProgrammer"));
        _programmerButton.setEnabled(false);
        _programmerButton.addActionListener((ActionEvent e) -> {
            openProgrammer();
        });
        buttonPanel.add(_programmerButton);
        
        JButton doneButton = new JButton(Bundle.getMessage("ButtonDone"));
        doneButton.setToolTipText(Bundle.getMessage("DoneButtonTip"));
        doneButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            dispose();
        });
        buttonPanel.add(doneButton);
        
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        throttlePanel.add(buttonPanel, c);
        
        contentPane.add(throttlePanel);
        
        
        
        
        JPanel dispatchPanel = new JPanel();
        dispatchPanel.setLayout(new GridBagLayout());
        Border dispatchBorderTitled = BorderFactory.createTitledBorder(_inputBorder,
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
            _currentDispatchAddress = 0;
            try {
                _currentDispatchAddress = Integer.parseInt(address.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, Bundle.getMessage("AddressIsInvalid"));
                return;
            }
            boolean requestOK
                    = InstanceManager.throttleManagerInstance().requestThrottle(_currentDispatchAddress, this, true);
            if (!requestOK) {
                JOptionPane.showMessageDialog(this, _rbx.getString("AddressInUse"));
            }
        });
        dispatchPanel.add(dispatchButton, c);
        
        contentPane.add(dispatchPanel);
        
        // Start listen to the LocoNet
        _tc = _memo.getLnTrafficController();
        _tc.addLocoNetListener(~0, this);
    }
    
    /**
     * Open programmer
     */
    public void openProgrammer() {
        
        // Check if we have a loco
        if (_discoverThrottle_DeviceID == 0) return;
        
        DecoderFile decoderFile = _lnNodeManager.getProduct(_manufacturerID, _developerID, _productID);
        
        log.debug(String.format("LnNode: Mfg: %s, Dev: %s, Prod: %s, decoderFile: %s%n",
                _manufacturer.getText(), _developer.getText(), _product.getText(), decoderFile));
        
        String programmerFilename;
        
        if (ProgDefault.getDefaultProgFile() != null) {
            programmerFilename = ProgDefault.getDefaultProgFile();
        } else {
            programmerFilename = ProgDefault.findListOfProgFiles()[0];
        }
        
        DccLocoAddress addr = new DccLocoAddress(_discoverThrottle_DeviceID, false);
        
        RosterEntryItem rosterEntryItem = (RosterEntryItem) _rosterEntryItem.getSelectedItem();
        
        RosterEntry re = rosterEntryItem._entry;
        if (re == null) {
            re = new RosterEntry();
            re.setDccAddress(Integer.toString(_discoverThrottle_DeviceID));
            re.setMfg(decoderFile.getMfg());
            re.setDecoderFamily(decoderFile.getFamily());
            re.setDecoderModel(decoderFile.getModel());
            re.setId(SymbolicProgBundle.getMessage("LabelNewDecoder")); // NOI18N
            // note that we're leaving the filename null
            // add the new roster entry to the in-memory roster
            Roster.getDefault().addEntry(re);
        }
        
        String title = java.text.MessageFormat.format(SymbolicProgBundle.getMessage("FrameServiceProgrammerTitle"),
                new Object[]{_product.getText()});
//        String title = java.text.MessageFormat.format(SymbolicProgBundle.getMessage("FrameServiceProgrammerTitle"),
//                new Object[]{"new decoder"});
//        title = java.text.MessageFormat.format(SymbolicProgBundle.getMessage("FrameServiceProgrammerTitle"),
//                new Object[]{re.getId()});
        PaneOpsProgFrame p = new PaneOpsProgFrame(decoderFile, re,
                title, "programmers" + File.separator + programmerFilename + ".xml",
                _memo.getProgrammerManager().getAddressedProgrammer(addr));
        p.pack();
        p.setVisible(true);
        ThreadingUtil.runOnGUIEventually(p::clickButtonReadAll);
    }
    
    @Override
    public void message(LocoNetMessage msg) {
        
        switch (msg.getOpCode()) {
            case LnConstants.OPC_LOCO_ADR:
                _getLocoState = GetLocoState.REQUEST_SLOT;
                _requestThrottleAddress = msg.getElement(1)*128 + msg.getElement(2);
                break;
                
            case LnConstants.OPC_SL_RD_DATA:
                if (_getLocoState == GetLocoState.REQUEST_SLOT
                        && _requestThrottleAddress == msg.getElement(9)*128 + msg.getElement(4)) {
                    _getLocoState = GetLocoState.READ_SLOT;
                } else {
                    _getLocoState = GetLocoState.NONE;
                }
                break;
                
            case LnConstants.OPC_WR_SL_DATA:
                if (_getLocoState == GetLocoState.READ_SLOT
                        && _requestThrottleAddress == msg.getElement(9)*128 + msg.getElement(4)) {
                    
                    _discoverLocoAddress = _requestThrottleAddress;
                    
//                    _discoverThrottleID = msg.getElement(12)*128 + msg.getElement(11);
                    _discoverThrottle_DeviceID = msg.getElement(12)*256 + msg.getElement(11);
                    
                    _tc.sendLocoNetMessage(LnSv2MessageContents.createSvDiscoverQueryMessage());
                }
                _getLocoState = GetLocoState.NONE;
                break;
                
            default:
                // Do nothing. We don't care about other opcodes.
        }
        
        // Return if the message is not a SV2 message
        if (LnSv2MessageContents.isSupportedSv2Message(msg)) {
            LnSv2MessageContents contents = new LnSv2MessageContents(msg);
            
            Sv2Command command = LnSv2MessageContents.extractMessageType(msg);
            if (command == Sv2Command.SV2_DISCOVER_DEVICE_REPORT) {
                int manufacturerID = contents.getSv2ManufacturerID();
                int developerID = contents.getSv2DeveloperID();
                int productID = contents.getSv2ProductID();

                if (_discoverThrottle_DeviceID == contents.getDestAddr() ) {
                    _manufacturerID = manufacturerID;
                    _developerID = developerID;
                    _productID = productID;
                    DecoderFile decoderFile = _lnNodeManager.getProduct(_manufacturerID, _developerID, _productID);

                    _locoAddress.setText(Integer.toString(_discoverLocoAddress));
                    _throttleId.setText(Integer.toString(_discoverThrottle_DeviceID));
                    _throttleId_Hex.setText(String.format("0x%04X", _discoverThrottle_DeviceID));
                    _manufacturer.setText(_lnNodeManager.getManufacturer(_manufacturerID));
                    _developer.setText(_lnNodeManager.getDeveloper(_developerID));
                    
                    if (decoderFile != null) {
                        _product.setText(decoderFile.getModel());
                        
                        List<RosterEntry> entries =
                                Roster.getDefault().getEntriesMatchingCriteria(
                                        null, null, Integer.toString(_discoverThrottle_DeviceID),
                                        _manufacturer.getText(), decoderFile.getModel(),
                                        decoderFile.getFamily(), null, null);
                        
                        _rosterEntryItem.removeAllItems();
                        _rosterEntryItem.addItem(new RosterEntryItem(null));
                        for (RosterEntry e : entries) {
                            RosterEntryItem entryItem = new RosterEntryItem(e);
                            _rosterEntryItem.addItem(entryItem);
                        }
                    } else {
                        _product.setText("");
                    }
                    
                    _programmerButton.setEnabled(true);
                }
            }
        }
    }
    
    /**
     * Get notification that a throttle has been found as we requested.
     *
     * @param t An instantiation of the DccThrottle with the address requested.
     */
    @Override
    public void notifyThrottleFound(DccThrottle t) {
        log.debug("Asked for " + _currentDispatchAddress + " got " + t.getLocoAddress());
        if (((DccLocoAddress) t.getLocoAddress()).getNumber() != _currentDispatchAddress) {
            log.warn("Not correct address, asked for " + _currentDispatchAddress + " got " + t.getLocoAddress() + ", requesting again...");
            boolean requestOK
                    = InstanceManager.throttleManagerInstance().requestThrottle(_currentDispatchAddress, this, true);
            if (!requestOK) {
                JOptionPane.showMessageDialog(this, _rbx.getString("AddressInUse"));
            }
            return;
        }
        
        _throttle = t;
        _currentDispatchAddress = t.getLocoAddress().getNumber();
        
        if (InstanceManager.throttleManagerInstance().hasDispatchFunction()) {
            ThreadingUtil.runOnGUIEventually(this::dispatchAddress);
        } else {
            ThreadingUtil.runOnGUIEventually(this::releaseAddress);
            JOptionPane.showMessageDialog(this, Bundle.getMessage("NoDispatchFunction"));
        }
    }

    /**
     * Dispatch the current address for use by other throttles
     */
    public void dispatchAddress() {
        final ThrottleListener listener = this;
        if (_throttle != null) {
            int usageCount  = InstanceManager.throttleManagerInstance().getThrottleUsageCount(_throttle.getLocoAddress()) - 1;
//            int usageCount  = InstanceManager.throttleManagerInstance().getThrottleUsageCount(throttle.getLocoAddress());

            if ( usageCount != 0 ) {
                ThreadingUtil.runOnGUIEventually(this::releaseAddress);
                JOptionPane.showMessageDialog(this, Bundle.getMessage("CannotDispatch", usageCount));
                return;
            }
//            jmri.util.ThreadingUtil.runOnGUIEventually(() -> {
                InstanceManager.throttleManagerInstance().dispatchThrottle(_throttle, listener);
//            });
//            InstanceManager.throttleManagerInstance().dispatchThrottle(throttle, this);
        }
    }

    /**
     * Release the current address.
     */
    public void releaseAddress() {
        InstanceManager.throttleManagerInstance().releaseThrottle(_throttle, this);
    }

    @Override
    public void notifyFailedThrottleRequest(LocoAddress address, String reason) {
        javax.swing.JOptionPane.showMessageDialog(null, reason, _rbx.getString("FailedSetupRequestTitle"), javax.swing.JOptionPane.WARNING_MESSAGE);
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
    
    
    private static class RosterEntryItem {
        
        private final RosterEntry _entry;
        
        public RosterEntryItem(@CheckForNull RosterEntry entry) {
            _entry = entry;
        }
        
        @Override
        public String toString() {
            if (_entry == null) return Bundle.getMessage("NewRosterEntry");
            return _entry.getId();
        }
    }
    
    private enum GetLocoState {
        NONE,
        REQUEST_SLOT,
        READ_SLOT,
        WRITE_SLOT,
    }
    
    
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DiscoverThrottleFrame.class);
    
}


// NOTE: If a card with manufacturerID, productID and serialNo has a new address,
// warn about the existing card. Maybe red color?
