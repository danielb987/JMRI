package jmri.jmrit.logixng.digital.expressions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evaluates the state of a Turnout.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionTurnout extends AbstractDigitalExpression implements PropertyChangeListener {

    private ExpressionTurnout _template;
    private String _turnoutName;
    private NamedBeanHandle<Turnout> _turnoutHandle;
    private Is_IsNot_Enum _is_IsNot = Is_IsNot_Enum.IS;
    private TurnoutState _turnoutState = TurnoutState.THROWN;
    private boolean _listenersAreRegistered = false;

    public ExpressionTurnout(ConditionalNG conditionalNG)
            throws BadUserNameException {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
    }

    public ExpressionTurnout(String sys)
            throws BadUserNameException, BadSystemNameException {
        super(sys);
    }

    public ExpressionTurnout(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    private ExpressionTurnout(ExpressionTurnout template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ExpressionTurnout(this, sys);
    }
    
    public void setTurnout(NamedBeanHandle<Turnout> handle) {
        _turnoutHandle = handle;
    }
    
    public void setTurnout(Turnout turnout) {
        _turnoutHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(turnout.getDisplayName(), turnout);
    }
    
    public NamedBeanHandle<Turnout> getTurnout() {
        return _turnoutHandle;
    }
    
    public void set_Is_IsNot(Is_IsNot_Enum is_IsNot) {
        _is_IsNot = is_IsNot;
    }
    
    public Is_IsNot_Enum get_Is_IsNot() {
        return _is_IsNot;
    }
    
    public void setTurnoutState(TurnoutState state) {
        _turnoutState = state;
    }
    
    public TurnoutState getTurnoutState() {
        return _turnoutState;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        TurnoutState currentTurnoutState = TurnoutState.get(_turnoutHandle.getBean().getCommandedState());
        if (_is_IsNot == Is_IsNot_Enum.IS) {
            return currentTurnoutState == _turnoutState;
        } else {
            return currentTurnoutState != _turnoutState;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Do nothing.
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("Turnout_Short");
    }

    @Override
    public String getLongDescription() {
        String turnoutName;
        if (_turnoutHandle != null) {
            turnoutName = _turnoutHandle.getBean().getDisplayName();
        } else {
            turnoutName = Bundle.getMessage("BeanNotSelected");
        }
        return Bundle.getMessage("Turnout_Long", turnoutName, _is_IsNot.toString(), _turnoutState._text);
    }
    
    public void setTurnoutName(String turnoutName) {
        _turnoutName = turnoutName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if (_listenersAreRegistered) {
            RuntimeException e = new RuntimeException("setup must not be called when listeners are registered");
            log.error("setup must not be called when listeners are registered", e);
            throw e;
        }
        
        // Remove the old _turnoutHandle if it exists
        _turnoutHandle = null;
        
        if (_turnoutName != null) {
            Turnout t = InstanceManager.getDefault(TurnoutManager.class).getTurnout(_turnoutName);
            if (t != null) {
                _turnoutHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_turnoutName, t);
            } else {
                log.error("Turnout {} does not exists", _turnoutName);
            }
        }
/*        
        _turnoutHandle.setBean(bean);
        if ((_turnoutHandle == null) && (_turnoutSystemName != null)) {
            Turnout t = InstanceManager.getDefault(TurnoutManager.class).getBeanBySystemName(_turnoutSystemName);
            _turnoutHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_turnoutSystemName, t);
            if (t != null) {
                _lightHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_lightSystemName, t);
            } else {
                log.error("Light {} does not exists", _lightSystemName);
            }
        }
*/        
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
        if (! _listenersAreRegistered) {
            _turnoutHandle.getBean().addPropertyChangeListener("KnownState", this);
            _listenersAreRegistered = true;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        if (! _listenersAreRegistered) {
            _turnoutHandle.getBean().removePropertyChangeListener("KnownState", this);
            _listenersAreRegistered = false;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getConditionalNG().execute();
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }
    
    
    
    public enum TurnoutState {
        CLOSED(Turnout.CLOSED, InstanceManager.getDefault(TurnoutManager.class).getClosedText()),
        THROWN(Turnout.THROWN, InstanceManager.getDefault(TurnoutManager.class).getThrownText()),
        OTHER(-1, Bundle.getMessage("TurnoutOtherStatus"));
        
        private final int _id;
        private final String _text;
        
        private TurnoutState(int id, String text) {
            this._id = id;
            this._text = text;
        }
        
        static public TurnoutState get(int id) {
            switch (id) {
                case Turnout.CLOSED:
                    return CLOSED;
                    
                case Turnout.THROWN:
                    return THROWN;
                    
                default:
                    return OTHER;
            }
        }
        
        public int getID() {
            return _id;
        }
        
        @Override
        public String toString() {
            return _text;
        }
        
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ExpressionTurnout.class);
    
}
