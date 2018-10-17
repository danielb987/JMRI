package jmri.managers.configurexml;

import java.util.List;
import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.NewLogix;
import jmri.NewLogixManager;
import jmri.managers.DefaultNewLogixManager;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the functionality for configuring NewLogixManagers
 * <P>
 *
 * @author Dave Duchamp Copyright (c) 2007
 * @author Daniel Bergqvist Copyright (c) 2018
 */
public class DefaultNewLogixManagerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public DefaultNewLogixManagerXml() {
    }

    /**
     * Default implementation for storing the contents of a LogixManager
     *
     * @param o Object to store, of type LogixManager
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        Element newLogixs = new Element("newlogixs");
        setStoreElementClass(newLogixs);
        NewLogixManager tm = (NewLogixManager) o;
        if (tm != null) {
            for (NewLogix newLogix : tm.getNamedBeanSet()) {
                log.debug("logix system name is " + newLogix.getSystemName());  // NOI18N
                boolean enabled = newLogix.getEnabled();
                Element elem = new Element("newlogix");  // NOI18N
                elem.addContent(new Element("systemName").addContent(newLogix.getSystemName()));  // NOI18N

                // As a work-around for backward compatibility, store systemName and username as attribute.
                // Remove this in e.g. JMRI 4.11.1 and then update all the loadref comparison files
                String uName = newLogix.getUserName();
                if (uName != null && !uName.isEmpty()) {
                    elem.setAttribute("userName", uName);  // NOI18N
                }

                // store common part
                storeCommon(newLogix, elem);

                if (enabled) {
                    elem.setAttribute("enabled", "yes");  // NOI18N
                } else {
                    elem.setAttribute("enabled", "no");  // NOI18N
                }
                
                newLogixs.addContent(elem);
            }
        }
        return (newLogixs);
    }

    /**
     * Subclass provides implementation to create the correct top element,
     * including the type information. Default implementation is to use the
     * local class here.
     *
     * @param newlogixs The top-level element being created
     */
    public void setStoreElementClass(Element newlogixs) {
        newlogixs.setAttribute("class", this.getClass().getName());  // NOI18N
    }

    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");  // NOI18N
    }

    /**
     * Create a NewLogixManager object of the correct class, then register and
     * fill it.
     *
     * @param sharedNewLogix  Shared top level Element to unpack.
     * @param perNodeNewLogix Per-node top level Element to unpack.
     * @return true if successful
     */
    @Override
    public boolean load(Element sharedNewLogix, Element perNodeNewLogix) {
        // create the master object
        replaceNewLogixManager();
        // load individual sharedLogix
        loadNewLogixs(sharedNewLogix);
        return true;
    }

    /**
     * Utility method to load the individual Logix objects. If there's no
     * additional info needed for a specific logix type, invoke this with the
     * parent of the set of Logix elements.
     *
     * @param newLogixs Element containing the Logix elements to load.
     */
    public void loadNewLogixs(Element newLogixs) {
        List<Element> newLogixList = newLogixs.getChildren("newlogix");  // NOI18N
        if (log.isDebugEnabled()) {
            log.debug("Found " + newLogixList.size() + " newlogixs");  // NOI18N
        }
        NewLogixManager tm = InstanceManager.getDefault(jmri.NewLogixManager.class);

        for (int i = 0; i < newLogixList.size(); i++) {

            String sysName = getSystemName(newLogixList.get(i));
            if (sysName == null) {
                log.warn("unexpected null in systemName " + newLogixList.get(i));  // NOI18N
                break;
            }

            String userName = getUserName(newLogixList.get(i));

            String yesno = "";
            if (newLogixList.get(i).getAttribute("enabled") != null) {  // NOI18N
                yesno = newLogixList.get(i).getAttribute("enabled").getValue();  // NOI18N
            }
            if (log.isDebugEnabled()) {
                log.debug("create newlogix: (" + sysName + ")("  // NOI18N
                        + (userName == null ? "<null>" : userName) + ")");  // NOI18N
            }

            NewLogix x = tm.createNewNewLogix(sysName, userName);
            if (x != null) {
                // load common part
                loadCommon(x, newLogixList.get(i));

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
                List<Element> logixConditionalList = newLogixList.get(i).getChildren("logixConditional");  // NOI18N
                if (logixConditionalList.size() > 0) {
                    // add conditionals
                    for (int n = 0; n < logixConditionalList.size(); n++) {
                        if (logixConditionalList.get(n).getAttribute("systemName") == null) {  // NOI18N
                            log.warn("unexpected null in systemName " + logixConditionalList.get(n)  // NOI18N
                                    + " " + logixConditionalList.get(n).getAttributes());
                            break;
                        }
                        String cSysName = logixConditionalList.get(n)
                                .getAttribute("systemName").getValue();  // NOI18N
                        int cOrder = Integer.parseInt(logixConditionalList.get(n)
                                .getAttribute("order").getValue());  // NOI18N
                        // add conditional to logix
                        x.addConditional(cSysName, cOrder);
                    }
                }
*/                
            }
        }
    }

    /**
     * Replace the current LogixManager, if there is one, with one newly created
     * during a load operation. This is skipped if they are of the same absolute
     * type.
     */
    protected void replaceNewLogixManager() {
        if (InstanceManager.getDefault(jmri.LogixManager.class).getClass().getName()
                .equals(DefaultNewLogixManager.class.getName())) {
            return;
        }
        // if old manager exists, remove it from configuration process
        if (InstanceManager.getNullableDefault(jmri.LogixManager.class) != null) {
            ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
            if (cmOD != null) {
                cmOD.deregister(InstanceManager.getDefault(jmri.LogixManager.class));
            }

        }

        // register new one with InstanceManager
        DefaultNewLogixManager pManager = DefaultNewLogixManager.instance();
        InstanceManager.store(pManager, NewLogixManager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.LOGIXS);
        }
    }

    @Override
    public int loadOrder() {
        return InstanceManager.getDefault(jmri.NewLogixManager.class).getXMLOrder();
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultNewLogixManagerXml.class);
}
