package jmri.jmrit.whereused;

import javax.swing.JTextArea;
import jmri.NamedBean;
import jmri.NamedBean.DisplayOptions;

/**
 * Find Warrant references.
 *
 * @author Dave Sand Copyright (C) 2020
 */

public class WarrantWhereUsed {

    /**
     * Populate a textarea with the where used content for the supplied bean.
     * @param warrant             The warrant bean.
     * @param showLogixNGParents  true if the LogixNG parents should be included, false otherwise
     * @return a populated textarea.
     */
    static public JTextArea getWhereUsed(NamedBean warrant, boolean showLogixNGParents) {
        JTextArea textArea = new JTextArea();
        String label = Bundle.getMessage("MakeLabel", Bundle.getMessage("BeanNameWarrant"));  // NOI18N
        textArea.append(Bundle.getMessage("ReferenceTitle", label, warrant.getDisplayName(DisplayOptions.USERNAME_SYSTEMNAME)));  // NOI18N
        textArea.append(Bundle.getMessage("ListenerCount", warrant.getNumPropertyChangeListeners()));  // NOI18N

        textArea.append(WhereUsedCollectors.checkLogixConditionals(warrant));
        textArea.append(WhereUsedCollectors.checkLogixNGConditionals(warrant, showLogixNGParents));
        textArea.append(WhereUsedCollectors.checkOBlocks(warrant));
        textArea.append(WhereUsedCollectors.checkWarrants(warrant));
        return textArea;
    }
}
