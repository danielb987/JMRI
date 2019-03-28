package jmri.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * This test demonstrates wrong behaviour of Assume.assumeNoException()
 * 
 * @author Daniel Bergqvist 2019
 */
public class AssumeNoExceptionTest {

    @Test
    public void assumeNoException() {
        try {
            throw new NullPointerException("Test exception");
        } catch (NullPointerException ex) {
            Assume.assumeNoException(ex);
//            throw ex;
        }
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        jmri.util.JUnitUtil.setUp();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
