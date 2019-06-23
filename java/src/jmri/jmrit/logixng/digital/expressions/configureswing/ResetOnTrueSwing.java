package jmri.jmrit.logixng.digital.expressions.configureswing;

import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.swing.JPanel;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.NamedBean.BadUserNameException;
import jmri.NamedBean.BadSystemNameException;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Turnout;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.digital.expressions.ResetOnTrue;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout.TurnoutState;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configures an ResetOnTrue object with a Swing JPanel.
 */
public class ResetOnTrueSwing implements SwingConfiguratorInterface {

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
    public MaleSocket createNewObject(@Nonnull String systemName)
            throws BadUserNameException, BadSystemNameException {
        ResetOnTrue expression = new ResetOnTrue(systemName, null);
        return InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @Nonnull String userName)
            throws BadUserNameException, BadSystemNameException {
        ResetOnTrue expression = new ResetOnTrue(systemName, userName);
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
        return Bundle.getMessage("ResetOnTrue_Short");
    }
    
    @Override
    public void dispose() {
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ResetOnTrueSwing.class);
    
}
