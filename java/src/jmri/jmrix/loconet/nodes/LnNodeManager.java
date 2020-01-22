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
import jmri.jmrit.roster.RosterEntry;
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
    private final List<DecoderFile> _decoders = new ArrayList<>();
    private final Map<Integer, List<DecoderFile>> decoderFileMap = new HashMap<>();
    
    
    public LnNodeManager() {
        _decoderIndexFile = InstanceManager.getDefault(DecoderIndexFile.class);
        try {
            readLnDIYDeveloperList();
        } catch (JDOMException | IOException ex) {
            log.error("Cannot load LocoNet Hackers DIY SV & IPL DeveloperId List", ex);
        }
        readDecoderTypes();
    }
    
    private void readLnDIYDeveloperList() throws org.jdom2.JDOMException, java.io.IOException {
        // always reads the file distributed with JMRI
        Element developerList = _xmlFile.rootFromName("loconet_hackers_diy_list.xml");
        
        if (developerList != null) {
            List<Element> l = developerList.getChildren("developer");
            if (log.isDebugEnabled()) {
                log.debug("readMfgSection sees " + l.size() + " children");
            }
            for (Element el : l) {
                int id = el.getAttribute("devID").getIntValue();
                String name = el.getAttribute("name").getValue();
                _lnDIYDeveloperList.put(id, name);
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
    
    public DecoderFile getProduct(int manufacturerID, int developerID, int productID) {
        
        String developer = manufacturerID == PUBLIC_DOMAIN_DIY_MANAGER_ID ? Integer.toString(developerID) : null;
        
        List<DecoderFile> decoders = decoderFileMap.get(manufacturerID);
        
        for (DecoderFile decoder : decoders) {
            if (developer == null || (developer.equals(decoder.getDeveloperID()))) {
                String productIDs = decoder.getProductID();
                String[] products = productIDs.split(",");
                for (String p : products) {
                    log.debug(String.format(
                            "Manufacturer: %s, Developer: %s, ProductID: %s. Expect manufacturer: %d, developer: %d, productID: %d, num decoders: %d%n",
                            decoder.getMfgID(), decoder.getDeveloperID(), p, manufacturerID, developerID, productID, decoders.size()));
                    if (productID == Integer.parseInt(p)) return decoder;
                }
            }
        }
        
        return null;
    }
    
    
    private void readDecoderTypes() {
        _decoders.clear();
        
        List<DecoderFile> decoders =
                InstanceManager.getDefault(DecoderIndexFile.class)
                        .matchingDecoderList(null, null, null, null, null, null);
        
        String oldMfg = "";
        String oldDev = "";
        String oldModel = "";
        String oldFam = "";
        String oldProd = "";
        
        for (DecoderFile decoderFile : decoders) {
            String mfg = decoderFile.getMfg();
            int mfgID = Integer.parseInt(decoderFile.getMfgID());
            String dev = decoderFile.getDeveloperID();
            String model = decoderFile.getModel();
            String fam = decoderFile.getFamily();
            String prod = decoderFile.getProductID();
            
            if (prod == null) prod = "";
            
            if (!oldMfg.equals(mfg) || !oldDev.equals(dev) || !oldModel.equals(model)
                    || !oldFam.equals(fam) || !oldProd.equals(prod)) {
                
                List<DecoderFile> decoderFiles = decoderFileMap.get(mfgID);
                if (decoderFiles == null) {
                    decoderFiles = new ArrayList<>();
                    decoderFileMap.put(mfgID, decoderFiles);
                }
                decoderFiles.add(decoderFile);
                _decoders.add(decoderFile);
            }
            
            oldMfg = mfg; oldDev = dev; oldModel = model;
            oldFam = fam; oldProd = prod;
        }
        
/*        
        
        String lastFile = "";
        
        for (DecoderFile decoderFile : _decoders) {
//            jmri.LocoAddress.Protocol[] protocols = decoderFile.getSupportedProtocols();
            
            RosterEntry re = new RosterEntry();
            re.setDecoderFamily(decoderFile.getFamily());
            re.setDecoderModel(decoderFile.getModel());
            re.setId("LabelNewDecoder");
//            re.setId(Bundle.getMessage("LabelNewDecoder"));
            
            // loadDecoderFile()
            
//            Element decoderRoot = null;
            Element decoderRoot;
            try {
                int manufacturerID = Integer.parseInt(decoderFile.getMfgID());
                List<DecoderFile> decoderFiles = decoderFileMap.get(manufacturerID);
                if (decoderFiles == null) {
                    decoderFiles = new ArrayList<>();
                    decoderFileMap.put(manufacturerID, decoderFiles);
                }
                decoderFiles.add(decoderFile);
////                System.out.format("%s, %s, %s, %s, %s%n", decoderFile.getMfg(), decoderFile.getMfgID(), decoderFile.getProductID(), decoderFile.getFamily(), decoderFile.getModel());
//                System.out.format("%s, %s%n", decoderFile.getMfgID(), decoderFile.getProductID());
                if (1==0) {
//////                if ("Public-domain and DIY".equals(decoderFile.getMfg())) {
//////                    if (!decoderFile.getFamily().equals(decoderFile.getModel())) {
//                        System.out.format("%s, %s, %s, %s, %s, %s, %s, %s%n", decoderFile.getShowable().name(), decoderFile.getMfg(), decoderFile.getMfgID(), decoderFile.getDeveloperID(), decoderFile.getProductID(), decoderFile.getFamily(), decoderFile.getModel(), decoderFile.getModelElement());
                        decoderRoot = decoderFile.rootFromName(DecoderFile.fileLocation + decoderFile.getFileName());
                        if (!lastFile.equals(decoderFile.getFileName())) {
                            Element e = decoderRoot.getChild("decoder");
                            Element ef = e.getChild("family");
                            String efa = ef != null ? ef.getAttribute("name").getValue() : "--";
                            Element e2 = e.getChild("programming");
                            String e3 = e2.getChildTextTrim("mode");
                            if (e3 != null)
//                            if ("LOCONETSV2MODE".equals(e3))
                                System.out.format("%s: %s:%s, %s, %s%n", decoderFile.getFileName(), e.getName(), efa, e2.getName(), e3);
                        }
                        
                        lastFile = decoderFile.getFileName();
//////                    }
                }
            } catch (org.jdom2.JDOMException e) {
                log.error("Exception while parsing decoder XML file: " + decoderFile.getFileName(), e);
                return;
            } catch (java.io.IOException e) {
                log.error("Exception while reading decoder XML file: " + decoderFile.getFileName(), e);
                return;
            }
//            re.
            
            // note that we're leaving the filename null
            // add the new roster entry to the in-memory roster
//////            Roster.getDefault().addEntry(re);
            
//            re.
//            startProgrammer(decoderFile, re, (String) programmerBox.getSelectedItem());
        }
*/        
        
    }
    
    
    
    
    private final static Logger log = LoggerFactory.getLogger(LnNodeManager.class);
    
}
