package jmri.jmrit.logixng.tools.swing;

import javax.swing.JMenu;

/**
 * Create a "LogixNG" menu
 *
 * @author Daniel Bergqvist Copyright 2019
 */
public class LogixNGMenu extends JMenu {

    public LogixNGMenu(String name) {
        this();
        setText(name);
    }

    public LogixNGMenu() {
        super();

        setText(Bundle.getMessage("MenuLogixNG"));

        add(new LogixNGEditorAction());
        add(new TimeDiagramAction());
    }

//    private final static Logger log = LoggerFactory.getLogger(LogixNGMenu.class);
}
