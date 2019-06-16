package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;

/**
 * Evaluates the state of a Turnout.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionTurnout extends AbstractDigitalExpression {

    private ExpressionTurnout _template;
    private String _turnoutSystemName;
    private NamedBeanHandle _turnout;
    private Is_IsNot_Enum _is_IsNot = Is_IsNot_Enum.IS;
    private TurnoutState _turnoutState = TurnoutState.THROWN;

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
    
    public void setTurnout(NamedBeanHandle handle) {
        _turnout = handle;
    }
    
    public NamedBeanHandle getTurnout() {
        return _turnout;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        // Do this on the correct thread??
        throw new UnsupportedOperationException("Not supported yet.");
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
        if (_turnout != null) {
            turnoutName = _turnout.getBean().getDisplayName();
        } else {
            turnoutName = Bundle.getMessage("BeanNotSelected");
        }
        return Bundle.getMessage("Turnout_Long", turnoutName, _is_IsNot.toString(), _turnoutState._text);
    }
    
    public void setTurnout_SystemName(String turnoutSystemName) {
        _turnoutSystemName = turnoutSystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_turnout == null) && (_turnoutSystemName != null)) {
            Turnout t = InstanceManager.getDefault(TurnoutManager.class).getBeanBySystemName(_turnoutSystemName);
            _turnout = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_turnoutSystemName, t);
        }
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
    
}
