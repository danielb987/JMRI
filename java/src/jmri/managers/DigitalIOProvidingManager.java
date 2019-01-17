package jmri.managers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import jmri.DigitalIO;
import jmri.NamedBean;
import jmri.ProvidingManager;
import jmri.Turnout;

/**
 * A manager that provides NamedBeans that extends DigitalIO. It is used by
 * managers that provides DigitalIO:s to tell the ProxyDigitalIO manager about
 * it.
 *
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under the
 * terms of version 2 of the GNU General Public License as published by the Free
 * Software Foundation. See the "COPYING" file for a copy of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 */
public interface DigitalIOProvidingManager extends ProvidingManager<DigitalIO> {

    /**
     * Get the Sensor with the user name, then system name if needed; if that fails, create a
     * new Sensor. 
     * If the name is a valid system name, it will be used for the new Sensor.
     * Otherwise, the {@link Manager#makeSystemName} method will attempt to turn it
     * into a valid system name.
     *
     * @param name User name, system name, or address which can be promoted to
     *             system name
     * @return Never null
     * @throws IllegalArgumentException if Sensor doesn't already exist and the
     *                                  manager cannot create the Sensor due to
     *                                  an illegal name or name that can't
     *                                  be parsed.
     */
    @Nonnull
    public DigitalIO provideDigitalIO(@Nonnull String name) throws IllegalArgumentException;

    @Override
    /** {@inheritDoc} */
    default public DigitalIO provide(@Nonnull String name) throws IllegalArgumentException { return provideDigitalIO(name); }

    /**
     * Get an existing DigitalIO or return null if it doesn't exist. 
     * 
     * Locates via user name, then system name if needed.
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getDigitalIO(@Nonnull String name);

    // to free resources when no longer used
    @Override
    public void dispose();

    /** {@inheritDoc} */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getByUserName(@Nonnull String s);

    /** {@inheritDoc} */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getBySystemName(@Nonnull String s);

    /**
     * Requests status of all layout DigitalIOs under this DigitalIO Manager. This
     * method may be invoked whenever the status of sensors needs to be updated
     * from the layout, for example, when an XML configuration file is read in.
     * Note that there is a null implementation of this method in
     * AbstractDigitalIOManager. This method only needs be implemented in
     * system-specific DigitalIO Managers where readout of sensor status from the
     * layout is possible.
     */
    public void updateAll();

    /**
     * Provide a manager-specific tooltip for the Add new item beantable pane.
     */
    @Override
    public String getEntryToolTip();












    public class Test implements ProvidingManager<Turnout> {

        public DigitalIOProvidingManager testing() {
            return this;
        }
        
        @Override
        public Turnout provide(String name) throws IllegalArgumentException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getSystemPrefix() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public char typeLetter() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String makeSystemName(String s) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public NameValidity validSystemNameFormat(String systemName) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void dispose() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getObjectCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String[] getSystemNameArray() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<String> getSystemNameList() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<Turnout> getNamedBeanList() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public SortedSet<Turnout> getNamedBeanSet() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Turnout getBeanBySystemName(String systemName) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Turnout getBeanByUserName(String userName) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Turnout getNamedBean(String name) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener l) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener l) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addVetoableChangeListener(VetoableChangeListener l) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void removeVetoableChangeListener(VetoableChangeListener l) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void deleteBean(Turnout n, String property) throws PropertyVetoException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void register(Turnout n) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void deregister(Turnout n) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getXMLOrder() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getBeanTypeHandled() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String normalizeSystemName(String inputName) throws NamedBean.BadSystemNameException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addDataListener(ManagerDataListener<Turnout> e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void removeDataListener(ManagerDataListener<Turnout> e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
