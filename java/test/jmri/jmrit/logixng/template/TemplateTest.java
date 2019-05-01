package jmri.jmrit.logixng.template;

// import java.awt.GraphicsEnvironment;
import jmri.Audio;
import jmri.AudioManager;
import jmri.IdTag;
import jmri.IdTagManager;
import jmri.Light;
import jmri.LightManager;
import jmri.Logix;
import jmri.LogixManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.OBlockManager;
import jmri.Reporter;
import jmri.ReporterManager;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.SignalHead;
import jmri.SignalHeadManager;
import jmri.SignalMast;
import jmri.SignalMastManager;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Test TemplateTest
 * 
 * @author Daniel Bergqvist 2018
 */
public class TemplateTest {

    @Test
    public void testNullNamedBeansCtor() {
        
        new NullAudio("AB1");
        new NullIdTag("AB1");
        new NullLight("AB1");
        new NullLogix("AB1");
        new NullMemory("AB1");
        new NullReporter("AB1");
        new NullSensor("AB1");
        new NullSignalHead("AB1");
        new NullSignalMast("AB1");
        new NullTurnout("AB1");
    }
    
    @Test
    public void testCtor() {
//        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        
    }
    
    @Test
    public void testTemplateInstanceManager() {
        TemplateInstanceManager tim = new TemplateInstanceManager();
        Audio a = tim.provide(AudioManager.class, NullAudio.class, "IA1");
        IdTag i = tim.provide(IdTagManager.class, NullIdTag.class, "ID1");
        Light l = tim.provide(LightManager.class, NullLight.class, "IL1");
        Logix x = tim.provide(LogixManager.class, NullLogix.class, "IX1");
        Memory m = tim.provide(MemoryManager.class, NullMemory.class, "IM1");
        OBlock ob = tim.provide(OBlockManager.class, OBlock.class, "IB1");
        Reporter r = tim.provide(ReporterManager.class, NullReporter.class, "IR1");
        Sensor s = tim.provide(SensorManager.class, NullSensor.class, "IS1");
        SignalHead sh = tim.provide(SignalHeadManager.class, NullSignalHead.class, "IH1");
        SignalMast sm = tim.provide(SignalMastManager.class, NullSignalMast.class, "IF1");
        Turnout t = tim.provide(TurnoutManager.class, NullTurnout.class, "IT1");
        Assert.assertNotNull("Not null", a);
        Assert.assertNotNull("Not null", i);
        Assert.assertNotNull("Not null", l);
        Assert.assertNotNull("Not null", x);
        Assert.assertNotNull("Not null", m);
        Assert.assertNotNull("Not null", ob);
        Assert.assertNotNull("Not null", r);
        Assert.assertNotNull("Not null", s);
        Assert.assertNotNull("Not null", sh);
        Assert.assertNotNull("Not null", sm);
        Assert.assertNotNull("Not null", t);
        Assert.assertTrue("Objects are the same", a.equals(tim.get(AudioManager.class, NullAudio.class, "IA1")));
        Assert.assertTrue("Objects are the same", i.equals(tim.get(IdTagManager.class, NullIdTag.class, "ID1")));
        Assert.assertTrue("Objects are the same", l.equals(tim.get(LightManager.class, NullLight.class, "IL1")));
        Assert.assertTrue("Objects are the same", x.equals(tim.get(LogixManager.class, NullLogix.class, "IX1")));
        Assert.assertTrue("Objects are the same", m.equals(tim.get(MemoryManager.class, NullMemory.class, "IM1")));
        Assert.assertTrue("Objects are the same", ob.equals(tim.get(OBlockManager.class, OBlock.class, "IB1")));
        Assert.assertTrue("Objects are the same", r.equals(tim.get(ReporterManager.class, NullReporter.class, "IR1")));
        Assert.assertTrue("Objects are the same", s.equals(tim.get(SensorManager.class, NullSensor.class, "IS1")));
        Assert.assertTrue("Objects are the same", sh.equals(tim.get(SignalHeadManager.class, NullSignalHead.class, "IH1")));
        Assert.assertTrue("Objects are the same", sm.equals(tim.get(SignalMastManager.class, NullSignalMast.class, "IF1")));
        Assert.assertTrue("Objects are the same", t.equals(tim.get(TurnoutManager.class, NullTurnout.class, "IT1")));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initRouteManager();
        JUnitUtil.initMemoryManager();
        JUnitUtil.initReporterManager();
        JUnitUtil.initOBlockManager();
        JUnitUtil.initWarrantManager();
        JUnitUtil.initSignalMastLogicManager();
        JUnitUtil.initLayoutBlockManager();
        JUnitUtil.initSectionManager();
        JUnitUtil.initInternalSignalHeadManager();
        JUnitUtil.initDefaultSignalMastManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
