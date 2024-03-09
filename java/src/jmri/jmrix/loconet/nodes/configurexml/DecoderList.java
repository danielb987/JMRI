package jmri.jmrix.loconet.nodes.configurexml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jmri.InstanceManager;
import jmri.jmrit.XmlFile;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrit.decoderdefn.DecoderIndexFile;
import jmri.jmrix.loconet.nodes.LnNodeManager;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * List of decoders.
 * This class is used internally by LnNodeManager. It takes some time to load
 * this list so we don't want each LnNodeManager to have its own list.
 *
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public final class DecoderList {

    private final XmlFile _xmlFile = new XmlFile();
    private final DecoderIndexFile _decoderIndexFile;
    private final Map<Integer, String> _lnDIYDeveloperList = new HashMap<>();
    private final List<DecoderFile> _decoders = new ArrayList<>();
    private final Map<Integer, List<DecoderFile>> decoderFileMap = new HashMap<>();

    public DecoderList() {
        _decoderIndexFile = InstanceManager.getDefault(DecoderIndexFile.class);
        try {
            readLnDIYDeveloperList();
        }
        catch (JDOMException | IOException ex) {
            log.error("Cannot load LocoNet Hackers DIY SV & IPL DeveloperId List", ex);
        }
        readDecoderTypes();
    }

    private void readLnDIYDeveloperList() throws JDOMException, IOException {
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
        return _decoderIndexFile.mfgNameFromID(Integer.toString(manufacturerID));
    }

    public String getDeveloper(int developerID) {
        return _lnDIYDeveloperList.get(developerID);
    }

    public DecoderFile getProduct(int manufacturerID, int developerID, int productID) {
        String developer = manufacturerID == LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID ? Integer.toString(developerID) : null;
        List<DecoderFile> decoders = decoderFileMap.get(manufacturerID);
        for (DecoderFile decoder : decoders) {
            if (developer == null || (developer.equals(decoder.getDeveloperID()))) {
                String productIDs = decoder.getProductID();
                String[] products = productIDs.split(",");
                for (String p : products) {
                    log.debug(String.format("Manufacturer: %s, Developer: %s, ProductID: %s. Expect manufacturer: %d, developer: %d, productID: %d, num decoders: %d%n", decoder.getMfgID(), decoder.getDeveloperID(), p, manufacturerID, developerID, productID, decoders.size()));
                    if (productID == Integer.parseInt(p)) {
                        return decoder;
                    }
                }
            }
        }
        return null;
    }

    private void readDecoderTypes() {
        _decoders.clear();
        List<DecoderFile> decoders = InstanceManager.getDefault(DecoderIndexFile.class).matchingDecoderList(null, null, null, null, null, null);
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
            if (prod == null) {
                prod = "";
            }
            if (!oldMfg.equals(mfg) || !oldDev.equals(dev) || !oldModel.equals(model) || !oldFam.equals(fam) || !oldProd.equals(prod)) {
                List<DecoderFile> decoderFiles = decoderFileMap.get(mfgID);
                if (decoderFiles == null) {
                    decoderFiles = new ArrayList<>();
                    decoderFileMap.put(mfgID, decoderFiles);
                }
                decoderFiles.add(decoderFile);
                _decoders.add(decoderFile);
            }
            oldMfg = mfg;
            oldDev = dev;
            oldModel = model;
            oldFam = fam;
            oldProd = prod;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(DecoderList.class);

}
