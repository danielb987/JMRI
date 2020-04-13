package jmri.jmrix.loconet.nodes.swing;

import java.awt.GraphicsEnvironment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import jmri.InstanceManager;
import jmri.ThrottleManager;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrit.roster.Roster;
import jmri.jmrit.roster.RosterEntry;
import jmri.jmrit.symbolicprog.ProgDefault;
import jmri.jmrix.loconet.LnCommandStationType;
import jmri.jmrix.loconet.LnThrottleManager;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LocoNetInterfaceScaffold;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.nodes.LnNodeManager;
import jmri.util.JUnitUtil;
import jmri.util.ThreadingUtil;
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
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.util.NameComponentChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test DiscoverNodesFrameTest
 * 
 * @author Daniel Bergqvist 2020
 */
public class DiscoverThrottleFrameTest {

    private LocoNetInterfaceScaffold _lnis;
    private LocoNetSystemConnectionMemo _memo;
    private ThrottleManager _tm = null;
    
    private void showDiscoverThrottleFrame() {
        ThreadingUtil.runOnGUI(() -> {
            DiscoverThrottleFrame f = new DiscoverThrottleFrame(_memo);
            f.initComponents();
            f.setVisible(true);
        });
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
    
    private void assertTextField(JFrameOperator jf, String label, String value) {
        // Find the combo box by label
        JLabelOperator jlo = new JLabelOperator(jf,label);
        JTextFieldOperator to = new JTextFieldOperator((JTextField) jlo.getLabelFor());
        Assert.assertEquals(label, value, to.getText());
    }
    
    private void verifyDiscoverThrottleFrameValues(JFrame f1, JFrameOperator jf) {
        assertTextField(jf, Bundle.getMessage("LocoAddress"), "5502");
        assertTextField(jf, Bundle.getMessage("ThrottleId"), "4218");
        assertTextField(jf, Bundle.getMessage("Manufacturer"), "Public-domain and DIY");
        assertTextField(jf, Bundle.getMessage("Developer"), "FREMO");
        assertTextField(jf, Bundle.getMessage("Product"), "FREDi using LNSV2");
        
        JTextField throttleId_Hex = JTextFieldOperator.findJTextField(f1, new NameComponentChooser("throttleId_Hex"));
        Assert.assertEquals("throttleId_Hex", "0x107A", throttleId_Hex.getText());
    }
    
    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        ThreadingUtil.runOnGUI(() -> {
            DiscoverThrottleFrame b = new DiscoverThrottleFrame(_memo);
            Assert.assertNotNull("exists", b);
        });
    }
    
    @Test
    public void testDiscoverThrottle() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        showDiscoverThrottleFrame();
        
        JFrame frame = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);  // NOI18N
        Assert.assertNotNull(frame);
        
        connectThrottle();
        
        verifyDiscoverThrottleFrameValues(frame, new JFrameOperator(frame));
        
        JUnitUtil.dispose(frame);
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
    public void testOpenProgrammerNewNode() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        // Show the discover throttle frame
        showDiscoverThrottleFrame();
        
        // Connect the FREDi
        connectThrottle();
        
        // Clear LocoNet outbound list
        _lnis.outbound.clear();
        
        // Test when ProgDefault.getDefaultProgFile() returns not null
        ProgDefault.setDefaultProgFile(ProgDefault.findListOfProgFiles()[0]);
        // Find the discover throttle frame
        JFrame f1 = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);
        JFrameOperator jf = new JFrameOperator(f1);
        verifyDiscoverThrottleFrameValues(f1, jf);
        // Find the combo box labeled "RosterEntry"
        JLabelOperator jlo = new JLabelOperator(jf,Bundle.getMessage("RosterEntry"));
        // Wait for the buttom OpenProgrammer gets enables
        JButtonOperator bo = new JButtonOperator(jf,Bundle.getMessage("ButtonOpenProgrammer"));
        Assert.assertTrue("button is enabled", JUnitUtil.waitFor(()->{return bo.isEnabled();}));
        // Select item "Test throttle"
        JComboBoxOperator co = new JComboBoxOperator((JComboBox) jlo.getLabelFor());
        
        co.selectItem("New roster entry");
        
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
        
        JUnitUtil.dispose(f1);
    }
    
    @Test
    public void testOpenProgrammerExistingNode() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        addItemToRoster();
        
        // Show the discover throttle frame
        showDiscoverThrottleFrame();
        
        // Connect the FREDi
        connectThrottle();
        
        // Clear LocoNet outbound list
        _lnis.outbound.clear();
        
        // Test when ProgDefault.getDefaultProgFile() returns null
        ProgDefault.setDefaultProgFile(null);
        
        // Find the discover throttle frame
        JFrame f1 = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);
        JFrameOperator jf = new JFrameOperator(f1);
        verifyDiscoverThrottleFrameValues(f1, jf);
        // Find the combo box labeled "RosterEntry"
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
        
        JUnitUtil.dispose(f1);
    }
    
    private void expectReply(int[] data) {
        Assert.assertTrue("reply not received", JUnitUtil.waitFor(()->{return _lnis.outbound.size() >= 1;}));
/*        
        System.out.format("LocoNet expectReply: ");
        for (int i=0; i < data.length; i++) {     // Don't check the last byte since the checksum is not calculated yet.
            System.out.format("%2X ", data[i]);
        }
        System.out.format("%n");
        
        for (LocoNetMessage l : _lnis.outbound) {
            System.out.format("LocoNet: %s%n", l.toString());
            System.out.format("LocoNet: %s", l.toMonitorString());
        }
        System.out.format("LocoNet expectReply done.%n");
        System.out.format("%n");
*/        
        Assert.assertEquals("num bytes sent is correct", data.length, _lnis.outbound.get(0).getNumDataElements());
        for (int i=0; i < data.length-1; i++) {     // Don't check the last byte since the checksum is not calculated yet.
            Assert.assertEquals("sent byte "+Integer.toString(i), data[i] & 0xFF, _lnis.outbound.get(0).getElement(i) & 0xFF);
        }
        
        // Remove the SV read message
        _lnis.outbound.remove(0);
    }
    
    @Test
    public void testDispatchThrottle() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        // Show the discover throttle frame
        showDiscoverThrottleFrame();
        
        // Clear LocoNet outbound list
        _lnis.outbound.clear();
        
        
        // Find the discover throttle frame
        JFrame f1 = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);
        JFrameOperator jf = new JFrameOperator(f1);
        
        // Find the text box by label
        JLabelOperator jlo = new JLabelOperator(jf,Bundle.getMessage("DispatchInfoMessage1"));
        JTextFieldOperator to = new JTextFieldOperator((JTextField) jlo.getLabelFor());
        
        to.setText("5502");
        
        // And press Dispatch
        jmri.util.swing.JemmyUtil.pressButton(jf,Bundle.getMessage("ButtonDispatch"));
        
        // Request slot for loco address 5502
        int[] data = {0xBF, 0x2A, 0x7E, 0x14};
        expectReply(data);
        
        // Report of slot 16 information:
	// Loco 5502 is Not Consisted, Idle, operating in 128 SS mode, and is moving Forward at speed 0,
	// F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
	// Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x02 0x71 (369).
        LocoNetMessage m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x23, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x71, 0x02, 0x05});
        _lnis.sendTestMessage(m);
        
        // Set status of slot 16 to IN_USE
        data = new int[]{0xBA, 0x10, 0x10, 0x45};
        expectReply(data);
        
        // Report of slot 16 information:
	// Loco 5502 is Not Consisted, In-Use, operating in 128 SS mode, and is moving Forward at speed 0,
	// F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
	// Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x02 0x71 (369).
        m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x33, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x71, 0x02, 0x15});
        _lnis.sendTestMessage(m);
        
        // Write slot 16 with status value 19 (0x13) - Loco is Not Consisted, Common and operating in 128 speed step mode
        data = new int[]{0xB5, 0x10, 0x13, 0x49};
        expectReply(data);
        
        // Mark slot 16 as DISPATCHED
        data = new int[]{0xBA, 0x10, 0x00, 0x55};
        expectReply(data);
        
        // Report of slot 16 information:
	// Loco 5502 is Not Consisted, Idle, operating in 128 SS mode, and is moving Forward at speed 0,
	// F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
	// Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x10 0x7A (2170)
        m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x23, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x7A, 0x10, 0x1C});
        _lnis.sendTestMessage(m);
        
        // Write slot 16 with status value 19 (0x13) - Loco is Not Consisted, Common and operating in 128 speed step mode
        data = new int[]{0xB5, 0x10, 0x13, 0x49};
        expectReply(data);
        
//        Thread.sleep(20000);
        
        JUnitUtil.dispose(f1);
    }
    
    @Test
    public void testDispatchThrottleBadAddress() throws InterruptedException {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        // Show the discover throttle frame
        showDiscoverThrottleFrame();
        
        // Find the discover throttle frame
        JFrame f1 = JFrameOperator.waitJFrame(Bundle.getMessage("DiscoverThrottleWindowTitle"), true, true);
        JFrameOperator jf = new JFrameOperator(f1);
        
        // Find the text box by label
        JLabelOperator jlo = new JLabelOperator(jf,Bundle.getMessage("DispatchInfoMessage1"));
        JTextFieldOperator to = new JTextFieldOperator((JTextField) jlo.getLabelFor());
        
        to.setText("a2");   // This address is deliberately bad
        
        // And press Dispatch
        jmri.util.swing.JemmyUtil.pressButton(jf,Bundle.getMessage("ButtonDispatch"));
        
        // Find the discover throttle frame
        JDialog d = JDialogOperator.waitJDialog("Message", true, true);
        JDialogOperator jd = new JDialogOperator(d);
        
        // Find the text box by label, to verify that the message box show the correct message
        new JLabelOperator(jd,Bundle.getMessage("AddressIsInvalid"));
        
        jd.requestClose();
        
//        Thread.sleep(20000);
        
        JUnitUtil.dispose(f1);
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
        
        _memo = new LocoNetSystemConnectionMemo();
        _lnis = new LocoNetInterfaceScaffold(_memo);
        _memo.setLnTrafficController(_lnis);
        _memo.configureCommandStation(LnCommandStationType.COMMAND_STATION_DCS100, false, false, false);
        _memo.configureManagers();
        _tm = new LnThrottleManager(_memo);
        InstanceManager.setDefault(ThrottleManager.class, _tm);
        log.debug("new throttle manager is {}", _tm.toString());
        _memo.getSensorManager().dispose(); // get rid of sensor manager to prevent it from sending interrogation messages
        _memo.getPowerManager().dispose(); // get rid of power manager to prevent it from sending slot 0 read message
        
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    private final static Logger log = LoggerFactory.getLogger(DiscoverThrottleFrameTest.class);

}
