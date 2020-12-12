package jmri.jmrix.loconet.nodes;

import jmri.Manager;
import jmri.NamedBean;

/**
 * An item on a node, for example an AnalogIO or a StringIO.
 */
public interface NodeItem {

    /**
     * Get the address.
     * @return the address
     */
    public int getAddress();
    
    /**
     * Get the start SV address.
     * @return the start SV address
     */
    public int getStartSVAddress();
    
    /**
     * Get the number of SV registers this item uses.
     * The number of SV registers depends on the configuration of this item.
     * @return the number of SVs used by this item.
     */
    public int getNumSVs();
    
    /*.*
     * Set the address.
     * @param address the address
     */
//    public void setAddress(int address);
    
    /**
     * Get the manager that manages this item.
     * @return the manager
     */
    public Manager<? extends NamedBean> getManager();
    
}
