package jmri.jmrix.loconet.nodes.configurexml;

import jmri.configurexml.AbstractXmlAdapter;
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
        int startSV_Address = Integer.parseInt(e.getChildText("first_sv"));
        String systemName = String.format("%sC%d:%d", systemPrefix, lnNode.getAddress(), startSV_Address);
        LnStringIO lnStringIO = new LnStringIO(systemName, null);
        
        lnStringIO.setDescription(e.getChildText("name"));
        
        AbstractXmlAdapter.EnumIO<LnStringIO.Type> map = new AbstractXmlAdapter.EnumIoNames<>(LnStringIO.Type.class);
        lnStringIO.setType(map.inputFromString(e.getChildText("type")));
        lnStringIO.setMaximumLength(Integer.parseInt(e.getChildText("max_length")));
        
//        System.out.format("System name: %s%n", lnStringIO.getSystemName());
//        System.out.format("Name: %s%n", lnStringIO.getDescription());
//        System.out.format("Type: %s%n", lnStringIO.getType().name());
//        System.out.format("Maximum length: %d%n", lnStringIO.getMaximumLength());
        
        return lnStringIO;
    }
    
}
