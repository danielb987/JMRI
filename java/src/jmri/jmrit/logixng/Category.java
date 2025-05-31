package jmri.jmrit.logixng;

/**
 * The category of expressions and actions.
 * <P>
 * It's used to group expressions or actions then the user creates a new
 * expression or action.
 * <P>
 * This class is intended to be an Enum, but implemented as an abstract class
 * to allow adding more categories later without needing to change this class.
 * For example, external programs using JMRI as a lib might want to add their
 * own categories.
 *
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class Category extends jmri.Category {

    /**
     * A item on the layout, for example turnout, sensor and signal mast.
     */
    public static final Item ITEM = new Item();

    /**
     * Common.
     */
    public static final Common COMMON = new Common();

    /**
     * Flow Control.
     */
    public static final FlowControl FLOW_CONTROL = new FlowControl();

    /**
     * Linux specific things.
     */
    public static final Linux LINUX = new Linux();

    static {
        registerCategory(ITEM);
        registerCategory(COMMON);
        registerCategory(FLOW_CONTROL);
        registerCategory(OTHER);
        if (jmri.util.SystemType.isLinux()) {
            registerCategory(LINUX);
        }
    }


    protected Category(String name, String description, int order) {
        super(name, description, order);
    }


    public static final class Item extends Category {

        public Item() {
            super("ITEM", Bundle.getMessage("CategoryItem"), 100);
        }
    }


    public static final class Common extends Category {

        public Common() {
            super("COMMON", Bundle.getMessage("CategoryCommon"), 200);
        }
    }


    public static final class FlowControl extends Category {

        public FlowControl() {
            super("FLOW_CONTROL", Bundle.getMessage("CategoryFlowControl"), 210);
        }
    }


    public static final class Linux extends Category {

        public Linux() {
            super("LINUX", Bundle.getMessage("CategoryLinux"), 2000);
        }
    }


    public static final class Other extends Category {

        public Other() {
            super("OTHER", Bundle.getMessage("CategoryOther"), Integer.MAX_VALUE);
        }
    }

}
