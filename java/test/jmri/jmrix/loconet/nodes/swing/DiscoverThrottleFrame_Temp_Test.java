package jmri.jmrix.loconet.nodes.swing;

import java.awt.GraphicsEnvironment;
import java.util.Vector;
import javax.swing.*;
import jmri.InstanceManager;
import jmri.ThrottleManager;
import jmri.jmrix.loconet.*;
import jmri.util.JUnitUtil;
import jmri.util.ThreadingUtil;
import org.junit.Assert;
import org.junit.Assume;
//import org.junit.*;
import org.netbeans.jemmy.operators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test DiscoverNodesFrameTest
 * 
 * @author Daniel Bergqvist 2020
 */
public class DiscoverThrottleFrame_Temp_Test {

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
    
    private void expectReply(int[] data) {
        Assert.assertTrue("reply not received", JUnitUtil.waitFor(()->{return _lnis.outbound.size() >= 1;}));
        
        if (data.length != _lnis.outbound.get(0).getNumDataElements()) {
            log.error(String.format("Count: %d: num bytes sent is wrong: expected: %d, actual: %d", count, data.length, _lnis.outbound.get(0).getNumDataElements()));
        }
//        Assume.assumeTrue(data.length == _lnis.outbound.get(0).getNumDataElements());
        
//        Assert.assertEquals("Random: "+Long.toString(Math.round(Math.random()*100))+",  num bytes sent is correct. Count: "+Integer.toString(count), data.length, _lnis.outbound.get(0).getNumDataElements());
        Assert.assertEquals("num bytes sent is correct", data.length, _lnis.outbound.get(0).getNumDataElements());
        
        if (data.length != _lnis.outbound.get(0).getNumDataElements()) {
            Vector<LocoNetMessage> outbound = new Vector<>(_lnis.outbound);
            log.error(String.format("LocoNet expectReply: "));
            for (int i=0; i < data.length; i++) {     // Don't check the last byte since the checksum is not calculated yet.
                log.error(String.format("%2X ", data[i]));
            }
            log.error(String.format("%n"));
            
            for (LocoNetMessage l : outbound) {
                log.error(String.format("LocoNet: %s%n", l.toString()));
                log.error(String.format("LocoNet: %s", l.toMonitorString()));
            }
            log.error(String.format("LocoNet expectReply done.%n"));
            log.error(String.format("%n"));
        }
        
        Assert.assertEquals("num bytes sent is correct", data.length, _lnis.outbound.get(0).getNumDataElements());
        for (int i=0; i < data.length-1; i++) {     // Don't check the last byte since the checksum is not calculated yet.
            Assert.assertEquals("sent byte "+Integer.toString(i), data[i] & 0xFF, _lnis.outbound.get(0).getElement(i) & 0xFF);
        }
        
        // Remove the SV read message
        _lnis.outbound.remove(0);
    }
    
//    @RepeatedTest(1000)
//    @RepeatedTest(100)
    @RepeatedTest(20)
//    @RepeatedTest(2)
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
        
        // Step 1: Request loco
        
        // When the button "Dispatch" is pressed, JMRI first request that loco
        // Request slot for loco address 5502
        int[] data = {0xBF, 0x2A, 0x7E, 0x14};
        expectReply(data);
        
        // The command station reponds to that throttle request
        // Report of slot 16 information:
        // Loco 5502 is Not Consisted, Idle, operating in 128 SS mode, and is moving Forward at speed 0,
        // F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
        // Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x02 0x71 (369).
        LocoNetMessage m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x23, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x71, 0x02, 0x05});
        _lnis.sendTestMessage(m);
        
        // JMRI sets the status of the slot to be IN_USE
        // Set status of slot 16 to IN_USE
        data = new int[]{0xBA, 0x10, 0x10, 0x45};
        expectReply(data);
        
        // The command station responds with updated status of the slot
        // Report of slot 16 information:
        // Loco 5502 is Not Consisted, In-Use, operating in 128 SS mode, and is moving Forward at speed 0,
        // F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
        // Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x02 0x71 (369).
        m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x33, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x71, 0x02, 0x15});
        _lnis.sendTestMessage(m);
        
        Assert.assertTrue("reply not received", JUnitUtil.waitFor(()->{return _lnis.outbound.size() >= 1;}));
        
//        if (1==0 && _lnis.outbound.get(0).getOpCode() == 0xEF) {
            // Write slot 16 information:
            // Loco 5502 is Not Consisted, In-Use, operating in 128 SS mode, and is moving Forward at speed 0,
            // F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
            // Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x02 0x71 (369).
            data = new int[]{0xEF, 0x0E, 0x10, 0x33, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x71, 0x02, 0x1D};
            expectReply(data);
//        }
        
        // Step 2: Dispatch the loco
        
        // JMRI now dispatches the loco
        // Write slot 16 with status value 19 (0x13) - Loco is Not Consisted, Common and operating in 128 speed step mode
        data = new int[]{0xB5, 0x10, 0x13, 0x49};
        expectReply(data);
        
        // Mark slot 16 as DISPATCHED
        data = new int[]{0xBA, 0x10, 0x00, 0x55};
        expectReply(data);
        
        // The command station responds with updated status of the slot
        // Report of slot 16 information:
        // Loco 5502 is Not Consisted, Idle, operating in 128 SS mode, and is moving Forward at speed 0,
        // F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
        // Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x10 0x7A (2170)
        m = new LocoNetMessage(new int[]{0xE7, 0x0E, 0x10, 0x23, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x7A, 0x10, 0x1C});
        _lnis.sendTestMessage(m);
        
        Assert.assertTrue("reply not received", JUnitUtil.waitFor(()->{return _lnis.outbound.size() >= 1;}));
        
/*        if (1==0 && _lnis.outbound.get(0).getOpCode() == 0xEF) {
            // Write slot 16 information:
            // Loco 5502 is Not Consisted, In-Use, operating in 128 SS mode, and is moving Forward at speed 0,
            // F0=Off, F1=Off, F2=Off, F3=Off, F4=Off, F5=Off, F6=Off, F7=Off, F8=Off
            // Master supports LocoNet 1.1; Track Status: On/Running; Programming Track Status: Available; STAT2=0x00, ThrottleID=0x02 0x71 (369).
            data = new int[]{0xEF, 0x0E, 0x10, 0x33, 0x7E, 0x00, 0x00, 0x07, 0x00, 0x2A, 0x00, 0x71, 0x02, 0x1D};
            expectReply(data);
        }
*/        
        // Write slot 16 with status value 19 (0x13) - Loco is Not Consisted, Common and operating in 128 speed step mode
        data = new int[]{0xB5, 0x10, 0x13, 0x49};
        expectReply(data);
        
//        Thread.sleep(20000);
        
        JUnitUtil.dispose(f1);
    }
    
    // The minimal setup for log4J
    @BeforeEach
    public void setUp() {
        count++;
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

    @AfterEach
    public void tearDown() throws InterruptedException {
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
        _lnis = null;
        _memo = null;
        _tm = null;
//        System.gc();
//        Thread.sleep(5000);
    }
    
    static int count = 0;
    private final static Logger log = LoggerFactory.getLogger(DiscoverThrottleFrame_Temp_Test.class);

}
