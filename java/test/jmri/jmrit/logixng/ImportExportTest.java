package jmri.jmrit.logixng;

import jmri.jmrit.logixng.configurexml.*;

import java.awt.GraphicsEnvironment;
import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import jmri.*;
import jmri.jmrit.logixng.Base.PrintTreeSettings;
import jmri.jmrit.logixng.actions.ActionTurnout;
import jmri.jmrit.logixng.util.LogixNG_Thread;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.util.*;

import org.apache.commons.lang3.mutable.MutableInt;

import org.junit.*;

/**
 * Creates a LogixNG with all actions and expressions to test store and load.
 * <P>
 * It uses the Base.printTree(PrintWriter writer, String indent) method to
 * compare the LogixNGs before and after store and load.
 */
public class ImportExportTest {

    @Test
    public void testExportImportAction()
            throws ParserException, NamedBean.BadUserNameException, NamedBean.BadSystemNameException, IOException, SocketAlreadyConnectedException {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        Turnout turnout1 = InstanceManager.getDefault(TurnoutManager.class).provide("IT1");
        turnout1.setCommandedState(Turnout.CLOSED);

        LogixNG_Manager logixNG_Manager = InstanceManager.getDefault(LogixNG_Manager.class);
        ConditionalNG_Manager conditionalNGManager = InstanceManager.getDefault(ConditionalNG_Manager.class);
        DigitalActionManager digitalActionManager = InstanceManager.getDefault(DigitalActionManager.class);

        // Load table turnout_and_signals.csv
        NamedTable csvTable = InstanceManager.getDefault(NamedTableManager.class)
                        .loadTableFromCSV("IQT1", null, "program:java/test/jmri/jmrit/logixng/panel_and_data_files/turnout_and_signals.csv");
        Assert.assertNotNull(csvTable);

        // Create an empty LogixNG
        LogixNG logixNG = logixNG_Manager.createLogixNG("The LogixNG");
        ConditionalNG conditionalNG =
                conditionalNGManager.createConditionalNG(logixNG, "The ConditionalNG");

        FemaleSocket femaleRootSocket = conditionalNG.getFemaleSocket();

        ActionTurnout actionTurnout = new ActionTurnout(digitalActionManager.getAutoSystemName(), null);
        actionTurnout.setComment("A comment");
        actionTurnout.getSelectNamedBean().setNamedBean(turnout1);
        actionTurnout.getSelectEnum().setEnum(ActionTurnout.TurnoutState.Closed);
        actionTurnout.getSelectNamedBean().setAddressing(NamedBeanAddressing.Direct);
        actionTurnout.getSelectNamedBean().setFormula("\"IT\"+index");
        actionTurnout.getSelectNamedBean().setLocalVariable("index");
        actionTurnout.getSelectNamedBean().setReference("{IM1}");
        CreateLogixNGTreeScaffold.set_LogixNG_SelectTable_Data(
                csvTable,
                actionTurnout.getSelectNamedBean().getSelectTable(),
                NamedBeanAddressing.Direct);
        actionTurnout.getSelectEnum().setAddressing(NamedBeanAddressing.LocalVariable);
        actionTurnout.getSelectEnum().setFormula("\"IT\"+index2");
        actionTurnout.getSelectEnum().setLocalVariable("index2");
        actionTurnout.getSelectEnum().setReference("{IM2}");
        CreateLogixNGTreeScaffold.set_LogixNG_SelectTable_Data(
                csvTable,
                actionTurnout.getSelectEnum().getSelectTable(),
                NamedBeanAddressing.Formula);
        MaleSocket maleSocket = digitalActionManager.registerAction(actionTurnout);
        femaleRootSocket.connect(maleSocket);

        PrintTreeSettings printTreeSettings = new PrintTreeSettings();
        printTreeSettings._printDisplayName = true;

        final String treeIndent = "   ";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        logixNG_Manager.printTree(
                printTreeSettings,
                Locale.ENGLISH,
                printWriter,
                treeIndent,
                new MutableInt(0));
        final String originalTree = stringWriter.toString();

        // Store panels
        jmri.ConfigureManager cm = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cm == null) {
            log.error("Unable to get default configure manager");
        } else {
            FileUtil.createDirectory(FileUtil.getUserFilesPath() + "temp");
            File theFile = new File(FileUtil.getUserFilesPath() + "temp/" + "SingleExportedLogixNG.xml");
            log.info("Temporary first file: %s%n", theFile.getAbsoluteFile());

            boolean results = cm.exportAllLogixNGs(theFile);
            log.debug(results ? "store was successful" : "store failed");
            if (!results) {
                log.error("Failed to store panel");
                throw new RuntimeException("Failed to store panel");
            }


            //**********************************
            // Delete all the LogixNGs, ConditionalNGs, and so on before reading the file.
            //**********************************
            CreateLogixNGTreeScaffold.cleanup();

            LogixNG_Thread.stopAllLogixNGThreads();
            LogixNG_Thread.assertLogixNGThreadNotRunning();


            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);

            cm.importAllLogixNGs(theFile);

            logixNG_Manager.printTree(
                    printTreeSettings,
                    Locale.ENGLISH,
                    printWriter,
                    treeIndent,
                    new MutableInt(0));
            final String importedTree = stringWriter.toString();

            System.out.format("%n%n%n------------------------------------%n%n%s%n%n------------------------------------%n%n%n", originalTree);
            System.out.format("%n%n%n------------------------------------%n%n%s%n%n------------------------------------%n%n%n", importedTree);

            Assert.assertEquals(originalTree, importedTree);
        }

    }

    @Ignore
    @Test
    public void testExportImportAllLogixNGs() throws PropertyVetoException, Exception {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Add new LogixNG actions and expressions to jmri.jmrit.logixng.CreateLogixNGTreeScaffold
        CreateLogixNGTreeScaffold.createLogixNGTree();

        //**********************************
        // Test export and import of all the LogixNGs since that
        // we have almost all actions and expressions with lots
        // of data.
        //**********************************

        LogixNG_Manager logixNG_Manager = InstanceManager.getDefault(LogixNG_Manager.class);

        // Store panels
        jmri.ConfigureManager cm = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cm == null) {
            log.error("Unable to get default configure manager");
        } else {
            PrintTreeSettings printTreeSettings = new PrintTreeSettings();
            printTreeSettings._printDisplayName = true;

            FileUtil.createDirectory(FileUtil.getUserFilesPath() + "temp");
            File theFile = new File(FileUtil.getUserFilesPath() + "temp/" + "AllExportedLogixNGs.xml");
            log.info("Temporary first file: %s%n", theFile.getAbsoluteFile());

            final String treeIndent = "   ";
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            logixNG_Manager.printTree(
                    printTreeSettings,
                    Locale.ENGLISH,
                    printWriter,
                    treeIndent,
                    new MutableInt(0));
            final String originalTree = stringWriter.toString();

            boolean results = cm.exportAllLogixNGs(theFile);
            log.debug(results ? "store was successful" : "store failed");
            if (!results) {
                log.error("Failed to store panel");
                throw new RuntimeException("Failed to store panel");
            }



            //**********************************
            // Delete all the LogixNGs, ConditionalNGs, and so on before reading the file.
            //**********************************
            CreateLogixNGTreeScaffold.cleanup();

            LogixNG_Thread.stopAllLogixNGThreads();
            LogixNG_Thread.assertLogixNGThreadNotRunning();

/*
            audioManager.cleanup();
            JUnitUtil.waitFor(()->{return !audioManager.isInitialised();});

            audioManager = new jmri.jmrit.audio.DefaultAudioManager(
                    InstanceManager.getDefault(jmri.jmrix.internal.InternalSystemConnectionMemo.class));
            audioManager.init();
            JUnitUtil.waitFor(()->{return audioManager.isInitialised();});
*/

            //**********************************
            // Try to load file
            //**********************************
/*FIX LATER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            results = cm.importAllLogixNGs(theFile);
//            results = cm.load(theFile);
            log.debug(results ? "load was successful" : "load failed");
            if (results) {
                logixNG_Manager.setupAllLogixNGs();

                stringWriter = new StringWriter();
                printWriter = new PrintWriter(stringWriter);
                logixNG_Manager.printTree(
                        printTreeSettings,
                        Locale.ENGLISH,
                        printWriter,
                        treeIndent,
                        new MutableInt(0));

                String newTree = stringWriter.toString();
                if (!originalTree.equals(newTree)) {
                    log.error("--------------------------------------------");
                    log.error("Old tree:");
                    log.error("XXX"+originalTree+"XXX");
                    log.error("--------------------------------------------");
                    log.error("New tree:");
                    log.error("XXX"+stringWriter.toString()+"XXX");
                    log.error("--------------------------------------------");

                    System.out.println("--------------------------------------------");
                    System.out.println("Old tree:");
                    System.out.println("XXX"+originalTree+"XXX");
                    System.out.println("--------------------------------------------");
                    System.out.println("New tree:");
                    System.out.println("XXX"+stringWriter.toString()+"XXX");
                    System.out.println("--------------------------------------------");

//                    log.error(conditionalNGManager.getBySystemName(originalTree).getChild(0).getConnectedSocket().getSystemName());

                    String[] originalTreeLines = originalTree.split(System.lineSeparator());
                    String[] newTreeLines = newTree.split(System.lineSeparator());
                    int line=0;
                    for (; line < Math.min(originalTreeLines.length, newTreeLines.length); line++) {
                        if (!originalTreeLines[line].equals(newTreeLines[line])) {
                            System.out.format("Tree differs on line %d:%nOrig: %s%n New: %s%n", line+1, originalTreeLines[line], newTreeLines[line]);
                            break;
                        }
                    }
                    System.out.println("The tree has changed. The tree differs on line "+Integer.toString(line+1));
                    Assert.fail("The tree has changed. The tree differs on line "+Integer.toString(line+1));
//                    throw new RuntimeException("tree has changed");
                }
            } else {
                Assert.fail("Failed to load panel");
//                throw new RuntimeException("Failed to load panel");
            }
*/
        }


//        for (LoggingEvent evt : JUnitAppender.getBacklog()) {
//            System.out.format("Log: %s, %s%n", evt.getLevel(), evt.getMessage());
//        }


//        JUnitAppender.assertErrorMessage("systemName is already registered: IH1");
//        JUnitAppender.assertErrorMessage("systemName is already registered: IH2");
    }


    @Before
    public void setUp() {
        CreateLogixNGTreeScaffold.setUp();
    }

    @After
    public void tearDown() {
//        JUnitAppender.clearBacklog();    // REMOVE THIS!!!
        CreateLogixNGTreeScaffold.tearDown();
    }


    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImportExportTest.class);

}
