package jmri.jmrix.loconet.nodes.swing;

import java.awt.GraphicsEnvironment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import jmri.InstanceManager;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrit.roster.Roster;
import jmri.jmrit.roster.RosterEntry;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LocoNetInterfaceScaffold;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.SlotManager;
import jmri.jmrix.loconet.nodes.LnNodeManager;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JLabelOperator;

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
//        f.setLocation(20,40);
        f.setVisible(true);
        return f;
    }
    
    private void connectThrottle() {
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
        
//        for (LocoNetMessage l : _lnis.outbound) {
//            System.out.format("LocoNet: %s%n", l.toString());
//            System.out.format("LocoNet: %s%n", l.toMonitorString());
//        }
        
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
    }
    
    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        DiscoverThrottleFrame b = new DiscoverThrottleFrame(_memo);
        Assert.assertNotNull("exists", b);
    }
    
    @Ignore
    @Test
    public void testDiscoverThrottle() throws InterruptedException {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
//        for (LocoNetMessage l : _lnis.outbound) {
//            System.out.format("LocoNet aaa: %s%n", l.toMonitorString());
//        }
        
        // Clear list of loconet messages
//        _lnis.outbound.clear();
        
        showDiscoverThrottleFrame();
        
        JFrame frame = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);  // NOI18N
        Assert.assertNotNull(frame);
        
        connectThrottle();
        
        Thread.sleep(20000);
        
        JUnitUtil.dispose(frame);
    }
    
    @Test
    public void testOpenProgrammerNewNode() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assume.assumeTrue("To do", false);
    }
    
    private void addItemToRoster() {
        LnNodeManager lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        int manufacturerID = 13;    // NMRA DIY
        int developerID = 1;        // FREMO
        int productID = 11;         // FREDi
        int _discoverThrottleID = 4218;
        DecoderFile decoderFile = lnNodeManager.getProduct(manufacturerID, developerID, productID);
        RosterEntry re = new RosterEntry();
        re.setDccAddress(Integer.toString(_discoverThrottleID));
        re.setMfg(decoderFile.getMfg());
        re.setDecoderFamily(decoderFile.getFamily());
        re.setDecoderModel(decoderFile.getModel());
        re.setId("Test throttle"); // NOI18N
        // note that we're leaving the filename null
        // add the new roster entry to the in-memory roster
        Roster.getDefault().addEntry(re);
    }
    
    private void respondToSvReadMessages() {
        // svSet is a set of bits, where each bit tells whenether that SV is read.
        // We expect SV1 - SV30 to be read.
        long svSet = 0;
        for (int i=1; i <= 30; i++) svSet |= 1 << (i-1);
        
        final int svNoIndex = 8;
        final int valueHighIndex = 10;
        final int valueLowIndex = 11;
        
        int[] svValues = {2, 20, 122, 16, 4, 4, 1, 42, 126, 3, 85, 3, 2, 0, 37, 3, 25, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255};
        
        while (svSet != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i=1; i <= 30; i++) {
                if ((svSet & (1 << (i-1))) != 0) sb.append(i).append(", ");
            }
            
            if (!JUnitUtil.waitFor(()->{return _lnis.outbound.size() > 0;})) {
                System.out.format("Not all SVs are read: %s%n", sb.toString());
                break;
            }
//            Assert.assertTrue("reply not received. numSVsNotRead: "+sb.toString(), JUnitUtil.waitFor(()->{return _lnis.outbound.size() > 0;}));
            
//            for (LocoNetMessage l : _lnis.outbound) {
//                System.out.format("LocoNet: %s%n", l.toString());
//                System.out.format("LocoNet: %s%n", l.toMonitorString());
//            }
            
            int svNo = -1;
            
            // (SV Format 2) Read single SV request to destination address 4 218 initiated by agent 1: Read request for SV#
            int[] data = {0xE5, 0x10, 0x01, 0x02, 0x02, 0x10, 0x7A, 0x10, 0x1E, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x7F};
            Assert.assertEquals("num bytes sent is correct", data.length, _lnis.outbound.get(0).getNumDataElements());
            for (int i=0; i < data.length-1; i++) {     // Don't check the last byte since the checksum is not calculated yet.
                if (i == svNoIndex) {
                    svNo = _lnis.outbound.get(0).getElement(i) & 0xFF;
                } else {
                    Assert.assertEquals("sent byte "+Integer.toString(i), data[i] & 0xFF, _lnis.outbound.get(0).getElement(i) & 0xFF);
                }
            }
            
            // Clear bit in sv set
            svSet &= ~ (1 << (svNo-1));
            
            // (SV Format 2) Reply from destination address 4 218 for Read single SV request initiated by agent 1: SV# current value is #
            data = new int[]{0xE5, 0x10, 0x01, 0x42, 0x02, 0x10, 0x7A, 0x10, 0x1E, 0x00, 0x11, 0x7F, 0x00, 0x00, 0x00, 0x00};
            data[svNoIndex] = svNo;
            data[valueHighIndex] = 0x10 + (svValues[svNo-1] >> 7);
            data[valueLowIndex] = svValues[svNo-1] & 0x7F;
            LocoNetMessage m = new LocoNetMessage(data);
            _lnis.sendTestMessage(m);
            
            // Remove the SV read message
            _lnis.outbound.remove(0);
        }
    }
    
    @Test
    public void testOpenProgrammerExistingNode() throws InterruptedException {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        addItemToRoster();
        
        // Show the discover throttle frame
        showDiscoverThrottleFrame();
        
        // Connect the FREDi
        connectThrottle();
        
        // Clear LocoNet outbound list
        _lnis.outbound.clear();
        
        
        // Find the discover throttle frame
        JFrame f1 = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);
        JFrameOperator jf = new JFrameOperator(f1);
        // Select the item "aaa" in the combo box labeled "RosterEntry"
        JLabelOperator jlo = new JLabelOperator(jf,Bundle.getMessage("RosterEntry"));
        // Wait for the buttom OpenProgrammer gets enables
        JButtonOperator bo = new JButtonOperator(jf,Bundle.getMessage("ButtonOpenProgrammer"));
        Assert.assertTrue("button is enabled", JUnitUtil.waitFor(()->{return bo.isEnabled();}));
        // Select item "Test throttle"
        JComboBoxOperator co = new JComboBoxOperator((JComboBox) jlo.getLabelFor());
        co.selectItem("Test throttle");
        // And press OpenProgrammer
        jmri.util.swing.JemmyUtil.pressButton(jf,Bundle.getMessage("ButtonOpenProgrammer"));
        
        
        // The programmer reads SV1 - SV30
        respondToSvReadMessages();
        
        
        // Find the programmer frame
        JFrame f2 = JFrameOperator.waitJFrame("Program FREDi using LNSV2 in Service Mode (Programming Track)", true, true);
        JFrameOperator jf2 = new JFrameOperator(f2);
        jf2.requestClose();
        
        // Find the "Choose one" frame that gives the user the option to save changes
        JDialog f3 = JDialogOperator.waitJDialog("Choose one", true, true);
        JDialogOperator jf3 = new JDialogOperator(f3);
        jf3.requestClose();
        
        
        
        
//        Thread.sleep(20000);
        
        JUnitUtil.dispose(f1);
        
        Assume.assumeTrue("To do", false);
    }
    
    @Test
    public void testDispatchThrottle() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assume.assumeTrue("To do", false);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initRosterConfigManager();
        JUnitUtil.initDebugProgrammerManager();
        InstanceManager.setDefault(jmri.jmrit.symbolicprog.ProgrammerConfigManager.class,
                new jmri.jmrit.symbolicprog.ProgrammerConfigManager());
        
//        _memo = new LocoNetSystemConnectionMemo();
//        _lnis = new LocoNetInterfaceScaffold(_memo);
//        _memo.setLnTrafficController(_lnis);
        
        _lnis = new LocoNetInterfaceScaffold();
        SlotManager sm = new SlotManager(_lnis);
        _memo = new LocoNetSystemConnectionMemo(_lnis, sm);
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
