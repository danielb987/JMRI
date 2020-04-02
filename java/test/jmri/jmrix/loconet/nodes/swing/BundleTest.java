package jmri.jmrix.loconet.nodes.swing;

import java.util.Locale;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Bundle
 * 
 * @author Daniel Bergqvist 2018
 */
public class BundleTest {

    @Test
    public void daniel() {
        String s =
"# NodesSwingBundle.properties\n" +
"#\n" +
"# Default properties for jmri.jmrix.loconet.nodes.swing\n" +
"\n" +
"ConfigureNodesTitle = Configure nodes\n" +
"\n" +
// "WindowTitle             = WindowTitle\n" +
// "WindowConnectionMemo    = WindowConnectionMemo\n" +
"SelectSelect            = SelectSelect\n" +
"SelectEdit              = SelectEdit\n" +
// "SelectInfo              = SelectInfo\n" +
"SelectDelete            = SelectDelete\n" +
"\n" +
"AddButtonTip            = AddButtonTip\n" +
"PrintButtonTip          = PrintButtonTip\n" +
"DoneButtonTip           = DoneButtonTip\n" +
"\n" +
"\n" +
"ButtonAdd       = ButtonAdd\n" +
"TipAddButton    = TipAddButton\n" +
"ButtonDelete    = ButtonDelete\n" +
"TipDeleteButton = TipDeleteButton\n" +
"ButtonUpdate    = ButtonUpdate\n" +
"TipUpdateButton = TipUpdateButton\n" +
"ButtonCancel    = ButtonCancel\n" +
"TipCancelButton = TipCancelButton\n" +
"ButtonDone      = ButtonDone\n" +
"TipDoneButton   = TipDoneButton\n" +
"\n" +
"\n" +
"NotesDel1   = NotesDel1\n" +
"NotesDel2   = NotesDel2\n" +
"NotesDel3   = NotesDel3\n" +
"\n" +
"\n" +
"ReminderTitle   = ReminderTitle\n" +
"Reminder1       = Reminder1\n" +
"Reminder2       = Reminder2\n" +
"\n" +
"\n" +
"Error1      = Error1\n" +
"Error2      = Error2\n" +
"Error3      = Error3\n" +
"Error4      = Error4\n" +
"Error5      = Error5\n" +
"Error6      = Error6\n" +
"Error7      = Error7\n" +
"Error8      = Error8\n" +
"Error9      = Error9\n" +
"Error10     = Error10\n" +
"Error11     = Error11\n" +
"Error12     = Error12\n" +
"Error13     = Error13\n" +
"Error14     = Error14\n" +
"Error15     = Error15\n" +
"Error16     = Error16\n" +
"Error17     = Error17\n" +
"Error18     = Error18\n" +
"\n" +
"\n" +
"\n" +
"FeedBackAdd     = FeedBackAdd\n" +
"FeedBackUpdate  = FeedBackUpdate\n" +
"\n" +
"\n" +
"\n" +
"ConfirmDeleteTitle  = ConfirmDeleteTitle\n" +
"ConfirmDelete1      = ConfirmDelete1\n" +
"ConfirmDelete2      = ConfirmDelete2\n" +
"\n" +
"FeedBackDelete      = FeedBackDelete\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"LabelNodeAddress        = LabelNodeAddress\n" +
"TipNodeAddress          = TipNodeAddress\n" +
"LabelNodeType           = LabelNodeType\n" +
"LabelOnBoardBytes       = LabelOnBoardBytes\n" +
"TipNodeType             = TipNodeType\n" +
"LabelDelay              = LabelDelay\n" +
"TipDelay                = TipDelay\n" +
"\n" +
"\n" +
"CardSize8               = CardSize8\n" +
"CardSize24              = CardSize24\n" +
"CardSize32              = CardSize32\n" +
"TipCardSize             = TipCardSize\n" +
"\n" +
"LabelPulseWidth         = LabelPulseWidth\n" +
"TipPulseWidth           = TipPulseWidth\n" +
"LabelMilliseconds       = LabelMilliseconds\n" +
"\n" +
"\n" +
"\n" +
"# Change these later\n" +
"PrintButtonText     = Print\n" +
"LabelOnBoardBytes   = aaa\n" +
"NotesAdd1           = bbb\n" +
"NotesAdd2           = ccc\n" +
"NotesAdd3           = ddd\n" +
"NotesDel1           = eee\n" +
"NotesDel2           = fff\n" +
"NotesDel3           = ggg\n" +
"cmrinetOpt0         = hhh\n" +
"cmrinetOpt1         = iii\n" +
"cmrinetOpt2         = jjj\n" +
"cmrinetOpt8         = kkk\n" +
"cmrinetOpt15        = lll\n" +
"cpnodeOpt0          = mmm\n" +
"cpnodeOpt1          = nnn\n" +
"cpnodeOpt2          = ooo\n" +
"cpnodeOpt8          = ppp\n" +
"cpnodeOpt15         = qqq\n" +
"HeadingCardAddress  = rrr\n" +
"HeadingCardType     = sss\n" +
"HeadingPort         = ttt\n" +
"CardTypeNone        = CardTypeNone\n" +
"CardTypeInput       = CardTypeInput\n" +
"CardTypeOutput      = CardTypeOutput\n" +
"\n" +
"HintCardTypePartA   = HintCardTypePartA\n" +
"HintCardTypePartB   = HintCardTypePartB\n" +
"HintCardTypePartC   = HintCardTypePartC\n" +
"HintCardTypePartD   = HintCardTypePartD\n" +
"HintCardTypePartE   = HintCardTypePartE\n" +
"HintCardTypePartF   = HintCardTypePartF\n" +
"\n" +
"HintSearchlightPartA    = HintSearchlightPartA\n" +
"HintSearchlightPartB    = HintSearchlightPartB\n" +
"HintSearchlightPartC    = HintSearchlightPartC\n" +
"HintSearchlightPartD    = HintSearchlightPartD\n" +
"HintSearchlightPartE    = HintSearchlightPartE\n" +
"HintSearchlightPartF    = HintSearchlightPartF\n" +
"\n" +
"BoxLabelNotes           = BoxLabelNotes\n" +
"\n" +
"\n" +
"HeadingCardAddress      = HeadingCardAddress\n" +
"HeadingCardType         = HeadingCardType\n" +
"HeadingPort             = HeadingPort\n" +
"\n" +
"\n" +
"NodeBoxLabel            = NodeBoxLabel\n" +
"";
        String sa[] = s.split("\n");
        for (String ss : sa) {
            if (ss.startsWith("#")) continue;
            if (ss.isEmpty()) continue;
            String s2 = ss.substring(0, ss.indexOf('=')).trim();
//            System.out.println(s2+";");
            try {
                String s3 = Bundle.getMessage(s2);
                Assert.assertNotNull("bundle is not null", s3);
//                System.out.println(s2+": "+s3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void testBundle() {
        Assert.assertTrue("bundle is correct", "About {0}".equals(Bundle.getMessage("TitleAbout")));
        Assert.assertTrue("bundle is correct", "About LocoNet".equals(Bundle.getMessage("TitleAbout", "LocoNet")));
        Assert.assertTrue("bundle is correct", "About {0}".equals(Bundle.getMessage(Locale.US, "TitleAbout")));
        Assert.assertTrue("bundle is correct", "About LocoNet".equals(Bundle.getMessage(Locale.US, "TitleAbout", "LocoNet")));
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
