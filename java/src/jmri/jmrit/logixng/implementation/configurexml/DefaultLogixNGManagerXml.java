package jmri.jmrit.logixng.implementation.configurexml;

import java.util.List;
import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.jmrit.logixng.implementation.DefaultLogixNGManager;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.MaleSocket;

/**
 * Provides the functionality for configuring LogixNGManagers
 * <P>
 *
 * @author Dave Duchamp Copyright (c) 2007
 * @author Daniel Bergqvist Copyright (c) 2018
 */
public class DefaultLogixNGManagerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public DefaultLogixNGManagerXml() {
    }

    /**
     * Default implementation for storing the contents of a LogixManager
     *
     * @param o Object to store, of type LogixManager
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        Element logixNGs = new Element("logixngs");
        setStoreElementClass(logixNGs);
        LogixNG_Manager tm = (LogixNG_Manager) o;
        if (tm != null) {
            for (LogixNG logixNG : tm.getNamedBeanSet()) {
                log.debug("logix system name is " + logixNG.getSystemName());  // NOI18N
                boolean enabled = logixNG.isEnabled();
                Element elem = new Element("logixng");  // NOI18N
                elem.addContent(new Element("systemName").addContent(logixNG.getSystemName()));  // NOI18N

                Element e2 = new Element("socket");
                e2.addContent(new Element("socketName").addContent(logixNG.getChild(0).getName()));
                MaleSocket socket = logixNG.getChild(0).getConnectedSocket();
                if (socket != null) {
                    e2.addContent(new Element("systemName").addContent(socket.getSystemName()));
                }
                elem.addContent(e2);
            
//                // As a work-around for backward compatibility, store systemName and username as attribute.
//                // Remove this in e.g. JMRI 4.11.1 and then update all the loadref comparison files
//                String uName = logixNG.getUserName();
//                if (uName != null && !uName.isEmpty()) {
//                    elem.setAttribute("userName", uName);  // NOI18N
//                }

                // store common part
                storeCommon(logixNG, elem);

                if (enabled) {
                    elem.setAttribute("enabled", "yes");  // NOI18N
                } else {
                    elem.setAttribute("enabled", "no");  // NOI18N
                }
                
                logixNGs.addContent(elem);
            }
        }
        return (logixNGs);
    }

    /**
     * Subclass provides implementation to create the correct top element,
     * including the type information. Default implementation is to use the
     * local class here.
     *
     * @param logixngs The top-level element being created
     */
    public void setStoreElementClass(Element logixngs) {
        logixngs.setAttribute("class", this.getClass().getName());  // NOI18N
    }

    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");  // NOI18N
    }

    /**
     * Create a LogixNG_Manager object of the correct class, then register and
     * fill it.
     *
     * @param sharedLogixNG  Shared top level Element to unpack.
     * @param perNodeLogixNG Per-node top level Element to unpack.
     * @return true if successful
     */
    @Override
    public boolean load(Element sharedLogixNG, Element perNodeLogixNG) {
        // create the master object
        replaceLogixNGManager();
        // load individual sharedLogix
        loadLogixNGs(sharedLogixNG);
        return true;
    }

    /**
     * Utility method to load the individual Logix objects. If there's no
     * additional info needed for a specific logix type, invoke this with the
     * parent of the set of Logix elements.
     *
     * @param logixNGs Element containing the Logix elements to load.
     */
    public void loadLogixNGs(Element logixNGs) {
        List<Element> logixNGList = logixNGs.getChildren("logixng");  // NOI18N
        if (log.isDebugEnabled()) {
            log.debug("Found " + logixNGList.size() + " logixngs");  // NOI18N
        }
        LogixNG_Manager tm = InstanceManager.getDefault(jmri.jmrit.logixng.LogixNG_Manager.class);

        for (int i = 0; i < logixNGList.size(); i++) {

            String sysName = getSystemName(logixNGList.get(i));
            if (sysName == null) {
                log.warn("unexpected null in systemName " + logixNGList.get(i));  // NOI18N
                break;
            }

            String userName = getUserName(logixNGList.get(i));

            String yesno = "";
            if (logixNGList.get(i).getAttribute("enabled") != null) {  // NOI18N
                yesno = logixNGList.get(i).getAttribute("enabled").getValue();  // NOI18N
            }
            if (log.isDebugEnabled()) {
                log.debug("create logixng: (" + sysName + ")("  // NOI18N
                        + (userName == null ? "<null>" : userName) + ")");  // NOI18N
            }

            LogixNG x = tm.createLogixNG(sysName, userName);
            if (x != null) {
                // load common part
                loadCommon(x, logixNGList.get(i));

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
                List<Element> logixConditionalList = logixNGList.get(i).getChildren("logixConditional");  // NOI18N
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
    protected void replaceLogixNGManager() {
        if (InstanceManager.getDefault(jmri.jmrit.logixng.LogixNG_Manager.class).getClass().getName()
                .equals(DefaultLogixNGManager.class.getName())) {
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
        DefaultLogixNGManager pManager = DefaultLogixNGManager.instance();
        InstanceManager.store(pManager, LogixNG_Manager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.LOGIXNGS);
        }
    }

    @Override
    public int loadOrder() {
        return InstanceManager.getDefault(jmri.jmrit.logixng.LogixNG_Manager.class).getXMLOrder();
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultLogixNGManagerXml.class);
}
