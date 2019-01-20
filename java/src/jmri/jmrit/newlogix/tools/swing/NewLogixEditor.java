package jmri.jmrit.newlogix.tools.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.NewLogixManager;
import jmri.InstanceManager;
import jmri.jmrit.newlogix.Action;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.MaleActionSocket;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.MaleSocket;
import jmri.jmrit.newlogix.actions.ActionIfThen;
import jmri.jmrit.newlogix.actions.ActionTurnout;
import jmri.jmrit.newlogix.expressions.ExpressionTurnout;
import jmri.util.JmriJFrame;

/**
 * Editor of NewLogix
 */
public class NewLogixEditor extends JmriJFrame {

    private static final int panelWidth700 = 700;
    private static final int panelHeight500 = 500;
    
    NewLogixEditor() {
        
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        // build menu
        JMenuBar menuBar = new JMenuBar();
        JMenu toolMenu = new JMenu(Bundle.getMessage("MenuTools"));
        toolMenu.add(new TimeDiagram.CreateNewNewLogixAction("Create a NewLogix"));
/*        
        toolMenu.add(new CreateNewNewLogixAction(Bundle.getMessage("TitleOptions")));
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
        InstanceManager.getDefault(jmri.jmrit.newlogix.NewLogixManager.class).createNewNewLogix("A new logix for test");  // NOI18N
/*        
        String systemName;
        NewLogix newLogix = InstanceManager.getDefault(jmri.jmrit.newlogix.NewLogixManager.class).createNewNewLogix("A new logix for test");  // NOI18N
        systemName = InstanceManager.getDefault(jmri.jmrit.newlogix.ExpressionManager.class).getNewSystemName(newLogix);
        Expression expression = new ExpressionTurnout(systemName, "An expression for test");  // NOI18N
        MaleExpressionSocket expressionSocket = InstanceManager.getDefault(jmri.jmrit.newlogix.ExpressionManager.class).register(expression);
//        InstanceManager.getDefault(jmri.ExpressionManager.class).addExpression(new ExpressionTurnout(systemName, "NewLogix 102, Expression 26"));  // NOI18N
        systemName = InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class).getNewSystemName(newLogix);
        Action actionTurnout = new ActionTurnout(systemName, "An action for test");  // NOI18N
        MaleActionSocket actionSocket = InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class).register(actionTurnout);
        systemName = InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class).getNewSystemName(newLogix);
        Action actionIfThen = new ActionIfThen(systemName, ActionIfThen.Type.TRIGGER_ACTION, "A", "B", expressionSocket, actionSocket);
        actionSocket = InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class).register(actionIfThen);
        newLogix.getFemaleSocket().connect(actionSocket);
*/        
        
        // Figure out where in the filesystem to start displaying
//        File root;
//        root = new File(System.getProperty("user.home"));
        FemaleSocket root;
        SortedSet<NewLogix> newLogixSet = InstanceManager.getDefault(NewLogixManager.class).getNamedBeanSet();
        for (NewLogix nl : newLogixSet) {
            System.out.format("NewLogix: %s%n", nl.toString());
            System.out.format("NewLogix female socket: %s. Connected: %b%n", nl.getFemaleSocket().toString(), nl.getFemaleSocket().isConnected());
        }
        root = newLogixSet.first().getFemaleSocket();

        // Create a TreeModel object to represent our tree of files
//        FileTreeModel model = new FileTreeModel(root);
//        // Create a TreeModel object to represent our tree of files
        FemaleSocketTreeModel model = new FemaleSocketTreeModel(root);

        // Create a JTree and tell it to display our model
        final JTree tree = new JTree();
        tree.setModel(model);
        tree.setCellRenderer(new FemaleSocketTreeRenderer());
        
        tree.setShowsRootHandles(true);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem mi = new JMenuItem("Insert a children");
//            mi.addActionListener(this);
        mi.setActionCommand("insert");
        popup.add(mi);
        mi = new JMenuItem("Remove this node");
//            mi.addActionListener(this);
        mi.setActionCommand("remove");
        popup.add(mi);
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);
        
        tree.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                            tree.setSelectionRow(row);
                            Object c = e.getSource();
                            System.out.format("Component is a %s%n", c.getClass().getName());
                            TreePath path = tree.getSelectionPath();
                            DefaultMutableTreeNode dmtn = null;
                            if (path != null) {
//                                dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
                                FemaleSocket femaleSocket = (FemaleSocket) path.getLastPathComponent();
                                System.out.format("femaleSocket is a %s. %s%n", femaleSocket.getClass().getName(), femaleSocket.getLongDescription());
                            }
                            popup.show((JComponent) e.getSource(), e.getX(), e.getY());
                        }
                    }
                }
        );
        
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
    
    
    
    
    /**
     * The methods in this class allow the JTree component to traverse the file
     * system tree and display the files and directories.
     */
    private static class FemaleSocketTreeModel implements TreeModel {

        private FemaleSocket root;
        private List<TreeModelListener> listeners = new ArrayList<>();

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
            MaleSocket maleSocket = socket.getConnectedSocket();
            for (int i = 0; i < maleSocket.getChildCount(); i++) {
                if (child == maleSocket.getChild(i)) {
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
    
/*    
    private static final class FemaleSocketPanel extends JPanel {
        
        private final FemaleSocket _socket;
        FemaleSocketPanel(FemaleSocket socket) {
            _socket = socket;
        }
    }
*/    
    
    private static final class FemaleSocketTreeRenderer implements TreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            
            FemaleSocket socket = (FemaleSocket)value;
            
//            FemaleSocketPanel panel = new FemaleSocketPanel(socket);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            panel.setOpaque(false);
            
            JLabel socketLabel = new JLabel(socket.getShortDescription());
            Font font = socketLabel.getFont();
            socketLabel.setFont(font.deriveFont((float)(font.getSize2D()*1.7)));
            socketLabel.setForeground(Color.red);
            panel.add(socketLabel);
            
            panel.add(javax.swing.Box.createRigidArea(new Dimension(5,0)));
            
            JLabel socketNameLabel = new JLabel(socket.getName());
            socketNameLabel.setForeground(Color.red);
            panel.add(socketNameLabel);
            
            panel.add(javax.swing.Box.createRigidArea(new Dimension(5,0)));
            
            JLabel connectedItemLabel = new JLabel();
            if (socket.isConnected()) {
                connectedItemLabel.setText(socket.getConnectedSocket().getLongDescription());
//            } else {
//                connectedItemLabel.setText("Not connected");
            }
            panel.add(connectedItemLabel);
            
            return panel;
        }
        
    }
    
}
