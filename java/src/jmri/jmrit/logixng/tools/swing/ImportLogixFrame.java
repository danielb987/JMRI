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
import javax.swing.JScrollPane;
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
    
    
    /**
     * This contains a list of commands to be processed by the listener
     * recipient.
     */
    public HashMap<String, String> logixData = new HashMap<>();
    
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
    
    /** {@inheritDoc} */
    @Override
    public void windowClosed(WindowEvent e) {
        logixData.clear();
        logixData.put("Finish", logixNG.getSystemName());  // NOI18N
    }
    
    /**
     * Create or copy LogixNG frame.
     *
     * @param messageId part 1 of property key to fetch as user instruction on
     *                  pane, either 1 or 2 is added to form the whole key
     * @param femaleSocket the female socket to which we want to add something
     * @return the button JPanel
     */
    JPanel makeAddEditFrame(String messageId, FemaleSocket femaleSocket) {
        addEditLogixNGFrame = new JmriJFrame(
                Bundle.getMessage(
                        "AddMaleSocketDialogTitle",
                        femaleSocket.getLongDescription()));
        addEditLogixNGFrame.addHelpMenu(
                "package.jmri.jmrit.beantable.LogixNGAddEdit", true);     // NOI18N
        addEditLogixNGFrame.setLocation(50, 30);
        Container contentPanel = addEditLogixNGFrame.getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

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
        
        System.out.format("isConnected: %b%n", femaleSocket.isConnected());
        if (femaleSocket.isConnected()) {
            _systemName.setText(femaleSocket.getConnectedSocket().getSystemName());
            _systemName.setEnabled(false);
            _addUserName.setText(femaleSocket.getConnectedSocket().getUserName());
        } else {
            _systemName.setText("");
            _systemName.setEnabled(true);
            _addUserName.setText("");
        }
        
        _addUserName.setToolTipText(Bundle.getMessage("UserNameHint"));    // NOI18N
//        _addUserName.setToolTipText("LogixNGUserNameHint");    // NOI18N
        _systemName.setToolTipText(Bundle.getMessage("SystemNameHint", femaleSocket.getExampleSystemName()));   // NOI18N
//        _systemName.setToolTipText("LogixNGSystemNameHint");   // NOI18N
        contentPanel.add(p);
        // set up message
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        JPanel panel31 = new JPanel();
//        panel31.setLayout(new FlowLayout());
        JPanel panel32 = new JPanel();
        if (messageId != null) {
            JLabel message1 = new JLabel(Bundle.getMessage(messageId + "1"));  // NOI18N
            panel31.add(message1);
            JLabel message2 = new JLabel(Bundle.getMessage(messageId + "2"));  // NOI18N
            panel32.add(message2);
        }
        
        connectableClasses = femaleSocket.getConnectableClasses();
        maleSocketClass = connectableClasses.get(Category.ITEM).get(0);
        
        swingConfiguratorInterface = SwingTools.getSwingConfiguratorForClass(maleSocketClass);
        JPanel panel33;
        if (femaleSocket.isConnected()) {
            panel33 = swingConfiguratorInterface.getConfigPanel(femaleSocket.getConnectedSocket().getObject());
        } else {
            panel33 = swingConfiguratorInterface.getConfigPanel();
        }
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
                cancelAddPressed(e);
            }
        });
//        cancel.setToolTipText(Bundle.getMessage("CancelLogixButtonHint"));      // NOI18N
        cancel.setToolTipText("CancelLogixButtonHint");      // NOI18N

        addEditLogixNGFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelAddPressed(null);
            }
        });

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
        addEditLogixNGFrame.setLocationRelativeTo(null);
        addEditLogixNGFrame.pack();
        addEditLogixNGFrame.setVisible(true);
        
        return panel5;
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
        addEditLogixNGFrame.setVisible(false);
        swingConfiguratorInterface.dispose();
        addEditLogixNGFrame.dispose();
        addEditLogixNGFrame = null;
//        _inCopyMode = false;
        this.setVisible(true);
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ImportLogixFrame.class);

}
