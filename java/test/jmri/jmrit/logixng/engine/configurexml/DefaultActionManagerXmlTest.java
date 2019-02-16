package jmri.jmrit.logixng.engine.configurexml;

import jmri.jmrit.logixng.engine.configurexml.DefaultActionManagerXml;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class DefaultActionManagerXmlTest {

    @Test
    public void testCTor() {
        new DefaultActionManagerXml();
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

    // private final static Logger log = LoggerFactory.getLogger(DefaultActionManagerXmlTest.class);

}
