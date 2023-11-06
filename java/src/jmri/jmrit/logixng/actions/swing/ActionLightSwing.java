package jmri.jmrit.logixng.actions.swing;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.swing.*;

import jmri.InstanceManager;
import jmri.Light;
import jmri.LightManager;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.actions.ActionLight;
import jmri.jmrit.logixng.actions.ActionLight.LightState;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.jmrit.logixng.util.swing.LogixNG_SelectEnumSwing;
import jmri.jmrit.logixng.util.swing.LogixNG_SelectIntegerSwing;
import jmri.jmrit.logixng.util.swing.LogixNG_SelectNamedBeanSwing;

/**
 * Configures an ActionLight object with a Swing JPanel.
 *
 * @author Daniel Bergqvist Copyright 2021
 */
public class ActionLightSwing extends AbstractDigitalActionSwing {

    private LogixNG_SelectNamedBeanSwing<Light> _selectNamedBeanSwing;
    private LogixNG_SelectEnumSwing<LightState> _selectEnumSwing;
    private LogixNG_SelectIntegerSwing _selectLightValueSwing;


    public ActionLightSwing() {
    }

    public ActionLightSwing(JDialog dialog) {
        super.setJDialog(dialog);
    }

    @Override
    protected void createPanel(@CheckForNull Base object, @Nonnull JPanel buttonPanel) {
        ActionLight action = (ActionLight)object;

        _selectNamedBeanSwing = new LogixNG_SelectNamedBeanSwing<>(
                InstanceManager.getDefault(LightManager.class), getJDialog(), this);

        _selectEnumSwing = new LogixNG_SelectEnumSwing<>(getJDialog(), this);

        _selectLightValueSwing = new LogixNG_SelectIntegerSwing(getJDialog(), this);

        panel = new JPanel();

        JPanel _tabbedPaneNamedBean;
        JPanel _tabbedPaneEnum;
        JPanel _tabbedPaneLightValue;

        if (action != null) {
            _tabbedPaneNamedBean = _selectNamedBeanSwing.createPanel(action.getSelectNamedBean());
            _tabbedPaneEnum = _selectEnumSwing.createPanel(action.getSelectEnum(), LightState.values());
            _tabbedPaneLightValue = _selectLightValueSwing.createPanel(action.getSelectLightValue());
        } else {
            // Create a temporary action
            action = new ActionLight("IQDA1", null);
            _tabbedPaneNamedBean = _selectNamedBeanSwing.createPanel(null);
            _tabbedPaneEnum = _selectEnumSwing.createPanel(null, LightState.values());
            _tabbedPaneLightValue = _selectLightValueSwing.createPanel(action.getSelectLightValue());
        }

        _selectEnumSwing.addEnumListener(e -> { setDataPanelState(); });

        setDataPanelState();

        JComponent[] components = new JComponent[]{
            _tabbedPaneNamedBean,
            _tabbedPaneEnum,
            _tabbedPaneLightValue};

        List<JComponent> componentList = SwingConfiguratorInterface.parseMessage(
                Bundle.getMessage("ActionLight_Components"), components);

        for (JComponent c : componentList) panel.add(c);
    }

    private void setDataPanelState() {
        boolean newState = _selectEnumSwing.isEnumSelectedOrIndirectAddressing(LightState.Intensity) ||
                _selectEnumSwing.isEnumSelectedOrIndirectAddressing(LightState.Interval);
        _selectLightValueSwing.setEnabled(newState);
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate(@Nonnull List<String> errorMessages) {
        // Create a temporary action to test formula
        ActionLight action = new ActionLight("IQDA1", null);

        _selectNamedBeanSwing.validate(action.getSelectNamedBean(), errorMessages);
        _selectEnumSwing.validate(action.getSelectEnum(), errorMessages);
        _selectLightValueSwing.validate(action.getSelectLightValue(), errorMessages);

        if (_selectLightValueSwing.isDirectAddressing()) {

            if (_selectEnumSwing.isEnumSelectedOrIndirectAddressing(LightState.Intensity)) {
                boolean result = true;
                try {
                    int value = _selectLightValueSwing.getValue(action.getSelectLightValue());
                    if (value < 0 || value > 100) {
                        result = false;
                    }
                } catch (NumberFormatException ex) {
                    result = false;
                }
                if (!result) {
                    errorMessages.add(Bundle.getMessage("Light_Error_Intensity"));
                    return false;
                }
            }

            if (_selectEnumSwing.isEnumSelectedOrIndirectAddressing(LightState.Interval)) {
                boolean result = true;
                try {
                    int value = _selectLightValueSwing.getValue(action.getSelectLightValue());
                    if (value < 0) {
                        result = false;
                    }
                } catch (NumberFormatException ex) {
                    result = false;
                }
                if (!result) {
                    errorMessages.add(Bundle.getMessage("Light_Error_Interval"));
                    return false;
                }
            }
        }

        return errorMessages.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @CheckForNull String userName) {
        ActionLight action = new ActionLight(systemName, userName);
        updateObject(action);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        if (! (object instanceof ActionLight)) {
            throw new IllegalArgumentException("object must be an ActionLight but is a: "+object.getClass().getName());
        }
        ActionLight action = (ActionLight)object;
        _selectNamedBeanSwing.updateObject(action.getSelectNamedBean());
        _selectEnumSwing.updateObject(action.getSelectEnum());
        _selectLightValueSwing.updateObject(action.getSelectLightValue());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Bundle.getMessage("Light_Short");
    }

    @Override
    public void dispose() {
        _selectNamedBeanSwing.dispose();
        _selectEnumSwing.dispose();
    }


//    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionLightSwing.class);

}
