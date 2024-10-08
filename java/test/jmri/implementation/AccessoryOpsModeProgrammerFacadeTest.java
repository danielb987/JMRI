package jmri.implementation;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jmri.CommandStation;
import jmri.InstanceManager;
import jmri.ProgListener;
import jmri.Programmer;
import jmri.progdebugger.ProgDebugger;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * Test the AccessoryOpsModeProgrammerFacade class.
 *
 * @author Bob Jacobsen Copyright 2014
 * @author Dave Heap 2017
 *
 */
// @ToDo("transform to annotations requires e.g. http://alchemy.grimoire.ca/m2/sites/ca.grimoire/todo-annotations/")
// @ToDo("test mode handling")
// @ToDo("test packet contents in each mode")
// @ToDo("test address handling")
public class AccessoryOpsModeProgrammerFacadeTest {

    @Test
    public void testCvLimit() {
        ProgDebugger dp = new ProgDebugger(true, 123);
        dp.setTestReadLimit(1024);
        dp.setTestWriteLimit(1024);

        Programmer p = new AccessoryOpsModeProgrammerFacade(dp, "", 0, dp);

        Assert.assertTrue("CV limit read OK", p.getCanRead("1024"));
        Assert.assertTrue("CV limit write OK", p.getCanWrite("1024"));
        Assert.assertTrue("CV limit read fail", !p.getCanRead("1025"));
        Assert.assertTrue("CV limit write fail", !p.getCanWrite("1025"));
    }

    @Test
    public void testWriteAddr123long1cv234val14delay200signal() throws jmri.ProgrammerException, InterruptedException {
        testMethod();
    }

    @Test
    public void testWriteAddr3long0cv12val0delay500accessory() throws jmri.ProgrammerException, InterruptedException {
        testMethod();
    }

    @Test
    public void testWriteAddr511long1cv1024val255delay0decoder() throws jmri.ProgrammerException, InterruptedException {
        testMethod();
    }

    // from here down is testing infrastructure
    // Perform tests with parameters parsed from the name of the calling method.
    synchronized void testMethod() throws jmri.ProgrammerException, InterruptedException {
        String methodName = "";
        int addr = 0;
        boolean isLong = false;
        String addrType = "";
        String cv = "";
        int value = 0;
        int delay = 0;

        ArrayList<String> items = itemsFromMethodName(3, 5);
        if (!items.isEmpty()) {
            methodName = items.get(0);
            addr = Integer.parseInt(items.get(2));
            isLong = (Integer.parseInt(items.get(4)) != 0);
            cv = items.get(6);
            value = Integer.parseInt(items.get(8));
            delay = Integer.parseInt(items.get(10));
            addrType = items.get(11);
        }
        log.debug(
                "Testing: {}:\nExtracted parameters addr='{}',isLong={},cv='{}', value={}, delay={}, addrType='{}'",
                methodName, addr, isLong, cv, value, delay, addrType);

        // Create an addressed programmer.
        ProgDebugger dp = new ProgDebugger(isLong, addr);
        // Create a facade over the base programmer and also a listener for the facade.
        Programmer p = new AccessoryOpsModeProgrammerFacade(dp, addrType, 0, dp);
        ProgListener l = (int value1, int status) -> {
            log.debug("callback value={} status={}", value1, status);
            replied = true;
            readValue = value1;
        };

        // Write to the facade programmer.
        p.writeCV(cv, value, l);
        waitReply();

        Assertions.assertEquals(value, readValue);

        // Check that the write did not through to the base programmer.
        Assert.assertTrue("target not directly written", !dp.hasBeenWritten(value));
        Assert.assertTrue("index not written", !dp.hasBeenWritten(81));
        // Check that a packet was sent.
        Assert.assertNotNull("packet sent", mockCS.lastPacket);
    }

    // Extract test parameters from test name.
    synchronized ArrayList<String> itemsFromMethodName(int methodOffset, int groupReps) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern;
        Matcher matcher;
        ArrayList<String> retString = new ArrayList<>();

        // Extract test parameters from test name.
        String methodName = Thread.currentThread().getStackTrace()[methodOffset].getMethodName();
        sb.append("^");
        for (int i = 1; i <= groupReps; i++) {
            sb.append("(\\D+)(\\d+)");
        }
        sb.append("(\\D*)$");
        String pat = sb.toString();
        pattern = Pattern.compile(pat);
        matcher = pattern.matcher(methodName);
        log.debug("Test: {} pat=\"{}\", groupCount={}", methodName, pat, matcher.groupCount());
        if (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount(); j++) {
                retString.add(matcher.group(j));
                log.debug("Adding item={}, string=\"{}\"", j, matcher.group(j));
            }
        } else {
            log.error("method=\"{}\" did not match pattern=\"{}\"", methodName, pat);
        }
        return retString;
    }

    private MockCommandStation mockCS;
    private int readValue = -2;
    private boolean replied = false;

    synchronized void waitReply() throws InterruptedException {
        while (!replied) {
            wait(200);
        }
        replied = false;
    }

    @BeforeEach
    public void setUp() throws Exception {
        JUnitUtil.setUp();
        mockCS = new MockCommandStation();
        InstanceManager.setDefault(CommandStation.class, mockCS);
        mockCS.lastPacket = null;
        replied = false;
        readValue = -2;
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockCS = null;
        JUnitUtil.tearDown();
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AccessoryOpsModeProgrammerFacadeTest.class);

}
