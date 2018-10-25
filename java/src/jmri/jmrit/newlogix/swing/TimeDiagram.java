package jmri.jmrit.newlogix.swing;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import jmri.InstanceManager;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.actions.ActionTurnout;
import jmri.jmrit.newlogix.expressions.ExpressionTurnout;
import jmri.util.JmriJFrame;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.Action;

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
        
        if (1==0) {
            try {
                testLoadExpression();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void initMinimumSize(Dimension dimension) {
        setMinimumSize(dimension);
        pack();
        setVisible(true);
    }
    
    @SuppressFBWarnings(value="DMI_HARDCODED_ABSOLUTE_FILENAME", justification="Only temporary for testing. Must be removed later.")
    public void testLoadExpression() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        
        String jarFileName = "F:\\Projekt\\Java\\GitHub\\JMRI_NewLogixPlugins\\dist\\JMRI_NewLogixPlugins.jar";
        
        List<String> classList = new ArrayList<>();
        try (JarInputStream jarFile = new JarInputStream(new FileInputStream(jarFileName))) {
            JarEntry jarEntry;

            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if ((jarEntry.getName().endsWith(".class"))) {
                    String className = jarEntry.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    classList.add(myClass);
                    System.out.format("AAAAAA: %s - %s%n", className, myClass);
                }
            }
        }
        
        
//        File file = new File("F:\\Projekt\\Java\\GitHub\\JMRI_NewLogixPlugins\\dist\\");
        File file = new File(jarFileName);
        
        // Convert the file to the URL format
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        
        // ------ Load this folder into Class loader - Remove comment?
        
        // Load this jar file into Class loader
        URLClassLoader cl = new URLClassLoader(urls);
        
        for (String c : classList) {
            // Load the class se.bergqvist.jmri_newlogix_plugin.ExpressionXor
            Class cls = cl.loadClass(c);
            
            if (cls.newInstance() instanceof Expression) {
                System.out.format("AAA: Class %s is an Expression%n", cls.getName());
            } else if (cls.isInstance(Expression.class)) {
                System.out.format("Class %s is an Expression%n", cls.getName());
            } else if (cls.isInstance(Action.class)) {
                System.out.format("Class %s is an Action%n", cls.getName());
            } else {
                System.out.format("Class %s is an unknown class%n", cls.getName());
            }

            cls.newInstance();
        }
        
        // Load the class se.bergqvist.jmri_newlogix_plugin.ExpressionXor
        Class cls = cl.loadClass("se.bergqvist.jmri_newlogix_plugin.ExpressionXor");
        
        // Print the location from where this class was loaded
        ProtectionDomain pDomain = cls.getProtectionDomain();
        CodeSource cSource = pDomain.getCodeSource();
        URL urlfrom = cSource.getLocation();
        System.out.format("Class from: %s%n", urlfrom.getFile());
        
        cls.newInstance();
        
        cl.close();
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
            NewLogix newLogix = InstanceManager.getDefault(jmri.jmrit.newlogix.NewLogixManager.class).createNewNewLogix("A new logix for test");  // NOI18N
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
