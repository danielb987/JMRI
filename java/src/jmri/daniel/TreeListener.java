package jmri.daniel;

import jmri.jmrix.jinput.TreeModel;

public class TreeListener {

    private static final String desiredControllerName = "Logitech K400";

    public TreeListener() {
        
        TreeModel model = TreeModel.instance();

        // Iterate over the controllers, creating a new listener for each
        // controller of the type we are interested in
        for (var c : model.controllers()) {
            String name = c.getName();
            int hashCode = c.hashCode();
            log.error("AAA Found {}, {}. Type: {}", name, hashCode, c.getType().toString());
//            if (name == desiredControllerName && c.getType().toString().equals("Gamepad")) {
            if (name.equals(desiredControllerName) && c.getType().toString().equals("Mouse")) {
                log.error("Found {}, {}", name, hashCode);
                model.addPropertyChangeListener(new Daniel(c));
            }
        }

    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TreeListener.class);
}
