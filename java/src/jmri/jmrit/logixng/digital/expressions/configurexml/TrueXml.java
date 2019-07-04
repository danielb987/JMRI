package jmri.jmrit.logixng.digital.expressions.configurexml;

import jmri.InstanceManager;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.digital.expressions.True;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalExpressionBean;

/**
 *
 */
public class TrueXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public TrueXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleTrueSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        True p = (True) o;

        Element element = new Element("true");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

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
    Element addTrueElement(NamedBeanHandle<True> to, String which) {
        Element el = new Element("truename");
        el.setAttribute("defines", which);
        el.addContent(to.getName());
        return el;
    }

    Element addTrueElement(True to) {
        String user = to.getUserName();
        String sys = to.getSystemName();

        Element el = new Element("true");
        el.setAttribute("systemName", sys);
        if (user != null) {
            el.setAttribute("userName", user);
        }

        return el;
    }
*/
    @Override
    public boolean load(Element shared, Element perNode) {
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        DigitalExpressionBean h;
        if (uname == null) {
            h = new True(sys);
        } else {
            h = new True(sys, uname);
        }

        loadCommon(h, shared);

        InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(h);
        return true;
    }
    
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(TrueXml.class);
}
