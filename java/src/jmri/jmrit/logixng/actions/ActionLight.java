package jmri.jmrit.logixng.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jmri.*;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.util.LogixNG_SelectEnum;
import jmri.jmrit.logixng.util.LogixNG_SelectInteger;
import jmri.jmrit.logixng.util.LogixNG_SelectNamedBean;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.util.ThreadingUtil;

/**
 * This action sets the state of a light.
 *
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionLight extends AbstractDigitalAction
        implements PropertyChangeListener {

    private final LogixNG_SelectNamedBean<Light> _selectNamedBean =
            new LogixNG_SelectNamedBean<>(
                    this, Light.class, InstanceManager.getDefault(LightManager.class), this);

    private final LogixNG_SelectEnum<LightState> _selectEnum =
            new LogixNG_SelectEnum<>(this, LightState.values(), LightState.On, this);

    private final LogixNG_SelectInteger _selectLightValue =
            new LogixNG_SelectInteger(this, this);


    public ActionLight(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    @Override
    public Base getDeepCopy(Map<String, String> systemNames, Map<String, String> userNames) throws ParserException {
        DigitalActionManager manager = InstanceManager.getDefault(DigitalActionManager.class);
        String sysName = systemNames.get(getSystemName());
        String userName = userNames.get(getSystemName());
        if (sysName == null) sysName = manager.getAutoSystemName();
        ActionLight copy = new ActionLight(sysName, userName);
        copy.setComment(getComment());
        _selectNamedBean.copy(copy._selectNamedBean);
        _selectEnum.copy(copy._selectEnum);
        _selectLightValue.copy(copy._selectLightValue);
        return manager.registerAction(copy);
    }

    public LogixNG_SelectNamedBean<Light> getSelectNamedBean() {
        return _selectNamedBean;
    }

    public LogixNG_SelectEnum<LightState> getSelectEnum() {
        return _selectEnum;
    }

    public LogixNG_SelectInteger getSelectLightValue() {
        return _selectLightValue;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws JmriException {
        ConditionalNG conditionalNG = getConditionalNG();
        Light light = _selectNamedBean.evaluateNamedBean(conditionalNG);

        if (light == null) return;

        LightState state = _selectEnum.evaluateEnum(conditionalNG);

        int tempValue;
        if (state == LightState.Intensity || state == LightState.Interval) {
            tempValue = _selectLightValue.evaluateValue(conditionalNG);
            if (tempValue < 0) tempValue = 0;
            if (tempValue > 100) tempValue = 100;
        } else {
            tempValue = 0;
        }

        // Local variables referenced from a lambda expression must be final or effectively final
        int value = tempValue;

        ThreadingUtil.runOnLayoutWithJmriException(() -> {
            if (state == LightState.Toggle) {
                if (light.getKnownState() == Light.ON) {
                    light.setCommandedState(Light.OFF);
                } else {
                    light.setCommandedState(Light.ON);
                }
            } else if (state == LightState.Intensity) {
                if (light instanceof VariableLight) {
                    ((VariableLight)light).setTargetIntensity(value / 100.0);
                } else {
                    light.setCommandedState(value > 50 ? Light.ON : Light.OFF);
                }
            } else if (state == LightState.Interval) {
                if (light instanceof VariableLight) {
                    ((VariableLight)light).setTransitionTime(value);
                }
            } else {
                light.setCommandedState(state.getID());
            }
        });
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
        return Bundle.getMessage(locale, "Light_Short");
    }

    @Override
    public String getLongDescription(Locale locale) {
        String namedBean = _selectNamedBean.getDescription(locale);
        String state = _selectEnum.getDescription(locale);

        if (_selectEnum.getAddressing() == NamedBeanAddressing.Direct) {
            if (_selectEnum.getEnum() == LightState.Intensity || _selectEnum.getEnum() == LightState.Interval) {
                String bundleKey = "Light_Long_Value";

                if (_selectLightValue.isDirectAddressing()) {
                    String type = _selectEnum.getEnum() == LightState.Intensity ?
                             Bundle.getMessage("Light_Intensity_Value") :
                             Bundle.getMessage("Light_Interval_Value");
                    return Bundle.getMessage(locale, bundleKey, namedBean, type, _selectLightValue.getValue());
                } else {
                    return Bundle.getMessage(locale, bundleKey, namedBean, "", _selectLightValue.getDescription(locale));
                }
            }
        }

        return Bundle.getMessage(locale, "Light_Long", namedBean, state);
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
        _selectNamedBean.registerListeners();
        _selectEnum.registerListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        _selectNamedBean.unregisterListeners();
        _selectEnum.unregisterListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }


    // This constant is only used internally in LightState but must be outside
    // the enum.
    private static final int TOGGLE_ID = -1;
    private static final int INTENSITY_ID = -2;
    private static final int INTERVAL_ID = -3;


    public enum LightState {
        Off(Light.OFF, Bundle.getMessage("StateOff")),
        On(Light.ON, Bundle.getMessage("StateOn")),
        Toggle(TOGGLE_ID, Bundle.getMessage("LightToggleStatus")),
        Intensity(INTENSITY_ID, Bundle.getMessage("LightIntensity")),
        Interval(INTERVAL_ID, Bundle.getMessage("LightInterval")),
        Unknown(Light.UNKNOWN, Bundle.getMessage("BeanStateUnknown")),
        Inconsistent(Light.INCONSISTENT, Bundle.getMessage("BeanStateInconsistent"));

        private final int _id;
        private final String _text;

        private LightState(int id, String text) {
            this._id = id;
            this._text = text;
        }

        static public LightState get(int id) {
            switch (id) {
                case Light.UNKNOWN:
                    return Unknown;

                case Light.INCONSISTENT:
                    return Inconsistent;

                case Light.OFF:
                    return Off;

                case Light.ON:
                    return On;

                case TOGGLE_ID:
                    return Toggle;

                default:
                    throw new IllegalArgumentException("invalid light state");
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

    /** {@inheritDoc} */
    @Override
    public void getUsageDetail(int level, NamedBean bean, List<NamedBeanUsageReport> report, NamedBean cdl) {
        _selectNamedBean.getUsageDetail(level, bean, report, cdl, this, LogixNG_SelectNamedBean.Type.Action);
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getConditionalNG().execute();
    }

//    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionLight.class);

}
