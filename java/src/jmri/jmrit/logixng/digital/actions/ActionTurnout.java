package jmri.jmrit.logixng.digital.actions;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;

/**
 * This action sets the state of a turnout.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionTurnout extends AbstractDigitalAction {

    private ActionTurnout _template;
    private String _turnoutSystemName;
    private NamedBeanHandle _turnout;
    private TurnoutState _turnoutState = TurnoutState.THROWN;
    
    public ActionTurnout(String sys)
            throws BadUserNameException {
        super(sys);
//        jmri.InstanceManager.turnoutManagerInstance().addVetoableChangeListener(this);
    }

    public ActionTurnout(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
//        jmri.InstanceManager.turnoutManagerInstance().addVetoableChangeListener(this);
    }
    
    private ActionTurnout(ActionTurnout template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ActionTurnout(this, sys);
    }
    
    public void setTurnout(NamedBeanHandle handle) {
        _turnout = handle;
    }
    
    public NamedBeanHandle getTurnout() {
        return _turnout;
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
    public boolean executeStart() {
        throw new UnsupportedOperationException("Not supported yet.");
        // Do this on the LayoutThread
//        _turnout.setState(_newState);
//        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // The executeStart() metod never return True from this action and
        // therefore executeContinue() should never be called.
        throw new RuntimeException("Turnout don't support executeContinue()");
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        // The executeStart() metod never return True from this action and
        // therefore executeRestart() should never be called.
        throw new RuntimeException("Turnout don't support executeRestart()");
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // The executeStart() metod never return True from this action and
        // therefore abort() should never be called.
        throw new RuntimeException("Turnout don't support abort()");
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
        return Bundle.getMessage("Turnout_Long", turnoutName, _turnoutState._text);
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
        THROWN(Turnout.THROWN, InstanceManager.getDefault(TurnoutManager.class).getThrownText());
        
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
                    throw new IllegalArgumentException("invalid turnout state");
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
