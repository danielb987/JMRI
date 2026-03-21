package jmri.time.implementation.configurexml;

import jmri.configurexml.JmriConfigureXmlException;

import jmri.InstanceManager;
import jmri.Timebase;
import static jmri.Timebase.ClockInitialRunState.DO_START;
import static jmri.Timebase.ClockInitialRunState.DO_STOP;

import org.jdom2.Element;

/**
 * Store and load a DefaultTimebase.
 *
 * @author Daniel Bergqvist (C) 2026
 */
public class DefaultTimebaseXml extends jmri.configurexml.AbstractXmlAdapter {

    @Override
    public Element store(Object o) {

        Timebase clock = InstanceManager.getDefault(jmri.Timebase.class);

        Element elem = new Element("timebase");
        elem.setAttribute("class", this.getClass().getName());

        var loadAndStorePreferences = InstanceManager.getDefault(jmri.configurexml.LoadAndStorePreferences.class);
        if (! loadAndStorePreferences.isExcludeTimebase() ) {
            elem.setAttribute("time", clock.getStartTime().toString());
        }

        elem.setAttribute("rate", "" + clock.userGetRate());
        elem.setAttribute("startrate", "" + clock.getStartRate());
        elem.setAttribute("run", (clock.getClockInitialRunState() == DO_START ? "yes" : "no"));
        elem.setAttribute("master", (clock.getInternalMaster() ? "yes" : "no"));
        if (!clock.getInternalMaster()) {
            elem.setAttribute("mastername", clock.getMasterName());
        }
        elem.setAttribute("sync", (clock.getSynchronize() ? "yes" : "no"));
        elem.setAttribute("correct", (clock.getCorrectHardware() ? "yes" : "no"));
        elem.setAttribute("display", (clock.use12HourDisplay() ? "yes" : "no"));
        elem.setAttribute("startstopped", (clock.getClockInitialRunState() == DO_STOP ? "yes" : "no"));
        elem.setAttribute("startrunning", ((clock.getClockInitialRunState() == DO_START) ? "yes" : "no"));
        elem.setAttribute("startsettime", (clock.getStartSetTime() ? "yes" : "no"));
        elem.setAttribute("startclockoption", Integer.toString(
                clock.getStartClockOption()));
        elem.setAttribute("showbutton", (clock.getShowStopButton() ? "yes" : "no"));
        elem.setAttribute("startsetrate", (clock.getSetRateAtStart() ? "yes" : "no"));

        return elem;
    }

    @Override
    public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {
        return true;
    }

}
