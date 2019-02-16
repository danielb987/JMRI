package jmri.jmrit.logixng.actions;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.Expression;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.ActionManager;
import jmri.jmrit.logixng.ExpressionManager;
import jmri.jmrit.logixng.FemaleActionSocket;
import jmri.jmrit.logixng.FemaleExpressionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleActionSocket;
import jmri.jmrit.logixng.MaleExpressionSocket;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNGManager;

/**
 * Executes an action when the expression is True.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionIfThenElse extends AbstractAction implements FemaleSocketListener {

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
    private final FemaleExpressionSocket _expressionSocket;
    private final FemaleActionSocket _actionSocket;
    
    /**
     * Create a new instance of ActionIfThen and generate a new system name.
     * @param newLogix the LogixNG that this action is related to
     */
    public ActionIfThenElse(LogixNG newLogix, Type type) {
        super(InstanceManager.getDefault(ActionManager.class).getNewSystemName(newLogix));
        _type = type;
        _expressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _actionSocket = InstanceManager.getDefault(ActionManager.class)
                .createFemaleActionSocket(this, "A1");
    }
    
    public ActionIfThenElse(String sys, Type type) {
        super(sys);
        _type = type;
        _expressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _actionSocket = InstanceManager.getDefault(ActionManager.class)
                .createFemaleActionSocket(this, "A1");
    }
    
    public ActionIfThenElse(String sys, String user, Type type) {
        super(sys, user);
        _type = type;
        _expressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _actionSocket = InstanceManager.getDefault(ActionManager.class)
                .createFemaleActionSocket(this, "A1");
    }
    
    public ActionIfThenElse(
            String sys, Type type,
            String expressionSocketName, String actionSocketName,
            MaleExpressionSocket expression, MaleActionSocket action) {
        
        super(sys);
        _type = type;
        _expressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, expressionSocketName, expression);
        _actionSocket = InstanceManager.getDefault(ActionManager.class)
                .createFemaleActionSocket(this, actionSocketName, action);
    }
    
    public ActionIfThenElse(
            String sys, String user, Type type,
            String expressionSocketName, String actionSocketName, 
            MaleExpressionSocket expression, MaleActionSocket action) {
        
        super(sys, user);
        _type = type;
        _expressionSocket = InstanceManager.getDefault(ExpressionManager.class)
                .createFemaleExpressionSocket(this, expressionSocketName, expression);
        _actionSocket = InstanceManager.getDefault(ActionManager.class)
                .createFemaleActionSocket(this, actionSocketName, action);
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
        _lastExpressionResult = _expressionSocket.evaluate();
        _lastActionResult = false;

        if (_lastExpressionResult) {
            _lastActionResult = _actionSocket.executeStart();
        }

        return _lastActionResult;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _actionSocket.executeContinue();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _expressionSocket.evaluate();
                if (exprResult) {
                    _lastActionResult = _actionSocket.executeContinue();
                } else {
                    _actionSocket.abort();
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
    public boolean executeRestart() {
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _actionSocket.executeRestart();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _expressionSocket.evaluate();
                if (exprResult) {
                    _lastActionResult = _actionSocket.executeRestart();
                } else {
                    _actionSocket.abort();
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
        _actionSocket.abort();
    }
    
    /** {@inheritDoc} */
    public Type getType() {
        return _type;
    }
    
    /** {@inheritDoc} */
    public void setType(Type type) {
        if ((_type != type) && _lastActionResult) {
            _actionSocket.abort();
        }
        _type = type;
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _expressionSocket;
                
            case 1:
                return _actionSocket;
                
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
        return Bundle.getMessage("ActionIfThen_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("ActionIfThen_Long", _expressionSocket.getName(), _actionSocket.getName());
    }

}
