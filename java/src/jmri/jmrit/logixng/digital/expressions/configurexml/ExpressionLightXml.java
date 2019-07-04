package jmri.jmrit.logixng.digital.expressions.configurexml;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.digital.expressions.ExpressionLight;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalExpressionBean;

/**
 *
 */
public class ExpressionLightXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ExpressionLightXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleLightSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ExpressionLight p = (ExpressionLight) o;

        Element element = new Element("expression-light");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

        NamedBeanHandle light = p.getLight();
        if (light != null) {
            element.addContent(new Element("light").addContent(light.getName()));
        }
        element.addContent(new Element("is_isNot").addContent(p.get_Is_IsNot().name()));
        element.addContent(new Element("lightState").addContent(p.getLightState().name()));
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
    Element addLightElement(NamedBeanHandle<Light> to, String which) {
        Element el = new Element("lightname");
        el.setAttribute("defines", which);
        el.addContent(to.getName());
        return el;
    }

    Element addLightElement(Light to) {
        String user = to.getUserName();
        String sys = to.getSystemName();

        Element el = new Element("light");
        el.setAttribute("systemName", sys);
        if (user != null) {
            el.setAttribute("userName", user);
        }

        return el;
    }
*/
    @Override
    public boolean load(Element shared, Element perNode) {
//        List<Element> l = shared.getChildren("lightname");
/*        
        if (l.size() == 0) {
            l = shared.getChildren("light");  // older form
        }
        NamedBeanHandle<Light> low = loadLight(l.get(0));
        NamedBeanHandle<Light> high = loadLight(l.get(1));
*/        
        // put it together
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        DigitalExpressionBean h;
        if (uname == null) {
            h = new ExpressionLight(sys);
        } else {
            h = new ExpressionLight(sys, uname);
        }

        loadCommon(h, shared);

        InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(h);
        return true;
    }
    
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(ExpressionLightXml.class);
}
