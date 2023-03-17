package jmri.jmrit.whereused;

import javax.swing.JTextArea;
import jmri.NamedBean;
import jmri.NamedBean.DisplayOptions;

/**
 * Find signal head references.
 *
 * @author Dave Sand Copyright (C) 2020
 */

public class SignalHeadWhereUsed {

    /**
     * Populate a textarea with the where used content for the supplied signal head.
     * @param signalHead          The signal head bean.
     * @param showLogixNGParents  true if the LogixNG parents should be included, false otherwise
     * @return a populated textarea.
     */
    static public JTextArea getWhereUsed(NamedBean signalHead, boolean showLogixNGParents) {
        JTextArea textArea = new JTextArea();
        String label = Bundle.getMessage("MakeLabel", Bundle.getMessage("BeanNameSignalHead"));  // NOI18N
        textArea.append(Bundle.getMessage("ReferenceTitle", label, signalHead.getDisplayName(DisplayOptions.USERNAME_SYSTEMNAME)));  // NOI18N
        textArea.append(Bundle.getMessage("ListenerCount", signalHead.getNumPropertyChangeListeners()));  // NOI18N

        textArea.append(WhereUsedCollectors.checkSignalHeadLogic(signalHead));
        textArea.append(WhereUsedCollectors.checkSignalGroups(signalHead));
        textArea.append(WhereUsedCollectors.checkOBlocks(signalHead));
        textArea.append(WhereUsedCollectors.checkWarrants(signalHead));
        textArea.append(WhereUsedCollectors.checkEntryExit(signalHead));
        textArea.append(WhereUsedCollectors.checkLogixConditionals(signalHead));
        textArea.append(WhereUsedCollectors.checkLogixNGConditionals(signalHead, showLogixNGParents));
        textArea.append(WhereUsedCollectors.checkPanels(signalHead));
        textArea.append(WhereUsedCollectors.checkCTC(signalHead));
        return textArea;
    }
}
