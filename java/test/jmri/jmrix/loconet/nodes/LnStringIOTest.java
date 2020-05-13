package jmri.jmrix.loconet.nodes;

import jmri.InstanceManager;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Test StringIO
 */
public class LnStringIOTest {

    private LnTrafficController _lnis;
    private LocoNetSystemConnectionMemo _memo;
    
    @Test
    public void testCTor() {
        LnNode lnNode = new LnNode(1,     // Address
                LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID,
                LnNodeTest.DEVELOPER_ID_DANIEL_BERGQVIST,
                LnNodeTest.PRODUCT_ID,
                _lnis);
//        if (1==1) throw new RuntimeException("System name: "+node.getSystemName());
//        LnStringIO b = new LnStringIO(node, "LC1:10", null);
        InstanceManager.getDefault(LnNodeManager.class).register(lnNode);
        LnStringIO b = new LnStringIO("LC1:10", null);
        Assert.assertNotNull("exists", b);
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
    }

    @After
    public void tearDown() {
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }

}
