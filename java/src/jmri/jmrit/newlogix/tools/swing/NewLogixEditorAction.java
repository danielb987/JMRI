package jmri.jmrit.newlogix.tools.swing;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Swing action to create and register a TimeDiagram object.
 *
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class NewLogixEditorAction extends AbstractAction {

    public NewLogixEditorAction(String s) {
        super(s);
    }

    public NewLogixEditorAction() {
        this(Bundle.getMessage("MenuNewLogixEditor")); // NOI18N
    }

    static NewLogixEditor newLogixEditorFrame = null;

    @Override
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "Only one OperationsSetupFrame")
    public void actionPerformed(ActionEvent e) {
        // create a settings frame
        if (newLogixEditorFrame == null || !newLogixEditorFrame.isVisible()) {
            newLogixEditorFrame = new NewLogixEditor();
            newLogixEditorFrame.initComponents();
        }
        newLogixEditorFrame.setExtendedState(Frame.NORMAL);
        newLogixEditorFrame.setVisible(true); // this also brings the frame into focus
    }
    
}
