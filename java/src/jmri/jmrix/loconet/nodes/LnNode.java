package jmri.jmrix.loconet.nodes;

import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;

/**
 * A node on the LocoNet bus.
 * The node is defined by its address. If the user wants to move the node to
 * a new address, JMRI needs to copy the node to a new node and remove the
 * old node.
 * <p>
 * A node may have a number of AnalogIO and StringIO beans.
 */
public class LnNode {

    private final int _address;
    private int _manufacturerID = 0;
    private int _developerID = 0;
    private int _productID = 0;
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
        _manufacturerID = contents.getSv2ManufacturerID();
        _developerID = contents.getSv2DeveloperID();
        _productID = contents.getSv2ProductID();
        _serialNumber = contents.getSv2SerialNum();
        _address = contents.getDestAddr();
//        _tc = _lm.getLnTrafficController();
    }
    
    public int getAddress() {
        return _address;
    }
    
    public void setManufacturerID(int id) {
        _manufacturerID = id;
    }
    
    public int getManufacturerID() {
        return _manufacturerID;
    }
    
    public void setDeveloperID(int id) {
        _developerID = id;
    }
    
    public int getDeveloperID() {
        return _developerID;
    }
    
    public void setProductID(int id) {
        _productID = id;
    }
    
    public int getProductID() {
        return _productID;
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
    
}
