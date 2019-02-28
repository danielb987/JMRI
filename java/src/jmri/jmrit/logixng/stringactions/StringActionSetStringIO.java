package jmri.jmrit.logixng.stringactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.StringIO;
import jmri.JmriException;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

/**
 * Sets an AnalogIO.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class StringActionSetStringIO extends AbstractStringAction {

    private StringIO _stringIO;
    
    public StringActionSetStringIO(String sys) {
        super(sys);
    }
    
    public StringActionSetStringIO(String sys, String user) {
        super(sys, user);
    }
    
    public StringActionSetStringIO(
            String sys,
            StringIO stringIO) {
        
        super(sys);
        _stringIO = stringIO;
    }
    
    public StringActionSetStringIO(
            String sys, String user,
            StringIO stringIO) {
        
        super(sys, user);
        _stringIO = stringIO;
    }
    
    @Override
    public void setValue(String value) {
        if (_stringIO != null) {
            try {
                _stringIO.setCommandedStringValue(value);
            } catch (JmriException ex) {
                log.warn("StringActionSetStringIO", ex);
            }
        }
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    public String getShortDescription() {
        if (_stringIO != null) {
            return Bundle.getMessage("StringActionSetStringIO", _stringIO.getDisplayName());
        } else {
            return Bundle.getMessage("StringActionSetStringIO", "none");
        }
    }

    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    private final static Logger log = LoggerFactory.getLogger(StringActionSetStringIO.class);

}
