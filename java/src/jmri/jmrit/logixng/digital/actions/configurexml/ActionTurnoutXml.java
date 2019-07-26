package jmri.jmrit.logixng.digital.actions.configurexml;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.TurnoutManager;
import jmri.configurexml.JmriConfigureXmlException;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalActionBean;

/**
 *
 */
public class ActionTurnoutXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ActionTurnoutXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleTurnoutSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ActionTurnout p = (ActionTurnout) o;

        Element element = new Element("action-turnout");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

        NamedBeanHandle turnout = p.getTurnout();
        if (turnout != null) {
            element.addContent(new Element("turnout").addContent(turnout.getName()));
        }
//        element.addContent(new Element("is_isNot").addContent(p.get_Is_IsNot().name()));
        element.addContent(new Element("turnoutState").addContent(p.getTurnoutState().name()));
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
    public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {     // Test class that inherits this class throws exception
//        List<Element> l = shared.getChildren("turnoutname");
/*        
        if (l.size() == 0) {
            l = shared.getChildren("turnout");  // older form
        }
        NamedBeanHandle<Turnout> low = loadTurnout(l.get(0));
        NamedBeanHandle<Turnout> high = loadTurnout(l.get(1));
*/        
        // put it together
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        ActionTurnout h;
        if (uname == null) {
            h = new ActionTurnout(sys);
        } else {
            h = new ActionTurnout(sys, uname);
        }

        loadCommon(h, shared);

        Element turnoutName = shared.getChild("turnout");
        if (turnoutName != null) {
            h.setTurnout(InstanceManager.getDefault(TurnoutManager.class).getTurnout(turnoutName.getTextTrim()));
        }

        // this.checkedNamedBeanReference()
        // <T extends NamedBean> T checkedNamedBeanReference(String name, @Nonnull T type, @Nonnull Manager<T> m) {

        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }
    
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(ActionTurnoutXml.class);
}
