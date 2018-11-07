package jmri.jmrit.newlogix.tools.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.SortedSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
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
        JTree tree = new JTree();
        tree.setModel(model);
        tree.setCellRenderer(new FemaleSocketTreeRenderer());
        
        tree.setShowsRootHandles(true);

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
        // We specify the root directory when we create the model.

        protected FemaleSocket root;

        public FemaleSocketTreeModel(FemaleSocket root) {
            this.root = root;
        }

        // The model knows how to return the root object of the tree
        @Override
        public Object getRoot() {
            return root;
        }

        // Tell JTree whether an object in the tree is a leaf
        @Override
        public boolean isLeaf(Object node) {
            FemaleSocket socket = (FemaleSocket) node;
            if (!socket.isConnected()) {
                return true;
            }
            return socket.getConnectedSocket().getChildCount() == 0;
        }

        // Tell JTree how many children a node has
        @Override
        public int getChildCount(Object parent) {
            FemaleSocket socket = (FemaleSocket) parent;
            if (!socket.isConnected()) {
                return 0;
            }
            return socket.getConnectedSocket().getChildCount();
        }

        // Fetch any numbered child of a node for the JTree.
        // Our model returns File objects for all nodes in the tree.  The
        // JTree displays these by calling the File.toString() method.
        @Override
        public Object getChild(Object parent, int index) {
            FemaleSocket socket = (FemaleSocket) parent;
            if (!socket.isConnected()) {
                return null;
            }
            return socket.getConnectedSocket().getChild(index);
        }

        // Figure out a child's position in its parent node.
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

        // Since this is not an editable tree model, we never fire any events,
        // so we don't actually have to keep track of interested listeners
        @Override
        public void addTreeModelListener(TreeModelListener l) {
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
        }
        
    }
    
    
    private static final class FemaleSocketTreeRenderer implements TreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            javax.swing.JPanel panel = new javax.swing.JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            javax.swing.JCheckBox checkbox = new javax.swing.JCheckBox();
            checkbox.setSelected((row%2)==0);
            panel.add(checkbox);
            javax.swing.JTextField field = new javax.swing.JTextField();
            field.setText(value.toString());
            panel.add(field);
            javax.swing.JTextField field2 = new javax.swing.JTextField();
            FemaleSocket socket = (FemaleSocket)value;
            if (socket.isConnected()) {
                field2.setText(socket.getLongDescription());
            } else {
                field2.setText("Not connected");
            }
            panel.add(field2);
            return panel;
//            return field;
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
