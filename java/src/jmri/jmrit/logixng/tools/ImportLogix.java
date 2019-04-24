package jmri.jmrit.logixng.tools;

import jmri.InstanceManager;
import jmri.Logix;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.tools.swing.ImportLogixFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        
        log.debug("Import Logix {} to LogixNG {}", _logix.getSystemName(), _logixNG.getSystemName());
    }
    
    public void doImport() {
        
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ImportLogixFrame.class);

}
