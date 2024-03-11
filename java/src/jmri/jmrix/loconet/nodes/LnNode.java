package jmri.jmrix.loconet.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.IOException;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import jmri.InstanceManager;
import jmri.beans.PropertyChangeProvider;
import jmri.jmrit.decoderdefn.DecoderFile;
import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetInterface;
import jmri.jmrix.loconet.lnsvf2.LnSv2MessageContents;
import jmri.jmrix.loconet.nodes.configurexml.LnNodeDecoderDefinition;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A node on the LocoNet bus.
 * The node is defined by its address. If the user wants to move the node to
 * a new address, JMRI needs to copy the node to a new node and remove the
 * old node.
 * <p>
 * A node may have a number of AnalogIO and StringIO beans.
 *
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class LnNode implements PropertyChangeProvider, VetoableChangeListener {

    private final LnNodeManager _lnNodeManager;
    private final int _address;
    private final int _manufacturerID;
    private String _manufacturer = "";
    private final int _developerID;
    private String _developer = "";
    private final int _productID;
    private String _product = "";
    private int _serialNumber = 0;
    private String _name;
    private DecoderFile _decoderFile;
    private boolean _decoderFileIsMissing = false;

    private final LocoNetInterface _tc;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Create a LnNode with an address.
     *
     * @param address the address of the node
     * @param manufacturerID
     * @param developerID
     * @param productID
     * @param tc the traffic controller for the LocoNet.
     */
    public LnNode(
            int address,
            int manufacturerID,
            int developerID,
            int productID,
            LocoNetInterface tc) {
        _lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        _address = address;
        _tc = tc;
        this._manufacturerID = manufacturerID;
        this._developerID = developerID;
        this._productID = productID;
    }

    /**
     * Create a LnNode with a LocoNet SV2 discover response packet.
     *
     * @param contents the LocoNet SV2 discover response packet
     * @param tc the traffic controller for the LocoNet.
     */
    public LnNode(LnSv2MessageContents contents, LnTrafficController tc) {
        _lnNodeManager = InstanceManager.getDefault(LnNodeManager.class);
        _manufacturerID = contents.getSv2ManufacturerID();
        _developerID = contents.getSv2DeveloperID();
        _productID = contents.getSv2ProductID();
        _serialNumber = contents.getSv2SerialNum();
        _address = contents.getDestAddr();
        _tc = tc;

        log.debug("LnNode: {}, {}, {}", contents.getSv2ManufacturerID(), contents.getSv2DeveloperID(), contents.getSv2ProductID());
    }

    /**
     * Create the NamedBeans that are defined in the decoder definition of this LnNode.
     */
    public void createNamedBeans() {
        try {
            LnNodeDecoderDefinition.createNamedBeans(this);
        } catch (IOException | JDOMException e) {
            log.error("cannot create named beans for LnNode {}", _name);
        }
    }

    public LocoNetInterface getTrafficController() {
        return _tc;
    }

    public int getAddress() {
        return _address;
    }

    public int getManufacturerID() {
        return _manufacturerID;
    }

    public String getManufacturer() {
        // No need to thread safe this. Once DecoderList has loaded the list of
        // manufacturers, that list is not supposed to be changed. So no problem
        // if we set _manufacturer twice due to concurrency.
        if (_manufacturer != null) {
            _manufacturer = _lnNodeManager.getDecoderList().getManufacturer(_manufacturerID);
        }
        return _manufacturer;
    }

    public int getDeveloperID() {
        return _developerID;
    }

    public String getDeveloper() {
        // No need to thread safe this. Once DecoderList has loaded the list of
        // developers, that list is not supposed to be changed. So no problem if
        // we set _developer twice due to concurrency.
        if (_developer != null) {
            if (_manufacturerID == LnNodeManager.PUBLIC_DOMAIN_DIY_MANAGER_ID) {
                _developer = _lnNodeManager.getDecoderList().getDeveloper(_developerID);
            } else {
                _developer = "";
            }
        }
        return _developer;
    }

    public int getProductID() {
        return _productID;
    }

    public String getProduct() {
        // No need to thread safe this. Once DecoderList has loaded the list of
        // products, that list is not supposed to be changed. So no problem if
        // we set _product twice due to concurrency.
        if (_product != null) {
//            _decoderFile = _lnNodeManager.getDecoderList()
//                    .getProduct(_manufacturerID, _developerID, _productID);
            // Ensure we have tried to load the decoder file
            DecoderFile df = getDecoderFile();
            if (df != null) {
                _product = df.getModel();
            } else {
                _product = "Unknown. ProductID: " + Integer.toString(_productID);
            }
        }
        return _product;
    }

    public DecoderFile getDecoderFile() {
        synchronized(this) {
            if (_decoderFile == null && !_decoderFileIsMissing) {
                _decoderFile = _lnNodeManager.getDecoderList()
                        .getProduct(_manufacturerID, _developerID, _productID);
                if (_decoderFile == null) _decoderFileIsMissing = true;
            }
        }
        return _decoderFile;
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
/*
    private void createStringIOsFromDecoderDefinition(Element stringIOsElement) {
        for (Element e : stringIOsElement.getChildren("stringio")) {

        }
    }

    private void createAnalogIOsFromDecoderDefinition(Element analogIOsElement) {
        for (Element e : analogIOsElement.getChildren("analogio")) {

        }
    }
*/
    /*.*
     * Reads the decoder definition and creates AnalogIOs and StringIOs as
     * defined in the decoder definition.
     *./
    public void createNamedBeansFromDecoderDefinition() {
        // Ensure we have tried to load the decoder file
        DecoderFile df = getDecoderFile();
        if (df != null) {
            Element decoderRoot;
            try {
                decoderRoot = df.rootFromName(DecoderFile.fileLocation + df.getFileName());
            } catch (JDOMException | IOException e) {
                log.error("Exception while loading decoder XML file: {}", df.getFileName(), e);
                return;
            }

            Element e = decoderRoot.getChild("loconet-node");
            createStringIOsFromDecoderDefinition(e.getChild("stringios"));
            createAnalogIOsFromDecoderDefinition(e.getChild("analogios"));
        } else {
            throw new NullPointerException("_decoderFile is null");
//            JOptionPane.showMessageDialog(null,
//                    Bundle.getMessage("ErrorNoDecoderFile"),
//                    jmri.Application.getApplicationName(),
//                    JOptionPane.ERROR_MESSAGE);
        }
    }
*/
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

    @OverridingMethodsMustInvokeSuper
    public void dispose() {
        PropertyChangeListener[] listeners = pcs.getPropertyChangeListeners();
        for (PropertyChangeListener l : listeners) {
            pcs.removePropertyChangeListener(l);
//            register.remove(l);
//            listenerRefs.remove(l);
        }
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        // Before this node gets deleted, all StringIOs and AnalogIOs and other beans
        // attached to this node must be deleted.
        throw new UnsupportedOperationException("Not supported yet.");
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

    private final static Logger log = LoggerFactory.getLogger(LnNode.class);

}
