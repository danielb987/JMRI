package jmri.jmrit.logixng.digital.expressions.configurexml;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.digital.expressions.ExpressionSensor;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ExpressionSensorXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ExpressionSensorXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleSensorSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ExpressionSensor p = (ExpressionSensor) o;

        Element element = new Element("expression-sensor");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

        NamedBeanHandle sensor = p.getSensor();
        if (sensor != null) {
            element.addContent(new Element("sensor").addContent(sensor.getName()));
        }
        element.addContent(new Element("is_isNot").addContent(p.get_Is_IsNot().name()));
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
        DigitalExpression h;
        if (uname == null) {
            h = new ExpressionSensor(sys);
        } else {
            h = new ExpressionSensor(sys, uname);
        }

        loadCommon(h, shared);

        InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(h);
        return true;
    }
    
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(ExpressionSensorXml.class);
}
