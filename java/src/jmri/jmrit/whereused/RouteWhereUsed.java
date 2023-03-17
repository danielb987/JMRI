package jmri.jmrit.whereused;

import javax.swing.JTextArea;
import jmri.NamedBean;
import jmri.NamedBean.DisplayOptions;

/**
 * Find Route references.
 *
 * @author Dave Sand Copyright (C) 2020
 */

public class RouteWhereUsed {

    /**
     * Populate a textarea with the where used content for the supplied sensor.
     * @param route               The route bean.
     * @param showLogixNGParents  true if the LogixNG parents should be included, false otherwise
     * @return a populated textarea.
     */
    static public JTextArea getWhereUsed(NamedBean route, boolean showLogixNGParents) {
        JTextArea textArea = new JTextArea();
        String label = Bundle.getMessage("MakeLabel", Bundle.getMessage("BeanNameRoute"));  // NOI18N
        textArea.append(Bundle.getMessage("ReferenceTitle", label, route.getDisplayName(DisplayOptions.USERNAME_SYSTEMNAME)));  // NOI18N
        textArea.append(Bundle.getMessage("ListenerCount", route.getNumPropertyChangeListeners()));  // NOI18N

        textArea.append(WhereUsedCollectors.checkLogixConditionals(route));
        textArea.append(WhereUsedCollectors.checkLogixNGConditionals(route, showLogixNGParents));
        return textArea;
    }
}
