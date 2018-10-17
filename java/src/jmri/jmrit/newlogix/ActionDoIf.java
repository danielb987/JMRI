package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.Action;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractAction;

/**
 * Executes an action when the expression is True.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionDoIf extends AbstractAction {

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
    private final Expression _expression;
    private final Action _action;
    private boolean _lastExpressionResult = false;
    private boolean _lastActionResult = false;
    
    public ActionDoIf(String sys, Type type, Expression expression, Action action) {
        super(sys);
        _type = type;
        _expression = expression;
        _action = action;
    }
    
    public ActionDoIf(String sys, String user, Type type, Expression expression, Action action) {
        super(sys, user);
        _type = type;
        _expression = expression;
        _action = action;
    }
    
    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        _lastExpressionResult = _expression.evaluate();
        _lastActionResult = false;

        if (_lastExpressionResult) {
            _lastActionResult = _action.executeStart();
        }

        return _lastActionResult;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _action.executeContinue();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _expression.evaluate();
                if (exprResult) {
                    _lastActionResult = _action.executeContinue();
                } else {
                    _action.abort();
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
                _lastActionResult = _action.executeRestart();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _expression.evaluate();
                if (exprResult) {
                    _lastActionResult = _action.executeRestart();
                } else {
                    _action.abort();
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
        _action.abort();
    }
    
    /** {@inheritDoc} */
    public Type getType() {
        return _type;
    }
    
    /** {@inheritDoc} */
    public void setType(Type type) {
        if ((_type != type) && _lastActionResult) {
            _action.abort();
        }
        _type = type;
    }
    
}
