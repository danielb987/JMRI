package jmri.jmrit.logixng.ztest;

import java.awt.GraphicsEnvironment;
import java.util.Locale;
import jmri.util.JUnitUtil;
import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 * Tests for the LogixNG_Startup Class
 * @author Dave Sand Copyright (C) 2018
 */
public class LogixNG_StartupTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreate() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertNotNull("exists", new LogixNG_Startup());
    }

//    @Ignore("For testing only. This method should be removed before merging into master.")
    @Test
    public void testAction() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        new LogixNG_StartupAction().actionPerformed(null);
    }

    @Test
    public void testGetTitle() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertEquals("LogixNG test action", new LogixNG_Startup().getTitle(LogixNG_StartupAction.class, Locale.US));  // NOI18N
    }

//    @Test
//    public void testGetTitleException() {
//        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
//        thrown.expect(IllegalArgumentException.class);
//        Assert.assertEquals("Open Timetable Exception", new LogixNG_Startup().getTitle(TimeTableFrame.class, Locale.US));  // NOI18N
//    }

    @Test
    public void testGetClass() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertNotNull(new LogixNG_Startup().getActionClasses());
    }

    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initInternalSensorManager();
        
        JUnitUtil.initInternalSignalHeadManager();
        JUnitUtil.initDefaultSignalMastManager();
        JUnitUtil.initSignalMastLogicManager();
        JUnitUtil.initOBlockManager();
        JUnitUtil.initWarrantManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
}