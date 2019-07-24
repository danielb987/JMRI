package jmri.jmrit.logixng;

/**
 * Helper class to test invalid enum values.
 * This class currently only works on Linux.
 */
public class TestEnumScaffold {

    static {
        if (isSupported()) {
            System.loadLibrary("invalidEnumValue");
        }
    }
    
    /**
     * Checks if this class is supported on this computer.
     * @return true if the class may be used on this computer, false otherwise
     */
    public static boolean isSupported() {
        return jmri.util.SystemType.isLinux();
    }
    
    private native Enum getInvalidValue(Class enumClass);
    
    /**
     * Returns an invalid enum value of this class.
     * @param enumClass the enum type for which we want an invalid value
     * @return an invalid value of the type specified by enumClass
     */
    public Enum getInvalidEnumValue(Class enumClass) {
        if (isSupported()) {
            return getInvalidValue(enumClass);
        } else {
            throw new UnsupportedOperationException("this method is not supported on this computer");
        }
    }
//    public Enum getInvalidEnumValue(Class enumClass) { return MyColors.BLUE; }
    
    public void test() {
        MyColors color = (MyColors) getInvalidEnumValue(MyColors.class);
        System.out.format("Color: %d%n", color.ordinal());
        System.out.format("Color: %s%n", color.name());
    }
    
    
    private enum MyColors {
        BLUE,
        RED,
    }
    
}
