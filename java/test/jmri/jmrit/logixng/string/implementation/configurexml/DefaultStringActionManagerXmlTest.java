package jmri.jmrit.logixng.string.implementation.configurexml;

import jmri.jmrit.logixng.digital.implementation.configurexml.DefaultDigitalActionManagerXml;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class DefaultStringActionManagerXmlTest {

    @Test
    public void testCTor() {
        DefaultStringActionManagerXml b = new DefaultStringActionManagerXml();
        Assert.assertNotNull("exists", b);
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(DefaultStringActionManagerXmlTest.class);

}
