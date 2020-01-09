package jmri.jmrix.loconet.streamport.configurexml;

import jmri.jmrix.configurexml.AbstractStreamConnectionConfigXml;
import jmri.jmrix.loconet.nodes.configurexml.LoadAndStoreXml;
import jmri.jmrix.loconet.streamport.LnStreamConnectionConfig;
import jmri.jmrix.loconet.streamport.LnStreamPortController;
import org.jdom2.Element;

/**
 * Handle XML persistance of layout connections by persistening the
 * LnStreamConnectionConfig (and connections). Note this is named as the 
 * XML version of a ConnectionConfig object, but it's actually persisting the
 * LnStreamPortController.
 * <p>
 * This class is invoked from jmrix.JmrixConfigPaneXml on write, as that class
 * is the one actually registered. Reads are brought here directly via the class
 * attribute in the XML.
 *
 * @author Andrew Crosland Copyright: Copyright (c) 2006
 */
public class LnStreamConnectionConfigXml extends AbstractStreamConnectionConfigXml {

    public LnStreamConnectionConfigXml() {
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
        if (adapter == null) {
            adapter = new LnStreamPortController();
        }
    }

    @Override
    protected void getInstance(Object object) {
        adapter = ((LnStreamConnectionConfig) object).getAdapter();
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
	if(adapter!=null) {
	   return; // already registered.
	}
        this.register(new LnStreamConnectionConfig(adapter));
    }

}
