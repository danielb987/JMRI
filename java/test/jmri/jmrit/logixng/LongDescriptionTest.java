package jmri.jmrit.logixng;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JPanel;

import jmri.*;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.jmrit.logixng.swing.SwingTools;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.util.JUnitUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 *
 * @author daniel
 */
public class LongDescriptionTest {

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    public void testLongDescription() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<Category, List<Class<? extends Base>>> classes = new HashMap<>();
        for (Category category : Category.values()) {
            classes.put(category, new ArrayList<>());
        }
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getAnalogActionClasses());
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getDigitalActionClasses());
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getDigitalBooleanActionClasses());
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getStringActionClasses());
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getAnalogExpressionClasses());
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getDigitalExpressionClasses());
        ActionsAndExpressionsTest.addClasses(classes, ActionsAndExpressionsTest.getStringExpressionClasses());

        JDialog dialog = new JDialog();

        for (var list : classes.values()) {
            for (var clazz : list) {
                SwingConfiguratorInterface configureSwing = SwingTools.getSwingConfiguratorForClass(clazz);
                configureSwing.setJDialog(dialog);
                configureSwing.getConfigPanel(new JPanel());
                configureSwing.setDefaultValues();
                Base object = configureSwing.createNewObject(configureSwing.getAutoSystemName(), null);
                System.out.println(object.getLongDescription(Locale.getDefault()));
            }
        }
    }

    // The minimal setup for log4J
    @Before
    public void setUp() throws SocketAlreadyConnectedException, ParserException, IOException {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initDebugThrottleManager();
        JUnitUtil.initLogixNGManager();

        InstanceManager.getDefault(MemoryManager.class).provide("IM1");
        InstanceManager.getDefault(ReporterManager.class).provide("IR1");
    }

    @After
    public void tearDown() {
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LongDescriptionTest.class);
}
