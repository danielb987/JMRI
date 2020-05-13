package jmri.jmrix.loconet.nodes.configurexml;

import java.util.List;
import jmri.jmrix.PortAdapter;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.nodes.LnNode;
import org.jdom2.Element;

/**
 * Load and store methods used by the ConnectionConfigXml classes.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LoadAndStoreXml {

    private final LnTrafficController _tc;
    
    
    public LoadAndStoreXml(LocoNetSystemConnectionMemo memo) {
        _tc = memo.getLnTrafficController();
    }
    
    public LoadAndStoreXml(PortAdapter adapter) {
        _tc = ((LocoNetSystemConnectionMemo)adapter.getSystemConnectionMemo())
                .getLnTrafficController();
    }
    
    private Element createElement(String name, int value) {
        Element e = new Element(name);
        e.addContent(Integer.toString(value));
        return e;
    }
    
    private Element createElement(String name, String value) {
        Element e = new Element(name);
        e.addContent(value);
        return e;
    }
    
    public void store(Element parent) {
        Element nodesElement = new Element("nodes");
        parent.addContent(nodesElement);
        
        for (LnNode node : _tc.getNodes().values()) {
            Element nodeElement = new Element("node");
            nodeElement.addContent(createElement("address", node.getAddress()));
            nodeElement.addContent(createElement("name", node.getName()));
            nodeElement.addContent(createElement("manufacturer-id", node.getManufacturerID()));
            nodeElement.addContent(createElement("developer-id", node.getDeveloperID()));
            nodeElement.addContent(createElement("serial-number", node.getSerialNumber()));
            nodesElement.addContent(nodeElement);
        }
    }
    
    public void load(Element parent) {
        List<Element> l = parent.getChildren("nodes");
        if (! l.isEmpty()) {
            List<Element> nodeElements = l.get(0).getChildren("node");
            for (Element e : nodeElements) {
                int addr = Integer.parseInt(e.getChild("address").getTextTrim());
                
                int manufacturerID = Integer.parseInt(e.getChild("manufacturer-id").getTextTrim());
                int developerID = Integer.parseInt(e.getChild("developer-id").getTextTrim());
                int productID = Integer.parseInt(e.getChild("product-id").getTextTrim());
                
                LnNode node = new LnNode(addr, manufacturerID, developerID, productID, _tc);
                
                node.setName(e.getChild("name").getTextTrim());
                node.setSerialNumber(Integer.parseInt(e.getChild("serial-number").getTextTrim()));
                
                _tc.register(node);
            }
        }
    }
    
}
