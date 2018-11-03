package jmri.jmrit.newlogix.tools.swing;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.event.TreeModelListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import jmri.jmrit.newlogix.NewLogix;
import jmri.util.JmriJFrame;
import jmri.jmrit.newlogix.actions.ActionTurnout;
import jmri.jmrit.newlogix.expressions.ExpressionTurnout;

import java.io.File;
import javax.swing.tree.TreePath;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.Action;

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

        // Figure out where in the filesystem to start displaying
        File root;
        root = new File(System.getProperty("user.home"));

        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);

        // Create a JTree and tell it to display our model
        JTree tree = new JTree();
        tree.setModel(model);

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
    
    
    
    
    //****************************************
    // For testing
    //****************************************
    
    /**
     * The methods in this class allow the JTree component to traverse the file
     * system tree and display the files and directories.
     */
    private static class FileTreeModel implements TreeModel {
        // We specify the root directory when we create the model.

        protected File root;

        public FileTreeModel(File root) {
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
            return ((File) node).isFile();
        }

        // Tell JTree how many children a node has
        @Override
        public int getChildCount(Object parent) {
            String[] children = ((File) parent).list();
            if (children == null) {
                return 0;
            }
            return children.length;
        }

        // Fetch any numbered child of a node for the JTree.
        // Our model returns File objects for all nodes in the tree.  The
        // JTree displays these by calling the File.toString() method.
        @Override
        public Object getChild(Object parent, int index) {
            String[] children = ((File) parent).list();
            if ((children == null) || (index >= children.length)) {
                return null;
            }
            return new File((File) parent, children[index]);
        }

        // Figure out a child's position in its parent node.
        @Override
        public int getIndexOfChild(Object parent, Object child) {
            String[] children = ((File) parent).list();
            if (children == null) {
                return -1;
            }
            String childname = ((File) child).getName();
            for (int i = 0; i < children.length; i++) {
                if (childname.equals(children[i])) {
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
    
    
}
