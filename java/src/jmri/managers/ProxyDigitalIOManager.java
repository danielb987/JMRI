package jmri.managers;

import javax.annotation.Nonnull;

import jmri.DigitalIO;
import jmri.DigitalIOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a DigitalIOManager that can serves as a proxy for multiple
 * system-specific implementations.
 *
 * @author	Bob Jacobsen Copyright (C) 2003, 2010
 * @author	Daniel Bergqvist Copyright (C) 2018
 */
public class ProxyDigitalIOManager extends AbstractProxyManager<DigitalIO>
        implements DigitalIOManager {

    public ProxyDigitalIOManager() {
        super();
    }

    @Override
    protected AbstractManager<DigitalIO> makeInternalManager() {
        return jmri.InstanceManager.getDefault(jmri.jmrix.internal.InternalSystemConnectionMemo.class).getDigitalIOManager();
    }

    /**
     * Locate via user name, then system name if needed.
     *
     * @return Null if nothing by that name exists
     */
    @Override
    public DigitalIO getDigitalIO(String name) {
        return super.getNamedBean(name);
    }

    @Override
    protected DigitalIO makeBean(int i, String systemName, String userName) throws IllegalArgumentException {
        throw new IllegalArgumentException("DigitalIO cannot be created directly. Create a turnout, sensor, etc, instead.");
    }

    @Override
    public DigitalIO provideDigitalIO(String sName) throws IllegalArgumentException {
        return super.provideNamedBean(sName);
    }

    @Override
    /** {@inheritDoc} */
    public DigitalIO provide(@Nonnull String name) throws IllegalArgumentException { return provideDigitalIO(name); }

    /**
     * Locate an instance based on a system name. Returns null if no instance
     * already exists.
     *
     * @return requested Turnout object or null if none exists
     */
    @Override
    public DigitalIO getBySystemName(String sName) {
        return super.getBeanBySystemName(sName);
    }

    /**
     * Locate an instance based on a user name. Returns null if no instance
     * already exists.
     *
     * @return requested Turnout object or null if none exists
     */
    @Override
    public DigitalIO getByUserName(String userName) {
        return super.getBeanByUserName(userName);
    }

    /**
     * Validate system name format.
     * A DigitalIO cannot be created directly so no system names are valid.
     * Use a turnout manager, sensor manager, and so on, to create a DigitalIO.
     *
     * @return INVALID since this manager cannot create a new DigitalIO
     */
    @Override
    public NameValidity validSystemNameFormat(String systemName) {
        return NameValidity.INVALID;
    }

    /**
     * Provide a connection system agnostic tooltip for the Add new item beantable pane.
     */
    @Override
    public String getEntryToolTip() {
        String entryToolTip = "Enter a number from 1 to 9999"; // Basic number format help
        return entryToolTip;
    }
    
    @Override
    public int getXMLOrder() {
        return jmri.Manager.SENSORS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameSensor");
    }

    // initialize logging
    private final static Logger log = LoggerFactory.getLogger(ProxyDigitalIOManager.class);

}
