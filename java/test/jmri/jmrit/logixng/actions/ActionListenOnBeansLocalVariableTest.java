package jmri.jmrit.logixng.actions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JTextArea;

import jmri.*;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.Base.PrintTreeSettings;
import jmri.jmrit.logixng.implementation.DefaultConditionalNGScaffold;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.script.swing.ScriptOutput;
import jmri.util.JUnitUtil;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionListenOnBeansLocalVariable
 *
 * @author Daniel Bergqvist 2019
 */
public class ActionListenOnBeansLocalVariableTest extends AbstractDigitalActionTestBase {

    private Sensor s1, s2, s3, sensorWait, s99;
    private LogixNG logixNG;
    private ConditionalNG conditionalNG;
    private WaitForScaffold actionWaitFor;
    private ActionListenOnBeansLocalVariable actionListenOnBeansLocalVariable;


    @Override
    public ConditionalNG getConditionalNG() {
        return conditionalNG;
    }

    @Override
    public LogixNG getLogixNG() {
        return logixNG;
    }

    @Override
    public MaleSocket getConnectableChild() {
        return null;
    }

    @Override
    public String getExpectedPrintedTree() {
        return String.format(
                "Listen on the bean in the local variable \"null\" of type Sensor ::: Use default%n" +
                "   ! Execute%n" +
                "      Socket not connected%n");
    }

    @Override
    public String getExpectedPrintedTreeFromRoot() {
        return String.format(
                "LogixNG: A logixNG%n" +
                "   ConditionalNG: A conditionalNG%n" +
                "      ! A%n" +
                "         Many ::: Use default%n" +
                "            ::: Local variable \"bean\", init to None \"null\"%n" +
                "            ::: Local variable \"event\", init to None \"null\"%n" +
                "            ::: Local variable \"value\", init to None \"null\"%n" +
                "            ! A1%n" +
                "               Wait for ::: Use default%n" +
                "            ! A2%n" +
                "               Listen on the bean in the local variable \"null\" of type Sensor ::: Use default%n" +
                "                  ! Execute%n" +
                "                     Socket not connected%n" +
                "            ! A3%n" +
                "               Log data: Comma separated list ::: Use default%n" +
                "            ! A4%n" +
                "               Set sensor IS99 to state Active ::: Use default%n" +
                "            ! A5%n" +
                "               Socket not connected%n"
        );
    }

    public String getExpectedPrintedTreeFromRootWithPrintTreeSettings() {
        return String.format(
                "LogixNG: A logixNG%n" +
                "   ConditionalNG: A conditionalNG%n" +
                "      ! A%n" +
                "         Many ::: Use default%n" +
                "            ::: Local variable \"bean\", init to None \"null\"%n" +
                "            ::: Local variable \"event\", init to None \"null\"%n" +
                "            ::: Local variable \"value\", init to None \"null\"%n" +
                "            ! A1%n" +
                "               Wait for ::: Use default%n" +
                "            ! A2%n" +
                "               Listen on the bean in the local variable \"null\" of type Sensor ::: Use default%n" +
//                "               Listen on the bean in the local variable \"null\" of type Sensor%n" +
//                "                  ::: Use default%n" +
                "                  ! Execute%n" +
                "                     Socket not connected%n" +
                "            ! A3%n" +
                "               Log data: Comma separated list ::: Use default%n" +
                "            ! A4%n" +
                "               Set sensor IS99 to state Active ::: Use default%n" +
                "            ! A5%n" +
                "               Socket not connected%n"
        );
    }

    @Override
    public NamedBean createNewBean(String systemName) {
        return new ActionListenOnBeansLocalVariable(systemName, null);
    }

    @Override
    public boolean addNewSocket() {
        return false;
    }

    @Test
    public void testShortDescription() {
        Assert.assertEquals("String matches", "Listen on beans - Local variable", _base.getShortDescription());
    }

    @Test
    public void testLongDescription() {
        ActionListenOnBeansLocalVariable a1 = new ActionListenOnBeansLocalVariable("IQDA321", null);
        Assert.assertEquals("strings are equal", "Listen on beans - Local variable", a1.getShortDescription());
        ActionListenOnBeansLocalVariable a2 = new ActionListenOnBeansLocalVariable("IQDA321", null);
        Assert.assertEquals("strings are equal", "Listen on the bean in the local variable \"null\" of type Light", a2.getLongDescription());
    }

    @Test
    public void testLongDescriptionWithPrintTreeSettings() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        var settings = new PrintTreeSettings();
        settings._completeOutput = true;
        _base.getRoot().printTree(settings, Locale.ENGLISH, printWriter, TREE_INDENT, new MutableInt(0));
        Assert.assertEquals("Tree is equal", getExpectedPrintedTreeFromRootWithPrintTreeSettings(), stringWriter.toString());
    }

    private JTextArea getOutputArea() {
        return ScriptOutput.getDefault().getOutputArea();
    }

    // The minimal setup for log4J
    @Before
    public void setUp() throws SocketAlreadyConnectedException, ParserException {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initLogixNGManager();

        _category = Category.ITEM;
        _isExternal = true;

        s1 = InstanceManager.getDefault(SensorManager.class).provideSensor("IS1");
        s2 = InstanceManager.getDefault(SensorManager.class).provideSensor("IS2");
        s3 = InstanceManager.getDefault(SensorManager.class).provideSensor("IS3");
        sensorWait = InstanceManager.getDefault(SensorManager.class).provideSensor("ISWait");
        s99 = InstanceManager.getDefault(SensorManager.class).provideSensor("IS99");

        logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        conditionalNG = new DefaultConditionalNGScaffold("IQC1", "A conditionalNG");  // NOI18N;
        InstanceManager.getDefault(ConditionalNG_Manager.class).register(conditionalNG);
        logixNG.addConditionalNG(conditionalNG);
        conditionalNG.setRunDelayed(false);
        conditionalNG.setEnabled(true);

        DigitalActionManager digitalActionManager = InstanceManager.getDefault(DigitalActionManager.class);

        DigitalMany many = new DigitalMany(digitalActionManager.getAutoSystemName(), null);
        MaleSocket socket = digitalActionManager.registerAction(many);
        socket.addLocalVariable("bean", SymbolTable.InitialValueType.None, null);
        socket.addLocalVariable("event", SymbolTable.InitialValueType.None, null);
        socket.addLocalVariable("value", SymbolTable.InitialValueType.None, null);
        conditionalNG.getChild(0).connect(socket);

        actionWaitFor = new WaitForScaffold(digitalActionManager.getAutoSystemName(), null, ()->{return true;});
        socket = digitalActionManager.registerAction(actionWaitFor);
        many.getChild(0).connect(socket);

        actionListenOnBeansLocalVariable = new ActionListenOnBeansLocalVariable(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeansLocalVariable.setNamedBeanType(NamedBeanType.Sensor);
        actionListenOnBeansLocalVariable.setListenOnAllProperties(true);
//        actionListenOnBeansLocalVariable.addReference("Sensor:IS1");
//        actionListenOnBeansLocalVariable.addReference("Sensor:IS2");
//        actionListenOnBeansLocalVariable.addReference("Sensor:IS3");
        actionListenOnBeansLocalVariable.setLocalVariableNamedBean("bean");
        actionListenOnBeansLocalVariable.setLocalVariableEvent("event");
        actionListenOnBeansLocalVariable.setLocalVariableNewValue("value");
        socket = digitalActionManager.registerAction(actionListenOnBeansLocalVariable);
        many.getChild(1).connect(socket);

        _base = actionListenOnBeansLocalVariable;
        _baseMaleSocket = socket;

        LogData logData = new LogData(digitalActionManager.getAutoSystemName(), null);
        logData.setFormatType(LogData.FormatType.CommaSeparatedList);
        logData.getDataList().add(new LogData.Data(LogData.DataType.LocalVariable, "bean"));
        logData.getDataList().add(new LogData.Data(LogData.DataType.LocalVariable, "event"));
        logData.getDataList().add(new LogData.Data(LogData.DataType.LocalVariable, "value"));
        logData.setLogToLog(false);
        logData.setLogToScriptOutput(true);
        socket = digitalActionManager.registerAction(logData);
        many.getChild(2).connect(socket);

        ActionSensor actionSensor = new ActionSensor(digitalActionManager.getAutoSystemName(), null);
        actionSensor.getSelectNamedBean().setNamedBean("IS99");
        actionSensor.getSelectEnum().setEnum(ActionSensor.SensorState.Active);
        socket = digitalActionManager.registerAction(actionSensor);
        many.getChild(3).connect(socket);

        if (! logixNG.setParentForAllChildren(new ArrayList<>())) throw new RuntimeException();
        logixNG.activate();
        logixNG.setEnabled(true);
    }

    @After
    public void tearDown() {
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
        s1 = s2 = s3 = s99 = null;
        logixNG = null;
        conditionalNG = null;
        actionWaitFor = null;
        actionListenOnBeansLocalVariable = null;
    }

}
