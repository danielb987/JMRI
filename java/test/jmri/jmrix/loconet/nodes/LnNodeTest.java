package jmri.jmrix.loconet.nodes;

import java.io.IOException;
import jmri.InstanceManager;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.util.JUnitUtil;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test LnNode.
 * This test loads the file xml/decoders/Public_Domain_LNSV2_DanielBergqvist.xml
 * to check that the LnStringIOs and LnAnalogIOs are created correctly.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LnNodeTest {

    // The decoder Public_Domain_LNSV2_DanielBergqvist.xml has these settings:
    public static final int DEVELOPER_ID_DANIEL_BERGQVIST = 17;
    public static final int PRODUCT_ID = 1;
    
    private LnTrafficController _lnis;
    private LocoNetSystemConnectionMemo _memo;
    
    @Test
    public void testDecoder() throws JDOMException, IOException, IOException {
        LnNodeManager lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        
        LnNode lnNode = new LnNode(1,     // Address
                LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID,
                LnNodeTest.DEVELOPER_ID_DANIEL_BERGQVIST,
                LnNodeTest.PRODUCT_ID,
                _lnis);
        
        lnNodeManager.register(lnNode);
        
        // Test create StringIOs and AnalogIOs from the decoder definition
//        lnNode.createNamedBeansFromDecoderDefinition();
        
        // Test load the decoder definition Public_Domain_LNSV2_DanielBergqvist.xml
        // ...
        // ...
        // ...
        
        
        // Test create StringIOs and AnalogIOs from the decoder definition
        lnNode.createNamedBeans();
        
        
        
/*        
        // Remove the rest of this method...
        DecoderFile decoderFile =
                lnNodeManager.getDecoderList()
                        .getProduct(
                                LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID,
                                DEVELOPER_ID_DANIEL_BERGQVIST,
                                PRODUCT_ID);
/*
        Keep this for later use...
        
        if (decoderFile != null) {
            Element decoderRoot;
            try {
                decoderRoot = decoderFile.rootFromName(DecoderFile.fileLocation + decoderFile.getFileName());
            } catch (JDOMException | IOException e) {
                log.error("Exception while loading decoder XML file: {}", decoderFile.getFileName(), e);
                return;
            }
            
            Element e = decoderRoot.getChild("loconet-node");
        } else {
            rosterPanel.setVisible(false);
            return;
        }
*./        
        
        Assert.assertNotNull("decoderFile is not null", decoderFile);
        
        Element decoderRoot = decoderFile.rootFromName(DecoderFile.fileLocation + decoderFile.getFileName());
        
        Element e = decoderRoot.getChild("loconet-node");
        Assert.assertNotNull("element is not null", e);
        
        // LnNodeManager.createAnalogIO_StringIO_FromDecoderDefinition()
        
/*        
        LnNodeManager m = new LnNodeManager(_lnis);
        Assert.assertEquals("PUBLIC_DOMAIN_DIY_MANAGER_ID is correct",
                13, LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID);
        Assert.assertEquals("PUBLIC_DOMAIN_DIY_MANAGER is correct",
                "Public-domain and DIY", LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER);
        Assert.assertEquals("String matches",
                LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER,
                m.getDecoderList().getManufacturer(LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID));
*/
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        
        // The class under test uses one LocoNet connection it pulls from the InstanceManager.
        _memo = new jmri.jmrix.loconet.LocoNetSystemConnectionMemo();
        _lnis = new jmri.jmrix.loconet.LocoNetInterfaceScaffold(_memo);
        _memo.setLnTrafficController(_lnis);
        _memo.configureCommandStation(jmri.jmrix.loconet.LnCommandStationType.COMMAND_STATION_DCS100, false, false, false);
        _memo.configureManagers();
        jmri.InstanceManager.store(_memo, jmri.jmrix.loconet.LocoNetSystemConnectionMemo.class);
        
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixManager();
        
        // Make sure we have a LnNodeManager
//        InstanceManager.setDefault(LnNodeManager.class, new LnNodeManager());
//        InstanceManager.setDefault(LnNodeManager.class, new LnNodeManager(_lnis));
    }

    @After
    public void tearDown() {
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }


    private final static Logger log = LoggerFactory.getLogger(LnNodeTest.class);

}
