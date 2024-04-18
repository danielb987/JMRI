package jmri.jmrit.logixng.actions;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JTextArea;

import jmri.*;
import jmri.implementation.VirtualSignalHead;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.Base.PrintTreeSettings;
import jmri.jmrit.logixng.implementation.DefaultConditionalNGScaffold;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.script.swing.ScriptOutput;
import jmri.util.JUnitAppender;
import jmri.util.JUnitUtil;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionListenOnBeansTable
 *
 * @author Daniel Bergqvist 2019
 */
public class ActionListenOnBeansTableTest extends AbstractDigitalActionTestBase {

    private Sensor s1, s2, s3, sensorWait, s99;
    private NamedTable csvTable;
    private LogixNG logixNG;
    private ConditionalNG conditionalNG;
    private WaitForScaffold actionWaitFor;
    private ActionListenOnBeansTable actionListenOnBeansTable;


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
        return String.format("Listen to each Signal Head in each column of row \"Signal before\" in table \"IQT1\" ::: Use default%n");
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
                "               Listen to each Signal Head in each column of row \"Signal before\" in table \"IQT1\" ::: Use default%n" +
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
                "               Listen to each Signal Head in each column of row \"Signal before\" in table \"IQT1\" ::: Use default%n" +
//                "               Listen to each Signal Head in each column of row \"Signal before\" in table \"IQT1\"%n" +
//                "                  ::: Use default%n" +
                "            ! A3%n" +
                "               Log data: Comma separated list, Format: \"\", Log to script output ::: Use default%n" +
                "            ! A4%n" +
                "               Set sensor IS99 to state Active ::: Use default%n" +
                "            ! A5%n" +
                "               Socket not connected%n"
        );
    }

    @Override
    public NamedBean createNewBean(String systemName) {
        return new ActionListenOnBeansTable(systemName, null);
    }

    @Override
    public boolean addNewSocket() {
        return false;
    }

    @Test
    public void testCtor() {
        ActionListenOnBeansTable t = new ActionListenOnBeansTable("IQDA1", null);
        Assert.assertNotNull("not null", t);
    }

    @Test
    public void testGetChild() {
        Assert.assertTrue("getChildCount() returns 0", 0 == actionListenOnBeansTable.getChildCount());

        boolean hasThrown = false;
        try {
            actionListenOnBeansTable.getChild(0);
        } catch (UnsupportedOperationException ex) {
            hasThrown = true;
            Assert.assertEquals("Error message is correct", "Not supported.", ex.getMessage());
        }
        Assert.assertTrue("Exception is thrown", hasThrown);
    }

    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.OTHER == _base.getCategory());
    }

    @Test
    public void testShortDescription() {
        Assert.assertEquals("String matches", "Listen on beans - Table", _base.getShortDescription());
    }

    @Test
    public void testLongDescription() {
        ActionListenOnBeansTable a1 = new ActionListenOnBeansTable("IQDA321", null);
        Assert.assertEquals("strings are equal", "Listen on beans - Table", a1.getShortDescription());
        ActionListenOnBeansTable a2 = new ActionListenOnBeansTable("IQDA321", null);
        Assert.assertEquals("strings are equal", "Listen to each Light in each column of row \"\" in table \"''\"", a2.getLongDescription());
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
    public void setUp() throws SocketAlreadyConnectedException, ParserException, IOException {
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

        InstanceManager.getDefault(SignalHeadManager.class)
                .register(new VirtualSignalHead("IH1"));

        // Load table turnout_and_signals.csv
        csvTable = InstanceManager.getDefault(NamedTableManager.class)
                        .loadTableFromCSV("IQT1", null, "program:java/test/jmri/jmrit/logixng/panel_and_data_files/turnout_and_signals.csv");
        Assert.assertNotNull(csvTable);

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

        actionListenOnBeansTable = new ActionListenOnBeansTable(digitalActionManager.getAutoSystemName(), null);
        actionListenOnBeansTable.getSelectNamedBean().setNamedBean(csvTable);
        actionListenOnBeansTable.setRowOrColumnName("Signal before");
        actionListenOnBeansTable.setTableRowOrColumn(TableRowOrColumn.Row);
        actionListenOnBeansTable.setNamedBeanType(NamedBeanType.SignalHead);
        actionListenOnBeansTable.setListenOnAllProperties(true);
        actionListenOnBeansTable.setIncludeCellsWithoutHeader(false);
//        actionListenOnBeansTable.addReference("Sensor:IS1");
//        actionListenOnBeansTable.addReference("Sensor:IS2");
//        actionListenOnBeansTable.addReference("Sensor:IS3");
        actionListenOnBeansTable.setLocalVariableNamedBean("bean");
        actionListenOnBeansTable.setLocalVariableEvent("event");
        actionListenOnBeansTable.setLocalVariableNewValue("value");
        socket = digitalActionManager.registerAction(actionListenOnBeansTable);
        many.getChild(1).connect(socket);

        _base = actionListenOnBeansTable;
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
        JUnitAppender.assertWarnMessage("The named bean \"\" cannot be found in the manager for Signal Head");
        JUnitAppender.assertWarnMessage("The named bean \"\" cannot be found in the manager for Signal Head");
        JUnitAppender.assertWarnMessage("The named bean \"\" cannot be found in the manager for Signal Head");
        JUnitAppender.assertWarnMessage("The named bean \"\" cannot be found in the manager for Signal Head");

        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
        s1 = s2 = s3 = s99 = null;
        logixNG = null;
        conditionalNG = null;
        actionWaitFor = null;
        actionListenOnBeansTable = null;
        csvTable = null;
    }

}
