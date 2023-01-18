package jmri.jmrit.logixng.actions.configurexml;

import java.util.List;

import jmri.InstanceManager;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle.ThrottleReference;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle.ThrottleType;
import jmri.jmrit.logixng.actions.NamedBeanType;

import org.jdom2.Element;

/**
 * Handle XML configuration for ActionListenOnThrottle objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2004, 2008, 2010
 * @author Daniel Bergqvist Copyright (C) 2019
 */
public class ActionListenOnThrottleXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ActionListenOnThrottleXml() {
    }

    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleLightSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ActionListenOnThrottle p = (ActionListenOnThrottle) o;

        Element element = new Element("ActionListenOnThrottle");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));

        storeCommon(p, element);

        Element parameters = new Element("References");
        for (ThrottleReference ref : p.getReferences()) {
            Element elementParameter = new Element("Reference");
//            elementParameter.addContent(new Element("name").addContent(ref.getAddress()));
            elementParameter.addContent(new Element("type").addContent(ref.getType().name()));
//            elementParameter.addContent(new Element("all").addContent(ref.getListenOnAllProperties() ? "yes" : "no"));  // NOI18N
            parameters.addContent(elementParameter);
        }
        element.addContent(parameters);

        element.addContent(new Element("listenOnSpeed").addContent(p.getListenOnSpeed() ? "yes" : "no"));
        element.addContent(new Element("listenOnIsForward").addContent(p.getListenOnIsForward() ? "yes" : "no"));
        element.addContent(new Element("listenOnFunction").addContent(p.getListenOnFunction() ? "yes" : "no"));
        element.addContent(new Element("localVariableEvent").addContent(p.getLocalVariableEvent()));
        element.addContent(new Element("localVariableLocoAddress").addContent(p.getLocalVariableLocoAddress()));
        element.addContent(new Element("localVariableRoster").addContent(p.getLocalVariableRoster()));
        element.addContent(new Element("localVariableSpeed").addContent(p.getLocalVariableSpeed()));
        element.addContent(new Element("localVariableIsForward").addContent(p.getLocalVariableIsForward()));
        element.addContent(new Element("localVariableFunction").addContent(p.getLocalVariableFunction()));
        element.addContent(new Element("localVariableFunctionState").addContent(p.getLocalVariableFunctionState()));
        element.addContent(new Element("localVariableUserData").addContent(p.getLocalVariableUserData()));

        return element;
    }

    @Override
    public boolean load(Element shared, Element perNode) {
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        ActionListenOnThrottle h = new ActionListenOnThrottle(sys, uname);

        loadCommon(h, shared);

        List<Element> parameterList = shared.getChild("References").getChildren();  // NOI18N
        log.debug("Found {} references", parameterList.size() );  // NOI18N

        for (Element e : parameterList) {
            Element elementName = e.getChild("name");

            ThrottleType type = null;
            Element elementType = e.getChild("type");
            if (elementType != null) {
                type = ThrottleType.valueOf(elementType.getTextTrim());
            }

//            if (elementName == null) throw new IllegalArgumentException("Element 'name' does not exists");
            if (type == null) throw new IllegalArgumentException("Element 'type' does not exists");

//            String all = "no";  // NOI18N
//            if (e.getChild("all") != null) {  // NOI18N
//                all = e.getChild("all").getValue();  // NOI18N
//            }
//            boolean listenToAll = "yes".equals(all); // NOI18N

//            h.addReference(new ThrottleReference(elementName.getTextTrim(), type, listenToAll));
        }

        String listenOnSpeedStr = "no";  // NOI18N
        if (shared.getChild("listenOnSpeed") != null) {  // NOI18N
           listenOnSpeedStr = shared.getChild("listenOnSpeed").getValue();  // NOI18N
        }
        h.setListenOnSpeed("yes".equals(listenOnSpeedStr)); // NOI18N

        String listenOnIsForwardStr = "no";  // NOI18N
        if (shared.getChild("listenOnIsForward") != null) {  // NOI18N
           listenOnIsForwardStr = shared.getChild("listenOnIsForward").getValue();  // NOI18N
        }
        h.setListenOnIsForward("yes".equals(listenOnIsForwardStr)); // NOI18N

        String listenOnFunctionStr = "no";  // NOI18N
        if (shared.getChild("listenOnFunction") != null) {  // NOI18N
           listenOnFunctionStr = shared.getChild("listenOnFunction").getValue();  // NOI18N
        }
        h.setListenOnFunction("yes".equals(listenOnFunctionStr)); // NOI18N

        Element variableName = shared.getChild("localVariableEvent");
        if (variableName != null) {
            h.setLocalVariableEvent(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableLocoAddress");
        if (variableName != null) {
            h.setLocalVariableLocoAddress(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableRoster");
        if (variableName != null) {
            h.setLocalVariableRoster(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableSpeed");
        if (variableName != null) {
            h.setLocalVariableSpeed(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableIsForward");
        if (variableName != null) {
            h.setLocalVariableIsForward(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableFunction");
        if (variableName != null) {
            h.setLocalVariableFunction(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableFunctionState");
        if (variableName != null) {
            h.setLocalVariableFunctionState(variableName.getTextTrim());
        }

        variableName = shared.getChild("localVariableUserData");
        if (variableName != null) {
            h.setLocalVariableUserData(variableName.getTextTrim());
        }

        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionListenOnThrottleXml.class);
}
