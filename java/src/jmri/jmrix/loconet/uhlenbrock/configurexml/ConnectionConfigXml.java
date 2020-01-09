package jmri.jmrix.loconet.uhlenbrock.configurexml;

import jmri.jmrix.configurexml.AbstractSerialConnectionConfigXml;
import jmri.jmrix.loconet.nodes.configurexml.LoadAndStoreXml;
import jmri.jmrix.loconet.uhlenbrock.ConnectionConfig;
import jmri.jmrix.loconet.uhlenbrock.UhlenbrockAdapter;
import org.jdom2.Element;

/**
 * Handle XML persistance of layout connections by persisting the
 * LocoBufferAdapter (and connections). Note this is named as the XML version of
 * a ConnectionConfig object, but it's actually persisting the
 * LocoBufferAdapter.
 * <p>
 * This class is invoked from jmrix.JmrixConfigPaneXml on write, as that class
 * is the one actually registered. Reads are brought here directly via the class
 * attribute in the XML.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2003
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
        adapter = new UhlenbrockAdapter();
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
        this.register(new ConnectionConfig(adapter));
    }

}
