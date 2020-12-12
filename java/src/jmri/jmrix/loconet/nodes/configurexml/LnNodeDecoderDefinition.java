package jmri.jmrix.loconet.nodes.configurexml;

import java.io.IOException;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrix.loconet.nodes.LnNode;
import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * Read LnNode decoder definitions from the decoder definition xml file.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LnNodeDecoderDefinition {
    
    /**
     * Create the NamedBeans that are defined in the decoder definition of this LnNode.
     * @param lnNode the lnNode to create NamedBeans for
     * @throws org.jdom2.JDOMException       only when all methods have failed
     * @throws java.io.FileNotFoundException if file not found
     */
    public static void createNamedBeans(LnNode lnNode) throws JDOMException, IOException {
        DecoderFile decoderFile = lnNode.getDecoderFile();
        Element decoderRoot = decoderFile.rootFromName(DecoderFile.fileLocation + decoderFile.getFileName());
        
        Element locoNetNodeElement = decoderRoot.getChild("loconet-node");
        
        Element stringIOsElement = locoNetNodeElement.getChild("stringios");
        
        for (Element e : stringIOsElement.getChildren()) {
            StringIO_DecoderDefinition.createStringIO(lnNode, e);
        }
    }
    
}
