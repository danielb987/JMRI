package jmri.jmrit.logixng.digital.expressions.configurexml;

import jmri.jmrit.logixng.digital.expressions.configurexml.AndXml;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionTurnoutXml
 * 
 * @author Daniel Bergqvist 2019
 */
public class ExpressionTurnoutXmlTest {

    @Test
    public void testCtor() {
        new ExpressionTurnoutXml();
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
