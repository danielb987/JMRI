package jmri.jmrit.logixng.actions.configurexml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jmri.InstanceManager;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.actions.IndependentTimer;
import jmri.jmrit.logixng.actions.IndependentTimer.IndependentTimerSocketConfiguration;
import jmri.jmrit.logixng.util.TimerUnit;

import org.jdom2.Element;

/**
 * Handle XML configuration for IndependentTimer objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2004, 2008, 2010
 * @author Daniel Bergqvist Copyright (C) 2023
 */
public class IndependentTimerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public IndependentTimerXml() {
    }

    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleLightSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        IndependentTimer p = (IndependentTimer) o;

        Element element = new Element("IndependentTimer");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));

        storeCommon(p, element);

        Element e = new Element("Actions");
        for (int i=0; i < p.getNumActions(); i++) {
            Element e2 = new Element("Socket");
//            e2.addContent(new Element("delay").addContent(Integer.toString(p.getDelay(i))));
            e2.addContent(new Element("socketName").addContent(p.getActionSocket(i).getName()));

            MaleSocket socket = p.getActionSocket(i).getConnectedSocket();
            String socketSystemName;
            if (socket != null) {
                socketSystemName = socket.getSystemName();
            } else {
                socketSystemName = p.getActionSocketSystemName(i);
            }
            if (socketSystemName != null) {
                e2.addContent(new Element("systemName").addContent(socketSystemName));
            }

            Element socketConfig = new Element("SocketConfig");
            if (!(p.getActionSocket(i).getConfiguration() instanceof IndependentTimerSocketConfiguration)) {
                throw new IllegalArgumentException("Configuration is not a IndependentTimerSocketConfiguration");
            }
            IndependentTimerSocketConfiguration config =
                    (IndependentTimerSocketConfiguration) p.getActionSocket(i).getConfiguration();
            socketConfig.addContent(new Element("delayByLocalVariable").addContent(config.isDelayByLocalVariable() ? "yes" : "no"));
            socketConfig.addContent(new Element("delay").addContent(Integer.toString(config.getDelay())));
            socketConfig.addContent(new Element("unit").addContent(config.getUnit().name()));
            socketConfig.addContent(new Element("delayLocalVariable").addContent(config.getDelayLocalVariable()));
            e2.addContent(socketConfig);

            e.addContent(e2);
        }
        element.addContent(e);

        element.addContent(new Element("startImmediately").addContent(p.isStartImmediately() ? "yes" : "no"));
        element.addContent(new Element("runContinuously").addContent(p.isRunContinuously() ? "yes" : "no"));

        return element;
    }

    @Override
    public boolean load(Element shared, Element perNode) {
        List<Map.Entry<String, String>> expressionSystemNames = new ArrayList<>();

        List<IndependentTimer.ActionData> actionDataList = new ArrayList<>();

        Element actionElement = shared.getChild("Actions");
        for (Element socketElement : actionElement.getChildren()) {
            String socketName = socketElement.getChild("socketName").getTextTrim();
            Element systemNameElement = socketElement.getChild("systemName");
            String systemName = null;
            if (systemNameElement != null) {
                systemName = systemNameElement.getTextTrim();
            }

            IndependentTimerSocketConfiguration socketConfig = new IndependentTimerSocketConfiguration();

            Element socketConfigElement = socketElement.getChild("SocketConfig");

            Element delayByLocalVariable = socketConfigElement.getChild("delayByLocalVariable");
            if (delayByLocalVariable != null) {
                socketConfig.setDelayByLocalVariable("yes".equals(delayByLocalVariable.getTextTrim()));
            }

            Element delayElement = socketConfigElement.getChild("delay");
            if (delayElement != null) {
                socketConfig.setDelay(Integer.parseInt(delayElement.getText()));
            }

            Element unit = socketConfigElement.getChild("unit");
            if (unit != null) {
                socketConfig.setUnit(TimerUnit.valueOf(unit.getTextTrim()));
            }

            Element delayLocalVariable = socketConfigElement.getChild("delayLocalVariable");
            if (delayLocalVariable != null) {
                socketConfig.setDelayLocalVariable(delayLocalVariable.getTextTrim());
            }

            actionDataList.add(new IndependentTimer.ActionData(socketName, systemName, socketConfig));
        }

        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        IndependentTimer h = new IndependentTimer(sys, uname, expressionSystemNames, actionDataList);

        loadCommon(h, shared);

        Element startImmediately = shared.getChild("startImmediately");
        if (startImmediately != null) {
            h.setStartImmediately("yes".equals(startImmediately.getTextTrim()));
        } else {
            h.setStartImmediately(false);
        }

        Element runContinuously = shared.getChild("runContinuously");
        if (runContinuously != null) {
            h.setRunContinuously("yes".equals(runContinuously.getTextTrim()));
        } else {
            h.setRunContinuously(false);
        }

        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }

//    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IndependentTimerXml.class);
}
