package jmri.jmrit.logixng.tools;

import jmri.InstanceManager;
import jmri.Logix;
import jmri.jmrit.logixng.LogixNG;

/**
 * Imports Logixs to LogixNG
 * 
 * @author Daniel Bergqvist 2019
 */
public class ImportLogix {

    private final Logix _logix;
    private final LogixNG _logixNG;
    
    public ImportLogix(Logix logix) {
        _logix = logix;
        _logixNG = InstanceManager.getDefault(jmri.jmrit.logixng.LogixNG_Manager.class)
                .createLogixNG("Logix: "+_logix.getDisplayName(), false);
    }
    
    public void doImport() {
        
    }
    
}
