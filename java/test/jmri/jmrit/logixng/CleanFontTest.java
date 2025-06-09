package jmri.jmrit.logixng;

import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jmri.util.JUnitUtil;

import org.junit.*;

/**
 * Test Category
 *
 * @author Daniel Bergqvist 2018
 */
public class CleanFontTest {


    /**
     * Remove verbose and redundant information from fontname field
     * @param fontname The system-specific font name with a possible trailing .plain, etc
     * @param style  From the Font class static style values
     * @return A font name without trailing modifiers
     */
    String simplifyFontname(String fontname, int style) {

        // The pattern consists of:
        // ^     - The beginning of the string
        // (.*)  - The rest of the font name. This is the return value of the method.
        // [ .-] - One of the characters ' ', '.', '-'.
        // (plain|bold|italic)  - Any of "plain", "bold", "italic"
        // $     - End of the string
        //
        // The parentheses groups the part together so that we can get it by m.group() later.
        Pattern p = Pattern.compile("^(.*)[ .-](plain|bold|italic)$");
        Matcher m = p.matcher(fontname);

        if (m.matches()) {
            // m.group(1) is the name of the font except plain/bold/italic
            // m.group(2) is plan/bold/italic
            // log.error("Match: Font: {}, Style: {}", m.group(1), m.group(2));

            if (("plain".equals(m.group(2)) && style != Font.PLAIN)
                    || ("bold".equals(m.group(2)) && style != Font.BOLD)
                    || ("italic".equals(m.group(2)) && style != Font.ITALIC)) {
                log.warn("fontname {} is not consistent with style {}", fontname, style);
                return fontname;
            }

            // Return the first part of the font name
            return m.group(1);
        }

        // No match
        return fontname;
    }

    @Test
    public void testWindowsFontSpecialCase () {
        Assert.assertEquals("Dialog", simplifyFontname("Dialog", 0));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog.plain", 0));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog.bold", 1));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog.italic", 2));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog", 0));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog plain", 0));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog bold", 1));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog italic", 2));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog", 0));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog-plain", 0));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog-bold", 1));
        Assert.assertEquals("Dialog", simplifyFontname("Dialog-italic", 2));
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

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CleanFontTest.class);
}
