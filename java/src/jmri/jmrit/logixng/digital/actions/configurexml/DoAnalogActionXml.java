package jmri.jmrit.logixng.digital.actions.configurexml;

import java.lang.reflect.Field;
import jmri.InstanceManager;
import jmri.jmrit.logixng.FemaleAnalogActionSocket;
import jmri.jmrit.logixng.FemaleAnalogExpressionSocket;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.digital.actions.DoAnalogAction;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalActionBean;

/**
 *
 */
public class DoAnalogActionXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public DoAnalogActionXml() {
    }

    private FemaleAnalogExpressionSocket getAnalogExpressionSocket(DigitalActionBean action) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = action.getClass().getDeclaredField("_analogExpressionSocket");
        f.setAccessible(true);
        return (FemaleAnalogExpressionSocket) f.get(action);
    }

    private FemaleAnalogActionSocket getAnalogActionSocket(DigitalActionBean action) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = action.getClass().getDeclaredField("_analogActionSocket");
        f.setAccessible(true);
        return (FemaleAnalogActionSocket) f.get(action);
    }

    /**
     * Default implementation for storing the contents of a DoAnalogAction
     *
     * @param o Object to store, of type TripleTurnoutSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        DoAnalogAction p = (DoAnalogAction) o;

        Element element = new Element("do-analog-action");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));
        if (p.getUserName() != null) {
            element.addContent(new Element("userName").addContent(p.getUserName()));
        }

        try {
            FemaleAnalogExpressionSocket ifExpressionSocket = getAnalogExpressionSocket(p);
            if (ifExpressionSocket.isConnected()) {
                element.addContent(new Element("expressionSystemName").addContent(ifExpressionSocket.getConnectedSocket().getSystemName()));
            }
            FemaleAnalogActionSocket _thenActionSocket = getAnalogActionSocket(p);
            if (_thenActionSocket.isConnected()) {
                element.addContent(new Element("actionSystemName").addContent(_thenActionSocket.getConnectedSocket().getSystemName()));
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            log.error("Error storing action: {}", e, e);
        }

        storeCommon(p, element);

        return element;
    }
    
    @Override
    public boolean load(Element shared, Element perNode) {
        
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        DoAnalogAction h;
        if (uname == null) {
            h = new DoAnalogAction(sys);
        } else {
            h = new DoAnalogAction(sys, uname);
        }

        loadCommon(h, shared);

        Element ifSystemNameElement = shared.getChild("ifSystemName");
        if (ifSystemNameElement != null) {
            h.setAnalogActionSocketSystemName(ifSystemNameElement.getTextTrim());
        }
        Element thenSystemNameElement = shared.getChild("thenSystemName");
        if (thenSystemNameElement != null) {
            h.setAnalogExpressionSocketSystemName(thenSystemNameElement.getTextTrim());
        }
        
        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }
    
    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    private final static Logger log = LoggerFactory.getLogger(DoAnalogActionXml.class);
}
