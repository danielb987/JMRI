package jmri.jmrix.loconet.nodes.swing;

import java.awt.GraphicsEnvironment;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jmri.jmrix.ConnectionConfig;
import jmri.util.JmriJFrame;
import jmri.util.ThreadingUtil;
import jmri.util.ThreadingUtil.ThreadAction;
import jmri.util.swing.JemmyUtil;
import org.junit.Assume;
import org.netbeans.jemmy.operators.JFrameOperator;

/**
 * Test the "Configure nodes" button in the ConnectionConfig classes.
 *
 * @author Daniel Bergqvist Copyright (C) 2020
 **/
public class ConnectionConfigScaffold {
    
    private static final ResourceBundle rbx = ResourceBundle.getBundle("jmri.NamedBeanBundle");
    
    public static void testConfigNodes(ConnectionConfig cc) {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        ThreadingUtil.runOnGUI(() -> {
            JmriJFrame frame = new JmriJFrame();
            frame.setTitle("ConfigNodePanel");  // NOI18N
            frame.setSize(800, 300);
            JPanel detailsPanel = new JPanel();
            frame.getContentPane().add(detailsPanel);
            
            // verify no exceptions thrown
            cc.loadDetails(detailsPanel);
            
            frame.setVisible(true);
        });
        
        // load details MAY produce an error message if no ports are found.
        jmri.util.JUnitAppender.suppressErrorMessage("No usable ports returned");
        
        // Find the discover throttle frame
        JFrame f1 = JFrameOperator.waitJFrame("ConfigNodePanel", true, true);
        JFrameOperator jf1 = new JFrameOperator(f1);
        
        // And press Configure nodes
        JemmyUtil.pressButton(jf1,Bundle.getMessage("ConfigureNodesButton"));
        
        // Find the configure nodes frame
        JFrame f2 = JFrameOperator.waitJFrame(Bundle.getMessage("ConfigureNodesWindowTitle"), true, true);
        JFrameOperator jf2 = new JFrameOperator(f2);
        
        // And press Done
        JemmyUtil.pressButton(jf2,rbx.getString("ButtonDone"));
        
        jf1.requestClose();
    }
    
}
