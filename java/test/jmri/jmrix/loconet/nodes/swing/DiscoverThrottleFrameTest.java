package jmri.jmrix.loconet.nodes.swing;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LocoNetInterfaceScaffold;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.operators.JFrameOperator;

/**
 * Test DiscoverNodesFrameTest
 * 
 * @author Daniel Bergqvist 2020
 */
public class DiscoverThrottleFrameTest {

    private LocoNetInterfaceScaffold _lnis;
    private LocoNetSystemConnectionMemo _memo;
    
    private DiscoverThrottleFrame showDiscoverThrottleFrame() {
        DiscoverThrottleFrame f = new DiscoverThrottleFrame(_memo);
        f.initComponents();
        f.setLocation(20,40);
        f.setVisible(true);
        return f;
    }
    
    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        DiscoverThrottleFrame b = new DiscoverThrottleFrame(_memo);
        Assert.assertNotNull("exists", b);
    }
    
    @Test
    public void testDiscoverThrottle() throws InterruptedException {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        for (LocoNetMessage l : _lnis.outbound) {
            System.out.format("LocoNet aaa: %s%n", l.toMonitorString());
        }
        
        // Clear list of loconet messages
//        _lnis.outbound.clear();
        
        showDiscoverThrottleFrame();
        
        JFrame frame = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);  // NOI18N
        Assert.assertNotNull(frame);
        
        // Request slot for loco address 5502
        LocoNetMessage m = new LocoNetMessage(new int[]{0xBF, 0x2A, 0x7E, 0x14});
        _lnis.sendTestMessage(m);
        
        // Report of slot 16 information
	// Loco 5502 is Not Consisted, In-Use, operating in 128 SS mode, and is moving Reverse at speed 0,
	// F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=On
	// Master supports LocoNet 1.1; Track Status: Off/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x10 0x7A (2170).
        m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x33, 0x7E, 0x00, 0x20, 0x06, 0x00, 0x2A, 0x08, 0x7A, 0x10, 0x25});
        _lnis.sendTestMessage(m);
        
        // Write slot 16 information
	// Loco 5502 is Not Consisted, In-Use, operating in 128 SS mode, and is moving Reverse at speed 0,
	// F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=On
	// Master supports LocoNet 1.1; Track Status: Off/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x10 0x7A (2170).
        m = new LocoNetMessage(new int[]{0xEF, 0x0E, 0x10, 0x33, 0x7E, 0x00, 0x20, 0x06, 0x00, 0x2A, 0x08, 0x7A, 0x10, 0x2D});
        _lnis.sendTestMessage(m);
        
        Assert.assertTrue("reply not received", JUnitUtil.waitFor(()->{return _lnis.outbound.size() >= 1;}));
        
        for (LocoNetMessage l : _lnis.outbound) {
            System.out.format("LocoNet: %s%n", l.toString());
            System.out.format("LocoNet: %s%n", l.toMonitorString());
        }
        
        // (SV Format 2) Discover all devices request initiated by agent 1
        int[] data = {0xE5, 0x10, 0x01, 0x07, 0x02, 0x10, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x0E};
        Assert.assertEquals("num bytes sent is correct", data.length, _lnis.outbound.get(0).getNumDataElements());
        for (int i=0; i < data.length-1; i++) {     // Don't check the last byte since the checksum is not calculated yet.
            Assert.assertEquals("sent byte "+Integer.toString(i), data[i] & 0xFF, _lnis.outbound.get(0).getElement(i) & 0xFF);
        }
        
        // (SV Format 2) Reply from destination address 4 218 to Discover devices request initiated by agent 1:
        // Device characteristics are manufacturer 13, developer number 1, product 11, serial number 4 218
        m = new LocoNetMessage(new int[]{0xE5, 0x10, 0x01, 0x47, 0x02, 0x10, 0x7A, 0x10, 0x0D, 0x01, 0x10, 0x0B, 0x00, 0x7A, 0x10, 0x49});
        _lnis.sendTestMessage(m);
        
        Thread.sleep(20000);
        
        JUnitUtil.dispose(frame);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initRosterConfigManager();
        
        _memo = new LocoNetSystemConnectionMemo();
        _lnis = new LocoNetInterfaceScaffold(_memo);
        _memo.setLnTrafficController(_lnis);
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
