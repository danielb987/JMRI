package jmri.jmrit.logixng;

import java.awt.GraphicsEnvironment;
import java.beans.PropertyVetoException;

import jmri.*;
import jmri.implementation.VirtualSignalHead;
import jmri.jmrit.entryexit.DestinationPoints;
import jmri.jmrit.entryexit.EntryExitPairs;
import jmri.jmrit.logixng.SymbolTable.InitialValueType;
import jmri.jmrit.logixng.actions.*;
import jmri.jmrit.logixng.actions.ActionListenOnBeans.NamedBeanReference;
import jmri.util.JUnitUtil;

import org.junit.*;

/**
 * Creates a LogixNG with all actions and expressions to test store and load.
 * <P>
 * It uses the Base.printTree(PrintWriter writer, String indent) method to
 * compare the LogixNGs before and after store and load.
 */
public class CreateLogixNGTreeScaffold {

    private static boolean setupHasBeenCalled = false;

    private static void setUpCalled(boolean newVal){
        setupHasBeenCalled = newVal;
    }

    private Block block1;
    private Block block2;
    private Reporter reporter1;
    private Light light1;
    private Light light2;
    private VariableLight variableLight1;
    private Section section1;
    private Section section2;
    private Sensor sensor1;
    private Sensor sensor2;
    private Transit transit1;
    private Turnout turnout1;
    private Turnout turnout2;
    private Turnout turnout3;
    private Turnout turnout4;
    private Turnout turnout5;
    private Memory memory2;
    private DestinationPoints dp1;
    private DestinationPoints dp2;

    private LogixNG_Manager logixNG_Manager;
    private ConditionalNG_Manager conditionalNGManager;
    private DigitalActionManager digitalActionManager;
    private GlobalVariableManager globalVariables_Manager;

//    private AudioManager audioManager;

    public void createLogixNGTree() throws PropertyVetoException, Exception {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Ensure the setUp() and tearDown() methods of this class are called.
        Assert.assertTrue(setupHasBeenCalled);

        block1 = InstanceManager.getDefault(BlockManager.class).provide("IB1");
        block1.setValue("Block 1 Value");
        block2 = InstanceManager.getDefault(BlockManager.class).provide("IB2");
        block2.setUserName("Some block");
        block1.setValue("Block 2 Value");
        reporter1 = InstanceManager.getDefault(ReporterManager.class).provide("IR1");
        reporter1.setReport("Reporter 1 Value");
        light1 = InstanceManager.getDefault(LightManager.class).provide("IL1");
        light1.setCommandedState(Light.OFF);
        light2 = InstanceManager.getDefault(LightManager.class).provide("IL2");
        light2.setUserName("Some light");
        light2.setCommandedState(Light.OFF);
        sensor1 = InstanceManager.getDefault(SensorManager.class).provide("IS1");
        sensor1.setCommandedState(Sensor.INACTIVE);
        sensor2 = InstanceManager.getDefault(SensorManager.class).provide("IS2");
        sensor2.setCommandedState(Sensor.INACTIVE);
        sensor2.setUserName("Some sensor");

        section1 = InstanceManager.getDefault(SectionManager.class).createNewSection("Section_1");
        section2 = InstanceManager.getDefault(SectionManager.class).createNewSection("Section_2");
        transit1 = InstanceManager.getDefault(TransitManager.class).createNewTransit("Transit_1");
        transit1.addTransitSection(new TransitSection(section1, 1, Section.FORWARD));
        transit1.addTransitSection(new TransitSection(section2, 2, Section.FORWARD));

        dp1 = InstanceManager.getDefault(EntryExitPairs.class).getBySystemName("DP1");
        if (!( dp1 instanceof TransitScaffold.MyDestinationPoints )) {
            Assert.fail("Destination point not MyDestinationPoints");
        }

        dp2 = InstanceManager.getDefault(EntryExitPairs.class).getBySystemName("DP2");
        if (!( dp2 instanceof TransitScaffold.MyDestinationPoints )) {
            Assert.fail("Destination point not MyDestinationPoints");
        }

        InstanceManager.getDefault(SignalHeadManager.class)
                .register(new VirtualSignalHead("IH1"));
        InstanceManager.getDefault(SignalHeadManager.class)
                .register(new VirtualSignalHead("IH2"));

        // The signal head IH1 created above is also used here in signal mast IF$shsm:AAR-1946:CPL(IH1)
        InstanceManager.getDefault(SignalMastManager.class)
                .provideSignalMast("IF$shsm:AAR-1946:CPL(IH1)");

        logixNG_Manager = InstanceManager.getDefault(LogixNG_Manager.class);
        conditionalNGManager = InstanceManager.getDefault(ConditionalNG_Manager.class);
        digitalActionManager = InstanceManager.getDefault(DigitalActionManager.class);
        globalVariables_Manager = InstanceManager.getDefault(GlobalVariableManager.class);


        // Test that global variables of any type can be stored and loaded
        // even if the initial value is null.
        for (InitialValueType type : InitialValueType.values()) {
            GlobalVariable globalVariable = globalVariables_Manager
                    .createGlobalVariable("TestVariable_"+type.name());
            globalVariable.setInitialValueType(type);
            globalVariable.setInitialValueData(null);

            globalVariable = globalVariables_Manager
                    .createGlobalVariable("TestVariable_"+type.name()+"_2");
            globalVariable.setInitialValueType(type);
            globalVariable.setInitialValueData("");

            globalVariable = globalVariables_Manager
                    .createGlobalVariable("TestVariable_"+type.name()+"_3");
            globalVariable.setInitialValueType(type);
            switch (type) {
                case None:
                    globalVariable.setInitialValueData("");
                    break;
                case Boolean:
                    globalVariable.setInitialValueData("true");
                    break;
                case Integer:
                    globalVariable.setInitialValueData("12");
                    break;
                case FloatingNumber:
                    globalVariable.setInitialValueData("32.12");
                    break;
                case String:
                    globalVariable.setInitialValueData("Hello");
                    break;
                case Array:
                case Map:
                case LocalVariable:
                case Memory:
                case Reference:
                case Formula:
                case ScriptExpression:
                case ScriptFile:
                case LogixNG_Table:
                    globalVariable.setInitialValueData("");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type: " + type.name());
            }
        }


        LogixNG logixNG = logixNG_Manager.createLogixNG("A logixNG");
        ConditionalNG conditionalNG =
                conditionalNGManager.createConditionalNG(logixNG, "Yet another conditionalNG");
        logixNG.setEnabled(false);
        conditionalNG.setEnabled(true);

        FemaleSocket femaleRootSocket = conditionalNG.getFemaleSocket();
        MaleDigitalActionSocket actionManySocket =
                digitalActionManager.registerAction(new DigitalMany(
                                        digitalActionManager.getAutoSystemName(), null));
        femaleRootSocket.connect(actionManySocket);



        int indexAction = 0;
        MaleSocket maleSocket;


        ActionListenOnBeans actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        maleSocket.setEnabled(false);
        actionManySocket.getChild(indexAction++).connect(maleSocket);

        for (NamedBeanType namedBeanType : NamedBeanType.values()) {
            if (namedBeanType == NamedBeanType.EntryExit) {
                actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
                actionListenOnBeans.setComment("A comment");
                actionListenOnBeans.addReference(new NamedBeanReference("MyBean"+namedBeanType.name(), namedBeanType, false));
                maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
                actionManySocket.getChild(indexAction++).connect(maleSocket);
            }
        }
    }



    /**
     * Delete all the LogixNGs, ConditionalNGs, and so on.
     */
    public void cleanup() {
        if (transit1 != null) {
            InstanceManager.getDefault(TransitManager.class).deleteTransit(transit1);
            InstanceManager.getDefault(SectionManager.class).deleteSection(section1);
            InstanceManager.getDefault(SectionManager.class).deleteSection(section2);
            transit1 = null;
            section1 = null;
            section2 = null;
        }

        LogixNG_Manager logixNG_Manager = InstanceManager.getDefault(LogixNG_Manager.class);
        ConditionalNG_Manager conditionalNGManager = InstanceManager.getDefault(ConditionalNG_Manager.class);
        AnalogActionManager analogActionManager = InstanceManager.getDefault(AnalogActionManager.class);
        AnalogExpressionManager analogExpressionManager = InstanceManager.getDefault(AnalogExpressionManager.class);
        DigitalActionManager digitalActionManager = InstanceManager.getDefault(DigitalActionManager.class);
        DigitalBooleanActionManager digitalBooleanActionManager = InstanceManager.getDefault(DigitalBooleanActionManager.class);
        DigitalExpressionManager digitalExpressionManager = InstanceManager.getDefault(DigitalExpressionManager.class);
        StringActionManager stringActionManager = InstanceManager.getDefault(StringActionManager.class);
        StringExpressionManager stringExpressionManager = InstanceManager.getDefault(StringExpressionManager.class);
        LogixNG_InitializationManager logixNG_InitializationManager = InstanceManager.getDefault(LogixNG_InitializationManager.class);

        java.util.Set<LogixNG> logixNG_Set = new java.util.HashSet<>(logixNG_Manager.getNamedBeanSet());
        for (LogixNG aLogixNG : logixNG_Set) {
            logixNG_Manager.deleteLogixNG(aLogixNG);
        }

        java.util.Set<ConditionalNG> conditionalNGSet = new java.util.HashSet<>(conditionalNGManager.getNamedBeanSet());
        for (ConditionalNG aConditionalNG : conditionalNGSet) {
            conditionalNGManager.deleteConditionalNG(aConditionalNG);
        }

        java.util.Set<MaleAnalogActionSocket> analogActionSet = new java.util.HashSet<>(analogActionManager.getNamedBeanSet());
        for (MaleAnalogActionSocket aAnalogAction : analogActionSet) {
            analogActionManager.deleteAnalogAction(aAnalogAction);
        }

        java.util.Set<MaleAnalogExpressionSocket> analogExpressionSet = new java.util.HashSet<>(analogExpressionManager.getNamedBeanSet());
        for (MaleAnalogExpressionSocket aAnalogExpression : analogExpressionSet) {
            analogExpressionManager.deleteAnalogExpression(aAnalogExpression);
        }

        java.util.Set<MaleDigitalActionSocket> digitalActionSet = new java.util.HashSet<>(digitalActionManager.getNamedBeanSet());
        for (MaleDigitalActionSocket aDigitalActionSocket : digitalActionSet) {
            digitalActionManager.deleteDigitalAction(aDigitalActionSocket);
        }

        java.util.Set<MaleDigitalBooleanActionSocket> digitalBooleanActionSet = new java.util.HashSet<>(digitalBooleanActionManager.getNamedBeanSet());
        for (MaleDigitalBooleanActionSocket aDigitalBooleanAction : digitalBooleanActionSet) {
            digitalBooleanActionManager.deleteDigitalBooleanAction(aDigitalBooleanAction);
        }

        java.util.Set<MaleDigitalExpressionSocket> digitalExpressionSet = new java.util.HashSet<>(digitalExpressionManager.getNamedBeanSet());
        for (MaleDigitalExpressionSocket aDigitalExpression : digitalExpressionSet) {
            digitalExpressionManager.deleteDigitalExpression(aDigitalExpression);
        }

        java.util.Set<MaleStringActionSocket> stringActionSet = new java.util.HashSet<>(stringActionManager.getNamedBeanSet());
        for (MaleStringActionSocket aStringAction : stringActionSet) {
            stringActionManager.deleteStringAction(aStringAction);
        }

        java.util.Set<MaleStringExpressionSocket> stringExpressionSet = new java.util.HashSet<>(stringExpressionManager.getNamedBeanSet());
        for (MaleStringExpressionSocket aStringExpression : stringExpressionSet) {
            stringExpressionManager.deleteStringExpression(aStringExpression);
        }

        java.util.Set<Module> moduleSet = new java.util.HashSet<>(InstanceManager.getDefault(ModuleManager.class).getNamedBeanSet());
        for (Module aModule : moduleSet) {
            InstanceManager.getDefault(ModuleManager.class).deleteModule(aModule);
        }

        java.util.Set<NamedTable> tableSet = new java.util.HashSet<>(InstanceManager.getDefault(NamedTableManager.class).getNamedBeanSet());
        for (NamedTable aTable : tableSet) {
            InstanceManager.getDefault(NamedTableManager.class).deleteNamedTable(aTable);
        }

        java.util.Set<GlobalVariable> globalVariableSet = new java.util.HashSet<>(InstanceManager.getDefault(GlobalVariableManager.class).getNamedBeanSet());
        for (GlobalVariable globalVariable : globalVariableSet) {
            InstanceManager.getDefault(GlobalVariableManager.class).deleteGlobalVariable(globalVariable);
        }

        while (! logixNG_InitializationManager.getList().isEmpty()) {
            logixNG_InitializationManager.delete(0);
        }

        Assert.assertEquals(0, logixNG_Manager.getNamedBeanSet().size());
        Assert.assertEquals(0, analogActionManager.getNamedBeanSet().size());
        Assert.assertEquals(0, analogExpressionManager.getNamedBeanSet().size());
        Assert.assertEquals(0, digitalActionManager.getNamedBeanSet().size());
        Assert.assertEquals(0, digitalExpressionManager.getNamedBeanSet().size());
        Assert.assertEquals(0, stringActionManager.getNamedBeanSet().size());
        Assert.assertEquals(0, stringExpressionManager.getNamedBeanSet().size());
        Assert.assertEquals(0, InstanceManager.getDefault(ModuleManager.class).getNamedBeanSet().size());
        Assert.assertEquals(0, InstanceManager.getDefault(NamedTableManager.class).getNamedBeanSet().size());
        Assert.assertEquals(0, InstanceManager.getDefault(GlobalVariableManager.class).getNamedBeanSet().size());
        Assert.assertEquals(0, logixNG_InitializationManager.getList().size());
    }


    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initDebugPowerManager();
        JUnitUtil.initDebugThrottleManager();
        JUnitUtil.initDebugProgrammerManager();
        JUnitUtil.initInternalSignalHeadManager();
        JUnitUtil.initDefaultSignalMastManager();

        TransitScaffold.initTransits();

//        JUnitUtil.initLogixNGManager();

        CreateLogixNGTreeScaffold.setUpCalled(true);
    }

    public void tearDown() {
        CreateLogixNGTreeScaffold.setUpCalled(false);     // Reset for the next test

        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();

        // Delete all the LogixNGs, ConditionalNGs, and so on.
        cleanup();

        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }


    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CreateLogixNGTreeScaffold.class);

}
