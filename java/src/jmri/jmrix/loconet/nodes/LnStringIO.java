package jmri.jmrix.loconet.nodes;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jmri.JmriException;
import jmri.Manager;
import jmri.NamedBean;
import jmri.implementation.AbstractStringIO;

/**
 * An StringIO on the LocoNet
 */
public class LnStringIO extends AbstractStringIO implements NodeItem {

    private LnNode _node;
    private int _address;
    
    private Type _type = Type.EightByteString;
    private int _maxLength;
    private boolean _cutLongStrings;
    
    
    public LnStringIO(@Nonnull String sysName, @CheckForNull String userName, @Nonnull LnNode node) {
        super(sysName, userName);
        _node = node;
    }
    
    /**
     * Get the node this analog IO belongs to.
     * @return the node that owns this item
     */
    public LnNode getNode() {
        return _node;
    }
    
    /**
     * Set the node this analog IO belongs to.
     * This method is package-private since it should only be changed by the
     * node that owns it, and only in the case when the item is moved from one
     * node to another.
     * @param node the new node that owns this item
     */
    void setNode(LnNode node) {
        _node = node;
    }
    
    @Override
    public int getAddress() {
        return _address;
    }
    
    @Override
    public void setAddress(int address) {
        _address = address;
    }
    
    @Override
    public Manager<? extends NamedBean> getManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            case EightByteString:
                if (length > 8) throw new IllegalArgumentException("Max length is greater than 8 bytes");
                break;
            case VariableString:
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
        
        EightByteString(Bundle.getMessage("TypeEightByteString"),
            Bundle.getMessage("TypeEightByteString_FormatDescr")),
        
        VariableString(Bundle.getMessage("TypeVariableString"),
            Bundle.getMessage("TypeVariableString_FormatDescr"));
        
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
