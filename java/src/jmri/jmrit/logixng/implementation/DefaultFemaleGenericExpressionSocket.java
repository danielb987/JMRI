package jmri.jmrit.logixng.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jmri.InstanceManager;
import jmri.jmrit.logixng.AnalogExpressionManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleAnalogExpressionSocket;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.FemaleGenericExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleStringExpressionSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleAnalogExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleStringExpressionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.StringExpressionManager;
import jmri.jmrit.logixng.analog.implementation.DefaultFemaleAnalogExpressionSocket;
import jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.string.implementation.DefaultFemaleStringExpressionSocket;

/**
 *
 */
public class DefaultFemaleGenericExpressionSocket
        extends AbstractFemaleSocket
        implements FemaleGenericExpressionSocket, FemaleSocketListener {

    private SocketType _socketType = SocketType.GENERIC;    // The type of the socket the user has selected
    private SocketType _currentSocketType;                  // The current type of the socket.
    private FemaleSocket _currentActiveSocket;              // The socket that is currently in use, if any. Null otherwise.
    private final FemaleAnalogExpressionSocket analogSocket = new DefaultFemaleAnalogExpressionSocket(this, this, "A");
    private final FemaleDigitalExpressionSocket digitalSocket = new DefaultFemaleDigitalExpressionSocket(this, this, "D");
    private final FemaleStringExpressionSocket stringSocket = new DefaultFemaleStringExpressionSocket(this, this, "S");
    
    public DefaultFemaleGenericExpressionSocket(Base parent, FemaleSocketListener listener, String name) {
        super(parent, listener, name);
    }
    
    public DefaultFemaleGenericExpressionSocket(
            Base parent,
            FemaleSocketListener listener,
            String name,
            MaleDigitalExpressionSocket maleSocket) {
        
        super(parent, listener, name);
        
        try {
            connect(maleSocket);
        } catch (SocketAlreadyConnectedException e) {
            // This should never be able to happen since a newly created
            // socket is not connected.
            throw new RuntimeException(e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        // Female sockets have special handling
        throw new UnsupportedOperationException();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isCompatible(MaleSocket socket) {
        return (socket instanceof MaleAnalogExpressionSocket)
                || (socket instanceof MaleDigitalExpressionSocket)
                || (socket instanceof MaleStringExpressionSocket);
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        if (isConnected()) {
            ((MaleDigitalExpressionSocket)getConnectedSocket()).initEvaluation();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluateBoolean(@Nonnull AtomicBoolean isCompleted) {
        if (isConnected()) {
            if (_currentSocketType == SocketType.DIGITAL) {
                return ((MaleDigitalExpressionSocket)getConnectedSocket()).evaluate(isCompleted);
            } else {
                return convertToBoolean(evaluateBoolean(isCompleted));
            }
        } else {
            return false;
        }
    }

    @Override
    public float evaluateFloat(@Nonnull AtomicBoolean isCompleted) {
        if (isConnected()) {
            if (_currentSocketType == SocketType.DIGITAL) {
                return ((MaleAnalogExpressionSocket)getConnectedSocket()).evaluate(isCompleted);
            } else {
                return convertToFloat(evaluateFloat(isCompleted));
            }
        } else {
            return 0.0f;
        }
    }

    @Override
    public String evaluateString(@Nonnull AtomicBoolean isCompleted) {
        if (isConnected()) {
            if (_currentSocketType == SocketType.DIGITAL) {
                return ((MaleStringExpressionSocket)getConnectedSocket()).evaluate(isCompleted);
            } else {
                return convertToString(evaluateString(isCompleted));
            }
        } else {
            return "";
        }
    }

    @Override
    @CheckForNull
    public Object evaluateGeneric(@Nonnull AtomicBoolean isCompleted) {
        if (isConnected()) {
            switch (_currentSocketType) {
                case DIGITAL:
                    return ((MaleDigitalExpressionSocket)getConnectedSocket())
                            .evaluate(isCompleted);
                    
                case ANALOG:
                    return ((MaleAnalogExpressionSocket)getConnectedSocket())
                            .evaluate(isCompleted);
                    
                case STRING:
                    return ((MaleStringExpressionSocket)getConnectedSocket())
                            .evaluate(isCompleted);
                    
                default:
                    throw new RuntimeException("_currentSocketType has invalid value: "+_currentSocketType.name());
            }
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        if (isConnected()) {
            switch (_currentSocketType) {
                case DIGITAL:
                    ((MaleDigitalExpressionSocket)getConnectedSocket()).reset();
                    
                case ANALOG:
                    ((MaleAnalogExpressionSocket)getConnectedSocket()).reset();
                    
                case STRING:
                    ((MaleStringExpressionSocket)getConnectedSocket()).reset();
                    
                default:
                    throw new RuntimeException("_currentSocketType has invalid value: "+_currentSocketType.name());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("DefaultFemaleGenericExpressionSocket_Short");
    }

    /** {@inheritDoc} */
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("DefaultFemaleGenericExpressionSocket_Long", getName());
    }

    /** {@inheritDoc} */
    @Override
    public String getExampleSystemName() {
        throw new RuntimeException("This method must be moved to the configuration of new expression");
//        return getConditionalNG().getSystemName() + ":DE10";
    }

    /** {@inheritDoc} */
    @Override
    public String getNewSystemName() {
        return InstanceManager.getDefault(DigitalExpressionManager.class)
                .getNewSystemName(getConditionalNG());
    }

    private void addClassesToMap(
            Map<Category, List<Class<? extends Base>>> destinationClasses,
            Map<Category, List<Class<? extends Base>>> sourceClasses) {
        
        for (Category category : Category.values()) {
            for (Class<? extends Base> clazz : sourceClasses.get(category)) {
                destinationClasses.get(category).add(clazz);
            }
        }
    }
    
    @Override
    public Map<Category, List<Class<? extends Base>>> getConnectableClasses() {
        Map<Category, List<Class<? extends Base>>> classes = new HashMap<>();
        
        for (Category category : Category.values()) {
            classes.put(category, new ArrayList<>());
        }
        
        addClassesToMap(classes, InstanceManager.getDefault(AnalogExpressionManager.class).getExpressionClasses());
        addClassesToMap(classes, InstanceManager.getDefault(DigitalExpressionManager.class).getExpressionClasses());
        addClassesToMap(classes, InstanceManager.getDefault(StringExpressionManager.class).getExpressionClasses());
        
        return classes;
    }

    @Override
    public void connected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean convertToBoolean(@Nullable Object value) {
        return false;
    }
    
    public float convertToFloat(@Nullable Object value) {
        return 0.0f;
    }
    
    @Nonnull
    public String convertToString(@Nullable Object value) {
        return "";
    }
    
}
