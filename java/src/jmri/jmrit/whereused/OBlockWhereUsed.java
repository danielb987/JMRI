package jmri.jmrit.whereused;

import javax.swing.JTextArea;
import jmri.NamedBean;
import jmri.NamedBean.DisplayOptions;

/**
 * Find OBlock references.
 *
 * @author Dave Sand Copyright (C) 2020
 */

public class OBlockWhereUsed {

    /**
     * Populate a textarea with the where used content for the supplied sensor.
     * @param oblock              The oblock bean.
     * @param showLogixNGParents  true if the LogixNG parents should be included, false otherwise
     * @return a populated textarea.
     */
    static public JTextArea getWhereUsed(NamedBean oblock, boolean showLogixNGParents) {
        JTextArea textArea = new JTextArea();
        String label = Bundle.getMessage("MakeLabel", Bundle.getMessage("BeanNameOBlock"));  // NOI18N
        textArea.append(Bundle.getMessage("ReferenceTitle", label, oblock.getDisplayName(DisplayOptions.USERNAME_SYSTEMNAME)));  // NOI18N
        textArea.append(Bundle.getMessage("ListenerCount", oblock.getNumPropertyChangeListeners()));  // NOI18N

        textArea.append(WhereUsedCollectors.checkLogixConditionals(oblock));
        textArea.append(WhereUsedCollectors.checkLogixNGConditionals(oblock, showLogixNGParents));
        textArea.append(WhereUsedCollectors.checkOBlocks(oblock));
        textArea.append(WhereUsedCollectors.checkWarrants(oblock));
        textArea.append(WhereUsedCollectors.checkPanels(oblock));
        return textArea;
    }
}
