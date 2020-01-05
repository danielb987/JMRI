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
     * Set the address.
     * @param address the address
     */
    public void setAddress(int address);
    
    /**
     * Get the manager that manages this item.
     * @return the manager
     */
    public Manager<? extends NamedBean> getManager();
    
}
