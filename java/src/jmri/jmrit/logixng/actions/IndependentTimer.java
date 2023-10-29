package jmri.jmrit.logixng.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jmri.jmrit.logixng.util.TimerUnit;

import java.util.*;

import jmri.*;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.util.*;
import jmri.util.TimerUtil;

/**
 * Executes an action after some time.
 * This timer calls ConditionalNG.execute(socket) to trigger execution only
 * of its child actions, not on the entire ConditionalNG.
 *
 * @author Daniel Bergqvist Copyright 2019
 */
public class IndependentTimer extends AbstractDigitalAction
        implements FemaleSocketListener, PropertyChangeListener {

    private static class State{
        private ProtectedTimerTask _timerTask;
        private int _currentTimer = -1;
        private TimerState _timerState = TimerState.Off;
        private long _currentTimerDelay = 0;
        private long _currentTimerStart = 0;
        private boolean _startIsActive = false;
    }

    private final LogixNG_SelectNamedBean<Sensor> _selectStartSensor =
            new LogixNG_SelectNamedBean<>(
                    this, Sensor.class, InstanceManager.getDefault(SensorManager.class), this);

    private final LogixNG_SelectNamedBean<Sensor> _selectStopSensor =
            new LogixNG_SelectNamedBean<>(
                    this, Sensor.class, InstanceManager.getDefault(SensorManager.class), this);

    private final LogixNG_SelectNamedBean<Sensor> _selectPauseSensor =
            new LogixNG_SelectNamedBean<>(
                    this, Sensor.class, InstanceManager.getDefault(SensorManager.class), this);

    private final LogixNG_SelectNamedBean<Sensor> _selectResetSensor =
            new LogixNG_SelectNamedBean<>(
                    this, Sensor.class, InstanceManager.getDefault(SensorManager.class), this);

    private final List<ActionEntry> _actionEntries = new ArrayList<>();
    private boolean disableCheckForUnconnectedSocket = false;
    private boolean _startImmediately = false;
    private boolean _runContinuously = false;
//    private TimerUnit _unit = TimerUnit.MilliSeconds;
    private boolean _delayByLocalVariables = false;
    private String _delayLocalVariablePrefix = "";  // An index is appended, for example Delay1, Delay2, ... Delay15.
    private final Map<ConditionalNG, State> _stateMap = new HashMap<>();


    public IndependentTimer(String sys, String user) {
        super(sys, user);
//        _selectStartSensor.setOnlyDirectAddressingAllowed();
//        _selectStopSensor.setOnlyDirectAddressingAllowed();
//        _selectPauseSensor.setOnlyDirectAddressingAllowed();
//        _selectResetSensor.setOnlyDirectAddressingAllowed();
        _actionEntries
                .add(new ActionEntry(InstanceManager.getDefault(DigitalActionManager.class)
                        .createFemaleSocket(this, this, getNewSocketName(),
                                new IndependentTimerSocketConfiguration())));
    }

    public IndependentTimer(String sys, String user,
            List<Map.Entry<String, String>> expressionSystemNames,
            List<ActionData> actionDataList)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
//        _selectStartSensor.setOnlyDirectAddressingAllowed();
//        _selectStopSensor.setOnlyDirectAddressingAllowed();
//        _selectPauseSensor.setOnlyDirectAddressingAllowed();
//        _selectResetSensor.setOnlyDirectAddressingAllowed();
        setActionData(actionDataList);
    }

    @Override
    public Base getDeepCopy(Map<String, String> systemNames, Map<String, String> userNames) throws JmriException {
        DigitalActionManager manager = InstanceManager.getDefault(DigitalActionManager.class);
        String sysName = systemNames.get(getSystemName());
        String userName = userNames.get(getSystemName());
        if (sysName == null) sysName = manager.getAutoSystemName();
        IndependentTimer copy = new IndependentTimer(sysName, userName);
        copy.setComment(getComment());
        copy.setNumActions(getNumActions());
//        for (int i=0; i < getNumActions(); i++) {
//            copy.setDelay(i, getDelay(i));
//        }
        copy.setStartImmediately(_startImmediately);
        copy.setRunContinuously(_runContinuously);
//        copy.setUnit(_unit);
        copy.setDelayByLocalVariables(_delayByLocalVariables);
        copy.setDelayLocalVariablePrefix(_delayLocalVariablePrefix);
        copy.setNumSockets(getChildCount());
        return manager.registerAction(copy).deepCopyChildren(this, systemNames, userNames);
    }

    private void setActionData(List<ActionData> actionDataList) {
        if (!_actionEntries.isEmpty()) {
            throw new RuntimeException("action system names cannot be set more than once");
        }

        for (ActionData data : actionDataList) {
            FemaleDigitalActionSocket socket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .createFemaleSocket(this, this, data._socketName,
                                    data._socketConfig);

            _actionEntries.add(new ActionEntry(socket, data._socketSystemName));
//            _actionEntries.add(new ActionEntry(data._delay, socket, data._socketSystemName));
        }
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.COMMON;
    }

    /**
     * Get a new timer task.
     */
    private ProtectedTimerTask getNewTimerTask(ConditionalNG conditionalNG, State state) {
        return new ProtectedTimerTask() {
            @Override
            public void execute() {
                try {
                    long currentTimerTime = System.currentTimeMillis() - state._currentTimerStart;
                    if (currentTimerTime < state._currentTimerDelay) {
                        scheduleTimer(conditionalNG, state, state._currentTimerDelay - currentTimerTime);
                    } else {
                        state._timerState = TimerState.Completed;
                        conditionalNG.execute();
                    }
                } catch (Exception e) {
                    log.error("Exception thrown", e);
                }
            }
        };
    }

    private void scheduleTimer(ConditionalNG conditionalNG, State state, long delay) {
        synchronized(this) {
            if (state._timerTask != null) {
                state._timerTask.stopTimer();
                state._timerTask = null;
            }
        }
        state._timerTask = getNewTimerTask(conditionalNG, state);
        TimerUtil.schedule(state._timerTask, delay);
    }

    private void schedule(ConditionalNG conditionalNG, SymbolTable symbolTable, State state) {
        synchronized(this) {
            long delay;

            if (_delayByLocalVariables) {
                delay = jmri.util.TypeConversionUtil
                        .convertToLong(symbolTable.getValue(
                                _delayLocalVariablePrefix + Integer.toString(state._currentTimer+1)));
            } else {
                delay = _actionEntries.get(state._currentTimer).getDelay();
            }

//            state._currentTimerDelay = delay * _unit.getMultiply();
            state._currentTimerDelay = delay;
            state._currentTimerStart = System.currentTimeMillis();
            state._timerState = TimerState.WaitToRun;
            scheduleTimer(conditionalNG, state, delay);
//            scheduleTimer(conditionalNG, state, delay * _unit.getMultiply());
        }
    }

    private boolean start(ConditionalNG conditionalNG, State state) throws JmriException {
        boolean lastStartIsActive = state._startIsActive;
        if (_selectStopSensor.getNamedBean() != null) {
            state._startIsActive = _selectStartSensor.evaluateNamedBean(conditionalNG)
                    .getState() == Sensor.ACTIVE;
        }
        return state._startIsActive && !lastStartIsActive;
    }

    private boolean checkStart(ConditionalNG conditionalNG, SymbolTable symbolTable, State state) throws JmriException {
        if (start(conditionalNG, state)) state._timerState = TimerState.RunNow;

        if (state._timerState == TimerState.RunNow) {
            synchronized(this) {
                if (state._timerTask != null) {
                    state._timerTask.stopTimer();
                    state._timerTask = null;
                }
            }
            state._currentTimer = 0;
            while (state._currentTimer < _actionEntries.size()) {
                ActionEntry ae = _actionEntries.get(state._currentTimer);
                if (ae.getDelay() > 0) {
                    schedule(conditionalNG, symbolTable, state);
                    return true;
                }
                else {
                    state._currentTimer++;
                }
            }
            // If we get here, all timers has a delay of 0 ms
            state._timerState = TimerState.Off;
            return true;
        }

        return false;
    }

    private boolean stop(ConditionalNG conditionalNG, State state) throws JmriException {
        boolean stop = false;

        if (_selectStopSensor.getNamedBean() != null) {
            stop = _selectStopSensor.evaluateNamedBean(conditionalNG)
                    .getState() == Sensor.ACTIVE;
        }

        if (stop) {
            synchronized(this) {
                if (state._timerTask != null) state._timerTask.stopTimer();
                state._timerTask = null;
            }
            state._timerState = TimerState.Off;
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws JmriException {
        ConditionalNG conditionalNG = getConditionalNG();
        State state = _stateMap.computeIfAbsent(conditionalNG, o -> new State());

        if (stop(conditionalNG, state)) {
            state._startIsActive = false;
            return;
        }

        if (checkStart(conditionalNG, conditionalNG.getSymbolTable(), state)) return;

        if (state._timerState == TimerState.Off) return;
        if (state._timerState == TimerState.Running) return;

        int startTimer = state._currentTimer;
        while (state._timerState == TimerState.Completed) {
            // If the timer has passed full time, execute the action
            if ((state._timerState == TimerState.Completed) && _actionEntries.get(state._currentTimer)._socket.isConnected()) {
                _actionEntries.get(state._currentTimer)._socket.execute();
            }

            // Move to them next timer
            state._currentTimer++;
            if (state._currentTimer >= _actionEntries.size()) {
                state._currentTimer = 0;
                if (!_runContinuously) {
                    state._timerState = TimerState.Off;
                    return;
                }
            }

            ActionEntry ae = _actionEntries.get(state._currentTimer);
            if (ae.getDelay() > 0) {
                schedule(conditionalNG, conditionalNG.getSymbolTable(), state);
                return;
            }

            if (startTimer == state._currentTimer) {
                // If we get here, all timers has a delay of 0 ms
                state._timerState = TimerState.Off;
            }
        }
    }

    /**
     * Get if to start immediately
     * @return true if to start immediately
     */
    public boolean isStartImmediately() {
        return _startImmediately;
    }

    /**
     * Set if to start immediately
     * @param startImmediately true if to start immediately
     */
    public void setStartImmediately(boolean startImmediately) {
        _startImmediately = startImmediately;
    }

    /**
     * Get if run continuously
     * @return true if run continuously
     */
    public boolean isRunContinuously() {
        return _runContinuously;
    }

    /**
     * Set if run continuously
     * @param runContinuously true if run continuously
     */
    public void setRunContinuously(boolean runContinuously) {
        _runContinuously = runContinuously;
    }

    /**
     * Is delays given by local variables?
     * @return value true if delay is given by local variables
     */
    public boolean isDelayByLocalVariables() {
        return _delayByLocalVariables;
    }

    /**
     * Set if delays should be given by local variables.
     * @param value true if delay is given by local variables
     */
    public void setDelayByLocalVariables(boolean value) {
        _delayByLocalVariables = value;
    }

    /**
     * Is both start and stop is controlled by the start expression.
     * @return true if to start immediately
     */
    public String getDelayLocalVariablePrefix() {
        return _delayLocalVariablePrefix;
    }

    /**
     * Set if both start and stop is controlled by the start expression.
     * @param value true if to start immediately
     */
    public void setDelayLocalVariablePrefix(String value) {
        _delayLocalVariablePrefix = value;
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        if ((index < 0) || (index >= _actionEntries.size())) {
            throw new IllegalArgumentException(
                    String.format("index has invalid value: %d", index));
        }
        return _actionEntries.get(index)._socket;
    }

    @Override
    public int getChildCount() {
        return _actionEntries.size();
    }

    // This method ensures that we have enough of children
    private void setNumSockets(int num) {
        List<FemaleSocket> addList = new ArrayList<>();

        // Is there not enough children?
        while (_actionEntries.size() < num) {
            FemaleDigitalActionSocket socket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .createFemaleSocket(this, this, getNewSocketName());
            _actionEntries.add(new ActionEntry(socket));
            addList.add(socket);
        }
        firePropertyChange(Base.PROPERTY_CHILD_COUNT, null, addList);
    }

    private void checkFreeSocket() {
        boolean hasFreeSocket = false;

        for (ActionEntry entry : _actionEntries) {
            hasFreeSocket |= !entry._socket.isConnected();
        }
        if (!hasFreeSocket) {
            FemaleDigitalActionSocket socket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .createFemaleSocket(this, this, getNewSocketName());
            _actionEntries.add(new ActionEntry(socket));

            List<FemaleSocket> list = new ArrayList<>();
            list.add(socket);
            firePropertyChange(Base.PROPERTY_CHILD_COUNT, null, list);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSocketOperationAllowed(int index, FemaleSocketOperation oper) {
        switch (oper) {
            case Remove:    // Possible if socket is not connected and there are at least two sockets
                if (_actionEntries.size() == 1) return false;
                return ! getChild(index).isConnected();
            case InsertBefore:
                return true;    // Always possible
            case InsertAfter:
                return true;    // Always possible
            case MoveUp:
                return index > 0;   // Possible if not first socket
            case MoveDown:
                return index+1 < getChildCount();   // Possible if not last socket
            default:
                throw new UnsupportedOperationException("Oper is unknown" + oper.name());
        }
    }

    private void insertNewSocket(int index) {
        FemaleDigitalActionSocket socket =
                InstanceManager.getDefault(DigitalActionManager.class)
                        .createFemaleSocket(this, this, getNewSocketName());
        _actionEntries.add(index, new ActionEntry(socket));

        List<FemaleSocket> addList = new ArrayList<>();
        addList.add(socket);
        firePropertyChange(Base.PROPERTY_CHILD_COUNT, null, addList);
    }

    private void removeSocket(int index) {
        List<FemaleSocket> removeList = new ArrayList<>();
        removeList.add(_actionEntries.remove(index)._socket);
        firePropertyChange(Base.PROPERTY_CHILD_COUNT, removeList, null);
    }

    private void moveSocketDown(int index) {
        ActionEntry temp = _actionEntries.get(index);
        _actionEntries.set(index, _actionEntries.get(index+1));
        _actionEntries.set(index+1, temp);

        List<FemaleSocket> list = new ArrayList<>();
        list.add(_actionEntries.get(index)._socket);
        list.add(_actionEntries.get(index+1)._socket);
        firePropertyChange(Base.PROPERTY_CHILD_REORDER, null, list);
    }

    /** {@inheritDoc} */
    @Override
    public void doSocketOperation(int index, FemaleSocketOperation oper) {
        switch (oper) {
            case Remove:
                if (getChild(index).isConnected()) throw new UnsupportedOperationException("Socket is connected");
                removeSocket(index);
                break;
            case InsertBefore:
                insertNewSocket(index);
                break;
            case InsertAfter:
                insertNewSocket(index+1);
                break;
            case MoveUp:
                if (index == 0) throw new UnsupportedOperationException("cannot move up first child");
                moveSocketDown(index-1);
                break;
            case MoveDown:
                if (index+1 == getChildCount()) throw new UnsupportedOperationException("cannot move down last child");
                moveSocketDown(index);
                break;
            default:
                throw new UnsupportedOperationException("Oper is unknown" + oper.name());
        }
    }

    @Override
    public void connected(FemaleSocket socket) {
        if (disableCheckForUnconnectedSocket) return;

        for (ActionEntry entry : _actionEntries) {
            if (socket == entry._socket) {
                entry._socketSystemName =
                        socket.getConnectedSocket().getSystemName();
            }
        }

        checkFreeSocket();
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        for (ActionEntry entry : _actionEntries) {
            if (socket == entry._socket) {
                entry._socketSystemName = null;
            }
        }
    }

    @Override
    public String getShortDescription(Locale locale) {
        return Bundle.getMessage(locale, "IndependentTimer_Short");
    }

    @Override
    public String getLongDescription(Locale locale) {
        String options = "";
        if (_delayByLocalVariables) {
            options = Bundle.getMessage("IndependentTimer_Options_DelayByLocalVariable", _delayLocalVariablePrefix);
        }
//        if (_startAndStopByStartExpression) {
//            return Bundle.getMessage(locale, "IndependentTimer_Long2",
//                    Bundle.getMessage("IndependentTimer_StartAndStopByStartExpression"), options);
//        } else {
            return Bundle.getMessage(locale, "IndependentTimer_Long", options);
//        }
    }

    public int getNumActions() {
        return _actionEntries.size();
    }

    public void setNumActions(int num) {
        List<FemaleSocket> addList = new ArrayList<>();
        List<FemaleSocket> removeList = new ArrayList<>();

        // Is there too many children?
        while (_actionEntries.size() > num) {
            ActionEntry ae = _actionEntries.get(num);
            if (ae._socket.isConnected()) {
                throw new IllegalArgumentException("Cannot remove sockets that are connected");
            }
            removeList.add(_actionEntries.get(_actionEntries.size()-1)._socket);
            _actionEntries.remove(_actionEntries.size()-1);
        }

        // Is there not enough children?
        while (_actionEntries.size() < num) {
            FemaleDigitalActionSocket socket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .createFemaleSocket(this, this, getNewSocketName());
            _actionEntries.add(new ActionEntry(socket));
            addList.add(socket);
        }
        firePropertyChange(Base.PROPERTY_CHILD_COUNT, removeList, addList);
    }

    public FemaleDigitalActionSocket getActionSocket(int socket) {
        return _actionEntries.get(socket)._socket;
    }

    public String getActionSocketSystemName(int socket) {
        return _actionEntries.get(socket)._socketSystemName;
    }

    public void setActionSocketSystemName(int socket, String systemName) {
        _actionEntries.get(socket)._socketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        // We don't want to check for unconnected sockets while setup sockets
        disableCheckForUnconnectedSocket = true;

        try {
            for (ActionEntry ae : _actionEntries) {
                if ( !ae._socket.isConnected()
                        || !ae._socket.getConnectedSocket().getSystemName()
                                .equals(ae._socketSystemName)) {

                    String socketSystemName = ae._socketSystemName;
                    ae._socket.disconnect();
                    if (socketSystemName != null) {
                        MaleSocket maleSocket =
                                InstanceManager.getDefault(DigitalActionManager.class)
                                        .getBySystemName(socketSystemName);
                        ae._socket.disconnect();
                        if (maleSocket != null) {
                            ae._socket.connect(maleSocket);
                            maleSocket.setup();
                        } else {
                            log.error("cannot load digital action {}", socketSystemName);
                        }
                    }
                } else {
                    ae._socket.getConnectedSocket().setup();
                }
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }

//        checkFreeSocket();

        disableCheckForUnconnectedSocket = false;
    }

    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
/*
        if (!_listenersAreRegistered) {
            _stateMap.forEach((conditionalNG, state) -> {
                // If _timerState is not TimerState.Off, the timer was running when listeners wss unregistered
                if ((_startImmediately) || (state._timerState != TimerState.Off)) {
                    if (state._timerState == TimerState.Off) {
                        state._timerState = TimerState.RunNow;
                    }
                    conditionalNG.execute();
                }
            });
            _listenersAreRegistered = true;
        }
*/
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
/*
        synchronized(this) {
            _stateMap.forEach((conditionalNG, state) -> {
                // stopTimer() will not return until the timer task
                // is cancelled and stopped.
                if (state._timerTask != null) state._timerTask.stopTimer();
                state._timerTask = null;
                state._timerState = TimerState.Off;
            });
        }
        _listenersAreRegistered = false;
*/
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        // Do nothing. LogixNG_SelectNamedBean is used only to get a direct
//        // Sensor bean, not an indirect one. propertyChange() is called only
//        // for indirect beans by LogixNG_SelectNamedBean.
        getConditionalNG().execute();
    }

    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
        synchronized(this) {
            _stateMap.forEach((conditionalNG, state) -> {
                if (state._timerTask != null) state._timerTask.stopTimer();
                state._timerTask = null;
            });
        }
    }


    private static class ActionEntry {
//        private int _delay;
        private String _socketSystemName;
        private final FemaleDigitalActionSocket _socket;

        private ActionEntry(FemaleDigitalActionSocket socket, String socketSystemName) {
//        private ActionEntry(int delay, FemaleDigitalActionSocket socket, String socketSystemName) {
//            _delay = delay;
            _socketSystemName = socketSystemName;
            _socket = socket;
        }

        private ActionEntry(FemaleDigitalActionSocket socket) {
            this._socket = socket;
        }

        private long getDelay() {
            FemaleSocketConfiguration config = _socket.getConfiguration();
            if (config instanceof IndependentTimerSocketConfiguration) {
                IndependentTimerSocketConfiguration c =
                        (IndependentTimerSocketConfiguration) config;
                return c._delay * c._unit.getMultiply();
            } else {
                throw new IllegalArgumentException("Configuration is not a IndependentTimerSocketConfiguration");
            }
        }

    }


    public static class ActionData {
        private String _socketName;
        private String _socketSystemName;
        private IndependentTimerSocketConfiguration _socketConfig;

        public ActionData(String socketName, String socketSystemName, IndependentTimerSocketConfiguration socketConfig) {
            _socketName = socketName;
            _socketSystemName = socketSystemName;
            _socketConfig = socketConfig;
        }
    }


    private enum TimerState {
        Off,
        RunNow,
        WaitToRun,
        Running,
        Completed,
    }


    public static class IndependentTimerSocketConfiguration
            implements FemaleSocketConfiguration {

        private int _delay;
        private TimerUnit _unit = TimerUnit.MilliSeconds;

        /** {@inheritDoc} */
        @Override
        public void copyConfiguration(FemaleSocketConfiguration config) {
            if (!(config instanceof IndependentTimerSocketConfiguration)) {
                throw new IllegalArgumentException("Configuration is not a IndependentTimerSocketConfiguration");
            }
            IndependentTimerSocketConfiguration c =
                    (IndependentTimerSocketConfiguration) config;
            _delay = c._delay;
            _unit = c._unit;
        }

        /**
         * Get the unit
         * @return the unit
         */
        public TimerUnit getUnit() {
            return _unit;
        }

        /**
         * Get the delay.
         * @param actionSocket the socket
         * @return the delay
         */
        public int getDelay(int actionSocket) {
            return _delay;
        }

        /**
         * Set the delay.
         * @param actionSocket the socket
         * @param delay the delay
         */
        public void setDelay(int actionSocket, int delay) {
            _delay = delay;
        }

        /**
         * Set the unit
         * @param unit the unit
         */
        public void setUnit(TimerUnit unit) {
            _unit = unit;
        }

        @Override
        public String getDescription(Locale locale) {
            return "Delay 30 seconds";
        }

    }


    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IndependentTimer.class);

}
