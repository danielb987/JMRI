package jmri.jmrit.logixng.digital.expressions.configureswing;

import javax.annotation.Nonnull;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.digital.expressions.ResetOnTrue;
import jmri.jmrit.logixng.digital.expressions.TriggerOnce;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.util.swing.BeanSelectCreatePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configures an ExpressionTurnout object with a Swing JPanel.
 */
public class TriggerOnceSwing implements SwingConfiguratorInterface {

    private JPanel panel;
    
    
    /** {@inheritDoc} */
    @Override
    public JPanel getConfigPanel() throws IllegalArgumentException {
        createPanel();
        return panel;
    }
    
    /** {@inheritDoc} */
    @Override
    public JPanel getConfigPanel(@Nonnull Base object) throws IllegalArgumentException {
        createPanel();
        return panel;
    }
    
    private void createPanel() {
        panel = new JPanel();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean validate(@Nonnull StringBuilder errorMessage) {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName) {
        TriggerOnce expression;
        try {
            expression = new TriggerOnce(systemName, null);
        } catch (SocketAlreadyConnectedException ex) {
            log.error("SocketAlreadyConnectedException", ex);
            return null;
        }
        return InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @Nonnull String userName) {
        TriggerOnce expression;
        try {
            expression = new TriggerOnce(systemName, null);
        } catch (SocketAlreadyConnectedException ex) {
            log.error("SocketAlreadyConnectedException", ex);
            return null;
        }
        return InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
    }
    
    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Bundle.getMessage("TriggerOnce_Short");
    }
    
    @Override
    public void dispose() {
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(TriggerOnceSwing.class);
    
}
