package jmri.daniel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jmri.jmrix.jinput.TreeModel;

import net.java.games.input.Component;
import net.java.games.input.Controller;


public class Daniel implements PropertyChangeListener {

    private static final String desiredControllerName = "";

    public Daniel(Controller controller) {
        
        log.error("Daniel: {}", controller.getName());
        
/*        
        TreeModel model = TreeModel.instance();

        // Iterate over the controllers, creating a new listener for each
        // controller of the type we are interested in
        for (var c : model.controllers()) {
            String name = c.getName();
            int hashCode = c.hashCode();
            log.error("AAA Found {}, {}", name, hashCode);
            if (name == desiredControllerName && c.getType().toString().equals("Gamepad")) {
                log.error("Found {}, {}", name, hashCode);
                model.addPropertyChangeListener(new Daniel(c));
            }
        }
*/
    }
    
    public void propertyChange(PropertyChangeEvent event) {
        log.error("Event: {}, Source: {}, Old: {}, New: {}", event.getPropertyName(), event.getSource(), event.getOldValue(), event.getNewValue());
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Daniel.class);
}
