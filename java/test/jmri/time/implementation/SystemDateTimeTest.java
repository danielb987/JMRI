package jmri.time.implementation;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import jmri.time.TimeProvider;
import jmri.util.JUnitUtil;
import jmri.util.ThreadingUtil;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for SystemDateTime class.
 */
public class SystemDateTimeTest {

    @Test
    public void testTickExactlyOneMinuteLaterFiresMinutes() {
        ZonedDateTime[] fakeNow = {ZonedDateTime.of(2026, 11, 26, 10, 15, 30, 0, ZoneOffset.UTC)};
        SystemDateTime clock = new SystemDateTime("IU3") {
            @Override
            ZonedDateTime now() {
                return fakeNow[0];
            }
        };
        clock.init();
        List<String> events = new ArrayList<>();
        clock.addPropertyChangeListener(evt -> events.add(evt.getPropertyName()));

        // the computer slept: the next tick comes exactly 60 s later, on the same second
        fakeNow[0] = fakeNow[0].plusSeconds(60);
        // on the GUI thread, like the clock's own timer, so the two ticks can't overlap
        ThreadingUtil.runOnGUI(clock::tick);

        assertTrue(events.contains(TimeProvider.PROPERTY_CHANGE_SECONDS), "seconds event fired");
        assertTrue(events.contains(TimeProvider.PROPERTY_CHANGE_MINUTES), "minutes event fired");
        clock.dispose();
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
    }

    @AfterEach
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
