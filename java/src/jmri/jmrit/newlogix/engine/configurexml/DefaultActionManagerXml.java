package jmri.jmrit.newlogix.engine.configurexml;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.jmrit.newlogix.ActionManager;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.newlogix.engine.DefaultActionManager;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.newlogix.Action;

/**
 * Provides the functionality for configuring ActionManagers
 * <P>
 *
 * @author Dave Duchamp Copyright (c) 2007
 * @author Daniel Bergqvist Copyright (c) 2018
 */
public class DefaultActionManagerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public DefaultActionManagerXml() {
    }

    private Action getAction(Action action) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = action.getClass().getDeclaredField("_action");
        f.setAccessible(true);
        return (Action) f.get(action);
    }
    
    /**
     * Default implementation for storing the contents of a ActionManager
     *
     * @param o Object to store, of type ActionManager
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        Element actions = new Element("newlogixActions");
        setStoreElementClass(actions);
        ActionManager tm = (ActionManager) o;
        if (tm != null) {
            for (Action action : tm.getNamedBeanSet()) {
                log.debug("action system name is " + action.getSystemName());  // NOI18N
                try {
                    Element e = jmri.configurexml.ConfigXmlManager.elementFromObject(getAction(action));
                    if (e != null) {
                        actions.addContent(e);
                    }
                } catch (Exception e) {
                    log.error("Error storing action: {}", e, e);
                }
/*                
                boolean enabled = action.getEnabled();
                Element elem = new Element("newlogix");  // NOI18N
                elem.addContent(new Element("systemName").addContent(action.getSystemName()));  // NOI18N

                // As a work-around for backward compatibility, store systemName and username as attribute.
                // Remove this in e.g. JMRI 4.11.1 and then update all the loadref comparison files
                String uName = action.getUserName();
                if (uName != null && !uName.isEmpty()) {
                    elem.setAttribute("userName", uName);  // NOI18N
                }

                // store common part
                storeCommon(action, elem);

                if (enabled) {
                    elem.setAttribute("enabled", "yes");  // NOI18N
                } else {
                    elem.setAttribute("enabled", "no");  // NOI18N
                }
                
                newLogixs.addContent(elem);
*/                
            }
        }
        return (actions);
    }

    /**
     * Subclass provides implementation to create the correct top element,
     * including the type information. Default implementation is to use the
     * local class here.
     *
     * @param actions The top-level element being created
     */
    public void setStoreElementClass(Element actions) {
        actions.setAttribute("class", this.getClass().getName());  // NOI18N
    }

    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");  // NOI18N
    }

    /**
     * Create a ActionManager object of the correct class, then register and
     * fill it.
     *
     * @param sharedAction  Shared top level Element to unpack.
     * @param perNodeAction Per-node top level Element to unpack.
     * @return true if successful
     */
    @Override
    public boolean load(Element sharedAction, Element perNodeAction) {
        // create the master object
        replaceActionManager();
        // load individual sharedAction
        loadActions(sharedAction);
        return true;
    }

    /**
     * Utility method to load the individual Action objects. If there's no
 additional info needed for a specific action type, invoke this with the
 parent of the set of Action elements.
     *
     * @param actions Element containing the Action elements to load.
     */
    public void loadActions(Element actions) {
/*        
        List<Element> actionList = actions.getChildren("action");  // NOI18N
        if (log.isDebugEnabled()) {
            log.debug("Found " + actionList.size() + " actions");  // NOI18N
        }
        ActionManager tm = InstanceManager.getDefault(jmri.ActionManager.class);

        for (int i = 0; i < actionList.size(); i++) {

            String sysName = getSystemName(actionList.get(i));
            if (sysName == null) {
                log.warn("unexpected null in systemName " + actionList.get(i));  // NOI18N
                break;
            }

            String userName = getUserName(actionList.get(i));

            String yesno = "";
            if (actionList.get(i).getAttribute("enabled") != null) {  // NOI18N
                yesno = actionList.get(i).getAttribute("enabled").getValue();  // NOI18N
            }
            if (log.isDebugEnabled()) {
                log.debug("create action: (" + sysName + ")("  // NOI18N
                        + (userName == null ? "<null>" : userName) + ")");  // NOI18N
            }

            Action x = tm.createNewAction(sysName, userName);
            if (x != null) {
                // load common part
                loadCommon(x, actionList.get(i));

                // set enabled/disabled if attribute was present
                if ((yesno != null) && (!yesno.equals(""))) {
                    if (yesno.equals("yes")) {  // NOI18N
                        x.setEnabled(true);
                    } else if (yesno.equals("no")) {  // NOI18N
                        x.setEnabled(false);
                    }
                }
/*                
                // load conditionals, if there are any
                List<Element> actionConditionalList = actionList.get(i).getChildren("actionConditional");  // NOI18N
                if (actionConditionalList.size() > 0) {
                    // add conditionals
                    for (int n = 0; n < actionConditionalList.size(); n++) {
                        if (actionConditionalList.get(n).getAttribute("systemName") == null) {  // NOI18N
                            log.warn("unexpected null in systemName " + actionConditionalList.get(n)  // NOI18N
                                    + " " + actionConditionalList.get(n).getAttributes());
                            break;
                        }
                        String cSysName = actionConditionalList.get(n)
                                .getAttribute("systemName").getValue();  // NOI18N
                        int cOrder = Integer.parseInt(actionConditionalList.get(n)
                                .getAttribute("order").getValue());  // NOI18N
                        // add conditional to action
                        x.addConditional(cSysName, cOrder);
                    }
                }
*./                
            }
        }
*/
    }

    /**
     * Replace the current ActionManager, if there is one, with one newly created
     * during a load operation. This is skipped if they are of the same absolute
     * type.
     */
    protected void replaceActionManager() {
        if (InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class).getClass().getName()
                .equals(DefaultActionManager.class.getName())) {
            return;
        }
        // if old manager exists, remove it from configuration process
        if (InstanceManager.getNullableDefault(jmri.jmrit.newlogix.ActionManager.class) != null) {
            ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
            if (cmOD != null) {
                cmOD.deregister(InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class));
            }

        }

        // register new one with InstanceManager
        DefaultActionManager pManager = DefaultActionManager.instance();
        InstanceManager.store(pManager, ActionManager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.ACTIONS);
        }
    }

    @Override
    public int loadOrder() {
        return InstanceManager.getDefault(jmri.jmrit.newlogix.ActionManager.class).getXMLOrder();
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultActionManagerXml.class);
}
