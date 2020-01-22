package jmri.jmrix.loconet.nodes;

import jmri.InstanceManager;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A node on the LocoNet bus.
 * The node is defined by its address. If the user wants to move the node to
 * a new address, JMRI needs to copy the node to a new node and remove the
 * old node.
 * <p>
 * A node may have a number of AnalogIO and StringIO beans.
 */
public class LnNode {

    private final LnNodeManager _lnNodeManager;
    private final int _address;
    private int _manufacturerID = 0;
    private String _manufacturer = "";
    private int _developerID = 0;
    private String _developer = "";
    private int _productID = 0;
    private String _product = "";
    private int _serialNumber = 0;
    private String _name;
    
//    private final LnTrafficController _tc;
    
    /**
     * Create a LnNode with an address.
     * 
     * @param address the address of the node
     * @param tc the traffic controller for the LocoNet.
     */
    public LnNode(int address, LnTrafficController tc) {
        _lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        _address = address;
//        _tc = _lm.getLnTrafficController();
    }
    
    /**
     * Create a LnNode with a LocoNet SV2 discover response packet.
     * 
     * @param contents the LocoNet SV2 discover response packet
     * @param tc the traffic controller for the LocoNet.
     */
    public LnNode(LnSv2MessageContents contents, LnTrafficController tc) {
        _lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        setManufacturerID(contents.getSv2ManufacturerID());
        setDeveloperID(contents.getSv2DeveloperID());
        setProductID(contents.getSv2ProductID());
        _serialNumber = contents.getSv2SerialNum();
        _address = contents.getDestAddr();
//        _tc = _lm.getLnTrafficController();
        
        log.debug(String.format("LnNode: %d, %d, %d%n", contents.getSv2ManufacturerID(), contents.getSv2DeveloperID(), contents.getSv2ProductID()));
    }
    
    public int getAddress() {
        return _address;
    }
    
    public final void setManufacturerID(int id) {
        _manufacturerID = id;
        _manufacturer = _lnNodeManager.getManufacturer(_manufacturerID);
        
        // Ensure that the _developer field is up to date
        setDeveloperID(_developerID);
    }
    
    public int getManufacturerID() {
        return _manufacturerID;
    }
    
    public String getManufacturer() {
        return _manufacturer;
    }
    
    public final void setDeveloperID(int id) {
        _developerID = id;
        if (_manufacturerID == LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID) {
            _developer = _lnNodeManager.getDeveloper(_developerID);
        } else {
            _developer = "";
        }
    }
    
    public int getDeveloperID() {
        return _developerID;
    }
    
    public String getDeveloper() {
        return _developer;
    }
    
    public final void setProductID(int id) {
        _productID = id;
        DecoderFile product =
                _lnNodeManager.getProduct(_manufacturerID, _developerID, _productID);
        if (product != null) {
            _product = product.getModel();
        } else {
            _product = "Unknown. ProductID: " + Integer.toString(id);
        }
    }
    
    public int getProductID() {
        return _productID;
    }
    
    public String getProduct() {
        return _product;
    }
    
    public void setSerialNumber(int serialNumber) {
        _serialNumber = serialNumber;
    }
    
    public int getSerialNumber() {
        return _serialNumber;
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public String getName() {
        return _name;
    }
    
    @Override
    public String toString() {
        return String.format("LnNode: Manufacturer ID: %d, Developer ID: %d, Product ID: %d, Serial number: %d, Address: %d, Name: %s",
                _manufacturerID,
                _developerID,
                _productID,
                _serialNumber,
                _address,
                _name);
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(LnNode.class);
    
}
