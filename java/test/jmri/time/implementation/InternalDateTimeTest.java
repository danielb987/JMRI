package jmri.time.implementation;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import jmri.time.TimeProvider;

import jmri.util.JUnitUtil;
import jmri.util.ThreadingUtil;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for InternalDateTime class.
 *
 * @author Bob Jacobsen (c) 2025
 **/
public class InternalDateTimeTest {

    InternalDateTime idt;
    ZonedDateTime time;

    @Test
    public void testCheckStartValues(){
        assertEquals(1.0, idt.getRate().getRate(), "initial rate");
        assertFalse(idt.isRunning(), "initial running");
    }

    @Test
    public void testTickExactlyOneMinuteLaterFiresMinutes() {
        long[] fakeNow = {1_000_000L};
        InternalDateTime clock = new InternalDateTime("IU2") {
            @Override
            long now() {
                return fakeNow[0];
            }
        };
        clock.init();
        List<String> events = new ArrayList<>();
        clock.addPropertyChangeListener(evt -> events.add(evt.getPropertyName()));
        clock.start();
        clock.setDateTime(ZonedDateTime.of(2026, 11, 26, 10, 15, 30, 0, ZoneOffset.UTC));
        events.clear();

        // a stalled timer: the next tick comes exactly 60 s later, on the same second
        fakeNow[0] += 60_000;
        // on the GUI thread, like the clock's own timer, so the two ticks can't overlap
        ThreadingUtil.runOnGUI(clock::tick);

        assertEquals(ZonedDateTime.of(2026, 11, 26, 10, 16, 30, 0, ZoneOffset.UTC), clock.getTime());
        assertTrue(events.contains(TimeProvider.PROPERTY_CHANGE_SECONDS), "seconds event fired");
        assertTrue(events.contains(TimeProvider.PROPERTY_CHANGE_MINUTES), "minutes event fired");
        clock.dispose();
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.initDefaultUserMessagePreferences();
        JUnitUtil.initTimeProviderManager();

        idt = new InternalDateTime("IU1");
        idt.init();
        idt.lockFromRunning();
        time = idt.getTime();
    }

    @AfterEach
    public void tearDown(){
        time = null;
        JUnitUtil.tearDown();
    }

}
