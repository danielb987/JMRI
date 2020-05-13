package jmri.jmrix.loconet.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.Manager;
import jmri.Sensor;
import jmri.implementation.AbstractSensor;

/**
 * An AnalogIO as a sensor on the LocoNet.
 * This is used for LnNodes to be able to have sensors that sets or reads a
 * SV value instead of sending LocoNet sensor messages.
 * <p>
 * Example use of this class is a voltage/current meter device that is placed
 * directly after a booster. The device could have a "sensor" that tells
 * whenether or not the booster outputs power to the layout, if there is a
 * short or not. And if the device have some sort of relay, it could turn the
 * power on/off to the layout.
 *
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public final class LnAnalogIO_Sensor extends AbstractSensor implements PropertyChangeListener {

    private final LnAnalogIO lnAnalogIO;
    
    
    public LnAnalogIO_Sensor(@Nonnull LnNode node, int startSVAddress, @CheckForNull String userName) {
        super(String.format("%s%d:%d",
                node.getTrafficController()
                        .getSystemConnectionMemo()
                        .getSensorManager()
                        .getSystemNamePrefix(),
                node.getAddress(),
                startSVAddress),
                userName);
        
        lnAnalogIO = new LnAnalogIO(node, startSVAddress, userName);
        
        lnAnalogIO.addPropertyChangeListener("State", this);
    }
    
    public LnAnalogIO_Sensor(@Nonnull String sysName, @CheckForNull String userName) {
        super(sysName, userName);
        
        
        String systemPrefix = Manager.getSystemPrefix(sysName);
        
        // Remove system prefix and type letter
        String name = sysName.substring(systemPrefix.length()+1);
        
        // Get LnNode address and startSV_Address
        String[] parts = name.split(":");
        
        LnNode node = InstanceManager.getDefault(LnNodeManager.class)
                .getLnNode(Integer.parseInt(parts[0]));
        
        if (node == null) {
            throw new IllegalArgumentException("LnNode "+parts[0]+" does not exist");
        }
        
        int startSVAddress = Integer.parseInt(parts[1]);
        
/*        
        String systemPrefix =
                node.getTrafficController()
                        .getSystemConnectionMemo()
                        .getSensorManager()
                        .getSystemNamePrefix();
        
        String nameWithoutPrefix = sysName.substring(systemPrefix.length());
        String[] parts = nameWithoutPrefix.split(":");
        
        if (! sysName.startsWith(systemPrefix+parts[0]+":")) {
            throw new IllegalArgumentException("System name is not valid");
        }
        
        int startSVAddress = 0;
        try {
            startSVAddress = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("System name is not valid", e);
        }
*/        
        
        lnAnalogIO = new LnAnalogIO(node, startSVAddress, userName);
        
        lnAnalogIO.addPropertyChangeListener("State", this);
    }
    
    @Override
    public void setKnownState(int newState) throws JmriException {
        if (newState == Sensor.ACTIVE) {
            lnAnalogIO.setCommandedAnalogValue(1);
        } else {
            lnAnalogIO.setCommandedAnalogValue(0);
        }
    }
/*    
    @Override
    public int getKnownState() {
        double value = lnAnalogIO_Sensor.getKnownAnalogValue();
        
        if (value != 0) {
            return Sensor.ACTIVE;
        } else {
            return Sensor.INACTIVE;
        }
    }
*/    
    @Override
    public void requestUpdateFromLayout() {
        lnAnalogIO.requestUpdateFromLayout();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        double value = lnAnalogIO.getKnownAnalogValue();
        
        if (value != 0) {
            setOwnState(Sensor.ACTIVE);
        } else {
            setOwnState(Sensor.INACTIVE);
        }
    }
    
}
