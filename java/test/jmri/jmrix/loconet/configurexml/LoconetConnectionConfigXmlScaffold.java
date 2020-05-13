package jmri.jmrix.loconet.configurexml;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import jmri.InstanceManager;
import jmri.configurexml.AbstractXmlAdapter;
import jmri.configurexml.JmriConfigureXmlException;
import jmri.jmrix.ConnectionConfig;
import jmri.jmrix.ConnectionConfigManager;
import jmri.jmrix.SystemConnectionMemo;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.nodes.LnNode;
import jmri.util.ThreadingUtil;
import org.jdom2.Element;
import org.junit.*;

/**
 * Class for testing ConnectionConfigXml classes in the Loconet tree.
 * This test check that the ConnectionConfigXml classes can store and load
 * LnNodes.
 * <p>
 * This class is a scaffold class since there are three different base classes
 * that is used to test the ConnectionConfigXml classes.
 * 
 * @author Paul Bender      Copyright (C) 2018
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LoconetConnectionConfigXmlScaffold {

    private final ConnectionConfig _cc;
    private final AbstractXmlAdapter _xmlAdapter;
    private LnTrafficController _tc;
    private List<LnNode> _nodes;
    
    public LoconetConnectionConfigXmlScaffold(ConnectionConfig cc, AbstractXmlAdapter xmlAdapter) {
        _cc = cc;
        _xmlAdapter = xmlAdapter;
    }
    
    private void createTestNode(int address, String name, int manID, int devID, int prodID, int serID) {
        LnNode node = new LnNode(address,
                manID,
                devID,
                prodID,
                _tc);
        node.setName(name);
        node.setSerialNumber(serID);
        _nodes.add(node);
        _tc.register(node);
    }
    
    private void checkItemsInTrafficController() {
        for (LnNode node : _nodes) {
            LnNode otherNode = _tc.getNode(node.getAddress());
            
            Assert.assertNotNull("Node exist in traffic controller", otherNode);
            
            Assert.assertFalse("Nodes are not the same", node == otherNode);
            Assert.assertEquals("Nodes have same address", node.getAddress(), otherNode.getAddress());
            Assert.assertEquals("Nodes have same name", node.getName(), otherNode.getName());
            Assert.assertEquals("Nodes have same manufacturer ID", node.getManufacturerID(), otherNode.getManufacturerID());
            Assert.assertEquals("Nodes have same developer ID", node.getDeveloperID(), otherNode.getDeveloperID());
            Assert.assertEquals("Nodes have same serial number", node.getSerialNumber(), otherNode.getSerialNumber());
        }
    }
    
    private void createLnNodes() throws JmriConfigureXmlException {
        SystemConnectionMemo memo = _cc.getAdapter().getSystemConnectionMemo();
        _tc = ((LocoNetSystemConnectionMemo)memo).getLnTrafficController();
        
        _nodes = new ArrayList<>(_tc.getNodes().values());
        
        createTestNode(57, "North yard", 10, 12, 0, 342);
        createTestNode(132, "East yard", 27, 33, 0, 1);
        createTestNode(14, "West yard", 312, 2, 0, 3223);
        createTestNode(253, "South yard", 3, 1212, 0, 23);
    }
    
    @Test
    public void testStore() throws JmriConfigureXmlException{
        Assume.assumeNotNull(_cc);
        Assume.assumeNotNull(_cc.getAdapter());
        _cc.loadDetails(new JPanel());
        // load details MAY produce an error message if no ports are found.
        jmri.util.JUnitAppender.suppressErrorMessage("No usable ports returned");
        createLnNodes();
        Element e = _xmlAdapter.store(_cc);
        Assert.assertNotNull("XML Element Produced", e); 
        if(e.getAttribute("class")!=null){
           Assert.assertEquals("class", _xmlAdapter.getClass().getName(), e.getAttribute("class").getValue());
        }
//        validateCommonDetails(_cc, e);
//        validateConnectionDetails(_cc, e);
    }

    @Test(timeout=5000)
    public void testLoad() throws jmri.configurexml.JmriConfigureXmlException {
        Assume.assumeNotNull(_cc);
        Assume.assumeNotNull(_cc.getAdapter());
        // reset the profile manager for this test, so it can run independently.
        jmri.util.JUnitUtil.resetProfileManager();
        // This test requires a configure manager.
        jmri.util.JUnitUtil.initConfigureManager();
        // Running this on the UI thread fixes some ConcurrentModificationExceptions errors.
        ThreadingUtil.runOnGUI(()->{
            _cc.loadDetails(new JPanel());
            _cc.setDisabled(true); // so we don't try to start the connection on load.
        });
        
        // There should be no connection config registered yet
        ConnectionConfig ccList[] = new ConnectionConfig[0];
        InstanceManager.getDefault(jmri.ConfigureManager.class).registerPref(this);
        ConnectionConfigManager ccm = InstanceManager.getNullableDefault(ConnectionConfigManager.class);
        if (ccm != null) {
            ccList = ccm.getConnections();
        }
        Assert.assertEquals("list has zero elements", 0, ccList.length);
        
        // Create the nodes we want to store and load
        createLnNodes();
        
        // load details MAY produce an error message if no ports are found.
        jmri.util.JUnitAppender.suppressErrorMessage("No usable ports returned");
        Element e = _xmlAdapter.store(_cc);
        
        // Remove all _nodes from the traffic controller
        _tc.deleteAllNodes();
        //load what we just produced.
        _xmlAdapter.load(e, e);
        
        // We should now have a new connection config. Try to get it and check
        // that we have exactly one connection config.
        InstanceManager.getDefault(jmri.ConfigureManager.class).registerPref(this);
        ccm = InstanceManager.getNullableDefault(ConnectionConfigManager.class);
        if (ccm != null) {
            ccList = ccm.getConnections();
        }
        Assert.assertEquals("list has one element", 1, ccList.length);
        
        // We have a new traffic controller
        LocoNetSystemConnectionMemo memo =
                (LocoNetSystemConnectionMemo)ccList[0].getAdapter().getSystemConnectionMemo();
        _tc = memo.getLnTrafficController();
        
        // Verify that the items have been loaded
        checkItemsInTrafficController();
    }
    
}
