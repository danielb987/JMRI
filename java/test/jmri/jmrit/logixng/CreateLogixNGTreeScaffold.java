package jmri.jmrit.logixng;

import java.awt.GraphicsEnvironment;
import java.beans.PropertyVetoException;
import java.util.*;

import jmri.*;
import jmri.implementation.VirtualSignalHead;
import jmri.jmrit.entryexit.DestinationPoints;
import jmri.jmrit.entryexit.EntryExitPairs;
import jmri.jmrit.logix.BlockOrder;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.Warrant;
import jmri.jmrit.logixng.SymbolTable.InitialValueType;
import jmri.jmrit.logixng.actions.*;
import jmri.jmrit.logixng.actions.ActionListenOnBeans.NamedBeanReference;
import jmri.jmrit.logixng.util.*;
import jmri.jmrix.loconet.*;
import jmri.jmrix.mqtt.MqttSystemConnectionMemo;
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

    private LocoNetSystemConnectionMemo _locoNetMemo;
    private MqttSystemConnectionMemo _mqttMemo;

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
    private Memory memory1;
    private Memory memory2;
    private DestinationPoints dp1;
    private DestinationPoints dp2;
    private NamedTable csvTable;
    private StringIO stringIO;

    private LogixManager logixManager = InstanceManager.getDefault(LogixManager.class);
    private ConditionalManager conditionalManager = InstanceManager.getDefault(ConditionalManager.class);

    private jmri.Logix logixIX1 = logixManager.createNewLogix("IX1", null);
    private Conditional conditionalIX1C1 = conditionalManager.createNewConditional("IX1C1", "First conditional");

    private LogixNG_Manager logixNG_Manager;
    private ConditionalNG_Manager conditionalNGManager;
    private DigitalActionManager digitalActionManager;
    private LogixNG_InitializationManager logixNG_InitializationManager;
    private GlobalVariableManager globalVariables_Manager;

//    private AudioManager audioManager;

    private static NamedBeanReference getNamedBeanReference(
            Collection<NamedBeanReference> collection, String name) {
        for (NamedBeanReference ref : collection) {
            if (name.equals(ref.getName())) return ref;
        }
        return null;
    }

    public void createLogixNGTree() throws PropertyVetoException, Exception {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Ensure the setUp() and tearDown() methods of this class are called.
        Assert.assertTrue(setupHasBeenCalled);
/*
        audioManager = new jmri.jmrit.audio.DefaultAudioManager(
                InstanceManager.getDefault(jmri.jmrix.internal.InternalSystemConnectionMemo.class));
        audioManager.init();
        JUnitUtil.waitFor(()->{return audioManager.isInitialised();});

        audioManager.provideAudio("IAB1");
        AudioSource audioSource = (AudioSource) audioManager.provideAudio("IAS1");
        audioSource.setAssignedBuffer((AudioBuffer) audioManager.getNamedBean("IAB1"));
*/
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
        variableLight1 = (VariableLight)InstanceManager.getDefault(LightManager.class).provide("ILVariable");
        variableLight1.setCommandedState(Light.OFF);
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

        turnout1 = InstanceManager.getDefault(TurnoutManager.class).provide("IT1");
        turnout1.setCommandedState(Turnout.CLOSED);
        turnout2 = InstanceManager.getDefault(TurnoutManager.class).provide("IT2");
        turnout2.setCommandedState(Turnout.CLOSED);
        turnout2.setUserName("Some turnout");
        turnout3 = InstanceManager.getDefault(TurnoutManager.class).provide("IT3");
        turnout3.setCommandedState(Turnout.CLOSED);
        turnout4 = InstanceManager.getDefault(TurnoutManager.class).provide("IT4");
        turnout4.setCommandedState(Turnout.CLOSED);
        turnout5 = InstanceManager.getDefault(TurnoutManager.class).provide("IT5");
        turnout5.setCommandedState(Turnout.CLOSED);

        memory1 = InstanceManager.getDefault(MemoryManager.class).provide("IM1");
        memory2 = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        memory2.setUserName("Some memory");

        dp1 = InstanceManager.getDefault(EntryExitPairs.class).getBySystemName("DP1");
        if (!( dp1 instanceof TransitScaffold.MyDestinationPoints )) {
            Assert.fail("Destination point not MyDestinationPoints");
        }

        dp2 = InstanceManager.getDefault(EntryExitPairs.class).getBySystemName("DP2");
        if (!( dp2 instanceof TransitScaffold.MyDestinationPoints )) {
            Assert.fail("Destination point not MyDestinationPoints");
        }

        logixManager = InstanceManager.getDefault(LogixManager.class);
        conditionalManager = InstanceManager.getDefault(ConditionalManager.class);

        logixIX1 = logixManager.createNewLogix("IX1", null);
        logixIX1.setEnabled(true);

        conditionalIX1C1 = conditionalManager.createNewConditional("IX1C1", "First conditional");
        logixIX1.addConditional(conditionalIX1C1.getSystemName(), 0);

        InstanceManager.getDefault(SignalHeadManager.class)
                .register(new VirtualSignalHead("IH1"));
        InstanceManager.getDefault(SignalHeadManager.class)
                .register(new VirtualSignalHead("IH2"));

        // The signal head IH1 created above is also used here in signal mast IF$shsm:AAR-1946:CPL(IH1)
        InstanceManager.getDefault(SignalMastManager.class)
                .provideSignalMast("IF$shsm:AAR-1946:CPL(IH1)");

        InstanceManager.getDefault(jmri.jmrit.logix.OBlockManager.class)
                .register(new OBlock("OB98"));
        InstanceManager.getDefault(jmri.jmrit.logix.OBlockManager.class)
                .register(new OBlock("OB99"));

        InstanceManager.getDefault(jmri.jmrit.logix.WarrantManager.class)
                .register(new Warrant("IW99", "Test Warrant"));
        Warrant warrant = InstanceManager.getDefault(jmri.jmrit.logix.WarrantManager.class).getWarrant("IW99");
        warrant.addBlockOrder(new BlockOrder(InstanceManager.getDefault(jmri.jmrit.logix.OBlockManager.class).getOBlock("OB98")));
        warrant.addBlockOrder(new BlockOrder(InstanceManager.getDefault(jmri.jmrit.logix.OBlockManager.class).getOBlock("OB99")));

        stringIO = InstanceManager.getDefault(StringIOManager.class).provideStringIO("MyStringIO");
        Assert.assertNotNull(stringIO);
        Assert.assertEquals("ICMyStringIO", stringIO.getSystemName());

        logixNG_Manager = InstanceManager.getDefault(LogixNG_Manager.class);
        conditionalNGManager = InstanceManager.getDefault(ConditionalNG_Manager.class);
        digitalActionManager = InstanceManager.getDefault(DigitalActionManager.class);
        logixNG_InitializationManager = InstanceManager.getDefault(LogixNG_InitializationManager.class);
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


        // Load table turnout_and_signals.csv
        csvTable = InstanceManager.getDefault(NamedTableManager.class)
                        .loadTableFromCSV("IQT1", null, "program:java/test/jmri/jmrit/logixng/panel_and_data_files/turnout_and_signals.csv");
        Assert.assertNotNull(csvTable);

        // Create module IQM1
        Module module =
                InstanceManager.getDefault(ModuleManager.class).createModule("IQM1", null,
                        InstanceManager.getDefault(FemaleSocketManager.class)
                                .getSocketTypeByType("DefaultFemaleDigitalActionSocket"));

        module.addParameter("other", true, true);
        module.addParameter("n", true, false);
        module.addParameter("result", false, true);
        module.addLocalVariable("temp1", SymbolTable.InitialValueType.None, null);
        module.addLocalVariable("temp2", SymbolTable.InitialValueType.None, null);

        DigitalMany many901 = new DigitalMany("IQDA901", null);
        MaleSocket manySocket901 =
                InstanceManager.getDefault(DigitalActionManager.class).registerAction(many901);
        module.getRootSocket().connect(manySocket901);

        // Create global variables
        GlobalVariable globalVariable =
                InstanceManager.getDefault(GlobalVariableManager.class)
                        .createGlobalVariable("IQGV1", "index");
        globalVariable.setInitialValueType(InitialValueType.String);
        globalVariable.setInitialValueData("Something");

        globalVariable =
                InstanceManager.getDefault(GlobalVariableManager.class)
                        .createGlobalVariable("IQGV2", "MyVariable");
        globalVariable.setInitialValueType(InitialValueType.Formula);
        globalVariable.setInitialValueData("\"Variable\" + str(index)");

        globalVariable =
                InstanceManager.getDefault(GlobalVariableManager.class)
                        .createGlobalVariable("IQGV15", "AnotherGlobalVariable");
        globalVariable.setInitialValueType(InitialValueType.Array);
        globalVariable.setInitialValueData("");

        globalVariable =
                InstanceManager.getDefault(GlobalVariableManager.class)
                        .createGlobalVariable(InstanceManager.getDefault(GlobalVariableManager.class)
                                .getAutoSystemName(), "SomeOtherGlobalVariable");
        globalVariable.setInitialValueType(InitialValueType.Map);
        globalVariable.setInitialValueData(null);




        // Create an empty LogixNG
        logixNG_Manager.createLogixNG("An empty logixNG");

        // Create a LogixNG with an empty ConditionalNG
        LogixNG logixNG = logixNG_Manager.createLogixNG("A logixNG with an empty conditionlNG");
        ConditionalNG conditionalNG =
                conditionalNGManager.createConditionalNG(logixNG, "An empty conditionalNG");
        logixNG.setEnabled(false);
        conditionalNG.setEnabled(false);


        // Create an empty ConditionalNG on the debug thread
        conditionalNG =
                conditionalNGManager.createConditionalNG(
                        logixNG, "A second empty conditionalNG", LogixNG_Thread.DEFAULT_LOGIXNG_THREAD);
        conditionalNG.setEnabled(false);


        // Create an empty ConditionalNG on another thread
        LogixNG_Thread.createNewThread(53, "My logixng thread");
        conditionalNG =
                conditionalNGManager.createConditionalNG(logixNG, "A third empty conditionalNG", 53);
        conditionalNG.setEnabled(false);


        // Create an empty ConditionalNG on another thread
        LogixNG_Thread.createNewThread("My other logixng thread");
        conditionalNG = conditionalNGManager.createConditionalNG(
                logixNG, "A fourth empty conditionalNG", LogixNG_Thread.getThreadID("My other logixng thread"));
        conditionalNG.setEnabled(false);


        logixNG = logixNG_Manager.createLogixNG("A logixNG in the initialization table");
        conditionalNGManager.createConditionalNG(logixNG, "Yet another another conditionalNG");
        logixNG_InitializationManager.add(logixNG);


        logixNG = logixNG_Manager.createLogixNG("Another logixNG in the initialization table");
        conditionalNGManager.createConditionalNG(logixNG, "Yet another another another conditionalNG");
        logixNG_InitializationManager.add(logixNG);


        logixNG = logixNG_Manager.createLogixNG("A logixNG");
        conditionalNG =
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

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Light:"+light1.getSystemName());
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        NamedBeanReference ref = getNamedBeanReference(actionListenOnBeans.getReferences(), light1.getSystemName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(light1.getSystemName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Light, ref.getType());
        Assert.assertFalse(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Light:"+light2.getUserName());
        actionListenOnBeans.setLocalVariableNamedBean("localVariableNamedBean");
        actionListenOnBeans.setLocalVariableEvent("localVariableEvent");
        actionListenOnBeans.setLocalVariableNewValue("localVariableNewValue");
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), light2.getUserName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(light2.getUserName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Light, ref.getType());
        Assert.assertFalse(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Memory:"+memory1.getSystemName()+":no");
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), memory1.getSystemName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(memory1.getSystemName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Memory, ref.getType());
        Assert.assertFalse(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Memory:"+memory2.getUserName()+":yes");
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), memory2.getUserName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(memory2.getUserName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Memory, ref.getType());
        Assert.assertTrue(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Sensor:"+sensor1.getSystemName());
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), sensor1.getSystemName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(sensor1.getSystemName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Sensor, ref.getType());
        Assert.assertFalse(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Sensor:"+sensor2.getUserName());
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), sensor2.getUserName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(sensor2.getUserName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Sensor, ref.getType());
        Assert.assertFalse(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Turnout:"+turnout1.getSystemName());
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), turnout1.getSystemName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(turnout1.getSystemName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Turnout, ref.getType());
        Assert.assertFalse(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference("Turnout:"+turnout2.getUserName()+":yes");
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
        ref = getNamedBeanReference(actionListenOnBeans.getReferences(), turnout2.getUserName());
        Assert.assertNotNull(ref);
        Assert.assertEquals(turnout2.getUserName(), ref.getName());
        Assert.assertEquals(NamedBeanType.Turnout, ref.getType());
        Assert.assertTrue(ref.getListenOnAllProperties());

        actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeans.setComment("A comment");
        actionListenOnBeans.addReference(new NamedBeanReference("MyGlobalVariable", NamedBeanType.GlobalVariable, false));
        maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
        actionManySocket.getChild(indexAction++).connect(maleSocket);

        for (NamedBeanType namedBeanType : NamedBeanType.values()) {
            actionListenOnBeans = new ActionListenOnBeans(digitalActionManager.getAutoSystemName(), null);
            actionListenOnBeans.setComment("A comment");
            actionListenOnBeans.addReference(new NamedBeanReference("MyBean"+namedBeanType.name(), namedBeanType, false));
            maleSocket = digitalActionManager.registerAction(actionListenOnBeans);
            actionManySocket.getChild(indexAction++).connect(maleSocket);
        }






        ActionEntryExit entryExit = new ActionEntryExit(digitalActionManager.getAutoSystemName(), null);
        maleSocket = digitalActionManager.registerAction(entryExit);
        maleSocket.setEnabled(false);
        actionManySocket.getChild(indexAction++).connect(maleSocket);

        entryExit = new ActionEntryExit(digitalActionManager.getAutoSystemName(), null);
        entryExit.setComment("A comment");
        entryExit.getSelectEnum().setEnum(ActionEntryExit.Operation.SetNXPairDisabled);
        entryExit.getSelectNamedBean().setNamedBean(dp1);
        entryExit.getSelectNamedBean().setAddressing(NamedBeanAddressing.Direct);
        entryExit.getSelectNamedBean().setFormula("\"IT\"+index");
        entryExit.getSelectNamedBean().setLocalVariable("index");
        entryExit.getSelectNamedBean().setReference("{IM1}");
        entryExit.getSelectEnum().setAddressing(NamedBeanAddressing.LocalVariable);
        entryExit.getSelectEnum().setFormula("\"IT\"+index2");
        entryExit.getSelectEnum().setLocalVariable("index2");
        entryExit.getSelectEnum().setReference("{IM2}");
        maleSocket = digitalActionManager.registerAction(entryExit);
        actionManySocket.getChild(indexAction++).connect(maleSocket);

        entryExit = new ActionEntryExit(digitalActionManager.getAutoSystemName(), null);
        entryExit.setComment("A comment");
        entryExit.getSelectEnum().setEnum(ActionEntryExit.Operation.SetNXPairEnabled);
        entryExit.getSelectNamedBean().setNamedBean(dp2);
        entryExit.getSelectNamedBean().setAddressing(NamedBeanAddressing.LocalVariable);
        entryExit.getSelectNamedBean().setFormula("\"IT\"+index");
        entryExit.getSelectNamedBean().setLocalVariable("index");
        entryExit.getSelectNamedBean().setReference("{IM1}");
        entryExit.getSelectEnum().setAddressing(NamedBeanAddressing.Formula);
        entryExit.getSelectEnum().setFormula("\"IT\"+index2");
        entryExit.getSelectEnum().setLocalVariable("index2");
        entryExit.getSelectEnum().setReference("{IM2}");
        maleSocket = digitalActionManager.registerAction(entryExit);
        actionManySocket.getChild(indexAction++).connect(maleSocket);

        entryExit = new ActionEntryExit(digitalActionManager.getAutoSystemName(), null);
        entryExit.setComment("A comment");
        entryExit.getSelectEnum().setEnum(ActionEntryExit.Operation.SetNXPairSegment);
        entryExit.getSelectNamedBean().setAddressing(NamedBeanAddressing.Formula);
        entryExit.getSelectNamedBean().setFormula("\"IT\"+index");
        entryExit.getSelectNamedBean().setLocalVariable("index");
        entryExit.getSelectNamedBean().setReference("{IM1}");
        entryExit.getSelectEnum().setAddressing(NamedBeanAddressing.Reference);
        entryExit.getSelectEnum().setFormula("\"IT\"+index2");
        entryExit.getSelectEnum().setLocalVariable("index2");
        entryExit.getSelectEnum().setReference("{IM2}");
        maleSocket = digitalActionManager.registerAction(entryExit);
        actionManySocket.getChild(indexAction++).connect(maleSocket);

        entryExit = new ActionEntryExit(digitalActionManager.getAutoSystemName(), null);
        entryExit.setUserName("An entry/exit action");     // Used by executeAction below
        entryExit.setComment("A comment");
        entryExit.getSelectEnum().setEnum(ActionEntryExit.Operation.SetNXPairDisabled);
        entryExit.getSelectNamedBean().setAddressing(NamedBeanAddressing.Reference);
        entryExit.getSelectNamedBean().setFormula("\"IT\"+index");
        entryExit.getSelectNamedBean().setLocalVariable("index");
        entryExit.getSelectNamedBean().setReference("{IM1}");
        entryExit.getSelectEnum().setAddressing(NamedBeanAddressing.Direct);
        entryExit.getSelectEnum().setFormula("\"IT\"+index2");
        entryExit.getSelectEnum().setLocalVariable("index2");
        entryExit.getSelectEnum().setReference("{IM2}");
        maleSocket = digitalActionManager.registerAction(entryExit);
        actionManySocket.getChild(indexAction++).connect(maleSocket);
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
//        JUnitUtil.initSignalMastLogicManager();
        JUnitUtil.initOBlockManager();
        JUnitUtil.initSectionManager();
        JUnitUtil.initWarrantManager();

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

        TransitScaffold.initTransits();

//        JUnitUtil.initLogixNGManager();

        CreateLogixNGTreeScaffold.setUpCalled(true);
    }

    public void tearDown() {
        CreateLogixNGTreeScaffold.setUpCalled(false);     // Reset for the next test

        _locoNetMemo = null;
        _mqttMemo = null;

//        JUnitAppender.clearBacklog();    // REMOVE THIS!!!
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();

        // Delete all the LogixNGs, ConditionalNGs, and so on.
        cleanup();

        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }


    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CreateLogixNGTreeScaffold.class);

}
