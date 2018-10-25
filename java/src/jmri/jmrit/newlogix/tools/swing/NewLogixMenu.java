/**
 * OperationsMenu.java
 */
package jmri.jmrit.newlogix.tools.swing;

import javax.swing.JMenu;

/**
 * Create a "Operations" menu
 *
 * @author Bob Jacobsen Copyright 2003
 * @author Daniel Boudreau Copyright 2008
 */
public class NewLogixMenu extends JMenu {

    public NewLogixMenu(String name) {
        this();
        setText(name);
    }

    public NewLogixMenu() {
        super();

        setText(Bundle.getMessage("MenuNewLogix"));

        add(new NewLogixEditorAction());
        add(new TimeDiagramAction());
//        add(new jmri.jmrit.operations.setup.OperationsSetupAction());
//        add(new jmri.jmrit.operations.locations.LocationsTableAction());
//        add(new jmri.jmrit.operations.rollingstock.cars.CarsTableAction());
//        add(new jmri.jmrit.operations.rollingstock.engines.EnginesTableAction());
//        add(new jmri.jmrit.operations.routes.RoutesTableAction());
//        add(new jmri.jmrit.operations.trains.TrainsTableAction());
    }

//    private final static Logger log = LoggerFactory.getLogger(OperationsMenu.class);
}
