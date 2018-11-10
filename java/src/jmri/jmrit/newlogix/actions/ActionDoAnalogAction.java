package jmri.jmrit.newlogix.actions;

import jmri.InstanceManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.FemaleAnalogActionSocket;
import jmri.jmrit.newlogix.FemaleAnalogExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleActionSocket;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.NewLogixManager;

/**
 * Executes an analog action with the result of an analog expression.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionDoAnalogAction
        extends AbstractAction
        implements FemaleSocketListener {

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
    private final FemaleAnalogExpressionSocket _analogExpressionSocket;
    private final FemaleAnalogActionSocket _analogActionSocket;
    
    public ActionDoAnalogAction(String sys, Type type) {
        super(sys);
        _type = type;
        _analogExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _analogActionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleActionSocket(this, "A1");
    }
    
    public ActionDoAnalogAction(String sys, String user, Type type) {
        super(sys, user);
        _type = type;
        _analogExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _analogActionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleActionSocket(this, "A1");
    }
    
    public ActionDoAnalogAction(
            String sys, Type type,
            String expressionSocketName, String actionSocketName,
            MaleExpressionSocket expression, MaleActionSocket action) {
        
        super(sys);
        _type = type;
        _analogExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, expressionSocketName, expression);
        _analogActionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleActionSocket(this, actionSocketName, action);
    }
    
    public ActionDoAnalogAction(
            String sys, String user, Type type,
            String expressionSocketName, String actionSocketName, 
            MaleExpressionSocket expression, MaleActionSocket action) {
        
        super(sys, user);
        _type = type;
        _analogExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, expressionSocketName, expression);
        _analogActionSocket = InstanceManager.getDefault(NewLogixManager.class)
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
        _lastExpressionResult = _analogExpressionSocket.evaluate();
        _lastActionResult = false;

        if (_lastExpressionResult) {
            _lastActionResult = _analogActionSocket.executeStart();
        }

        return _lastActionResult;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        switch (_type) {
            case TRIGGER_ACTION:
                _lastActionResult = _analogActionSocket.executeContinue();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _analogExpressionSocket.evaluate();
                if (exprResult) {
                    _lastActionResult = _analogActionSocket.executeContinue();
                } else {
                    _analogActionSocket.abort();
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
                _lastActionResult = _analogActionSocket.executeRestart();
                break;
                
            case CONTINOUS_ACTION:
                boolean exprResult = _analogExpressionSocket.evaluate();
                if (exprResult) {
                    _lastActionResult = _analogActionSocket.executeRestart();
                } else {
                    _analogActionSocket.abort();
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
        _analogActionSocket.abort();
    }
    
    /** {@inheritDoc} */
    public Type getType() {
        return _type;
    }
    
    /** {@inheritDoc} */
    public void setType(Type type) {
        if ((_type != type) && _lastActionResult) {
            _analogActionSocket.abort();
        }
        _type = type;
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _analogExpressionSocket;
                
            case 1:
                return _analogActionSocket;
                
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
    public String toString() {
        return Bundle.getMessage("ActionIfThen", _analogExpressionSocket.getName(), _analogActionSocket.getName());
    }
    
}
