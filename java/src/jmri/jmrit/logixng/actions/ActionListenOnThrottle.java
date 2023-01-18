package jmri.jmrit.logixng.actions;

import java.beans.*;
import java.util.*;

import jmri.*;
import jmri.jmrit.logixng.*;

/**
 * This action listens on throttles and runs the ConditionalNG on property change.
 *
 * @author Daniel Bergqvist Copyright 2019
 */
public final class ActionListenOnThrottle extends AbstractDigitalAction
        implements PropertyChangeListener {

    private SystemConnectionMemo _memo;
    private ThrottleManager _throttleManager;

    private boolean _listenOnSpeed = true;
    private boolean _listenOnIsForward = true;
    private boolean _listenOnFunction = true;
    private final Set<ThrottleReference> _throttleReferences = new HashSet<>();
    private String _localVariableEvent;
    private String _localVariableLocoAddress;
    private String _localVariableRoster;
    private String _localVariableSpeed;
    private String _localVariableIsForward;
    private String _localVariableFunction;
    private String _localVariableFunctionState;
    private String _localVariableUserData;
    private Runnable _runnable;


    public ActionListenOnThrottle(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);

        // Set the _throttleManager variable
        setMemo(null);

        addReference(new ThrottleReference(ThrottleType.Address, 10, ShortOrLongType.Unknown, null, "2"));
    }

    @Override
    public Base getDeepCopy(Map<String, String> systemNames, Map<String, String> userNames) {
        DigitalActionManager manager = InstanceManager.getDefault(DigitalActionManager.class);
        String sysName = systemNames.get(getSystemName());
        String userName = userNames.get(getSystemName());
        if (sysName == null) sysName = manager.getAutoSystemName();
        ActionListenOnThrottle copy = new ActionListenOnThrottle(sysName, userName);
        copy.setComment(getComment());
        copy.setMemo(_memo);
        copy.setListenOnSpeed(_listenOnSpeed);
        copy.setListenOnIsForward(_listenOnIsForward);
        copy.setListenOnFunction(_listenOnFunction);
        copy.setLocalVariableEvent(_localVariableEvent);
        copy.setLocalVariableLocoAddress(_localVariableLocoAddress);
        copy.setLocalVariableRoster(_localVariableRoster);
        copy.setLocalVariableSpeed(_localVariableSpeed);
        copy.setLocalVariableIsForward(_localVariableIsForward);
        copy.setLocalVariableFunction(_localVariableFunction);
        copy.setLocalVariableFunctionState(_localVariableFunctionState);
        copy.setLocalVariableUserData(_localVariableUserData);
        for (ThrottleReference reference : _throttleReferences) {
            copy.addReference(reference);
        }
        return manager.registerAction(copy);
    }

    public void setMemo(SystemConnectionMemo memo) {
        assertListenersAreNotRegistered(log, "setMemo");
        _memo = memo;
        if (_memo != null) {
            _throttleManager = _memo.get(jmri.ThrottleManager.class);
            if (_throttleManager == null) {
                throw new IllegalArgumentException("Memo "+memo.getUserName()+" doesn't have a ThrottleManager");
            }
        } else {
            _throttleManager = InstanceManager.getDefault(ThrottleManager.class);
        }
    }

    public SystemConnectionMemo getMemo() {
        return _memo;
    }

    public void setListenOnSpeed(boolean value) {
        this._listenOnSpeed = value;
    }

    public boolean getListenOnSpeed() {
        return _listenOnSpeed;
    }

    public void setListenOnIsForward(boolean value) {
        this._listenOnIsForward = value;
    }

    public boolean getListenOnIsForward() {
        return _listenOnIsForward;
    }

    public void setListenOnFunction(boolean value) {
        this._listenOnFunction = value;
    }

    public boolean getListenOnFunction() {
        return _listenOnFunction;
    }

    public void addReference(ThrottleReference reference) {
        assertListenersAreNotRegistered(log, "addReference");
        _throttleReferences.add(reference);
    }

    public void removeReference(ThrottleReference reference) {
        assertListenersAreNotRegistered(log, "removeReference");
        _throttleReferences.remove(reference);
    }

    public Collection<ThrottleReference> getReferences() {
        return _throttleReferences;
    }

    public void clearReferences() {
        _throttleReferences.clear();
    }

    public void setLocalVariableEvent(String localVariableEvent) {
        if ((localVariableEvent != null) && (!localVariableEvent.isEmpty())) {
            this._localVariableEvent = localVariableEvent;
        } else {
            this._localVariableEvent = null;
        }
    }

    public String getLocalVariableEvent() {
        return _localVariableEvent;
    }

    public void setLocalVariableLocoAddress(String localVariableNamedBean) {
        if ((localVariableNamedBean != null) && (!localVariableNamedBean.isEmpty())) {
            this._localVariableLocoAddress = localVariableNamedBean;
        } else {
            this._localVariableLocoAddress = null;
        }
    }

    public String getLocalVariableLocoAddress() {
        return _localVariableLocoAddress;
    }

    public void setLocalVariableRoster(String localVariableRoster) {
        if ((localVariableRoster != null) && (!localVariableRoster.isEmpty())) {
            this._localVariableRoster = localVariableRoster;
        } else {
            this._localVariableRoster = null;
        }
    }

    public String getLocalVariableRoster() {
        return _localVariableRoster;
    }

    public void setLocalVariableSpeed(String localVariableEvent) {
        if ((localVariableEvent != null) && (!localVariableEvent.isEmpty())) {
            this._localVariableSpeed = localVariableEvent;
        } else {
            this._localVariableSpeed = null;
        }
    }

    public String getLocalVariableSpeed() {
        return _localVariableSpeed;
    }

    public void setLocalVariableIsForward(String localVariableNewValue) {
        if ((localVariableNewValue != null) && (!localVariableNewValue.isEmpty())) {
            this._localVariableIsForward = localVariableNewValue;
        } else {
            this._localVariableIsForward = null;
        }
    }

    public String getLocalVariableIsForward() {
        return _localVariableIsForward;
    }

    public void setLocalVariableFunction(String localVariableFunction) {
        if ((localVariableFunction != null) && (!localVariableFunction.isEmpty())) {
            this._localVariableFunction = localVariableFunction;
        } else {
            this._localVariableFunction = null;
        }
    }

    public String getLocalVariableFunction() {
        return _localVariableFunction;
    }

    public void setLocalVariableFunctionState(String localVariableState) {
        if ((localVariableState != null) && (!localVariableState.isEmpty())) {
            this._localVariableFunctionState = localVariableState;
        } else {
            this._localVariableFunctionState = null;
        }
    }

    public String getLocalVariableFunctionState() {
        return _localVariableFunctionState;
    }

    public void setLocalVariableUserData(String localVariableUserData) {
        if ((localVariableUserData != null) && (!localVariableUserData.isEmpty())) {
            this._localVariableUserData = localVariableUserData;
        } else {
            this._localVariableUserData = null;
        }
    }

    public String getLocalVariableUserData() {
        return _localVariableUserData;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        // The main purpose of this action is only to listen on property
        // changes of the registered beans and execute the ConditionalNG
        // when it happens.

        synchronized(this) {
            if (_runnable != null) _runnable.run();
        }
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
    public String getShortDescription(Locale locale) {
        return Bundle.getMessage(locale, "ActionListenOnThrottle_Short");
    }

    @Override
    public String getLongDescription(Locale locale) {
        return Bundle.getMessage(locale, "ActionListenOnThrottle_Long");
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
        if (_listenersAreRegistered) return;

        for (ThrottleReference ref : _throttleReferences) {

            ThrottleListener throttleListener =  new ThrottleListener() {
                @Override
                public void notifyThrottleFound(DccThrottle t) {
                    ref._throttle = t;
                    ref._throttleListener = this;
                    t.addPropertyChangeListener(ActionListenOnThrottle.this);
                }

                @Override
                public void notifyFailedThrottleRequest(LocoAddress address, String reason) {
                    log.warn("loco {} cannot be aquired", address.getNumber());
                }

                @Override
                public void notifyDecisionRequired(LocoAddress address, ThrottleListener.DecisionType question) {
                    log.warn("Loco {} cannot be aquired. Decision required.", address.getNumber());
                }
            };

            boolean result;

            switch (ref._type) {
                case Address:
                    if (ref._shortOrLong == ShortOrLongType.Unknown) {
                        result = _throttleManager.requestThrottle(ref._address, throttleListener);
                    } else {
                        result = _throttleManager.requestThrottle(ref._address, throttleListener, ref._shortOrLong._isLong);
                    }
                    if (!result) {
                        log.warn("loco {} cannot be aquired", ref._address);
                    }
                    break;

                case Roster:
                    result = _throttleManager.requestThrottle(ref._rosterEntry, throttleListener, false);
                    if (!result) {
                        log.warn("loco {} cannot be aquired", ref._address);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unkown _type: "+ref._type.name());
            }
        }
        _listenersAreRegistered = true;
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        if (!_listenersAreRegistered) return;

        for (ThrottleReference ref : _throttleReferences) {
            if (ref._throttle != null) {
                ref._throttle.release(ref._throttleListener);
            }
        }
        _listenersAreRegistered = false;
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.format("Property: %s, old: %s, new: %s, source: %s, %s%n", evt.getPropertyName(), evt.getOldValue(), evt.getNewValue(), evt.getSource(), evt.getSource().getClass().getName());

        Throttle t = (Throttle) evt.getSource();
        System.out.format("Property: %s, old: %s, new: %s, throttle: %s, addr: %s, forward: %b, speed: %1.2f, func: %s, mom: %s%n",
                evt.getPropertyName(), evt.getOldValue(), evt.getNewValue(), t, t.getLocoAddress(), t.getIsForward(), t.getSpeedSetting(), t.getFunctions(), t.getFunctionsMomentary());

        String data = null;
        for (ThrottleReference ref: _throttleReferences) {
            if (t.getLocoAddress().getNumber() == ref._address) {
                data = ref._userData;
                break;
            }
        }

        final String userData = data;

        if ("SpeedSetting".equals(evt.getPropertyName())) {
            if (_listenOnSpeed) {
                synchronized(this) {
                    _runnable = () -> {
                        SymbolTable symbolTable = getConditionalNG().getSymbolTable();
                        if (_localVariableEvent != null) {
                            symbolTable.setValue(_localVariableEvent, "Speed");
                        }
                        if (_localVariableLocoAddress != null) {
                            symbolTable.setValue(_localVariableLocoAddress, t.getLocoAddress().getNumber());
                        }
                        if (_localVariableRoster != null) {
                            symbolTable.setValue(_localVariableRoster, t.getRosterEntry());
                        }
                        if (_localVariableSpeed != null) {
                            symbolTable.setValue(_localVariableSpeed, (float) evt.getNewValue());
                        }
                        if (_localVariableIsForward != null) {
                            symbolTable.setValue(_localVariableIsForward, t.getIsForward());
                        }
                        if (_localVariableUserData != null) {
                            symbolTable.setValue(_localVariableUserData, userData);
                        }
                    };
                }
                getConditionalNG().execute();
            }
        } else if ("IsForward".equals(evt.getPropertyName())) {
            if (_listenOnIsForward) {
                synchronized(this) {
                    _runnable = () -> {
                        SymbolTable symbolTable = getConditionalNG().getSymbolTable();
                        if (_localVariableEvent != null) {
                            symbolTable.setValue(_localVariableEvent, "IsForward");
                        }
                        if (_localVariableLocoAddress != null) {
                            symbolTable.setValue(_localVariableLocoAddress, t.getLocoAddress().getNumber());
                        }
                        if (_localVariableRoster != null) {
                            symbolTable.setValue(_localVariableRoster, t.getRosterEntry());
                        }
                        if (_localVariableSpeed != null) {
                            symbolTable.setValue(_localVariableSpeed, t.getSpeedSetting());
                        }
                        if (_localVariableIsForward != null) {
                            symbolTable.setValue(_localVariableIsForward, (boolean) evt.getNewValue());
                        }
                        if (_localVariableUserData != null) {
                            symbolTable.setValue(_localVariableUserData, userData);
                        }
                    };
                }
                getConditionalNG().execute();
            }
        } else if (evt.getPropertyName().startsWith("F")) {
            if (_listenOnFunction) {
                int function;
                try {
                    function = Integer.parseInt(evt.getPropertyName().substring(1));
                } catch (NumberFormatException e) {
                    log.error("Unknown property: {}", evt.getPropertyName());
                    return;
                }
                synchronized(this) {
                    _runnable = () -> {
                        SymbolTable symbolTable = getConditionalNG().getSymbolTable();
                        if (_localVariableEvent != null) {
                            symbolTable.setValue(_localVariableEvent, "Function");
                        }
                        if (_localVariableLocoAddress != null) {
                            symbolTable.setValue(_localVariableLocoAddress, t.getLocoAddress().getNumber());
                        }
                        if (_localVariableRoster != null) {
                            symbolTable.setValue(_localVariableRoster, t.getRosterEntry());
                        }
                        if (_localVariableSpeed != null) {
                            symbolTable.setValue(_localVariableSpeed, t.getSpeedSetting());
                        }
                        if (_localVariableIsForward != null) {
                            symbolTable.setValue(_localVariableIsForward, t.getIsForward());
                        }
                        if (_localVariableFunction != null) {
                            symbolTable.setValue(_localVariableFunction, function);
                        }
                        if (_localVariableFunctionState != null) {
                            symbolTable.setValue(_localVariableFunctionState, (boolean) evt.getNewValue());
                        }
                        if (_localVariableUserData != null) {
                            symbolTable.setValue(_localVariableUserData, userData);
                        }
                    };
                }
                getConditionalNG().execute();
            }
        } else {
            log.error("Unknown property: {}", evt.getPropertyName());
        }
    }

    /** {@inheritDoc} */
//    @Override
//    public void getUsageDetail(int level, NamedBean bean, List<NamedBeanUsageReport> report, NamedBean cdl) {
//        log.debug("getUsageReport :: ActionListenOnThrottle: bean = {}, report = {}", cdl, report);
//    }

    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }


    public enum ShortOrLongType {
        Short(false, Bundle.getMessage("ActionListenOnThrottle_ShortOrLongType_Short")),
        Long(true, Bundle.getMessage("ActionListenOnThrottle_ShortOrLongType_Long")),
        Unknown(null, Bundle.getMessage("ActionListenOnThrottle_ShortOrLongType_Unknown"));

        private final Boolean _isLong;
        private final String _text;

        private ShortOrLongType(Boolean isLong, String text) {
            this._isLong = isLong;
            this._text = text;
        }

        public Boolean isLong() {
            return _isLong;
        }

        @Override
        public String toString() {
            return _text;
        }

    }


    public enum ThrottleType {
        Address(Bundle.getMessage("ActionListenOnThrottle_ThrottleType_Address")),
        Roster(Bundle.getMessage("ActionListenOnThrottle_ThrottleType_Roster"));

        private final String _text;

        private ThrottleType(String text) {
            this._text = text;
        }

        @Override
        public String toString() {
            return _text;
        }

    }


    public static class ThrottleReference {

        private Throttle _throttle;
        private ThrottleListener _throttleListener;
        private ThrottleType _type;
        private int _address;
        private ShortOrLongType _shortOrLong;
        private BasicRosterEntry _rosterEntry;
        private String _userData;   // Arbitrary user data

        public ThrottleReference(ThrottleReference ref) {
            this(ref._type, ref._address, ref._shortOrLong, ref._rosterEntry, ref._userData);
        }

        public ThrottleReference(ThrottleType type, int address, ShortOrLongType shortOrLong, BasicRosterEntry rosterEntry, String data) {
            _type = type;
            _address = address;
            _shortOrLong = shortOrLong;
            _rosterEntry = rosterEntry;
            _userData = data;
        }

        public void setType(ThrottleType type) {
            if (type == null) {
                log.warn("type is null");
                type = ThrottleType.Address;
            }
            _type = type;
        }

        public ThrottleType getType() {
            return _type;
        }

        public int getAddress() {
            return _address;
        }

        public void setAddress(int address) {
            _address = address;
        }

        public void setShortOrLongType(ShortOrLongType shortOrLong) {
            _shortOrLong = shortOrLong;
        }

        public ShortOrLongType getShortOrLongType() {
            return _shortOrLong;
        }

        public void setRosterEntry(BasicRosterEntry rosterEntry) {
            _rosterEntry = rosterEntry;
        }

        public BasicRosterEntry getRosterEntry() {
            return _rosterEntry;
        }

        public void setData(String data) {
            _userData = data;
        }

        public String getData() {
            return _userData;
        }

        // This method is used by ListenOnBeansTableModel
        @Override
        public String toString() {
            if (_type == ThrottleType.Roster) {
                return _rosterEntry != null ? _rosterEntry.getId() : "";
            } else {
                return Integer.toString(_address);
            }
        }

    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionListenOnThrottle.class);

}
