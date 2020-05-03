package jmri.jmrix.loconet.nodes.stringio;

/**
 * Types of StringIO
 * Note that depending on the character encoding used, one character may take
 * one or several bytes. For example, UTF-8 may use more than one bytes for
 * each character.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public enum LnStringIOType {
    
    /**
     * The StringIO uses N SV registers, where N is the maximum length of the
     * string this StringIO can handle. For example, if the StringIO has a
     * maximum length of 8 bytes, the StringIO uses 8 SV registers.
     * <p>
     * The maximum length must be a multiple of 4.
     */
    FIXED_LENGTH("TypeFixedLength"),
    
    /**
     * The StringIO uses four SV registers. The first SV register is the index
     * and the second to forth SV registers are data. Since each SV register is
     * 8 bits, the maximum length of a variable string is 256 bytes.
     */
    VARIABLE_LENGTH("TypeVariableLength");
    
    private final String _bundleKey;
    
    private LnStringIOType(String bundleKey) {
        _bundleKey = bundleKey;
    }
    
    @Override
    public String toString() {
        return Bundle.getMessage(_bundleKey);
    }

}
