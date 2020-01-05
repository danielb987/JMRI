package jmri.jmrix.loconet.nodes;

import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.LnTrafficController;

/**
 * A node on the LocoNet bus.
 * A node has an address and may have a number of AnalogIO and StringIO beans.
 */
public class LocoNetNode {

    private int _address;
    private int _manufacturerID = 0;
    private int _developerID = 0;
    private int _serialNumber = 0;
    private String _name;
    
//    private final LocoNetSystemConnectionMemo _lm;
//    private final LnTrafficController _tc;
    
    public LocoNetNode(int address, LocoNetSystemConnectionMemo lm) {
        _address = address;
//        _lm = lm;
//        _tc = _lm.getLnTrafficController();
    }
    
    public void setAddress(int address) {
        _address = address;
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
