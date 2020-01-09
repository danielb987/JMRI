package jmri.jmrix.loconet.usb_dcs240.configurexml;

import jmri.jmrix.configurexml.AbstractSerialConnectionConfigXml;
import jmri.jmrix.loconet.nodes.configurexml.LoadAndStoreXml;
import jmri.jmrix.loconet.usb_dcs240.ConnectionConfig;
import jmri.jmrix.loconet.usb_dcs240.UsbDcs240Adapter;
import org.jdom2.Element;

/**
 * Handle XML persistance of layout connections by persisting the UsbDcs240Adapter
 * (and connections). Note this is named as the XML version of a
 * ConnectionConfig object, but it's actually persisting the UsbDcs240Adapter.
 * <p>
 * This class is invoked from jmrix.JmrixConfigPaneXml on write, as that class
 * is the one actually registered. Reads are brought here directly via the class
 * attribute in the XML.
 *
 * Based on loconet.pr3.configurexml.ConnectionConfigXml.java
 * 
 * @author Bob Jacobsen Copyright: Copyright (c) 2003, 2005, 2006, 2008
 * @author B. Milhaupt Copyright (C) 2019
 */
public class ConnectionConfigXml extends AbstractSerialConnectionConfigXml {

    public ConnectionConfigXml() {
        super();
    }

    /**
     * Write out the LnNode objects too
     *
     * @param e Element being extended
     */
    @Override
    protected void extendElement(Element e) {
        new LoadAndStoreXml(adapter).store(e);
    }

    @Override
    protected void getInstance() {
        adapter = new UsbDcs240Adapter();
    }

    @Override
    protected void getInstance(Object object) {
        adapter = ((ConnectionConfig) object).getAdapter();
    }

    /**
     * Read the LnNode elements
     * @param shared  connection information common to all nodes
     * @param perNode connection information unique to this node
     */
    @Override
    protected void unpackElement(Element shared, Element perNode) {
        new LoadAndStoreXml(adapter).load(shared);
    }
    
    @Override
    protected void register() {
        super.register(new ConnectionConfig(adapter));
    }

}
