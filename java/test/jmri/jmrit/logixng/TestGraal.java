package jmri.jmrit.logixng;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.script.*;

import jmri.script.JmriScriptEngineManager;
import jmri.util.JUnitUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Graal
 * 
 * @author Daniel Bergqvist (C) 2022
 */
public class TestGraal {

    private static final String script = "ab.set(True)";


    @Test
    public void testDefaultJavaScript() {
        AtomicBoolean ab = new AtomicBoolean();

        JmriScriptEngineManager scriptEngineManager = jmri.script.JmriScriptEngineManager.getDefault();
        Bindings bindings = new SimpleBindings();
        bindings.put("ab", ab);

        Assert.assertFalse(ab.get());
        try {
            String theScript = String.format("import jmri%n") + script;
            scriptEngineManager.getEngineByName(JmriScriptEngineManager.PYTHON)
                    .eval(theScript, bindings);
        } catch (ScriptException e) {
            log.warn("cannot execute script", e);
        }
        Assert.assertTrue(ab.get());
    }

    @Test
    public void testNashornJavaScript() {
        AtomicBoolean ab = new AtomicBoolean();

        JmriScriptEngineManager scriptEngineManager = jmri.script.JmriScriptEngineManager.getDefault();
        Bindings bindings = new SimpleBindings();
        bindings.put("ab", ab);

        Assert.assertFalse(ab.get());
        try {
            String theScript = String.format("import jmri%n") + script;
            ScriptEngineFactory sf = scriptEngineManager.getFactory("py");
            Assert.assertNotNull(sf);
            sf.getScriptEngine().eval(theScript, bindings);
        } catch (ScriptException e) {
            log.warn("cannot execute script", e);
        }
        Assert.assertTrue(ab.get());
    }

    @Test
    public void testGraalJavaScript() {
        AtomicBoolean ab = new AtomicBoolean();

        JmriScriptEngineManager scriptEngineManager = jmri.script.JmriScriptEngineManager.getDefault();
        Bindings bindings = new SimpleBindings();
        bindings.put("ab", ab);

        Assert.assertFalse(ab.get());
        try {
            String theScript = String.format("import jmri%n") + script;
            ScriptEngineFactory sf = scriptEngineManager.getFactory("py");
            Assert.assertNotNull(sf);
            sf.getScriptEngine().eval(theScript, bindings);
        } catch (ScriptException e) {
            log.warn("cannot execute script", e);
        }
        Assert.assertTrue(ab.get());
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initLogixNGManager();
    }

    @After
    public void tearDown() {
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestGraal.class);
}
