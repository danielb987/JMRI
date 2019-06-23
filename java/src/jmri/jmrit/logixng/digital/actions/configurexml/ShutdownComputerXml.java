package jmri.jmrit.logixng.digital.actions.configurexml;

import java.util.logging.Level;
import jmri.InstanceManager;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.digital.actions.ShutdownComputer;
import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ShutdownComputerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ShutdownComputerXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleSensorSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ShutdownComputer p = (ShutdownComputer) o;

        Element element = new Element("shutdown-computer");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

//        element.addContent(new Element("is_isNot").addContent(p.get_Is_IsNot().name()));
        element.setAttribute("seconds", Integer.toString(p.getSeconds()));
/*        
        if (p.isUserEnabled()) {
            element.addContent(new Element("enabled").addContent("yes"));
        } else {
            element.addContent(new Element("enabled").addContent("no"));
        }
*/
        storeCommon(p, element);

        return element;
    }
/*
    Element addSensorElement(NamedBeanHandle<Sensor> to, String which) {
        Element el = new Element("shutdown-computername");
        el.setAttribute("defines", which);
        el.addContent(to.getName());
        return el;
    }

    Element addSensorElement(Sensor to) {
        String user = to.getUserName();
        String sys = to.getSystemName();

        Element el = new Element("shutdown-computer");
        el.setAttribute("systemName", sys);
        if (user != null) {
            el.setAttribute("userName", user);
        }

        return el;
    }
*/
    @Override
    public boolean load(Element shared, Element perNode) {
//        List<Element> l = shared.getChildren("shutdown-computername");
/*        
        if (l.size() == 0) {
            l = shared.getChildren("shutdown-computer");  // older form
        }
        NamedBeanHandle<Sensor> low = loadSensor(l.get(0));
        NamedBeanHandle<Sensor> high = loadSensor(l.get(1));
*/        
        // put it together
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        
        int seconds = 0;
        try {
            seconds = shared.getAttribute("seconds").getIntValue();
        } catch (DataConversionException ex) {
            log.error("seconds attribute is not an integer", ex);
        }
        
        DigitalAction h;
        if (uname == null) {
            h = new ShutdownComputer(sys, seconds);
        } else {
            h = new ShutdownComputer(sys, uname, seconds);
        }

        loadCommon(h, shared);

        // this.checkedNamedBeanReference()
        // <T extends NamedBean> T checkedNamedBeanReference(String name, @Nonnull T type, @Nonnull Manager<T> m) {

        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }
    
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(ShutdownComputerXml.class);
}
