package jmri.jmrix.loconet.nodes.swing;

import static jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.isSupportedSv2Message;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import jmri.InstanceManager;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrit.decoderdefn.DecoderIndexFile;
import jmri.jmrit.roster.Roster;
import jmri.jmrit.roster.RosterEntry;
import jmri.jmrix.loconet.LocoNetListener;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents.Sv2Command;
import jmri.jmrix.loconet.nodes.LnNode;
import jmri.jmrix.loconet.nodes.LnNodeManager;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Frame for discover nodes on the LocoNet.
 * <p>
 * Note: This code uses the decoder definitions in the xml/decoders folder
 * to find manufacturer and developer. If a decoder has a manufacturer
 * of "Public-domain and DIY", it's important that that decoder, in its
 * family definition, has a developerID.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class DiscoverNodesFrame extends jmri.util.JmriJFrame implements LocoNetListener {

    private LocoNetSystemConnectionMemo _memo = null;
    private LnTrafficController _tc;
    private List<DecoderFile> _decoders;
    private Map<Integer, List<DecoderFile>> decoderFileMap = new HashMap<>();
    private List<LnNode> lnNodes = new ArrayList<>();
    
    protected JPanel nodeTablePanel = null;
    protected Border inputBorder = BorderFactory.createEtchedBorder();
    
    protected NodeTableModel nodeTableModel = null;
    protected JTable nodeTable = null;
    
    JButton doneButton = new JButton(Bundle.getMessage("ButtonDone"));
    
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
        
        // Set up the assignment panel
        nodeTablePanel = new JPanel();
        nodeTablePanel.setLayout(new BoxLayout(nodeTablePanel, BoxLayout.Y_AXIS));
        
        nodeTableModel = new NodeTableModel();
        nodeTable = new JTable(nodeTableModel);
        
        nodeTable.setShowGrid(true);
        nodeTable.setGridColor(Color.black);
        nodeTable.setRowSelectionAllowed(false);
        nodeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        nodeTable.setRowHeight(30);
        
        nodeTable.getTableHeader().setReorderingAllowed(false);
        nodeTable.setPreferredScrollableViewportSize(new java.awt.Dimension(300, 350));
        TableColumnModel assignmentColumnModel = nodeTable.getColumnModel();
        
        DefaultTableCellRenderer dtcen = new DefaultTableCellRenderer();
        dtcen.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer dtrt = new DefaultTableCellRenderer();
        dtrt.setHorizontalAlignment(SwingConstants.RIGHT);
        
        TableCellRenderer rendererFromHeader = nodeTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setBackground(Color.LIGHT_GRAY);
        
        TableColumn nodeAddrColumn = assignmentColumnModel.getColumn(NodeTableModel.ADDRESS_COLUMN);
        nodeAddrColumn.setMinWidth(40);
        nodeAddrColumn.setMaxWidth(80);
        nodeAddrColumn.setCellRenderer(dtcen);
//        nodeAddrColumn.setResizable(true);
        nodeAddrColumn.setResizable(false);
        
        TableColumn nodenumColumn = assignmentColumnModel.getColumn(NodeTableModel.MANUFACTURER_COLUMN);
//        nodenumColumn.setMinWidth(40);
//        nodenumColumn.setMaxWidth(80);
        nodenumColumn.setMinWidth(150);
        nodenumColumn.setMaxWidth(200);
        nodenumColumn.setCellRenderer(dtcen);
        nodenumColumn.setResizable(true);
//        nodenumColumn.setResizable(false);
        
        TableColumn nodetypeColumn = assignmentColumnModel.getColumn(NodeTableModel.DEVELOPER_COLUMN);
//        nodenumColumn.setMinWidth(40);
//        nodenumColumn.setMaxWidth(80);
        nodetypeColumn.setMinWidth(150);
        nodetypeColumn.setMaxWidth(200);
        nodetypeColumn.setCellRenderer(dtcen);
        nodetypeColumn.setResizable(true);
//        nodetypeColumn.setResizable(false);
        
        JScrollPane nodeTableScrollPane = new JScrollPane(nodeTable);
        
        TableColumn selectColumn = assignmentColumnModel.getColumn(NodeTableModel.SELECT_COLUMN);
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem(Bundle.getMessage("SelectSelect"));
        comboBox.addItem(Bundle.getMessage("SelectEdit"));
        comboBox.addItem(Bundle.getMessage("SelectInfo"));
        comboBox.addItem(Bundle.getMessage("SelectDelete"));
        selectColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        selectColumn.setMinWidth(40);
        selectColumn.setMaxWidth(90);
        selectColumn.setCellRenderer(dtcen);
        selectColumn.setResizable(false);
        
        
        
        
        Border inputBorderTitled = BorderFactory.createTitledBorder(inputBorder,
                " ",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        nodeTablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
        nodeTablePanel.setBorder(inputBorderTitled);
        setPreferredSize(new Dimension(950, 550));
        
        nodeTable.setAutoCreateRowSorter(true);
        nodeTable.getRowSorter().toggleSortOrder(NodeTableModel.ADDRESS_COLUMN);
//        nodeTable.getRowSorter().toggleSortOrder(NodeTableModel.NODENUM_COLUMN);
        
        contentPane.add(nodeTablePanel);
        
        // Setup main window buttons
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, FlowLayout.RIGHT));
        panel3.setPreferredSize(new Dimension(950, 50));
        
        // Set up Done button
        doneButton.setVisible(true);
        doneButton.setToolTipText(Bundle.getMessage("DoneButtonTip"));
        doneButton.addActionListener((java.awt.event.ActionEvent e) -> {
            doneButtonActionPerformed();
        });
        panel3.add(doneButton);
        
        contentPane.add(panel3);
        addHelpMenu("package.jmri.jmrix.cmri.serial.nodeconfigmanager.NodeConfigManagerFrame", true);
        // pack for display
        pack();
        nodeTablePanel.setVisible(true);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        readDecoderTypes();
        
        
        
        _tc = _memo.getLnTrafficController();
        _tc.addLocoNetListener(~0, this);
        
        _tc.sendLocoNetMessage(LnSv2MessageContents.createSvDiscoverQueryMessage());
    }
    
    /**
     * Handle the done button click.
     */
    public void doneButtonActionPerformed() {
//        changedNode = false;
        setVisible(false);
        dispose();
    }
    
    
    public void readDecoderTypes() {
//        LnNodeManager lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        _decoders = InstanceManager.getDefault(DecoderIndexFile.class).matchingDecoderList(null, null, null, null, null, null);
        
        
        
        for (DecoderFile decoderFile : _decoders) {
//            jmri.LocoAddress.Protocol[] protocols = decoderFile.getSupportedProtocols();
            
            RosterEntry re = new RosterEntry();
            re.setDecoderFamily(decoderFile.getFamily());
            re.setDecoderModel(decoderFile.getModel());
            re.setId("LabelNewDecoder");
//            re.setId(Bundle.getMessage("LabelNewDecoder"));
            
            // loadDecoderFile()
            
//            Element decoderRoot = null;
//////            try {
                int manufacturerID = Integer.parseInt(decoderFile.getMfgID());
                List<DecoderFile> decoderFiles = decoderFileMap.get(manufacturerID);
                if (decoderFiles == null) {
                    decoderFiles = new ArrayList<>();
                    decoderFileMap.put(manufacturerID, decoderFiles);
                }
                decoderFiles.add(decoderFile);
////                System.out.format("%s, %s, %s, %s, %s%n", decoderFile.getMfg(), decoderFile.getMfgID(), decoderFile.getProductID(), decoderFile.getFamily(), decoderFile.getModel());
//                System.out.format("%s, %s%n", decoderFile.getMfgID(), decoderFile.getProductID());
//                if (1==1)
//////                if ("Public-domain and DIY".equals(decoderFile.getMfg())) {
//////                    if (!decoderFile.getFamily().equals(decoderFile.getModel())) {
//                        System.out.format("%s, %s, %s, %s, %s, %s, %s, %s%n", decoderFile.getShowable().name(), decoderFile.getMfg(), decoderFile.getMfgID(), decoderFile.getDeveloperID(), decoderFile.getProductID(), decoderFile.getFamily(), decoderFile.getModel(), decoderFile.getModelElement());
//////                        decoderRoot = decoderFile.rootFromName(DecoderFile.fileLocation + decoderFile.getFileName());
//////                    }
//////                }
//////            } catch (org.jdom2.JDOMException e) {
//////                log.error("Exception while parsing decoder XML file: " + decoderFile.getFileName(), e);
//////                return;
//////            } catch (java.io.IOException e) {
//////                log.error("Exception while reading decoder XML file: " + decoderFile.getFileName(), e);
//////                return;
//////            }
//            re.
            
            // note that we're leaving the filename null
            // add the new roster entry to the in-memory roster
//////            Roster.getDefault().addEntry(re);
            
//            re.
//            startProgrammer(decoderFile, re, (String) programmerBox.getSelectedItem());
        }
        
        
    }
    
    
    
    
    private void addNode(LnNode node) {
//        List<DecoderFile> decoderFiles = decoderFileMap.get(node.getManufacturerID());
//        System.out.format("Num items for manufacturerID %d: %d%n", node.getManufacturerID(), decoderFiles.size());
        jmri.util.ThreadingUtil.runOnGUIEventually(() -> {
//            System.out.println(node.toString());
            nodeTableModel.addRow(node);
        });
    }
    
    
    @Override
    public void message(LocoNetMessage msg) {
        // Return if the message is not a SV2 message
        if (!isSupportedSv2Message(msg)) return;
        LnSv2MessageContents contents = new LnSv2MessageContents(msg);
        
        Sv2Command command = LnSv2MessageContents.extractMessageType(msg);
        
        if (command == Sv2Command.SV2_DISCOVER_DEVICE_REPORT) {
            LnNode node = new LnNode(contents, _tc);
            addNode(node);
        }
    }
    
    
    
    /**
     * Set up table for displaying bit assignments
     */
    public class NodeTableModel extends AbstractTableModel {

        @Override
        public String getColumnName(int c) {
            return nodeTableColumnsNames[c];
        }
        
        @Override
        public Class<?> getColumnClass(int c) {
            switch (c) {
                case MANUFACTURER_COLUMN:
                case DEVELOPER_COLUMN:
                case PRODUCT_COLUMN:
                    return String.class;

                case SERIALNO_COLUMN:
                case ADDRESS_COLUMN:
//                case NUMINBYTES_COLUMN:
//                case NUMOUTBYTES_COLUMN:
                    return Integer.class;

                case SELECT_COLUMN:
                    return String.class;

//                case NODEDESC_COLUMN:
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            if (c == SELECT_COLUMN) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int getColumnCount() {
            return NUM_COLUMNS;
        }

        @Override
        public int getRowCount() {
            return lnNodes.size();
        }

        public void removeRow(int row) {
            lnNodes.remove(row);
//            numConfigNodes = lnNode.size();
            fireTableRowsDeleted(row, row);
        }

        public void addRow(LnNode newNode) {
            lnNodes.add(newNode);
//            numConfigNodes = lnNode.size();
            fireTableDataChanged();
        }

        public void changeRow(int row, LnNode aNode) {
            lnNodes.set(row, aNode);
            fireTableDataChanged();
        }
/*
        @Override
        public void setValueAt(Object value, int row, int col) {
            if (col == SELECT_COLUMN) {
                if (Bundle.getMessage("SelectEdit").equals(value)) {
                    editActionSelected();
                } else if (Bundle.getMessage("SelectInfo").equals(value)) {
                    infoActionSelected();
                } else if (Bundle.getMessage("SelectDelete").equals(value)) {
                    deleteActionSelected();
                }
            } else {
                log.info("setValueAt Row" + row + " value " + value);
            }
            fireTableDataChanged();
        }
*/
        @Override
        public Object getValueAt(int r, int c) {
            switch (c) {
                case ADDRESS_COLUMN:
                    return lnNodes.get(r).getAddress();
//                    return Integer.toString(lnNode.get(r).getNumBitsPerCard());
                    
                case MANUFACTURER_COLUMN:
                    if (lnNodes.get(r) == null) return 0;    // DANIEL
                    return lnNodes.get(r).getManufacturer();
//                    return lnNode.get(r).getAddress();
                    
                case DEVELOPER_COLUMN:
                    return lnNodes.get(r).getDeveloper();
//                    return "  " + nodeTableTypes[lnNode.get(r).getNodeType()];
                    
                case PRODUCT_COLUMN:
                    return lnNodes.get(r).getProduct();
//                    return Integer.toString(lnNode.get(r).getNumBitsPerCard());
                    
                case SERIALNO_COLUMN:
                    return lnNodes.get(r).getSerialNumber();
//                    return Integer.toString(lnNode.get(r).getNumBitsPerCard());
/*                    
                case NUMINCARDS_COLUMN:
                    if (lnNode.get(r).getNodeType() == LnNode.SMINI) {
                        return Integer.toString(lnNode.get(r).numInputCards() * 3);
                    } else {
                        return Integer.toString(lnNode.get(r).numInputCards());
                    }

                case NUMOUTCARDS_COLUMN:
                    if (lnNode.get(r).getNodeType() == LnNode.SMINI) {
                        return Integer.toString(lnNode.get(r).numOutputCards() * 3);
                    } else {
                        return Integer.toString(lnNode.get(r).numOutputCards());
                    }

                case NUMINBYTES_COLUMN:
                    return Integer.toString((lnNode.get(r).getNumBitsPerCard()) * lnNode.get(r).numInputCards());

                case NUMOUTBYTES_COLUMN:
                    return Integer.toString((lnNode.get(r).getNumBitsPerCard()) * lnNode.get(r).numOutputCards());

                case SELECT_COLUMN:

                    return "Select";
                case NODEDESC_COLUMN:

                    return " " + lnNode.get(r).getcmriNodeDesc();
*/                    
                default:
                    return "";
            }
        }
        
        private static final int ADDRESS_COLUMN = 0;
        private static final int MANUFACTURER_COLUMN = 1;
        private static final int DEVELOPER_COLUMN = 2;
        private static final int PRODUCT_COLUMN = 3;
        private static final int SERIALNO_COLUMN = 4;
//        private static final int NUMINBYTES_COLUMN = 5;
//        private static final int NUMOUTBYTES_COLUMN = 6;
//        private static final int SELECT_COLUMN = 7;
        private static final int SELECT_COLUMN = 5;
//        private static final int NODEDESC_COLUMN = 8;
//        private static final int NUM_COLUMNS = NODEDESC_COLUMN + 1;
        private static final int NUM_COLUMNS = SELECT_COLUMN + 1;
        
    }

    private final String[] nodeTableColumnsNames
            = {"Address", "Manufacturer", "Developer", "Product", "Serial No", "OUT Cards", "IN Bits", "OUT Bits", " ", "  Description"};
    
    
    
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DiscoverNodesFrame.class);
    
}


// NOTE: If a card with manufacturerID, productID and serialNo has a new address,
// warn about the existing card. Maybe red color?
