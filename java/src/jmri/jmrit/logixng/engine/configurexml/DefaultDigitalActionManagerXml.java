package jmri.jmrit.logixng.engine.configurexml;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.logixng.engine.DefaultDigitalActionManager;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionManager;

/**
 * Provides the functionality for configuring ActionManagers
 * <P>
 *
 * @author Dave Duchamp Copyright (c) 2007
 * @author Daniel Bergqvist Copyright (c) 2018
 */
public class DefaultDigitalActionManagerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public DefaultDigitalActionManagerXml() {
    }

    private DigitalAction getAction(DigitalAction action) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = action.getClass().getDeclaredField("_action");
        f.setAccessible(true);
        return (DigitalAction) f.get(action);
    }
    
    /**
     * Default implementation for storing the contents of a DigitalActionManager
     *
     * @param o Object to store, of type DigitalActionManager
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        Element actions = new Element("logixngActions");
        setStoreElementClass(actions);
        DigitalActionManager tm = (DigitalActionManager) o;
        if (tm != null) {
            for (DigitalAction action : tm.getNamedBeanSet()) {
                log.debug("action system name is " + action.getSystemName());  // NOI18N
                try {
                    Element e = jmri.configurexml.ConfigXmlManager.elementFromObject(getAction(action));
                    if (e != null) {
                        actions.addContent(e);
                    }
                } catch (Exception e) {
                    log.error("Error storing action: {}", e, e);
                }
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
     * Create a DigitalActionManager object of the correct class, then register and
 fill it.
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
     * Utility method to load the individual DigitalAction objects. If there's no
 additional info needed for a specific action type, invoke this with the
 parent of the set of DigitalAction elements.
     *
     * @param actions Element containing the DigitalAction elements to load.
     */
    public void loadActions(Element actions) {
/*        
        List<Element> actionList = actions.getChildren("action");  // NOI18N
        if (log.isDebugEnabled()) {
            log.debug("Found " + actionList.size() + " actions");  // NOI18N
        }
        DigitalActionManager tm = InstanceManager.getDefault(jmri.DigitalActionManager.class);

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

            DigitalAction x = tm.createNewAction(sysName, userName);
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
     * Replace the current DigitalActionManager, if there is one, with one newly created
 during a load operation. This is skipped if they are of the same absolute
     * type.
     */
    protected void replaceActionManager() {
        if (InstanceManager.getDefault(jmri.jmrit.logixng.DigitalActionManager.class).getClass().getName()
                .equals(DefaultDigitalActionManager.class.getName())) {
            return;
        }
        // if old manager exists, remove it from configuration process
        if (InstanceManager.getNullableDefault(jmri.jmrit.logixng.DigitalActionManager.class) != null) {
            ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
            if (cmOD != null) {
                cmOD.deregister(InstanceManager.getDefault(jmri.jmrit.logixng.DigitalActionManager.class));
            }

        }

        // register new one with InstanceManager
        DefaultDigitalActionManager pManager = DefaultDigitalActionManager.instance();
        InstanceManager.store(pManager, DigitalActionManager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.ACTIONS);
        }
    }

    @Override
    public int loadOrder() {
        return InstanceManager.getDefault(jmri.jmrit.logixng.DigitalActionManager.class).getXMLOrder();
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultDigitalActionManagerXml.class);
}
