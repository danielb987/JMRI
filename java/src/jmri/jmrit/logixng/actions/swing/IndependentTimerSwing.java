package jmri.jmrit.logixng.actions.swing;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.swing.*;

import jmri.InstanceManager;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.actions.IndependentTimer;
import jmri.jmrit.logixng.actions.IndependentTimer.IndependentTimerSocketConfiguration;
import jmri.jmrit.logixng.swing.FemaleSocketConfigurationSwing;
import jmri.jmrit.logixng.util.TimerUnit;
import jmri.util.swing.JComboBoxUtil;

/**
 * Configures an IndependentTimer object with a Swing JPanel.
 *
 * @author Daniel Bergqvist Copyright 2023
 */
public class IndependentTimerSwing extends AbstractDigitalActionSwing {

    private JCheckBox _startImmediately;
    private JCheckBox _runContinuously;

    @Override
    protected void createPanel(@CheckForNull Base object, @Nonnull JPanel buttonPanel) {
        if ((object != null) && !(object instanceof IndependentTimer)) {
            throw new IllegalArgumentException("object must be an IndependentTimer but is a: "+object.getClass().getName());
        }

        // Create a temporary action in case we don't have one.
        IndependentTimer action = object != null ? (IndependentTimer)object : new IndependentTimer("IQDA1", null);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        _startImmediately = new JCheckBox(Bundle.getMessage("IndependentTimerSwing_StartImmediately"));
        _startImmediately.setSelected(action.isStartImmediately());
        panel.add(_startImmediately);

        _runContinuously = new JCheckBox(Bundle.getMessage("IndependentTimerSwing_RunContinuously"));
        _runContinuously.setSelected(action.isRunContinuously());
        panel.add(_runContinuously);
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate(@Nonnull List<String> errorMessages) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @CheckForNull String userName) {
        IndependentTimer action = new IndependentTimer(systemName, userName);
        updateObject(action);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        if (!(object instanceof IndependentTimer)) {
            throw new IllegalArgumentException("object must be an IndependentTimer but is a: "+object.getClass().getName());
        }

        IndependentTimer action = (IndependentTimer)object;

        action.setStartImmediately(_startImmediately.isSelected());
        action.setRunContinuously(_runContinuously.isSelected());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Bundle.getMessage("IndependentTimer_Short");
    }

    @Override
    public void dispose() {
    }


    public static class IndependentTimerSocketConfigurationSwing
            implements FemaleSocketConfigurationSwing {

        private JTabbedPane _tabbedPane;
        private JPanel _panelDirect;
        private JPanel _panelLocalVariable;
        private JTextField _delayTextField;
        private JComboBox<TimerUnit> _unitComboBox;
        private JTextField _delayLocalVariable;

        @Override
        public JPanel getConfigPanel(FemaleSocketConfiguration config, JPanel buttonPanel) throws IllegalArgumentException {
            if (!(config instanceof IndependentTimerSocketConfiguration)) {
                throw new IllegalArgumentException("Configuration is not a IndependentTimerSocketConfiguration");
            }
            IndependentTimerSocketConfiguration c =
                    (IndependentTimerSocketConfiguration) config;

            JPanel panel = new JPanel();

            _tabbedPane = new JTabbedPane();
            _panelDirect = new JPanel();
            _panelLocalVariable = new JPanel();

            _panelDirect.setLayout(new java.awt.GridBagLayout());
            java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = java.awt.GridBagConstraints.EAST;
            _panelDirect.add(new JLabel(Bundle.getMessage("IndependentTimerSwing_Config_Delay")), constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = java.awt.GridBagConstraints.WEST;
            constraints.weightx = 1.0;
            constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
            _delayTextField = new JTextField(20);
            _delayTextField.setText(Integer.toString(c.getDelay()));
            _panelDirect.add(_delayTextField, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = java.awt.GridBagConstraints.EAST;
            _panelDirect.add(new JLabel(Bundle.getMessage("IndependentTimerSwing_Config_Unit")), constraints);
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = java.awt.GridBagConstraints.WEST;
            constraints.weightx = 1.0;
            constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
            _unitComboBox = new JComboBox<>();
            for (TimerUnit u : TimerUnit.values()) _unitComboBox.addItem(u);
            JComboBoxUtil.setupComboBoxMaxRows(_unitComboBox);
            _unitComboBox.setSelectedItem(c.getUnit());
            _panelDirect.add(_unitComboBox, constraints);

            _delayLocalVariable = new JTextField(20);
            _delayLocalVariable.setText(c.getDelayLocalVariable());
            _panelLocalVariable.add(_delayLocalVariable);

            _tabbedPane.add(Bundle.getMessage("IndependentTimerSwing_Config_Direct"), _panelDirect);
            _tabbedPane.add(Bundle.getMessage("IndependentTimerSwing_Config_LocalVariable"), _panelLocalVariable);

            if (c.isDelayByLocalVariable()) {
                _tabbedPane.setSelectedComponent(_panelLocalVariable);
            }

            panel.add(_tabbedPane);

            return panel;
        }

        @Override
        public boolean validate(List<String> errorMessages) {
            if (_tabbedPane.getSelectedComponent() == _panelDirect) {
                try {
                    Integer.parseInt(_delayTextField.getText());
                } catch (NumberFormatException e) {
                    errorMessages.add(Bundle.getMessage("IndependentTimerSwing_InvalidDelay",
                            _delayTextField.getText()));
                }
            }
            return errorMessages.isEmpty();
        }

        /**{@inheritDoc} */
        @Override
        public void updateObject(@Nonnull FemaleSocketConfiguration config) {
            if (!(config instanceof IndependentTimerSocketConfiguration)) {
                throw new IllegalArgumentException("Configuration is not a IndependentTimerSocketConfiguration");
            }
            IndependentTimerSocketConfiguration c =
                    (IndependentTimerSocketConfiguration) config;

            boolean delayByLocalVariable =
                    _tabbedPane.getSelectedComponent() == _panelLocalVariable;
            c.setDelayByLocalVariable(delayByLocalVariable);
            if (delayByLocalVariable) {
                c.setDelayLocalVariable(_delayLocalVariable.getText());
            } else {
                c.setDelay(Integer.parseInt(_delayTextField.getText()));
                c.setUnit(_unitComboBox.getItemAt(_unitComboBox.getSelectedIndex()));
            }
        }

    }

}
