package jmri.jmrit.logixng.digital.actions.configureswing;

import javax.annotation.Nonnull;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;

/**
 *
 */
public class ActionTurnoutSwing implements SwingConfiguratorInterface {

    /** {@inheritDoc} */
    @Override
    public JPanel getConfigPanel() throws IllegalArgumentException {
        JPanel panel = new JPanel();
        panel.add(new JTextField("Kalle"));
        return panel;
    }

    /** {@inheritDoc} */
    @Override
    public JPanel getConfigPanel(@Nonnull Base object) throws IllegalArgumentException {
        JPanel panel = new JPanel();
        panel.add(new JTextField("Kalle"));
        return panel;
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName) {
        System.out.format("System name: %s%n", systemName);
        ActionTurnout action = new ActionTurnout(systemName);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @Nonnull String userName) {
        System.out.format("System name: %s, user name: %s%n", systemName, userName);
        ActionTurnout action = new ActionTurnout(systemName, userName);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

}
