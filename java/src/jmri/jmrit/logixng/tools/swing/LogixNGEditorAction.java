package jmri.jmrit.logixng.tools.swing;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Swing action to create and register a LogixNGEditor object.
 *
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class LogixNGEditorAction extends AbstractAction {

    public LogixNGEditorAction(String s) {
        super(s);
    }

    public LogixNGEditorAction() {
        this(Bundle.getMessage("MenuLogixNGEditor")); // NOI18N
    }

    static LogixNGEditor newLogixEditorFrame = null;

    @Override
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "Only one LogixNGEditorFrame")
    public void actionPerformed(ActionEvent e) {
        // create a settings frame
        if (newLogixEditorFrame == null || !newLogixEditorFrame.isVisible()) {
            newLogixEditorFrame = new LogixNGEditor();
            newLogixEditorFrame.initComponents();
        }
        newLogixEditorFrame.setExtendedState(Frame.NORMAL);
        newLogixEditorFrame.setVisible(true); // this also brings the frame into focus
    }
    
}
