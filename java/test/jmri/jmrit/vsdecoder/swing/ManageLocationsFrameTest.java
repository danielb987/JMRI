package jmri.jmrit.vsdecoder.swing;

import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.SortedSet;
import jmri.Block;
import jmri.BlockManager;
import jmri.InstanceManager;
import jmri.Reporter;
import jmri.ReporterManager;
import jmri.jmrit.operations.locations.Location;
import jmri.jmrit.operations.locations.LocationManager;
import jmri.jmrit.vsdecoder.listener.ListeningSpot;
import org.junit.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class ManageLocationsFrameTest extends jmri.util.JmriJFrameTestBase {

    @Before
    @Override
    public void setUp() {
        jmri.util.JUnitUtil.setUp();
        ListeningSpot s = new ListeningSpot();
        ReporterManager rmgr = jmri.InstanceManager.getDefault(jmri.ReporterManager.class);
        SortedSet<Reporter> reporterSet = rmgr.getNamedBeanSet();

        Object[][] reporterTable = new Object[reporterSet.size()][6];
        BlockManager bmgr = jmri.InstanceManager.getDefault(jmri.BlockManager.class);
        SortedSet<Block> blockSet = bmgr.getNamedBeanSet();
        Object[][] blockTable = new Object[blockSet.size()][6];

        LocationManager lmgr = InstanceManager.getDefault(LocationManager.class);
        List<Location> locations = lmgr.getLocationsByIdList();
        Object[][] opsTable = new Object[locations.size()][6];


        if(!GraphicsEnvironment.isHeadless()){
           frame = new ManageLocationsFrame(s,reporterTable,blockTable,opsTable);
        }
    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(ManageLocationsFrameTest.class.getName());

}
