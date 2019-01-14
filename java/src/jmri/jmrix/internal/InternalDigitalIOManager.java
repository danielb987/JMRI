package jmri.jmrix.internal;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import jmri.DigitalIO;
import jmri.DigitalIOManager;
import jmri.Manager;
import jmri.managers.AbstractManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement a digitalio manager for "Internal" (virtual) digitalio:s.
 *
 * @author	Bob Jacobsen Copyright (C) 2001, 2003, 2006
 * @author	Daniel Bergqvist Copyright (C) 2018
 */
public class InternalDigitalIOManager extends AbstractManager<DigitalIO> implements DigitalIOManager {

    /** {@inheritDoc} */
    @Override
    public int getXMLOrder() {
        // This manager cannot create a new DigitalIO so this value should
        // never be used.
        return Manager.SENSORS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char typeLetter() {
        // This manager cannot create a new DigitalIO so this value should
        // never be used.
        return 'D';
    }

    @Override
    public DigitalIO provideDigitalIO(String name) throws IllegalArgumentException {
        throw new IllegalArgumentException("DigitalIO cannot be created directly. Create a turnout, sensor, etc, instead.");
    }

    /** {@inheritDoc} */
    @Override
    public DigitalIO getDigitalIO(String name) {
        // This manager cannot create a DigitalIO so there will never be any
        // DigitalIO to return.
        return null;
    }

    /**
     * Locate an existing instance based on a system name.
     * This manager cannot have any NamedBeans so it always returns null.
     *
     * @param systemName System Name of the required NamedBean
     * @return requested NamedBean object or null if none exists
     * @throws IllegalArgumentException if provided name is invalid
     */
    @Override
    public DigitalIO getBeanBySystemName(String systemName) {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public DigitalIO getBySystemName(String key) {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * Forces upper case and trims leading and trailing whitespace.
     * Does not check for valid prefix, hence doesn't throw NamedBean.BadSystemNameException.
     */
    @CheckReturnValue
    @Override
    public @Nonnull
    String normalizeSystemName(@Nonnull String inputName) {
        // does not check for valid prefix, hence doesn't throw NamedBean.BadSystemNameException
        return inputName.toUpperCase().trim();
    }

    /** {@inheritDoc} */
    @Override
    protected DigitalIO getInstanceBySystemName(String systemName) {
        return getBySystemName(systemName);
    }

    /** {@inheritDoc} */
    @Override
    public DigitalIO getByUserName(String key) {
        return _tuser.get(key);
    }

    /** {@inheritDoc} */
    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameDigitalIO");
    }

    public InternalDigitalIOManager() {
        log.debug("InternalDigitalIOManager constructed");
    }
    
    public InternalDigitalIOManager(String prefix) {
        log.debug("InternalDigitalIOManager constructed");
        this.prefix = prefix;
    }
    
    protected String prefix = "I";

    /** {@inheritDoc} */
    @Override
    public NameValidity validSystemNameFormat(String systemName) {
        return NameValidity.VALID;
    }

    /** {@inheritDoc} */
    @Override
    public String getEntryToolTip() {
        String entryToolTip = Bundle.getMessage("AddInputEntryToolTip");
        return entryToolTip;
    }

    /** {@inheritDoc} */
    @Override
    public String getSystemPrefix() {
        return prefix;
    }

    private final static Logger log = LoggerFactory.getLogger(InternalDigitalIOManager.class);

}
