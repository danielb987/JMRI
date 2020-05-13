package jmri.jmrix.loconet.nodes.configurexml;

import jmri.jmrix.loconet.nodes.LnNode;
import jmri.jmrix.loconet.nodes.LnStringIO;
import org.jdom2.Element;

/**
 * Reads the definition of a StringIO from a decoder definition file, for
 * example Public_Domain_LNSV2_DanielBergqvist.xml.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class StringIO_DecoderDefinition {
    
    public static LnStringIO createStringIO(LnNode lnNode, Element e) {
        
        // The lnNode must already be registered in the LnNode mananger
        
        String systemPrefix = lnNode.getTrafficController().getSystemConnectionMemo().getSystemPrefix();
        int startSV_Address = 0;
        String systemName = String.format("%sC%d:%d", systemPrefix, lnNode.getAddress(), startSV_Address);
        LnStringIO lnStringIO = new LnStringIO(systemName, null);
        
        return lnStringIO;
    }
    
}
