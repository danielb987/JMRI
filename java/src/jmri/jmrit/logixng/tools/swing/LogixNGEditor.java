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
 * Editor of LogixNG
 * 
 * @author Daniel Bergqvist 2018
 */
public final class LogixNGEditor extends JmriJFrame {

    private static final int panelWidth700 = 700;
    private static final int panelHeight500 = 500;
    
    private static final Map<String, Color> FEMALE_SOCKET_COLORS = new HashMap<>();
    
    private final LogixNG logixNG;
    
    // Add LogixNG Variables
    private JmriJFrame addEditLogixNGFrame = null;
    private FemaleSocketTreeModel femaleSocketTreeModel;
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
     * Maintain a list of listeners -- normally only one.
     */
    private List<LogixNGEventListener> listenerList = new ArrayList<>();
    
    /**
     * This contains a list of commands to be processed by the listener
     * recipient.
     */
    public HashMap<String, String> logixNGData = new HashMap<>();
    
    LogixNGEditor() {
        logixNG = null;
    }
    
    /**
     * Construct a LogixNGEditor.
     *
     * @param sName system name of LogixNG to be edited
     */
    public LogixNGEditor(String sName) {
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
        root = logixNGSet.first().getFemaleSocket();

        // Create a TreeModel object to represent our tree of files
//        FileTreeModel model = new FileTreeModel(root);
//        // Create a TreeModel object to represent our tree of files
        if (logixNG != null)
            femaleSocketTreeModel = new FemaleSocketTreeModel(logixNG.getFemaleSocket());
        else
            femaleSocketTreeModel = new FemaleSocketTreeModel(root);

        // Create a JTree and tell it to display our model
        final JTree tree = new JTree();
        tree.setModel(femaleSocketTreeModel);
        tree.setCellRenderer(new FemaleSocketTreeRenderer());
        
        tree.setShowsRootHandles(true);
        
        PopupMenu popup = new PopupMenu(tree, femaleSocketTreeModel);
        popup.init();
        
        // The JTree can get big, so allow it to scroll
        JScrollPane scrollpane = new JScrollPane(tree);

        // create panel
        JPanel pPanel = new JPanel();
        pPanel.setLayout(new BoxLayout(pPanel, BoxLayout.Y_AXIS));
        
        // Display it all in a window and make the window appear
        pPanel.add(scrollpane, "Center");

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
        logixNGData.clear();
        logixNGData.put("Finish", logixNG.getSystemName());  // NOI18N
        fireLogixNGEvent();
    }
    
    public void addLogixNGEventListener(LogixNGEventListener listener) {
        listenerList.add(listener);
    }
    
    /**
     * Notify the listeners to check for new data.
     */
    void fireLogixNGEvent() {
        for (LogixNGEventListener l : listenerList) {
            l.logixNGEventOccurred();
        }
    }
    
    
    public interface LogixNGEventListener extends EventListener {
        
        public void logixNGEventOccurred();
    }
    
    
    /**
     * Check if another LogixNG editing session is currently open or no system
     * name is provided.
     *
     * @param sName system name of LogixNG to be copied
     * @return true if a new session may be started
     */
    boolean checkFlags(String sName) {
        if (_inEditMode) {
            // Already editing a LogixNG, ask for completion of that edit
            JOptionPane.showMessageDialog(null,
//                    Bundle.getMessage("LogixNGError32", _curLogixNG.getSystemName()),
                    Bundle.getMessage("LogixNGError32", "aaa"),
                    Bundle.getMessage("ErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            toFront();
//            if (_treeEdit != null) {
//                _treeEdit.toFront();
////                _treeEdit.bringToFront();
//            }
            return false;
        }
/*
        if (_inCopyMode) {
            // Already editing a LogixNG, ask for completion of that edit
            JOptionPane.showMessageDialog(null,
                    Bundle.getMessage("LogixNGError31", _logixNGSysName),
                    Bundle.getMessage("ErrorTitle"), // NOI18N
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
*/
/*
        if (sName != null) {
            // check if a LogixNG with this name exists
            LogixNG x = _logixNGManager.getBySystemName(sName);
            if (x == null) {
                // LogixNG does not exist, so cannot be edited
                log.error("No LogixNG with system name: " + sName);
                JOptionPane.showMessageDialog(null,
                        Bundle.getMessage("LogixNGError5"),
                        Bundle.getMessage("ErrorTitle"), // NOI18N
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
*/
        return true;
    }

    /**
     * Respond to the Add menu choice in the popup menu.
     *
     * @param femaleSocket the female socket
     */
    protected void addPressed(FemaleSocket femaleSocket, TreePath path) {
        // possible change
        if (!checkFlags(null)) {
            return;
        }
        _showReminder = true;
        // make an Add LogixNG Frame
        if (addEditLogixNGFrame == null) {
            JPanel panel5 = makeAddEditFrame("AddMessage", femaleSocket);  // NOI18N
            // Create LogixNG
            create = new JButton(Bundle.getMessage("ButtonCreate"));  // NOI18N
            panel5.add(create);
            create.addActionListener((ActionEvent e) -> {
                if (_systemName.getText().isEmpty() && _autoSystemName.isSelected()) {
                    _systemName.setText(femaleSocket.getNewSystemName());
                }
                MaleSocket socket;
                if (_addUserName.getText().isEmpty()) {
                    socket = swingConfiguratorInterface.createNewObject(_systemName.getText());
                } else {
                    socket = swingConfiguratorInterface.createNewObject(_systemName.getText(), _addUserName.getText());
                }
                try {
                    femaleSocket.connect(socket);
                } catch (SocketAlreadyConnectedException ex) {
                    throw new RuntimeException(ex);
                }
                swingConfiguratorInterface.dispose();
                addEditLogixNGFrame.dispose();
                for (TreeModelListener l : femaleSocketTreeModel.listeners) {
                    TreeModelEvent tme = new TreeModelEvent(
                            femaleSocket,
                            path.getPath()
                    );
                    l.treeNodesChanged(tme);
                }
            });
            create.setToolTipText(Bundle.getMessage("CreateButtonHint"));  // NOI18N
        }
        addEditLogixNGFrame.pack();
        addEditLogixNGFrame.setVisible(true);
        _autoSystemName.setSelected(false);
        InstanceManager.getOptionalDefault(UserPreferencesManager.class).ifPresent((prefMgr) -> {
            _autoSystemName.setSelected(prefMgr.getSimplePreferenceState(systemNameAuto));
        });
    }

    /**
     * Respond to the Edit menu choice in the popup menu.
     *
     * @param femaleSocket the female socket
     */
    protected void editPressed(FemaleSocket femaleSocket, TreePath path) {
        // possible change
        if (!checkFlags(null)) {
            return;
        }
        _showReminder = true;
        // make an Add LogixNG Frame
        if (addEditLogixNGFrame == null) {
            JPanel panel5 = makeAddEditFrame(null, femaleSocket);  // NOI18N
            // Create LogixNG
            edit = new JButton(Bundle.getMessage("ButtonOK"));  // NOI18N
            panel5.add(edit);
            edit.addActionListener((ActionEvent e) -> {
                swingConfiguratorInterface.updateObject(femaleSocket.getConnectedSocket().getObject());
//                if (_addUserName.getText().isEmpty()) {
//                    socket = swingConfiguratorInterface.createNewObject(_systemName.getText());
//                } else {
//                    socket = swingConfiguratorInterface.createNewObject(_systemName.getText(), _addUserName.getText());
//                }
//                try {
//                    femaleSocket.connect(socket);
//                } catch (SocketAlreadyConnectedException ex) {
//                    throw new RuntimeException(ex);
//                }
                swingConfiguratorInterface.dispose();
                addEditLogixNGFrame.dispose();
                for (TreeModelListener l : femaleSocketTreeModel.listeners) {
                    TreeModelEvent tme = new TreeModelEvent(
                            femaleSocket,
                            path.getPath()
                    );
//                    TreeModelEvent tme = new TreeModelEvent(
//                            femaleSocket,
//                            path.getPath(),
//                            indices_of_inserted_items,
//                            inserted_items
//                    );
                    l.treeNodesChanged(tme);
//                    l.treeStructureChanged(tme);
                }
            });
//            edit.setToolTipText(Bundle.getMessage("EditButtonHint"));  // NOI18N
        }
        addEditLogixNGFrame.pack();
        addEditLogixNGFrame.setVisible(true);
        _autoSystemName.setSelected(false);
        InstanceManager.getOptionalDefault(UserPreferencesManager.class).ifPresent((prefMgr) -> {
            _autoSystemName.setSelected(prefMgr.getSimplePreferenceState(systemNameAuto));
        });
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
    
    
    
    /**
     * The methods in this class allow the JTree component to traverse the file
     * system tree and display the files and directories.
     */
    private static class FemaleSocketTreeModel implements TreeModel {

        private final FemaleSocket root;
        private final List<TreeModelListener> listeners = new ArrayList<>();

        public FemaleSocketTreeModel(FemaleSocket root) {
            this.root = root;
        }

        @Override
        public Object getRoot() {
            return root;
        }

        @Override
        public boolean isLeaf(Object node) {
            FemaleSocket socket = (FemaleSocket) node;
            if (!socket.isConnected()) {
                return true;
            }
            return socket.getConnectedSocket().getChildCount() == 0;
        }

        @Override
        public int getChildCount(Object parent) {
            FemaleSocket socket = (FemaleSocket) parent;
            if (!socket.isConnected()) {
                return 0;
            }
            return socket.getConnectedSocket().getChildCount();
        }

        @Override
        public Object getChild(Object parent, int index) {
            FemaleSocket socket = (FemaleSocket) parent;
            if (!socket.isConnected()) {
                return null;
            }
            return socket.getConnectedSocket().getChild(index);
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            FemaleSocket socket = (FemaleSocket) parent;
            if (!socket.isConnected()) {
                return -1;
            }
            
            MaleSocket connectedSocket = socket.getConnectedSocket();
            for (int i = 0; i < connectedSocket.getChildCount(); i++) {
                if (child == connectedSocket.getChild(i)) {
                    return i;
                }
            }
            return -1;
        }

        // This method is invoked by the JTree only for editable trees.  
        // This TreeModel does not allow editing, so we do not implement 
        // this method.  The JTree editable property is false by default.
        @Override
        public void valueForPathChanged(TreePath path, Object newvalue) {
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            listeners.remove(l);
        }

//        private void notify() {
//            for (TreeModelListener l : listeners) {
//                l.treeNodesChanged(e);
//            }
//        }

    }
    
    
    private static final class FemaleSocketTreeRenderer implements TreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            
            FemaleSocket socket = (FemaleSocket)value;
            
//            FemaleSocketPanel panel = new FemaleSocketPanel(socket);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            panel.setOpaque(false);
            
//            System.out.format("Daniel AAAA: %s, %s, %s%n", socket.getClass().getCanonicalName(), socket.getClass().getName(), socket.getClass().getSimpleName());
            JLabel socketLabel = new JLabel(socket.getShortDescription());
            Font font = socketLabel.getFont();
            socketLabel.setFont(font.deriveFont((float)(font.getSize2D()*1.7)));
            socketLabel.setForeground(FEMALE_SOCKET_COLORS.get(socket.getClass().getName()));
//            socketLabel.setForeground(Color.red);
            panel.add(socketLabel);
            
            panel.add(javax.swing.Box.createRigidArea(new Dimension(5,0)));
            
            JLabel socketNameLabel = new JLabel(socket.getName());
            socketNameLabel.setForeground(FEMALE_SOCKET_COLORS.get(socket.getClass().getName()));
//            socketNameLabel.setForeground(Color.red);
            panel.add(socketNameLabel);
            
            panel.add(javax.swing.Box.createRigidArea(new Dimension(5,0)));
            
            JLabel connectedItemLabel = new JLabel();
            if (socket.isConnected()) {
                connectedItemLabel.setText(socket.getConnectedSocket().getLongDescription());
            }
            
            panel.add(connectedItemLabel);
            
            return panel;
        }
        
    }
    
    
    private final class PopupMenu extends JPopupMenu implements ActionListener {
        
        private static final String ACTION_COMMAND_ADD = "add";
        private static final String ACTION_COMMAND_REMOVE = "remove";
        private static final String ACTION_COMMAND_EDIT = "edit";
        private static final String ACTION_COMMAND_CUT = "cut";
        private static final String ACTION_COMMAND_COPY = "copy";
        private static final String ACTION_COMMAND_PASTE = "paste";
        private static final String ACTION_COMMAND_LOCK = "lock";
        private static final String ACTION_COMMAND_UNLOCK = "unlock";
        
        private final JTree _tree;
//        private final FemaleSocketTreeModel _model;
        private FemaleSocket _currentFemaleSocket;
        private TreePath _currentPath;
        
        private JMenuItem menuItemAdd;
        private JMenuItem menuItemRemove;
        private JMenuItem menuItemEdit;
        private JMenuItem menuItemCut;
        private JMenuItem menuItemCopy;
        private JMenuItem menuItemPaste;
        private JMenuItem menuItemLock;
        private JMenuItem menuItemUnlock;
        
        PopupMenu(JTree tree, FemaleSocketTreeModel model) {
            _tree = tree;
//            _model = model;
        }
        
        private void init() {
            menuItemAdd = new JMenuItem("Add");
            menuItemAdd.addActionListener(this);
            menuItemAdd.setActionCommand(ACTION_COMMAND_ADD);
            add(menuItemAdd);
            addSeparator();
            menuItemEdit = new JMenuItem("Edit");
            menuItemEdit.addActionListener(this);
            menuItemEdit.setActionCommand(ACTION_COMMAND_EDIT);
            add(menuItemEdit);
            menuItemRemove = new JMenuItem("Remove");
            menuItemRemove.addActionListener(this);
            menuItemRemove.setActionCommand(ACTION_COMMAND_REMOVE);
            add(menuItemRemove);
            addSeparator();
            menuItemCut = new JMenuItem("Cut");
            menuItemCut.addActionListener(this);
            menuItemCut.setActionCommand(ACTION_COMMAND_CUT);
            add(menuItemCut);
            menuItemCopy = new JMenuItem("Copy");
            menuItemCopy.addActionListener(this);
            menuItemCopy.setActionCommand(ACTION_COMMAND_COPY);
            add(menuItemCopy);
            menuItemPaste = new JMenuItem("Paste");
            menuItemPaste.addActionListener(this);
            menuItemPaste.setActionCommand(ACTION_COMMAND_PASTE);
            add(menuItemPaste);
            addSeparator();
            menuItemLock = new JMenuItem("Lock");
            menuItemLock.addActionListener(this);
            menuItemLock.setActionCommand(ACTION_COMMAND_LOCK);
            add(menuItemLock);
            menuItemUnlock = new JMenuItem("Unlock");
            menuItemUnlock.addActionListener(this);
            menuItemUnlock.setActionCommand(ACTION_COMMAND_UNLOCK);
            add(menuItemUnlock);
            setOpaque(true);
            setLightWeightPopupEnabled(true);
            
            _tree.addMouseListener(
                    new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (e.isPopupTrigger()) {
                                // Get the row the user has clicked on
                                TreePath path = _tree.getClosestPathForLocation(e.getX(), e.getY());
                                if (path != null) {
                                    // Check that the user has clicked on a row.
                                    Rectangle rect = _tree.getPathBounds(path);
                                    if ((e.getY() >= rect.y) && (e.getY() <= rect.y + rect.height)) {
                                        FemaleSocket femaleSocket = (FemaleSocket) path.getLastPathComponent();
                                        System.out.format("femaleSocket is a %s. %s%n", femaleSocket.getClass().getName(), femaleSocket.getLongDescription());
                                        _tree.getLocationOnScreen();
                                        _tree.getX();
                                        showPopup(e.getX(), e.getY(), femaleSocket, path);
                                    }
                                }
                            }
                        }
                    }
            );
        }
        
        private void showPopup(int x, int y, FemaleSocket femaleSocket, TreePath path) {
            _currentFemaleSocket = femaleSocket;
            _currentPath = path;
            
            boolean isConnected = femaleSocket.isConnected();
            menuItemAdd.setEnabled(!isConnected);
            menuItemRemove.setEnabled(isConnected);
            menuItemEdit.setEnabled(isConnected);
            menuItemCut.setEnabled(isConnected);
            menuItemCopy.setEnabled(isConnected);
            menuItemPaste.setEnabled(!isConnected);
            
            if (femaleSocket.isConnected()) {
                MaleSocket connectedSocket = femaleSocket.getConnectedSocket();
                menuItemLock.setEnabled(connectedSocket.getLock() == Base.Lock.NONE);
                menuItemUnlock.setEnabled(connectedSocket.getLock() == Base.Lock.USER_LOCK);
            } else {
                menuItemLock.setEnabled(false);
                menuItemUnlock.setEnabled(false);
            }
            show(_tree, x, y);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case ACTION_COMMAND_ADD:
                    addPressed(_currentFemaleSocket, _currentPath);
                    break;
                    
                case ACTION_COMMAND_EDIT:
                    editPressed(_currentFemaleSocket, _currentPath);
//                    EditMaleSocketDialog editDialog =
//                            new EditMaleSocketDialog(_currentFemaleSocket);
//                    editDialog.init(((Component)e.getSource()).getX()+_x, ((Component)e.getSource()).getY()+_y, (Component)e.getSource());
//                    dialog.init(_x, _y);
//                    dialog.setVisible(true);
                    break;
                    
                case ACTION_COMMAND_REMOVE:
                    break;
                    
                case ACTION_COMMAND_CUT:
                    break;
                    
                case ACTION_COMMAND_COPY:
                    break;
                    
                case ACTION_COMMAND_PASTE:
                    break;
                    
                case ACTION_COMMAND_LOCK:
                    break;
                    
                case ACTION_COMMAND_UNLOCK:
                    break;
                    
                default:
                    log.error("e.getActionCommand() returns unknown value {}", e.getActionCommand());
            }
        }
        
    }
    
    private final static Logger log = LoggerFactory.getLogger(LogixNGEditor.class);

}
