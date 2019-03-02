package jmri.jmrit.logixng.digitalexpressions;

import jmri.Sensor;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

    // For this expression to work, the LogixNG engine needs to notify those
    // who wants to be notified when all LogixNGs are finished running.
    
    // Enum Trigger: ON_TRUE, ON_FALSE, ON_CHANGE
    // Put this enum in Expression since it may be used by many expressions

/**
 * An expression that buffers the changes of a sensor.
 * 
 * A sensor, like an optical sensor looking at wheel axles or a button switch
 * may be "on" or "off" a very short time and in some cases that time might be
 * too short. This expression remembers the new state of a sensor and triggers
 * the LogixNGs again.
 * 
 * Each time a change of the sensor is notified which this expression should
 * trigger on, this expression notifies the LogixNGManager that it should
 * run all LogixNGs again. If LogixNGManager is already running the LogixNGs,
 * the expression waits to notify the LogixNGManager until it's done.
 * 
 * The expression remembers the state of the sensor until the LogixNGs has been
 * run.
 * 
 * @author Daniel Bergqvist 2018
 */
public class BufferedSensor extends AbstractDigitalExpression {

//    private final Sensor sensor;
    
    public BufferedSensor(Base parent, String sys) throws BadUserNameException, BadSystemNameException {
        super(parent, sys);
//        sensor = null;
    }

    public BufferedSensor(Base parent, String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(parent, sys, user);
//        sensor = null;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    public boolean evaluate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
