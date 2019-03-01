package jmri.jmrit.logixng.engine.configurexml;

import jmri.jmrit.logixng.engine.configurexml.DefaultDigitalActionManagerXml;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class DefaultDigitalActionManagerXmlTest {

    @Test
    public void testCTor() {
        new DefaultDigitalActionManagerXml();
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

    // private final static Logger log = LoggerFactory.getLogger(DefaultDigitalActionManagerXmlTest.class);

}
