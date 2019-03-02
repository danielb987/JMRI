package jmri.jmrit.logixng.digitalactions;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;

/**
 * Executes an action when the expression is True.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class IfThen extends AbstractDigitalAction implements FemaleSocketListener {

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

    private Type _type;
    private boolean _lastExpressionResult = false;
    private boolean _lastActionResult = false;
    private final FemaleDigitalExpressionSocket _ifExpressionSocket;
    private final FemaleDigitalActionSocket _thenActionSocket;
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     */
    public IfThen(Base parent, Type type) {
        super(parent, InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(parent.getLogixNG()));
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, "E");
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, "A");
    }
    
    public IfThen(Base parent, String sys, Type type) {
        super(parent, sys);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, "E");
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, "A");
    }
    
    public IfThen(Base parent, String sys, String user, Type type) {
        super(parent, sys, user);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, "E");
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, "A");
    }
    
    public IfThen(
            Base parent,
            String sys, Type type,
            String ifExpressionSocketName,
            String thenActionSocketName,
            MaleDigitalExpressionSocket ifExpression,
            MaleDigitalActionSocket thenAction) {
        
        super(parent, sys);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, ifExpressionSocketName, ifExpression);
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, thenActionSocketName, thenAction);
    }
    
    public IfThen(
            Base parent,
            String sys, String user, Type type,
            String ifExpressionSocketName,
            String thenActionSocketName,
            MaleDigitalExpressionSocket ifExpression,
            MaleDigitalActionSocket thenAction) {
        
        super(parent, sys, user);
        _type = type;
        _ifExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, this, ifExpressionSocketName, ifExpression);
        _thenActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleActionSocket(this, this, thenActionSocketName, thenAction);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        _lastExpressionResult = _ifExpressionSocket.evaluate();
        _lastActionResult = false;

        if (_lastExpressionResult) {
            _lastActionResult = _thenActionSocket.executeStart();
        }

        return _lastActionResult;
    }

    /**
     * Continue execution of this Action.
     * This method is called if Type == TRIGGER_ACTION, the previous call to
     * one of the execute???() methods returned True and the expression is
     * still True.
     * 
     * @return true if this action is not finished.
     */
    @Override
    public boolean executeContinue() {
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _thenActionSocket.executeContinue();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _ifExpressionSocket.evaluate();
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
        
        return _lastActionResult;
    }

    /**
     * Restart the execute of this Action.
     * This method is called if Type == TRIGGER_ACTION and the expression has
     * become False and then True again.
     * 
     * If a parent action is restarted, it must restart all its children.
     * 
     * @return true if this action is not finished.
     */
    @Override
    public boolean executeRestart() {
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _thenActionSocket.executeRestart();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _ifExpressionSocket.evaluate();
                if (exprResult) {
                    _lastActionResult = _thenActionSocket.executeRestart();
                } else {
                    _thenActionSocket.abort();
                    _lastActionResult = false;
                }
                break;
                
            default:
                throw new RuntimeException(String.format("Unknown type '%s'", _type.name()));
        }
        
        return _lastActionResult;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        _thenActionSocket.abort();
    }
    
    /** {@inheritDoc} */
    public Type getType() {
        return _type;
    }
    
    /** {@inheritDoc} */
    public void setType(Type type) {
        if ((_type != type) && _lastActionResult) {
            _thenActionSocket.abort();
        }
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

}
