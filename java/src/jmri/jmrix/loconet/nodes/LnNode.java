package jmri.jmrix.loconet.nodes;

import jmri.jmrix.loconet.LnTrafficController;

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
    private int _serialNumber = 0;
    private String _name;
    
//    private final LnTrafficController _tc;
    
    public LnNode(int address, LnTrafficController tc) {
        _address = address;
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
    
}
