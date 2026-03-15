package jmri.time.implementation.configurexml;

// import jmri.configurexml.JmriConfigureXmlException;

import org.jdom2.Element;

/**
 * Store and load a DefaultTimebase.
 *
 * @author Daniel Bergqvist (C) 2026
 */
public class DefaultTimebaseXml extends jmri.configurexml.AbstractXmlAdapter {

    @Override
    public Element store(Object o) {
        Element e = new Element("timebase");
        e.setAttribute("class", "jmri.jmrit.simpleclock.configurexml.SimpleTimebaseXml");
        e.setAttribute("time", "Sun May 17 08:12:43 PDT 2020");
        e.setAttribute("rate", "1.0");
        e.setAttribute("startrate", "1.0");
        e.setAttribute("run", "yes");
        e.setAttribute("master", "yes");
        e.setAttribute("sync", "no");
        e.setAttribute("correct", "no");
        e.setAttribute("display", "no");
        e.setAttribute("startstopped", "no");
        e.setAttribute("startrunning", "yes");
        e.setAttribute("startsettime", "no");
        e.setAttribute("startclockoption", "0");
        e.setAttribute("showbutton", "no");
        e.setAttribute("startsetrate", "yes");
        return e;
    }
/*
    @Override
    public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {
        return true;
    }
*/
}
