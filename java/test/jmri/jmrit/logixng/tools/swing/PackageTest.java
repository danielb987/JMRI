package jmri.jmrit.logixng.tools.swing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddMaleSocketDialogTest.class,
    EditMaleSocketDialogTest.class,
    LogixNGEditorTest.class,
    TimeDiagramTest.class,
})

/**
 * Invokes complete set of tests in the jmri.jmrit.newlogix tree
 *
 * @author Daniel Bergqvist 2018
 */
public class PackageTest {
}
