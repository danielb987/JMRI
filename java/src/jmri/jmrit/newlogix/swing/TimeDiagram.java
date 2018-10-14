package jmri.jmrit.newlogix.swing;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import jmri.Action;
import jmri.Expression;
import jmri.InstanceManager;
import jmri.NewLogix;
import jmri.jmrit.newlogix.ActionTurnout;
import jmri.jmrit.newlogix.ExpressionTurnout;
import jmri.util.JmriJFrame;

/**
 *
 */
public class TimeDiagram extends JmriJFrame {
    
    private static final int panelWidth700 = 700;
    private static final int panelHeight500 = 500;
    
    
    public TimeDiagram() {
        
    }

    @Override
    public void initComponents() {
        super.initComponents();
        // build menu
        JMenuBar menuBar = new JMenuBar();
        JMenu toolMenu = new JMenu(Bundle.getMessage("MenuTools"));
        toolMenu.add(new CreateNewNewLogixAction("Create a NewLogix"));
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

        initMinimumSize(new Dimension(panelWidth700, panelHeight500));
    }

    public void initMinimumSize(Dimension dimension) {
        setMinimumSize(dimension);
        pack();
        setVisible(true);
    }
    
    
    
    /**
     * Swing action to load the options frame.
     *
     * @author Bob Jacobsen Copyright (C) 2001
     * @author Daniel Boudreau Copyright (C) 2010
     */
    public static class CreateNewNewLogixAction extends AbstractAction {

        public CreateNewNewLogixAction(String s) {
            super(s);
        }

//        OptionFrame f = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            
            String systemName;
            NewLogix newLogix = InstanceManager.getDefault(jmri.NewLogixManager.class).createNewNewLogix("A new logix for test");  // NOI18N
            systemName = InstanceManager.getDefault(jmri.ExpressionManager.class).getNewSystemName(newLogix);
            Expression expression = new ExpressionTurnout(systemName, "An expression for test");  // NOI18N
            InstanceManager.getDefault(jmri.ExpressionManager.class).addExpression(expression);
//            InstanceManager.getDefault(jmri.ExpressionManager.class).addExpression(new ExpressionTurnout(systemName, "NewLogix 102, Expression 26"));  // NOI18N
            systemName = InstanceManager.getDefault(jmri.ActionManager.class).getNewSystemName(newLogix);
            Action action = new ActionTurnout(systemName, "An action for test");  // NOI18N
            InstanceManager.getDefault(jmri.ActionManager.class).addAction(action);
/*            
            if (f == null || !f.isVisible()) {
                f = new OptionFrame();
                f.initComponents();
            }
            f.setExtendedState(Frame.NORMAL);
            f.setVisible(true); // this also brings the frame into focus
*/
        }
    }

}
