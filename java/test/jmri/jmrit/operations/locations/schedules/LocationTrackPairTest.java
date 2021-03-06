package jmri.jmrit.operations.locations.schedules;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import jmri.jmrit.operations.OperationsTestCase;
import jmri.jmrit.operations.locations.Location;
import jmri.jmrit.operations.locations.Track;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class LocationTrackPairTest extends OperationsTestCase {

    @Test
    public void testCTor() {
        Location l = new Location("Location Test Attridutes id", "Location Test Name");
        Track trk = new Track("Test id", "Test Name", "Test Type", l);
        LocationTrackPair t = new LocationTrackPair(trk);
        Assert.assertNotNull("exists", t);
    }

    // private final static Logger log =
    // LoggerFactory.getLogger(LocationTrackPairTest.class);

}
