package jmri.jmrix.loconet.nodes;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.Manager;
import jmri.NamedBean;
import jmri.implementation.AbstractAnalogIO;

/**
 * An AnalogIO on the LocoNet
 */
public class LnAnalogIO extends AbstractAnalogIO implements NodeItem {

    private final LnNode _node;
    private final int _startSVAddress;
    
    private Type _type = Type.EightBitSigned;
    
    // Which min and max value that is used depends on the type of the AnalogIO.
    private int _minInt;
    private int _maxInt;
    private double _minDouble;
    private double _maxDouble;
    
    private double _resolution = 1.0;
    private AbsoluteOrRelative _absoluteOrRelative;
    private boolean _cutOutOfBoundsValues;
    
    
    public LnAnalogIO(@Nonnull LnNode node, int startSVAddress, @CheckForNull String userName) {
        super(getSystemName(node, startSVAddress), userName);
        _node = node;
        _startSVAddress = startSVAddress;
    }
    
    public LnAnalogIO(@Nonnull String sysName, @CheckForNull String userName, @Nonnull LnNode node) {
        super(sysName, userName);
        
        String[] parts = sysName.split(":");
        _node = node;
        
        _startSVAddress = Integer.parseInt(parts[1]);
        
        if (!sysName.equals(getSystemName(node, _startSVAddress))) {
            throw new IllegalArgumentException("the system name is invalid");
        }
    }
    
    /**
     * Get the system name for this StringIO.
     * This method is needed since the constructor needs a system name to its parent constructor.
     * @param node the LnNode that is StringIO is located on
     * @param startSVAddress the start address of the StringIO of the LnNode
     * @return the system name
     */
    private static String getSystemName(@Nonnull LnNode node, int startSVAddress) {
        // The system name of StringIO with address 1234 and start SV 59 on connection L2 should be L2C1234:59
        String systemPrefix = node.getTrafficController().getSystemConnectionMemo().getSystemPrefix();
        return String.format("%sV%d:%d", systemPrefix, node.getAddress(), startSVAddress);
    }
    
    /**
     * Get the node this AnalogIO belongs to.
     * @return the node that owns this item
     */
    public LnNode getNode() {
        return _node;
    }
    
    @Override
    public int getAddress() {
        return _node.getAddress();
    }
    
    @Override
    public int getStartSVAddress() {
        return _startSVAddress;
    }
    
    @Override
    public int getNumSVs() {
        switch (_type) {
            case EightBitSigned:
            case EightBitUnsigned:
                return 1;
                
            case SixteenBitSigned:
            case SixteenBitUnsigned:
                return 2;
                
            case ThirtyTwoBitSigned:
            case Float:
                return 4;
                
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    @Override
    public Manager<? extends NamedBean> getManager() {
        return InstanceManager.getDefault(jmri.AnalogIOManager.class);
    }
    
    public Type getType() {
        return _type;
    }
    
    protected void setType(Type type) {
        _type = type;
    }
    
    @Override
    public double getMin() {
        switch (_type) {
            case EightBitSigned:
                // fall through
            case EightBitUnsigned:
                // fall through
            case SixteenBitSigned:
                // fall through
            case SixteenBitUnsigned:
                // fall through
            case ThirtyTwoBitSigned:
                return _minInt;
            case Float:
                return _minDouble;
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    @Override
    public double getMax() {
        switch (_type) {
            case EightBitSigned:
                // fall through
            case EightBitUnsigned:
                // fall through
            case SixteenBitSigned:
                // fall through
            case SixteenBitUnsigned:
                // fall through
            case ThirtyTwoBitSigned:
                return _maxInt;
            case Float:
                return _maxDouble;
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    public void setMin(int min) {
        switch (_type) {
            case EightBitSigned:
                if (min < -128) throw new IllegalArgumentException("Min value is less than -128");
                break;
            case EightBitUnsigned:
                if (min < 0) throw new IllegalArgumentException("Min value is less than 0");
                break;
            case SixteenBitSigned:
                if (min < -32768) throw new IllegalArgumentException("Min value is less than -32768");
                break;
            case SixteenBitUnsigned:
                if (min < 0) throw new IllegalArgumentException("Min value is less than 0");
                break;
            case ThirtyTwoBitSigned:
                // min is a 32 bit signed integer and cannot be to small
                break;
            case Float:
                throw new UnsupportedOperationException("Use setMin(double) to set min value for floating types");
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
        _minInt = min;
    }
    
    public void setMax(int max) {
        switch (_type) {
            case EightBitSigned:
                if (max > 127) throw new IllegalArgumentException("Min value is greater than -128");
                break;
            case EightBitUnsigned:
                if (max > 255) throw new IllegalArgumentException("Min value is greater than 0");
                break;
            case SixteenBitSigned:
                if (max > 32767) throw new IllegalArgumentException("Min value is greater than 32767");
                break;
            case SixteenBitUnsigned:
                if (max > 65535) throw new IllegalArgumentException("Min value is greater than 65535");
                break;
            case ThirtyTwoBitSigned:
                // max is a 32 bit signed integer and cannot be to big
                break;
            case Float:
                throw new UnsupportedOperationException("Use setMax(double) to set max value for floating types");
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
        _maxInt = max;
    }
    
    public void setMin(double min) {
        switch (_type) {
            case EightBitSigned:
                // fall through
            case EightBitUnsigned:
                // fall through
            case SixteenBitSigned:
                // fall through
            case SixteenBitUnsigned:
                // fall through
            case ThirtyTwoBitSigned:
                throw new UnsupportedOperationException("Use setMin(int) to set min value for integer types");
            case Float:
                _minDouble = min;
                return;
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    public void setMax(double max) {
        switch (_type) {
            case EightBitSigned:
                // fall through
            case EightBitUnsigned:
                // fall through
            case SixteenBitSigned:
                // fall through
            case SixteenBitUnsigned:
                // fall through
            case ThirtyTwoBitSigned:
                throw new UnsupportedOperationException("Use setMax(int) to set max value for integer types");
            case Float:
                _maxDouble = max;
                return;
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    @Override
    public double getResolution() {
        return _resolution;
    }
    
    public void setResolution(double resolution) {
        switch (_type) {
            case EightBitSigned:
                // fall through
            case EightBitUnsigned:
                // fall through
            case SixteenBitSigned:
                // fall through
            case SixteenBitUnsigned:
                // fall through
            case ThirtyTwoBitSigned:
                throw new UnsupportedOperationException("Resolution cannot be set for integer types");
            case Float:
                _resolution = resolution;
                return;
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    @Override
    public AbsoluteOrRelative getAbsoluteOrRelative() {
        return _absoluteOrRelative;
    }
    
    public void setAbsoluteOrRelative(AbsoluteOrRelative absoluteOrRelative) {
        _absoluteOrRelative = absoluteOrRelative;
    }
    
    @Override
    public boolean cutOutOfBoundsValues() {
        return _cutOutOfBoundsValues;
    }
    
    public void cutOutOfBoundsValues(boolean cut) {
        _cutOutOfBoundsValues = cut;
    }
    
    @Override
    protected void sendValueToLayout(double value) throws JmriException {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    
    public enum Type {
        
        EightBitSigned(Bundle.getMessage("TypeEightBitSigned"),
            Bundle.getMessage("TypeEightBitSigned_FormatDescr")),
        
        EightBitUnsigned(Bundle.getMessage("TypeEightBitUnsigned"),
            Bundle.getMessage("TypeEightBitUnsigned_FormatDescr")),
        
        SixteenBitSigned(Bundle.getMessage("TypeSixteenBitSigned"),
            Bundle.getMessage("TypeSixteenBitSigned_FormatDescr")),
        
        SixteenBitUnsigned(Bundle.getMessage("TypeSixteenBitUnsigned"),
            Bundle.getMessage("TypeSixteenBitUnsigned_FormatDescr")),
        
        ThirtyTwoBitSigned(Bundle.getMessage("TypeThirtyTwoBitSigned"),
            Bundle.getMessage("TypeThirtyTwoBitSigned_FormatDescr")),
        
        Float(Bundle.getMessage("TypeFloat"),
            Bundle.getMessage("TypeFloat_FormatDescr"));
        
        private final String _name;
        private final String _formatDescr;
        
        private Type(String name, String formatDescr) {
            _name = name;
            _formatDescr = formatDescr;
        }
        
        @Override
        public String toString() {
            return _name;
        }
        
        public String getFormatDescr() {
            return _formatDescr;
        }
        
    }
    
}
