package jmri.jmrit.logixng.digital.actions.configurexml;

import java.lang.reflect.Field;
import jmri.InstanceManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.digital.actions.IfThen;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalActionBean;

/**
 *
 */
public class IfThenXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public IfThenXml() {
    }

    private FemaleDigitalExpressionSocket getIfExpressionSocket(DigitalActionBean action) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = action.getClass().getDeclaredField("_ifExpressionSocket");
        f.setAccessible(true);
        return (FemaleDigitalExpressionSocket) f.get(action);
    }

    private FemaleDigitalActionSocket getThenActionSocket(DigitalActionBean action) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = action.getClass().getDeclaredField("_thenActionSocket");
        f.setAccessible(true);
        return (FemaleDigitalActionSocket) f.get(action);
    }

    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleTurnoutSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        IfThen p = (IfThen) o;

        Element element = new Element("if-then");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

        element.setAttribute("type", p.getType().name());
        
        try {
            FemaleDigitalExpressionSocket ifExpressionSocket = getIfExpressionSocket(p);
            if (ifExpressionSocket.isConnected()) {
                element.addContent(new Element("ifSystemName").addContent(ifExpressionSocket.getConnectedSocket().getSystemName()));
            }
            FemaleDigitalActionSocket _thenActionSocket = getThenActionSocket(p);
            if (_thenActionSocket.isConnected()) {
                element.addContent(new Element("thenSystemName").addContent(_thenActionSocket.getConnectedSocket().getSystemName()));
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            log.error("Error storing action: {}", e, e);
        }

        storeCommon(p, element);

//        element.addContent(addTurnoutElement(p.getLow(), "low"));
//        element.addContent(addTurnoutElement(p.getHigh(), "high"));

        return element;
    }
/*
    Element addTurnoutElement(NamedBeanHandle<Turnout> to, String which) {
        Element el = new Element("turnoutname");
        el.setAttribute("defines", which);
        el.addContent(to.getName());
        return el;
    }

    Element addTurnoutElement(Turnout to) {
        String user = to.getUserName();
        String sys = to.getSystemName();

        Element el = new Element("turnout");
        el.setAttribute("systemName", sys);
        if (user != null) {
            el.setAttribute("userName", user);
        }

        return el;
    }
*/
    @Override
    public boolean load(Element shared, Element perNode) {
//        List<Element> l = shared.getChildren("turnoutname");
/*        
        if (l.size() == 0) {
            l = shared.getChildren("turnout");  // older form
        }
        NamedBeanHandle<Turnout> low = loadTurnout(l.get(0));
        NamedBeanHandle<Turnout> high = loadTurnout(l.get(1));
*/        
        // put it together
        
        IfThen.Type type = IfThen.Type.TRIGGER_ACTION;
        Attribute typeAttribute = shared.getAttribute("type");
        if (typeAttribute != null) {
            type = IfThen.Type.valueOf(typeAttribute.getValue());
        }
        
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        IfThen h;
        if (uname == null) {
            h = new IfThen(sys, type);
        } else {
            h = new IfThen(sys, uname, type);
        }

        loadCommon(h, shared);

        Element ifSystemNameElement = shared.getChild("ifSystemName");
        if (ifSystemNameElement != null) {
            h.setIfExpressionSocketSystemName(ifSystemNameElement.getTextTrim());
        }
        Element thenSystemNameElement = shared.getChild("thenSystemName");
        if (thenSystemNameElement != null) {
            h.setThenActionSocketSystemName(thenSystemNameElement.getTextTrim());
        }
        
        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }
/*
    /.**
     * Process stored signal head output (turnout).
     * <p>
     * Needs to handle two types of element: turnoutname is new form; turnout is
     * old form.
     *
     * @param o xml object defining a turnout on an SE8C signal head
     * @return named bean for the turnout
     *./
    NamedBeanHandle<Turnout> loadTurnout(Object o) {
        Element e = (Element) o;

        if (e.getName().equals("turnout")) {
            String name = e.getAttribute("systemName").getValue();
            Turnout t;
            if (e.getAttribute("userName") != null
                    && !e.getAttribute("userName").getValue().equals("")) {
                name = e.getAttribute("userName").getValue();
                t = InstanceManager.turnoutManagerInstance().getTurnout(name);
            } else {
                t = InstanceManager.turnoutManagerInstance().getBySystemName(name);
            }
            return jmri.InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(name, t);
        } else {
            String name = e.getText();
            try {
                Turnout t = InstanceManager.turnoutManagerInstance().provideTurnout(name);
                return jmri.InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(name, t);
            } catch (IllegalArgumentException ex) {
                log.warn("Failed to provide Turnout \"{}\" in loadTurnout", name);
                return null;
            }
        }
    }
*/
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(IfThenXml.class);
}
