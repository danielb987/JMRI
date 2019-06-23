package jmri.jmrit.logixng.digital.actions.configurexml;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.digital.actions.ActionSensor;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ActionSensorXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ActionSensorXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleSensorSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ActionSensor p = (ActionSensor) o;

        Element element = new Element("action-sensor");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

        NamedBeanHandle sensor = p.getSensor();
        if (sensor != null) {
            element.addContent(new Element("sensor").addContent(sensor.getName()));
        }
//        element.addContent(new Element("is_isNot").addContent(p.get_Is_IsNot().name()));
        element.addContent(new Element("sensorState").addContent(p.getSensorState().name()));
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
        Element el = new Element("sensorname");
        el.setAttribute("defines", which);
        el.addContent(to.getName());
        return el;
    }

    Element addSensorElement(Sensor to) {
        String user = to.getUserName();
        String sys = to.getSystemName();

        Element el = new Element("sensor");
        el.setAttribute("systemName", sys);
        if (user != null) {
            el.setAttribute("userName", user);
        }

        return el;
    }
*/
    @Override
    public boolean load(Element shared, Element perNode) {
//        List<Element> l = shared.getChildren("sensorname");
/*        
        if (l.size() == 0) {
            l = shared.getChildren("sensor");  // older form
        }
        NamedBeanHandle<Sensor> low = loadSensor(l.get(0));
        NamedBeanHandle<Sensor> high = loadSensor(l.get(1));
*/        
        // put it together
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        DigitalAction h;
        if (uname == null) {
            h = new ActionSensor(sys);
        } else {
            h = new ActionSensor(sys, uname);
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

    private final static Logger log = LoggerFactory.getLogger(ActionSensorXml.class);
}
