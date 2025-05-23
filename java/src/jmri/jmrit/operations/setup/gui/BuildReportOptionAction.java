package jmri.jmrit.operations.setup.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Swing action to load the print options.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 * @author Daniel Boudreau Copyright (C) 2012
 */
public class BuildReportOptionAction extends AbstractAction {

    public BuildReportOptionAction() {
        super(Bundle.getMessage("TitleBuildReportOptions"));
    }

    BuildReportOptionFrame f;

    @Override
    public void actionPerformed(ActionEvent e) {
        // create a settings frame
        if (f == null || !f.isVisible()) {
            f = new BuildReportOptionFrame();
            f.initComponents();
        }
        f.setExtendedState(Frame.NORMAL);
        f.setVisible(true); // this also brings the frame into focus
    }

//    private final static Logger log = LoggerFactory.getLogger(BuildReportOptionAction.class);
}


