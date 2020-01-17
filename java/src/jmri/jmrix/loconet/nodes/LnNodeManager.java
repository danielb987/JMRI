package jmri.jmrix.loconet.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jmri.InstanceManager;
import jmri.InstanceManagerAutoDefault;
import jmri.Manager;
import jmri.jmrit.XmlFile;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrit.decoderdefn.DecoderIndexFile;
import jmri.managers.AbstractManager;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager of LnNodes.
 * 
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LnNodeManager implements InstanceManagerAutoDefault {

    public static final int PUBLIC_DOMAIN_DIY_MANAGER_ID = 13;
    public static final String PUBLIC_DOMAIN_DIY_MANAGER = "Public-domain and DIY";
    private final XmlFile _xmlFile = new XmlFile();
    private final DecoderIndexFile _decoderIndexFile;
    private final Map<Integer, String> _lnDIYDeveloperList = new HashMap<>();
//    private String _developerListDate;
//    private String _developerListUpdated;
//    private String _developerListLastAdd;
    
    public LnNodeManager() {
        _decoderIndexFile = InstanceManager.getDefault(DecoderIndexFile.class);
        try {
            readLnDIYDeveloperList();
        } catch (JDOMException | IOException ex) {
            log.error("Cannot load LocoNet Hackers DIY SV & IPL DeveloperId List", ex);
        }
    }
    
    private void readLnDIYDeveloperList() throws org.jdom2.JDOMException, java.io.IOException {
        // always reads the file distributed with JMRI
        Element developerList = _xmlFile.rootFromName("loconet_hackers_diy_list.xml");
        
        if (developerList != null) {
/*            
            Attribute a;
            a = developerList.getAttribute("nmraListDate");
            if (a != null) {
                _developerListDate = a.getValue();
            }
            a = developerList.getAttribute("updated");
            if (a != null) {
                _developerListUpdated = a.getValue();
            }
            a = developerList.getAttribute("lastadd");
            if (a != null) {
                _developerListLastAdd = a.getValue();
            }
*/            
            List<Element> l = developerList.getChildren("developer");
            if (log.isDebugEnabled()) {
                log.debug("readMfgSection sees " + l.size() + " children");
            }
            for (Element el : l) {
                int id = el.getAttribute("devID").getIntValue();
                String name = el.getAttribute("name").getValue();
                _lnDIYDeveloperList.put(id, name);
//                System.out.format("Developer: %d, %s%n", id, name);
            }
        } else {
            log.warn("no mfgList found");
        }
    }
    
    public String getManufacturer(int manufacturerID) {
        return _decoderIndexFile.mfgNameFromId(Integer.toString(manufacturerID));
    }
    
    public String getDeveloper(int developerID) {
        return _lnDIYDeveloperList.get(developerID);
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(LnNodeManager.class);
    
}
