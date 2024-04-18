package jmri.jmrit.logixng;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JPanel;

import jmri.*;
import jmri.beans.PropertyChangeProvider;
import jmri.jmrit.display.layoutEditor.*;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.jmrit.logixng.swing.SwingTools;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.jmrix.loconet.*;
import jmri.jmrix.mqtt.MqttSystemConnectionMemo;
import jmri.util.JUnitUtil;
import jmri.util.JmriJFrame;

import org.junit.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 *
 * @author daniel
 */
public class LongDescriptionTest {

    private final Set<String> _interfaceMethods = new HashSet<>();
    private LocoNetSystemConnectionMemo _locoNetMemo;
    private MqttSystemConnectionMemo _mqttMemo;
    private JmriJFrame _frame1, _frame2;
    private LayoutTurnout _layoutTurnout1, _layoutTurnout2;


    private String getMethodString(Method m) {
        var sb = new StringBuilder(m.getName());
        sb.append(": ");
        for (Class<?> param : m.getParameterTypes()) {
            sb.append(param.toString()).append(", ");
        }
        return sb.toString();
    }

    private void fetchInterfaceMethods(Class<?> iface) {
        for (Method m : iface.getDeclaredMethods()) {
            _interfaceMethods.add(getMethodString(m));
        }
    }

    private void fetchInterfaces() {
        fetchInterfaceMethods(jmri.NamedBean.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.AnalogAction.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.DigitalAction.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.DigitalBooleanAction.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.StringAction.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.AnalogExpression.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.DigitalExpression.class);
        fetchInterfaceMethods(jmri.jmrit.logixng.StringExpression.class);
    }

    private void callMethods(Base object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

//        if (!jmri.jmrit.logixng.actions.ProgramOnMain.class.equals(object.getClass())) return;
//        if (!jmri.jmrit.logixng.expressions.LogData.class.equals(object.getClass())) return;

        if (jmri.jmrit.logixng.actions.ActionCreateBeansFromTable.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionFindTableRowOrColumn.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionListenOnBeans.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionListenOnBeansLocalVariable.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionListenOnBeansTable.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionMemory.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionDispatcher.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionTable.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionSetReporter.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionOBlock.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionWarrant.class.equals(object.getClass())) return;
//        if (jmri.jmrit.logixng.actions.ProgramOnMain.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionLocalVariable.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ActionTimer.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ForEach.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.Sequence.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.ShowDialog.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.actions.WebRequest.class.equals(object.getClass())) return;

//        if (jmri.jmrit.logixng.expressions.LogData.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionAudio.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionClock.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionBlock.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionOBlock.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionEntryExit.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionMemory.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionLight.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionReference.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionSensor.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionSensorEdge.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionTurnout.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionConditional.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionDispatcher.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionLocalVariable.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionReporter.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionSection.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionScript.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionTransit.class.equals(object.getClass())) return;
        if (jmri.jmrit.logixng.expressions.ExpressionWarrant.class.equals(object.getClass())) return;

        if (jmri.jmrit.display.logixng.ActionPositionable.class.equals(object.getClass())) return;
        if (jmri.jmrit.display.logixng.ActionPositionableByClass.class.equals(object.getClass())) return;
        if (jmri.jmrit.display.logixng.WindowManagement.class.equals(object.getClass())) return;
        if (jmri.jmrit.display.logixng.ActionAudioIcon.class.equals(object.getClass())) return;
        if (jmri.jmrit.display.logixng.ActionLayoutTurnout.class.equals(object.getClass())) return;


//        log.error(object.getClass().getName());

        var settings = new Base.PrintTreeSettings();
        settings._completeOutput = true;

        var settingsNoCompleteOutput = new Base.PrintTreeSettings();
        settingsNoCompleteOutput._completeOutput = false;

////        System.out.println(object.getLongDescription(Locale.getDefault()));

        Set<String> longDescriptions = new HashSet<>();
        Set<String> addressingMethods = new HashSet<>();
        Set<String> addressingMethodNames = new HashSet<>();

//        List<String> calledMethods = new ArrayList<>();

        List<Method> methods = new ArrayList<>();

        for (Method m : object.getClass().getDeclaredMethods()) {
            int modifiers = m.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
                // Ignore non public and static methods
                continue;
            }

            String name = m.getName();
            if (name.startsWith("set")
                    && name.endsWith("Addressing")
                    && m.getParameterCount() == 1
                    && NamedBeanAddressing.class.equals(m.getParameterTypes()[0])) {

                name = name.substring(0, name.length()-"Addressing".length());
                addressingMethods.add(name);
//                log.error("Addressing: {}, {}", name, m.getName());
            }
        }
//        if (1==1) return;

        for (Method m : object.getClass().getDeclaredMethods()) {
            int modifiers = m.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
                // Ignore non public and static methods
                continue;
            }
            if (_interfaceMethods.contains(getMethodString(m))) {
//                System.out.format("Method %s is in interface%n", m.toString());
                continue;
            }
            if (!m.getName().startsWith("set")) {
                continue;
            }
            if ("setup".equals(m.getName())
                    || m.getName().endsWith("SystemName")
                    || "setChildCount".equals(m.getName())) {
//                    || m.getName().endsWith("SocketSystemName")) {
                continue;
            }
            if (m.getName().endsWith("Addressing")) {
                String name = m.getName().substring(0, m.getName().length()-"Addressing".length());
                if (addressingMethods.contains(name)) {
                    addressingMethodNames.add(m.getName());
//                    log.error("Ignore method {} for {}", m.getName(), object.getShortDescription());
                    continue;
                }
            }
            String name = m.getName();
            if (name.endsWith("Value")) {
                name = name.substring(0, name.length()-"Value".length());
            }
            if (name.endsWith("Reference")) {
                name = name.substring(0, name.length()-"Reference".length());
            }
            if (name.endsWith("LocalVariable")) {
                name = name.substring(0, name.length()-"LocalVariable".length());
            }
            if (name.endsWith("Formula")) {
                name = name.substring(0, name.length()-"Formula".length());
            }
            if (addressingMethods.contains(name)) {
                addressingMethodNames.add(m.getName());
//                log.error("Ignore method {} for {}", m.getName(), object.getShortDescription());
                continue;
            }
            methods.add(m);
        }

        boolean matchFound = false;
        List<String> descriptions = new ArrayList<>();
        List<String> methodCalls = new ArrayList<>();

        if (!methods.isEmpty()) {
            for (long count = 0; count < (1 << methods.size()); count++) {

                for (int methodIndex=0; methodIndex < methods.size(); methodIndex++) {
                    Method m = methods.get(methodIndex);

                    List<Object> parameters = new ArrayList<>();
                    for (Parameter p : m.getParameters()) {
                        var onOrOff = (count & (1 << methodIndex)) > 0;

//                        System.out.format("%2d, %3s, %s%n", count, Long.toBinaryString(count), Integer.toBinaryString(onOrOff ? 1 : 0));

                        Class<?> type = p.getType();
                        Object param;
                        if (boolean.class.equals(type) || Boolean.class.equals(type)) {
                            param = onOrOff;
                        } else if (int.class.equals(type) || Integer.class.equals(type)) {
                            param = onOrOff ? 1 : 0;
                        } else if (double.class.equals(type) || Double.class.equals(type)) {
                            param = onOrOff ? 1.0 : 0.0;
                        } else if (String.class.equals(type)) {
                            if ("setFormula".equals(m.getName())) {
                                param = onOrOff ? "a + b" : "c - d";
                            } else if ("setLocalVariable".equals(m.getName())) {
                                param = onOrOff ? "myVar" : "myOtherVar";
                            } else {
                                param = onOrOff ? "{SomeReference}" : "{SomeOtherReference}";
                            }
//                        } else if (Is_IsNot_Enum.class.equals(type)) {
//                            param = Is_IsNot_Enum.IsNot;
//                        } else if (NamedBeanAddressing.class.equals(type)) {
//                            param = NamedBeanAddressing.LocalVariable;
//                        } else if (TableRowOrColumn.class.equals(type)) {
//                            param = TableRowOrColumn.Row;
                        } else if (PropertyChangeProvider.class.equals(type)) {
                            param = new PropertyChangeProvider() {
                                @Override public void addPropertyChangeListener(PropertyChangeListener listener) {}
                                @Override public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {}
                                @Override public PropertyChangeListener[] getPropertyChangeListeners() { return new PropertyChangeListener[]{};}
                                @Override public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) { return new PropertyChangeListener[]{}; }
                                @Override public void removePropertyChangeListener(PropertyChangeListener listener) {}
                                @Override public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {}
                            };
                        } else if (SystemConnectionMemo.class.equals(type)) {
                            param = onOrOff ? _locoNetMemo : _mqttMemo;
                        } else if (JmriJFrame.class.equals(type)) {
                            param = onOrOff ? _frame1 : _frame2;
                        } else if (LayoutTurnout.class.equals(type)) {
                            param = onOrOff ? _layoutTurnout1 : _layoutTurnout2;
                        } else if (type.getEnumConstants() != null) {
                            var enums = type.getEnumConstants();
                            param = enums[onOrOff ? 0 : 1];
                        } else {
                            for (Class<?> iface : type.getInterfaces()) {
                                System.out.format("Interface: %s%n", iface.getName());
                            }
                            log.error("Class {} is unknown: {}", type.getName());
                            param = null;
                        }
                        parameters.add(param);
                    }

                    try {
                        m.invoke(object, parameters.toArray());

                        var sb = new StringBuilder();
                        for (Object o : parameters) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(o.toString());
                        }

                        String longDescr = object.getLongDescription(Locale.getDefault(), settings);
                        methodCalls.add(m.getName()+"("+sb.toString()+")" + "\n" + longDescr);
                        System.out.format("Method call: %s%n", m.getName()+"("+sb.toString()+")");

                        String longDescrNoCompleteOutput = object.getLongDescription(Locale.getDefault(), settingsNoCompleteOutput);
                        Assert.assertEquals("Long description is correct for class " + object.getClass().getName(),
                                longDescrNoCompleteOutput,
                                object.getLongDescription(Locale.getDefault()));


    //                    methodCalls.add(m.toString());

                    } catch (InvocationTargetException e) {
                        System.out.format("Cannot invoke %s: %s%n", m.toString(), e.getCause());
    //                        log.error("Cannot invoke {}: {}", m.toString(), e.getCause());
                    }
                }

                String longDescr = object.getLongDescription(Locale.getDefault(), settings);
                descriptions.add(longDescr);

                System.out.format("Long descr: %s%n%n", longDescr);

                if (longDescriptions.contains(longDescr)) {
                    matchFound = true;
                    log.error("Description already exists: {}", longDescr);
    //                log.error("Num methods: {}", methods.size());
    //                log.error("  Called method: {}", m);
    //                        log.error("Error:  Called method: {}", m);
                }
    //                    Assert.assertFalse(String.format("Description doesn't exists: %s", longDescr), longDescriptions.contains(longDescr));
                longDescriptions.add(longDescr);
            }

/*
            var sb = new StringBuilder();
            for (Object o : parameters) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(o.toString());
            }
            methodCalls.add(m.getName()+"("+sb.toString()+")" + "\n" + longDescr);
//                    methodCalls.add(m.toString());
*/


        }

//            for (String s : methodCalls) {
//                log.error("Method call: {}", s);
//            }

        if (matchFound) {
//            log.error(object.getClass().getName());
//            if (1==0)
            for (String s : descriptions) {
//                log.error("Descriptions: {}", s);
            }
            for (String s : methodCalls) {
                System.out.format("Method call: %s%n", s);
//                log.error("Method call: {}", s);
            }
        }
    }

    @Test
    @DisabledIfSystemProperty(named = "java.awt.headless", matches = "true")
    public void testLongDescription() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        fetchInterfaces();

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

                while (object instanceof MaleSocket) {
                    object = ((MaleSocket)object).getObject();
                }

//                System.out.println(object.getLongDescription(Locale.getDefault()));
                callMethods(object);
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

        LocoNetInterfaceScaffold lnis = new LocoNetInterfaceScaffold();
        SlotManager sm = new SlotManager(lnis);
        _locoNetMemo = new LocoNetSystemConnectionMemo(lnis, sm);
        _locoNetMemo.setThrottleManager(new LnThrottleManager(_locoNetMemo));
        sm.setSystemConnectionMemo(_locoNetMemo);
        InstanceManager.setDefault(LocoNetSystemConnectionMemo.class, _locoNetMemo);
        InstanceManager.store(_locoNetMemo, SystemConnectionMemo.class);

        _mqttMemo = new MqttSystemConnectionMemo();
        InstanceManager.setDefault(MqttSystemConnectionMemo.class, _mqttMemo);
        InstanceManager.store(_mqttMemo, SystemConnectionMemo.class);

        _frame1 = new JmriJFrame("My frame");
        _frame2 = new JmriJFrame("My other frame");

        LayoutEditor layoutEditor = new LayoutEditor();
        _layoutTurnout1 = new LayoutRHTurnout("MyId", layoutEditor);
        _layoutTurnout2 = new LayoutRHTurnout("MyOtherId", layoutEditor);
    }

    @After
    public void tearDown() {
        _frame1.dispose();
        _frame2.dispose();
        _frame1 = null;
        _frame2 = null;
        _layoutTurnout1 = null;
        _layoutTurnout2 = null;
        _locoNetMemo = null;
        _mqttMemo = null;

        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LongDescriptionTest.class);
}
