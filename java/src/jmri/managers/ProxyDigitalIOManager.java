package jmri.managers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;

import jmri.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a DigitalIOManager that can serve as a proxy for multiple
 * system-specific implementations.
 *
 * @author	Bob Jacobsen Copyright (C) 2003, 2010
 */
public class ProxyDigitalIOManager extends AbstractProxyManager<DigitalIO> implements DigitalIOManager {

    public ProxyDigitalIOManager() {
        super();
    }

    @Override
    protected AbstractManager<DigitalIO> makeInternalManager() {
        return null;
//        return jmri.InstanceManager.getDefault(jmri.jmrix.internal.InternalSystemConnectionMemo.class).getDigitalIOManager();
    }

    /**
     * Revise superclass behavior: support DigitalIOOperations
     */
    @Override
    public void addManager(Manager<DigitalIO> m) {
        super.addManager(m);
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
    protected DigitalIO makeBean(int i, String systemName, String userName) {
        return ((DigitalIOManager) getMgr(i)).newDigitalIO(systemName, userName);
    }

    @Override
    public DigitalIO provideDigitalIO(String name) throws IllegalArgumentException {
        return super.provideNamedBean(name);
    }

    @Override
    /** {@inheritDoc} */
    public DigitalIO provide(@Nonnull String name) throws IllegalArgumentException { return provideDigitalIO(name); }

    /**
     * Locate an instance based on a system name. Returns null if no instance
     * already exists.
     *
     * @return requested DigitalIO object or null if none exists
     */
    @Override
    public DigitalIO getBySystemName(String systemName) {
        return super.getBeanBySystemName(systemName);
    }

    /**
     * Locate an instance based on a user name. Returns null if no instance
     * already exists.
     *
     * @return requested DigitalIO object or null if none exists
     */
    @Override
    public DigitalIO getByUserName(String userName) {
        return super.getBeanByUserName(userName);
    }

    /**
     * Return an instance with the specified system and user names. Note that
     * two calls with the same arguments will get the same instance; there is
     * only one Sensor object representing a given physical turnout and
     * therefore only one with a specific system or user name.
     * <P>
     * This will always return a valid object reference for a valid request; a
     * new object will be created if necessary. In that case:
     * <UL>
     * <LI>If a null reference is given for user name, no user name will be
     * associated with the DigitalIO object created; a valid system name must be
     * provided
     * <LI>If a null reference is given for the system name, a system name will
     * _somehow_ be inferred from the user name. How this is done is system
     * specific. Note: a future extension of this interface will add an
     * exception to signal that this was not possible.
     * <LI>If both names are provided, the system name defines the hardware
     * access of the desired turnout, and the user address is associated with
     * it.
     * </UL>
     * Note that it is possible to make an inconsistent request if both
     * addresses are provided, but the given values are associated with
     * different objects. This is a problem, and we don't have a good solution
     * except to issue warnings. This will mostly happen if you're creating
     * Sensors when you should be looking them up.
     *
     * @return requested DigitalIO object (never null)
     */
    @Override
    public DigitalIO newDigitalIO(String systemName, String userName) {
        return newNamedBean(systemName, userName);
    }

    @Override
    public boolean allowMultipleAdditions(String systemName) {
        int i = matchTentative(systemName);
        if (i >= 0) {
            return ((DigitalIOManager) getMgr(i)).allowMultipleAdditions(systemName);
        }
        return ((DigitalIOManager) getMgr(0)).allowMultipleAdditions(systemName);
    }

    @Override
    public String createSystemName(String curAddress, String prefix) throws jmri.JmriException {
        for (int i = 0; i < nMgrs(); i++) {
            if (prefix.equals(
                    ((DigitalIOManager) getMgr(i)).getSystemPrefix())) {
                try {
                    return ((DigitalIOManager) getMgr(i)).createSystemName(curAddress, prefix);
                } catch (jmri.JmriException ex) {
                    throw ex;
                }
            }
        }
        throw new jmri.JmriException("DigitalIO Manager could not be found for System Prefix " + prefix);
    }

    /**
     * Validate system name format. Locate a system specfic DigitalIOManager based on a system name.
     *
     * @return if a manager is found, return its determination of validity of
     * system name format. Return INVALID if no manager exists.
     */
    @Override
    public NameValidity validSystemNameFormat(String systemName) {
        int i = matchTentative(systemName);
        if (i >= 0) {
            return ((DigitalIOManager) getMgr(i)).validSystemNameFormat(systemName);
        }
        return NameValidity.INVALID;
    }

    @Override
    public String getNextValidAddress(String curAddress, String prefix) throws jmri.JmriException {
        for (int i = 0; i < nMgrs(); i++) {
            if (prefix.equals(
                    ((DigitalIOManager) getMgr(i)).getSystemPrefix())) {
                try {
                    return ((DigitalIOManager) getMgr(i)).getNextValidAddress(curAddress, prefix);
                } catch (jmri.JmriException ex) {
                    throw ex;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntryToolTip() {
        String entryToolTip = "Enter a number from 1 to 9999"; // Basic number format help
        return entryToolTip;
    }

    @Override
    public int getXMLOrder() {
        return jmri.Manager.TURNOUTS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameDigitalIO");
    }

    // initialize logging
    private final static Logger log = LoggerFactory.getLogger(ProxyDigitalIOManager.class);

}
