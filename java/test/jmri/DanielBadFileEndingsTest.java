package jmri;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.*;

/**
 *
 * @author Daniel
 */
public class DanielBadFileEndingsTest {

    @Test
    public void testCtor() {
        assertFalse(new DanielBadFileEndings() == null);
    }
    
    @BeforeEach
    public void setUp() {
        jmri.util.JUnitUtil.setUp();
    }

    @AfterEach
    public void tearDown() {
        jmri.util.JUnitUtil.tearDown();
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VersionTest.class);
}
