package jmri.jmrit.newlogix.swing;

import jmri.jmrit.newlogix.tools.swing.TimeDiagram;
import java.awt.GraphicsEnvironment;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Test TimeDiagram
 * 
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public class TimeDiagramTest {

    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
        TimeDiagram t = new TimeDiagram();
        Assert.assertNotNull("exists",t);
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

    // private final static Logger log = LoggerFactory.getLogger(TimeDiagramTest.class);

}
