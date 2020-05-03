package jmri.jmrix.loconet.nodes;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.Manager;
import jmri.NamedBean;
import jmri.implementation.AbstractStringIO;

/**
 * An StringIO on the LocoNet
 */
public class LnStringIO extends AbstractStringIO implements NodeItem {

    private final LnNode _node;
    private final int _startSVAddress;
    
    private Type _type = Type.FixedLengthString;
    private int _maxLength;
    private boolean _cutLongStrings;
    
    
    public LnStringIO(@Nonnull LnNode node, int startSVAddress, @CheckForNull String userName) {
        super(getSystemName(node, startSVAddress), userName);
        _node = node;
        _startSVAddress = startSVAddress;
    }
    
    public LnStringIO(@Nonnull String sysName, @CheckForNull String userName, @Nonnull LnNode node) {
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
        return String.format("%sC%d:%d", systemPrefix, node.getAddress(), startSVAddress);
    }
    
    /**
     * Get the node this StringIO belongs to.
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
            case FixedLengthString:
                return _maxLength;
                
            case VariableLenghtString:
                return 4;
                
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
    }
    
    @Override
    public Manager<? extends NamedBean> getManager() {
        return InstanceManager.getDefault(jmri.StringIOManager.class);
    }
    
    public Type getType() {
        return _type;
    }
    
    protected void setType(Type type) {
        _type = type;
    }
    
    @Override
    public int getMaximumLength() {
        return _maxLength;
    }
    
    public void setMaximumLength(int length) {
        switch (_type) {
            case FixedLengthString:
                if (length > 8) throw new IllegalArgumentException("Max length is greater than 8 bytes");
                break;
            case VariableLenghtString:
                if (length > 129) throw new IllegalArgumentException("Max length is greater than 129 bytes");
                break;
            default:
                throw new RuntimeException("_type has invalid value: "+_type.name());
        }
        _maxLength = length;
    }
    
    @Override
    public boolean cutLongStrings() {
        return _cutLongStrings;
    }
    
    public void cutOutOfBoundsValues(boolean cut) {
        _cutLongStrings = cut;
    }
    
    @Override
    protected void sendStringToLayout(@Nonnull String value) throws JmriException {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    
    public enum Type {
        
        FixedLengthString(Bundle.getMessage("TypeFixedLengthString"),
            Bundle.getMessage("TypeFixedLengthString_FormatDescr")),
        
        VariableLenghtString(Bundle.getMessage("TypeVariableLenghtString"),
            Bundle.getMessage("TypeVariableLenghtString_FormatDescr"));
        
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
