package jmri.jmrit.logixng.enums;

/**
 * An enum with the values "is" and "is not"
 * 
 * @author Daniel Bergqvist 2019
 */
public enum Is_IsNot_Enum {

    IS(Bundle.getMessage("Is")),
    IS_NOT(Bundle.getMessage("IsNot"));

    private final String _text;

    private Is_IsNot_Enum(String text) {
        this._text = text;
    }

    @Override
    public String toString() {
        return _text;
    }
    
}
