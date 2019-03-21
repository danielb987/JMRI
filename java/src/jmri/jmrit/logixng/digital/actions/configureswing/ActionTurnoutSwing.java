package jmri.jmrit.logixng.digital.actions.configureswing;

import javax.swing.JPanel;
import javax.swing.JTextField;
import jmri.InstanceManager;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;

/**
 *
 */
public class ActionTurnoutSwing implements SwingConfiguratorInterface {

    @Override
    public JPanel getConfigPanel(Object object) throws IllegalArgumentException {
        JPanel panel = new JPanel();
        panel.add(new JTextField("Kalle"));
        return panel;
    }

    @Override
    public MaleSocket createNewObject(String systemName, String userName) {
        System.out.format("System name: %s, user name: %s%n", systemName, userName);
        ActionTurnout action = new ActionTurnout(systemName, userName);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

}
