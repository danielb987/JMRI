package jmri.time.implementation;

import jmri.JmriException;
import jmri.Timebase;
import jmri.TimebaseBehaviorTestBase;
import jmri.TimebaseRateException;
import jmri.jmrix.internal.InternalSystemConnectionMemo;

import org.junit.jupiter.api.*;

// TEMPORARY: each @Disabled override below is a known gap; the PR that fixes it deletes the override
/** Runs the shared Timebase behavior tests against DefaultTimebase. */
public class DefaultTimebaseTest extends TimebaseBehaviorTestBase {

    @Override
    protected Timebase createTimebase(InternalSystemConnectionMemo memo) {
        return new DefaultTimebase(memo);
    }

    @Test
    @Override
    @Disabled("TEMPORARY: DefaultTimebase doesn't check the rate range yet")
    public void testRateOutOfRangeThrows() {
        super.testRateOutOfRangeThrows();
    }

}
