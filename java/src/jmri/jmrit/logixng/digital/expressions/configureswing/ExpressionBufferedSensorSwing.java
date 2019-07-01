package jmri.jmrit.logixng.digital.expressions.configureswing;

import javax.annotation.Nonnull;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.digital.expressions.ExpressionSensor.SensorState;
import jmri.jmrit.logixng.digital.expressions.ExpressionBufferedSensor;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.util.swing.BeanSelectCreatePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configures an ExpressionBufferedSensor object with a Swing JPanel.
 */
public class ExpressionBufferedSensorSwing implements SwingConfiguratorInterface {

    private JPanel panel;
    private BeanSelectCreatePanel<Sensor> sensorBeanPanel;
    private JComboBox<Is_IsNot_Enum> is_IsNot_ComboBox;
    private JComboBox<SensorState> stateComboBox;
    
    
    /** {@inheritDoc} */
    @Override
    public JPanel getConfigPanel() throws IllegalArgumentException {
        createPanel(null);
        return panel;
    }
    
    /** {@inheritDoc} */
    @Override
    public JPanel getConfigPanel(@Nonnull Base object) throws IllegalArgumentException {
        createPanel(object);
        return panel;
    }
    
    private void createPanel(Base object) {
        ExpressionBufferedSensor expression = (ExpressionBufferedSensor)object;
        
        panel = new JPanel();
        sensorBeanPanel = new BeanSelectCreatePanel<>(InstanceManager.getDefault(SensorManager.class), null);
        
        is_IsNot_ComboBox = new JComboBox<>();
        for (Is_IsNot_Enum e : Is_IsNot_Enum.values()) {
            is_IsNot_ComboBox.addItem(e);
        }
        
        stateComboBox = new JComboBox<>();
        for (SensorState e : SensorState.values()) {
            stateComboBox.addItem(e);
        }
        
        if (expression != null) {
            if (expression.getSensor() != null) {
                sensorBeanPanel.setDefaultNamedBean(expression.getSensor().getBean());
            }
            is_IsNot_ComboBox.setSelectedItem(expression.get_Is_IsNot());
            stateComboBox.setSelectedItem(expression.getSensorState());
        }
        
        panel.add(new JLabel(Bundle.getMessage("BeanNameSensor")));
        panel.add(sensorBeanPanel);
        panel.add(is_IsNot_ComboBox);
        panel.add(stateComboBox);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean validate(@Nonnull StringBuilder errorMessage) {
        if (1==0) {
            errorMessage.append("An error");
            return false;
        }
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName) {
        System.out.format("System name: %s%n", systemName);
        ExpressionBufferedSensor expression = new ExpressionBufferedSensor(systemName);
        try {
            Sensor turnout = sensorBeanPanel.getNamedBean();
            if (turnout != null) {
                NamedBeanHandle<Sensor> handle
                        = InstanceManager.getDefault(NamedBeanHandleManager.class)
                                .getNamedBeanHandle(turnout.getDisplayName(), turnout);
                expression.setSensor(handle);
            }
            expression.set_Is_IsNot((Is_IsNot_Enum)is_IsNot_ComboBox.getSelectedItem());
            expression.setSensorState((SensorState)stateComboBox.getSelectedItem());
        } catch (JmriException ex) {
            log.error("Cannot get NamedBeanHandle for turnout", ex);
        }
        return InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @Nonnull String userName) {
        System.out.format("System name: %s, user name: %s%n", systemName, userName);
        ExpressionBufferedSensor expression = new ExpressionBufferedSensor(systemName, userName);
        try {
            Sensor turnout = sensorBeanPanel.getNamedBean();
            if (turnout != null) {
                NamedBeanHandle<Sensor> handle
                        = InstanceManager.getDefault(NamedBeanHandleManager.class)
                                .getNamedBeanHandle(turnout.getDisplayName(), turnout);
                expression.setSensor(handle);
            }
            expression.set_Is_IsNot((Is_IsNot_Enum)is_IsNot_ComboBox.getSelectedItem());
            expression.setSensorState((SensorState)stateComboBox.getSelectedItem());
        } catch (JmriException ex) {
            log.error("Cannot get NamedBeanHandle for turnout", ex);
        }
        return InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
    }
    
    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        ExpressionBufferedSensor expression = (ExpressionBufferedSensor)object;
        try {
            Sensor turnout = sensorBeanPanel.getNamedBean();
            if (turnout != null) {
                NamedBeanHandle<Sensor> handle
                        = InstanceManager.getDefault(NamedBeanHandleManager.class)
                                .getNamedBeanHandle(turnout.getDisplayName(), turnout);
                expression.setSensor(handle);
            }
            expression.set_Is_IsNot((Is_IsNot_Enum)is_IsNot_ComboBox.getSelectedItem());
            expression.setSensorState((SensorState)stateComboBox.getSelectedItem());
        } catch (JmriException ex) {
            log.error("Cannot get NamedBeanHandle for turnout", ex);
        }
    }
    
    
    /**
     * Create Sensor object for the expression
     *
     * @param reference Sensor application description
     * @return The new output as Sensor object
     */
    protected Sensor getSensorFromPanel(String reference) {
        if (sensorBeanPanel == null) {
            return null;
        }
        sensorBeanPanel.setReference(reference); // pass turnout application description to be put into turnout Comment
        try {
            return sensorBeanPanel.getNamedBean();
        } catch (jmri.JmriException ex) {
            log.warn("skipping creation of turnout not found for " + reference);
            return null;
        }
    }
    
//    private void noSensorMessage(String s1, String s2) {
//        log.warn("Could not provide turnout " + s2);
//        String msg = Bundle.getMessage("WarningNoSensor", new Object[]{s1, s2});
//        JOptionPane.showMessageDialog(editFrame, msg,
//                Bundle.getMessage("WarningTitle"), JOptionPane.ERROR_MESSAGE);
//    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Bundle.getMessage("BufferedSensor_Short");
    }
    
    @Override
    public void dispose() {
        if (sensorBeanPanel != null) {
            sensorBeanPanel.dispose();
        }
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ExpressionBufferedSensorSwing.class);
    
}
