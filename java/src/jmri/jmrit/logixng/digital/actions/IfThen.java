package jmri.jmrit.logixng.digital.actions;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.DigitalActionWithEnableExecution;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes an action when the expression is True.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class IfThen extends AbstractDigitalAction
        implements FemaleSocketListener, DigitalActionWithEnableExecution {

    /**
     * The type of Action. If the type is changed, the action is aborted if it
     * is currently running.
     */
    public enum Type {
        /**
         * Action is triggered when the expression is True. The action may
         * continue even if the expression becomes False.
         * 
         * If the expression is False and then True again before the action
         * is finished, action.executeAgain() is called instead of action.execute().
         * 
         * Note that in a tree of actions, some actions may have been finished
         * and some actions still running. In this case, the actions that are
         * still running will be called with executeAgain() but those actions
         * that are finished will be called with execute(). Actions that have
         * child actions need to deal with this.
         */
        TRIGGER_ACTION,
        
        /**
         * Action is executed when the expression is True but only as long as
         * the expression stays True. If the expression becomes False, the
         * action is aborted.
         */
        CONTINOUS_ACTION,
    }

    private IfThen _template;
    private boolean _enableExecution;
    private Type _type;
    private boolean _lastExpressionResult = false;
    private String _ifExpressionSocketSystemName;
    private String _thenActionSocketSystemName;
    private final FemaleDigitalExpressionSocket _ifExpressionSocket;
    private final FemaleDigitalActionSocket _thenActionSocket;
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     */
    public IfThen(Type type) {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName());
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E");
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleSocket(this, this, "A");
    }
    
    public IfThen(String sys, Type type) {
        super(sys);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E");
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleSocket(this, this, "A");
    }
    
    public IfThen(String sys, String user, Type type) {
        super(sys, user);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E");
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleSocket(this, this, "A");
    }
/*    
    public IfThen(
            String sys, Type type,
            String ifExpressionSocketName,
            String thenActionSocketName,
            MaleDigitalExpressionSocket ifExpression,
            MaleDigitalActionSocket thenAction) {
        
        super(sys);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, ifExpressionSocketName, ifExpression);
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, thenActionSocketName, thenAction);
    }
    
    public IfThen(
            String sys, String user, Type type,
            String ifExpressionSocketName,
            String thenActionSocketName,
            MaleDigitalExpressionSocket ifExpression,
            MaleDigitalActionSocket thenAction) {
        
        super(sys, user);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, ifExpressionSocketName, ifExpression);
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, thenActionSocketName, thenAction);
    }
*/    
    private IfThen(IfThen template, String sys) {
        super(sys);
        _template = template;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, _template._ifExpressionSocket.getName());
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleSocket(this, this, _template._thenActionSocket.getName());
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new IfThen(this, sys);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsEnableExecution() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setEnableExecution(boolean b) {
        _enableExecution = b;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExecutionEnabled() {
        return _enableExecution;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    @Override
    public void evaluateOnly() {
        _lastExpressionResult = _ifExpressionSocket.evaluate();
    }
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        _lastExpressionResult = _ifExpressionSocket.evaluate();

        if (_lastExpressionResult) {
            _thenActionSocket.execute();
        }
    }

    /*.*
     * Continue execution of this Action.
     * This method is called if Type == TRIGGER_ACTION, the previous call to
     * one of the execute???() methods returned True and the expression is
     * still True.
     * 
     * @return true if this action is not finished.
     *./
    @Override
    public boolean executeContinue() {
        _isExpressionCompleted.set(true);
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _thenActionSocket.executeContinue();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _ifExpressionSocket.evaluate(_isExpressionCompleted);
                if (exprResult) {
                    _lastActionResult = _thenActionSocket.executeContinue();
                } else {
                    _thenActionSocket.abort();
                    _lastActionResult = false;
                }
                break;
                
            default:
                throw new RuntimeException(String.format("Unknown type '%s'", _type.name()));
        }
        
        return _lastActionResult || !_isExpressionCompleted.get();
    }
*/
    /**
     * Get the type.
     */
    public Type getType() {
        return _type;
    }
    
    /**
     * Set the type.
     */
    public void setType(Type type) {
        _type = type;
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _ifExpressionSocket;
                
            case 1:
                return _thenActionSocket;
                
            default:
                throw new IllegalArgumentException(
                        String.format("index has invalid value: %d", index));
        }
    }

    @Override
    public int getChildCount() {
        return 2;
    }

    @Override
    public void connected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("IfThen_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("IfThen_Long", _ifExpressionSocket.getName(), _thenActionSocket.getName());
    }

    public void setIfExpressionSocketSystemName(String systemName) {
        _ifExpressionSocketSystemName = systemName;
    }

    public String getIfExpressionSocketSystemName() {
        return _ifExpressionSocketSystemName;
    }

    public void setThenActionSocketSystemName(String systemName) {
        _thenActionSocketSystemName = systemName;
    }

    public String getThenExpressionSocketSystemName() {
        return _thenActionSocketSystemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ((!_ifExpressionSocket.isConnected()) && (_ifExpressionSocketSystemName != null)) {
                MaleSocket maleSocket =
                        InstanceManager.getDefault(DigitalExpressionManager.class)
                                .getBeanBySystemName(_ifExpressionSocketSystemName);
                if (maleSocket != null) {
                    _ifExpressionSocket.connect(maleSocket);
                    maleSocket.setup();
                } else {
                    log.error("cannot load digital expression " + _ifExpressionSocketSystemName);
                }
            }
            if ((!_thenActionSocket.isConnected()) && (_thenActionSocketSystemName != null)) {
                MaleSocket maleSocket =
                        InstanceManager.getDefault(DigitalActionManager.class)
                                .getBeanBySystemName(_thenActionSocketSystemName);
                if (maleSocket != null) {
                    _thenActionSocket.connect(maleSocket);
                    maleSocket.setup();
                } else {
                    log.error("cannot load digital action " + _thenActionSocketSystemName);
                }
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }

    private final static Logger log = LoggerFactory.getLogger(IfThen.class);

}
