package jmri.jmrix.loconet.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import jmri.jmrix.loconet.nodes.configurexml.DecoderList;
import jmri.InstanceManagerAutoDefault;
import jmri.NamedBean;
import jmri.NamedBean.DuplicateSystemNameException;
import jmri.beans.PropertyChangeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager of LnNodes.
 *
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LnNodeManager implements InstanceManagerAutoDefault, VetoableChangeListener, PropertyChangeProvider {

    private static DecoderList _decoderList;

    public static final int PUBLIC_DOMAIN_DIY_MANAGER_ID = 13;
    public static final String PUBLIC_DOMAIN_DIY_MANAGER = "Public-domain and DIY";

    protected final ConcurrentMap<Integer, LnNode> _lnNodesMap = new ConcurrentHashMap<>();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);


    public LnNodeManager() {
    }

    /**
     * Get the LnNode.
     *
     * @param address the address of the node
     * @return the LnNode or null if none exists
     */
    @CheckReturnValue
    @CheckForNull
    public LnNode getLnNode(int address) {
        return _lnNodesMap.get(address);
    }

    /**
     * Method for a UI to delete a LnNode.
     * <p>
     * The UI should first request a "CanDelete", this will return a list of
     * locations (and descriptions) where the bean is in use via throwing a
     * VetoException, then if that comes back clear, or the user agrees with the
     * actions, then a "DoDelete" can be called which inform the listeners to
     * delete the bean, then it will be deregistered and disposed of.
     * <p>
     * If a property name of "DoNotDelete" is thrown back in the VetoException
     * then the delete process should be aborted.
     *
     * @param lnNode   The NamedBean to be deleted
     * @param property The programmatic name of the request. "CanDelete" will
     *                 enquire with all listeners if the item can be deleted.
     *                 "DoDelete" tells the listener to delete the item
     * @throws java.beans.PropertyVetoException - If the recipients wishes the
     *                                          delete to be aborted (see
     *                                          above)
     */
    public void deleteBean(@Nonnull LnNode lnNode, @Nonnull String property)
            throws java.beans.PropertyVetoException {

        // throws PropertyVetoException if vetoed
        fireVetoableChange(property, lnNode, null);
        if (property.equals("DoDelete")) { // NOI18N
            deregister(lnNode);
            lnNode.dispose();
        }
    }

    /**
     * Remember a LnNode created outside the manager.
     *
     * @param lnNode the bean
     * @throws DuplicateSystemNameException if a different node with the same
     *                                      address is already registered in the
     *                                      manager
     */
    public void register(@Nonnull LnNode lnNode) throws DuplicateSystemNameException {
        LnNode existingBean = getLnNode(lnNode.getAddress());
        if (existingBean != null) {
            if (lnNode == existingBean) {
                log.debug("the LnNode is already registered: {}", lnNode.getAddress());
            } else {
                log.error("a different LnNode with this address is already registered: {}", lnNode.getAddress());
                throw new NamedBean.DuplicateSystemNameException(
                        "a different LnNode with this address is already registered: "
                                + Integer.toString(lnNode.getAddress()));
            }
        }

        // clear caches
//        cachedSystemNameList = null;
//        cachedNamedBeanList = null;

        // save this bean
//        _lnNodes.add(lnNode);
        _lnNodesMap.put(lnNode.getAddress(), lnNode);

        // notifications
//        int position = getPosition(lnNode);
//        fireDataListenersAdded(position, position, lnNode);
//        fireIndexedPropertyChange("beans", position, null, lnNode);
//        firePropertyChange("length", null, _lnNodes.size());
        // listen for name and state changes to forward
//        lnNode.addPropertyChangeListener(this);
    }

    public void deregister(@Nonnull LnNode lnNode) {
//        int position = getPosition(lnNode);

        // clear caches
//        cachedSystemNameList = null;
//        cachedNamedBeanList = null;

        // stop listening for user name changes
//        lnNode.removePropertyChangeListener(this);

        // remove LnNode from local storage
//        _lnNodes.remove(lnNode);
        _lnNodesMap.remove(lnNode.getAddress());

        // notifications
//        fireDataListenersRemoved(position, position, lnNode);
//        fireIndexedPropertyChange("beans", position, lnNode, null);
//        firePropertyChange("length", null, _lnNodes.size());
    }

    public DecoderList getDecoderList() {
        synchronized(LnNodeManager.class) {
            if (_decoderList == null) {
                _decoderList = new DecoderList();
            }
            return _decoderList;
        }
    }

    /**
     * Inform all registered listeners of a vetoable change. If the
     * propertyName is "CanDelete" ALL listeners with an interest in the bean
     * will throw an exception, which is recorded returned back to the invoking
     * method, so that it can be presented back to the user. However if a
     * listener decides that the bean can not be deleted then it should throw an
     * exception with a property name of "DoNotDelete", this is thrown back up
     * to the user and the delete process should be aborted.
     *
     * @param p   The programmatic name of the property that is to be changed.
     *            "CanDelete" will inquire with all listeners if the item can
     *            be deleted. "DoDelete" tells the listener to delete the item.
     * @param old The old value of the property.
     * @param n   The new value of the property.
     * @throws PropertyVetoException if the recipients wishes the delete to be
     *                               aborted.
     */
    @OverridingMethodsMustInvokeSuper
    public void fireVetoableChange(String p, Object old, Object n) throws PropertyVetoException {
        PropertyChangeEvent evt = new PropertyChangeEvent(this, p, old, n);
        if (p.equals("CanDelete")) { // NOI18N
            StringBuilder message = new StringBuilder();
            for (VetoableChangeListener vc : vetoableChangeSupport.getVetoableChangeListeners()) {
                try {
                    vc.vetoableChange(evt);
                } catch (PropertyVetoException e) {
                    if (e.getPropertyChangeEvent().getPropertyName().equals("DoNotDelete")) { // NOI18N
                        log.info("{}", e.getMessage());
                        throw e;
                    }
                    message.append(e.getMessage()).append("<hr>"); // NOI18N
                }
            }
            throw new PropertyVetoException(message.toString(), evt);
        } else {
            try {
                vetoableChangeSupport.fireVetoableChange(evt);
            } catch (PropertyVetoException e) {
                log.error("Change vetoed.", e);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @OverridingMethodsMustInvokeSuper
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {

        Collection<LnNode> nodes = _lnNodesMap.values();

        if ("CanDelete".equals(evt.getPropertyName())) { // NOI18N
            StringBuilder message = new StringBuilder();
            message.append(Bundle.getMessage("VetoFoundIn", "LnNode"))
                    .append("<ul>");
            boolean found = false;
            for (LnNode node : nodes) {
                try {
                    node.vetoableChange(evt);
                } catch (PropertyVetoException e) {
                    if (e.getPropertyChangeEvent().getPropertyName().equals("DoNotDelete")) { // NOI18N
                        throw e;
                    }
                    found = true;
                    message.append("<li>")
                            .append(e.getMessage())
                            .append("</li>");
                }
            }
            message.append("</ul>")
                    .append(Bundle.getMessage("VetoWillBeRemovedFrom", "LnNode"));
            if (found) {
                throw new PropertyVetoException(message.toString(), evt);
            }
        } else {
            for (LnNode node : nodes) {
                // throws PropertyVetoException if vetoed
                node.vetoableChange(evt);
            }
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return pcs.getPropertyChangeListeners(propertyName);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }


    private final static Logger log = LoggerFactory.getLogger(LnNodeManager.class);

}
