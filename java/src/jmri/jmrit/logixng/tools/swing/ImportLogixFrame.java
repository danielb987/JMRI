package jmri.jmrit.logixng.tools.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.InstanceManager;
import jmri.UserPreferencesManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.digital.actions.IfThen;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout;
import jmri.util.JmriJFrame;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.MaleDigitalActionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.jmrit.logixng.swing.SwingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Imports Logixs to LogixNG
 * 
 * @author Daniel Bergqvist 2019
 */
public final class ImportLogixFrame extends JmriJFrame {

    private static final int panelWidth700 = 700;
    private static final int panelHeight500 = 500;
    
    private static final Map<String, Color> FEMALE_SOCKET_COLORS = new HashMap<>();
    
    private final LogixNG logixNG;
    
    // Add LogixNG Variables
    private JmriJFrame addEditLogixNGFrame = null;
    private final JTextField _systemName = new JTextField(20);
    private final JTextField _addUserName = new JTextField(20);
    private final JCheckBox _autoSystemName = new JCheckBox(Bundle.getMessage("LabelAutoSysName"));   // NOI18N
    private final JLabel _sysNameLabel = new JLabel(Bundle.getMessage("SystemName") + ":");  // NOI18N
    private final JLabel _userNameLabel = new JLabel(Bundle.getMessage("UserName") + ":");   // NOI18N
    private final String systemNameAuto = this.getClass().getName() + ".AutoSystemName";      // NOI18N
    private Class maleSocketClass = null;
    private JButton create;
    private JButton edit;
    boolean _showReminder = false;
    
    private Map<Category, List<Class<? extends Base>>> connectableClasses;
    
    private SwingConfiguratorInterface swingConfiguratorInterface;
    
    // Edit LogixNG Variables
    private boolean _inEditMode = false;
    
    
    ImportLogixFrame() {
        logixNG = null;
    }
    
    /**
     * Construct a LogixNGEditor.
     *
     * @param sName system name of LogixNG to be edited
     */
    public ImportLogixFrame(String sName) {
        FEMALE_SOCKET_COLORS.put("jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalActionSocket", Color.RED);
        FEMALE_SOCKET_COLORS.put("jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket", Color.BLUE);
        logixNG = InstanceManager.getDefault(LogixNG_Manager.class).getBySystemName(sName);
        
        if (logixNG.getUserName() == null) {
            setTitle(Bundle.getMessage("TitleEditLogixNG", sName));
        } else {
            setTitle(Bundle.getMessage("TitleEditLogixNG2", sName, logixNG.getUserName()));
        }
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        
        Container contentPanel = getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        
        p2.add(new JLabel("Which Logixs to import?"));
        JRadioButton r1 = new JRadioButton("Import all Logixs");
        JRadioButton r2 = new JRadioButton("Import all active Logixs");
        JRadioButton r3 = new JRadioButton("Import selected Logixs");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);
        buttonGroup.add(r3);
        r2.setSelected(true);
        r1.setAlignmentX(LEFT_ALIGNMENT);
        r2.setAlignmentX(LEFT_ALIGNMENT);
        r3.setAlignmentX(LEFT_ALIGNMENT);
        p2.add(r1);
        p2.add(r2);
        p2.add(r3);
        
        p2.add(new JLabel("What to do with the Logixs after the import?"));
        JRadioButton r4 = new JRadioButton("Nothing");
        JRadioButton r5 = new JRadioButton("Disable the Logixs");
        JRadioButton r6 = new JRadioButton("Delete the Logixs - Warning!");
        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(r4);
        buttonGroup2.add(r5);
        buttonGroup2.add(r6);
        r5.setSelected(true);
        r4.setAlignmentX(LEFT_ALIGNMENT);
        r5.setAlignmentX(LEFT_ALIGNMENT);
        r6.setAlignmentX(LEFT_ALIGNMENT);
        p2.add(r4);
        p2.add(r5);
        p2.add(r6);
        
        String warningMessage
                = "Warning\n"
                + "The import tool will do its best to import the requested\n"
                + "Logixs to LogixNG. But LogixNG works in a different way\n"
                + "than Logix and therefore there may be subtle differences\n"
                + "between the original Logix and the imported LogixNG.\n"
                + "\n"
                + "Also, there may be special Logixs not known to the import\n"
                + "tool that should not be imported to LogixNG, for example\n"
                + "the Logix that handles sensor groups. The import tool\n"
                + "knows about some of these Logix (SYS and RTX), but there\n"
                + "may be others not known to the import tool.";
        JTextArea warning = new JTextArea(warningMessage);
        warning.setEditable(false);
        warning.setAlignmentX(LEFT_ALIGNMENT);
        warning.setColumns(60);
        p2.add(warning);
        
        JPanel p;
        p = new JPanel();
//        p.setLayout(new FlowLayout());
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
        
        _systemName.setText("");
        _systemName.setEnabled(true);
        _addUserName.setText("");
        
        _addUserName.setToolTipText(Bundle.getMessage("UserNameHint"));    // NOI18N
//        _addUserName.setToolTipText("LogixNGUserNameHint");    // NOI18N
        _systemName.setToolTipText(Bundle.getMessage("SystemNameHint", "aaa"));   // NOI18N
//        _systemName.setToolTipText("LogixNGSystemNameHint");   // NOI18N



        contentPanel.add(p2);
        // set up message
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        JPanel panel31 = new JPanel();
//        panel31.setLayout(new FlowLayout());
        JPanel panel32 = new JPanel();
        
        String messageId = "AAAAA";
        if (messageId != null) {
            JLabel message1 = new JLabel(messageId + "1");  // NOI18N
            panel31.add(message1);
            JLabel message2 = new JLabel(messageId + "2");  // NOI18N
            panel32.add(message2);
        }
        
        JPanel panel33 = new JPanel();
        panel3.add(panel31);
        panel3.add(panel32);
        panel3.add(panel33);
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
                dispose();
            }
        });
//        cancel.setToolTipText(Bundle.getMessage("CancelLogixButtonHint"));      // NOI18N
        cancel.setToolTipText("CancelLogixButtonHint");      // NOI18N

        contentPanel.add(panel5);

        _autoSystemName.addItemListener(
                new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                autoSystemName();
            }
        });
        System.out.format("Component: %s%n", c.getClass().getName());
//        addLogixNGFrame.setLocationRelativeTo(component);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        
        if (1==1)
            return;
        
        
        
        
        // build menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(Bundle.getMessage("MenuFile"));
        JMenuItem closeWindowItem = new JMenuItem(Bundle.getMessage("CloseWindow"));
        closeWindowItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        fileMenu.add(closeWindowItem);
        menuBar.add(fileMenu);
        
        JMenu toolMenu = new JMenu(Bundle.getMessage("MenuTools"));
        toolMenu.add(new TimeDiagram.CreateNewLogixNGAction("Create a LogixNG"));
/*        
        toolMenu.add(new CreateNewLogixNGAction(Bundle.getMessage("TitleOptions")));
        toolMenu.add(new PrintOptionAction());
        toolMenu.add(new BuildReportOptionAction());
        toolMenu.add(new BackupFilesAction(Bundle.getMessage("Backup")));
        toolMenu.add(new RestoreFilesAction(Bundle.getMessage("Restore")));
        toolMenu.add(new LoadDemoAction(Bundle.getMessage("LoadDemo")));
        toolMenu.add(new ResetAction(Bundle.getMessage("ResetOperations")));
        toolMenu.add(new ManageBackupsAction(Bundle.getMessage("ManageAutoBackups")));
*/
        menuBar.add(toolMenu);
//        menuBar.add(new jmri.jmrit.operations.OperationsMenu());

        setJMenuBar(menuBar);
//        addHelpMenu("package.jmri.jmrit.operations.Operations_Settings", true); // NOI18N
        
        
        // For testing only
/*        
        InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        String systemName;
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        systemName = InstanceManager.getDefault(ExpressionManager.class).getNewSystemName(logixNG);
        DigitalExpression expression = new ExpressionTurnout(systemName, "An expression for test");  // NOI18N
        MaleDigitalExpressionSocket expressionSocket = InstanceManager.getDefault(ExpressionManager.class).register(expression);
//        InstanceManager.getDefault(jmri.ExpressionManager.class).addExpression(new ExpressionTurnout(systemName, "LogixNG 102, DigitalExpression 26"));  // NOI18N
        systemName = InstanceManager.getDefault(ActionManager.class).getNewSystemName(logixNG);
        DigitalAction actionTurnout = new ActionTurnout(systemName, "An action for test");  // NOI18N
        MaleDigitalActionSocket actionSocket = InstanceManager.getDefault(ActionManager.class).register(actionTurnout);
        systemName = InstanceManager.getDefault(ActionManager.class).getNewSystemName(logixNG);
        DigitalAction actionIfThen = new IfThen(systemName, IfThen.Type.TRIGGER_ACTION, "A", "B", expressionSocket, actionSocket);
        actionSocket = InstanceManager.getDefault(ActionManager.class).register(actionIfThen);
        logixNG.getFemaleSocket().connect(actionSocket);
*/        
        
        // Figure out where in the filesystem to start displaying
//        File root;
//        root = new File(System.getProperty("user.home"));
        FemaleSocket root;
        SortedSet<LogixNG> logixNGSet = InstanceManager.getDefault(LogixNG_Manager.class).getNamedBeanSet();
        for (LogixNG nl : logixNGSet) {
            System.out.format("LogixNG: %s%n", nl.toString());
            System.out.format("LogixNG female socket: %s. Connected: %b%n", nl.getFemaleSocket().toString(), nl.getFemaleSocket().isConnected());
        }
        if (logixNGSet != null) {
            root = logixNGSet.first().getFemaleSocket();
        }

        // Create a TreeModel object to represent our tree of files
//        FileTreeModel model = new FileTreeModel(root);
//        // Create a TreeModel object to represent our tree of files

        // create panel
        JPanel pPanel = new JPanel();
        pPanel.setLayout(new BoxLayout(pPanel, BoxLayout.Y_AXIS));
        
        // Test
//        JPanel pComment = new JPanel();
//        pComment.setLayout(new GridBagLayout());
//        pComment.setBorder(BorderFactory.createTitledBorder("Test"));
//        addItem(pComment, "Testar", 1, 0);
//        pOptional.add(pComment);
        
        // button panel
//        JPanel pButtons = new JPanel();
//        pButtons.setLayout(new GridBagLayout());
//        addItem(pButtons, deleteButton, 0, 25);
//        addItem(pButtons, addButton, 1, 25);
//        addItem(pButtons, saveButton, 3, 25);
        
        // add panels
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(pPanel);
//        getContentPane().add(pButtons);
        
        
        initMinimumSize(new Dimension(panelWidth700, panelHeight500));
    }

    public void initMinimumSize(Dimension dimension) {
        setMinimumSize(dimension);
        pack();
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

    
    private final static Logger log = LoggerFactory.getLogger(ImportLogixFrame.class);

}
