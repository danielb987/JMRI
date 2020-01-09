package jmri.jmrix.loconet.configurexml;

import jmri.configurexml.JmriConfigureXmlException;
import org.junit.*;

/**
 * Base class for testing ConnectionConfigXml classes in the Loconet tree.
 * This test check that the ConnectionConfigXml classes can store and load
 * LnNodes.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public abstract class LoconetStreamConnectionConfigXmlTestBase extends jmri.jmrix.configurexml.AbstractStreamConnectionConfigXmlTestBase {

    @Test
    public void testStore() throws JmriConfigureXmlException{
        new LoconetConnectionConfigXmlScaffold(cc, xmlAdapter).testStore();
    }

    @Test(timeout=5000)
    public void testLoad() throws jmri.configurexml.JmriConfigureXmlException {
        new LoconetConnectionConfigXmlScaffold(cc, xmlAdapter).testLoad();
    }
    
}
