package jmri.jmrit.logixng.digital.actions.configureswing;

import javax.annotation.Nonnull;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jmri.InstanceManager;
import jmri.Turnout;
import jmri.TurnoutManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.util.swing.BeanSelectCreatePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configures an ActionTurnout object with a Swing JPanel.
 */
public class ActionTurnoutSwing implements SwingConfiguratorInterface {

    public enum TurnoutState {
        CLOSED(Turnout.CLOSED, InstanceManager.getDefault(TurnoutManager.class).getClosedText()),
        THROWN(Turnout.THROWN, InstanceManager.getDefault(TurnoutManager.class).getThrownText()),
        OTHER(-1, Bundle.getMessage("TurnoutOtherStatus"));
        
        private final int _id;
        private final String _text;
        
        private TurnoutState(int id, String text) {
            this._id = id;
            this._text = text;
        }
        
        static public TurnoutState get(int id) {
            switch (id) {
                case Turnout.CLOSED:
                    return CLOSED;
                    
                case Turnout.THROWN:
                    return THROWN;
                    
                default:
                    return OTHER;
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
    
//    private final String stateThrown = InstanceManager.turnoutManagerInstance().getThrownText();
//    private final String stateClosed = InstanceManager.turnoutManagerInstance().getClosedText();
//    private final String[] turnoutStates = new String[]{stateClosed, stateThrown};
//    private final int[] turnoutStateValues = new int[]{Turnout.CLOSED, Turnout.THROWN};
    
    private JPanel panel;
    private BeanSelectCreatePanel turnoutBeanPanel;
    private JComboBox<Is_IsNot_Enum> is_IsNot_ComboBox;
    private JComboBox<TurnoutState> stateComboBox;
    
    
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
        turnoutBeanPanel = new BeanSelectCreatePanel<>(InstanceManager.getDefault(TurnoutManager.class), null);
        
        is_IsNot_ComboBox = new JComboBox<>();
        for (Is_IsNot_Enum e : Is_IsNot_Enum.values()) {
            is_IsNot_ComboBox.addItem(e);
        }
        
        stateComboBox = new JComboBox<>();
        for (TurnoutState e : TurnoutState.values()) {
            stateComboBox.addItem(e);
        }
        
        panel.add(new JLabel("Turnout"));
        panel.add(turnoutBeanPanel);
        panel.add(is_IsNot_ComboBox);
        panel.add(stateComboBox);
//        panel.add(new JTextField("Kalle"));
/*        
        panel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.EAST;
        panel.add(_sysNameLabel, c);
        c.gridy = 1;
        panel.add(_userNameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
        panel.add(_systemName, c);
        c.gridy = 1;
        panel.add(_addUserName, c);
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
        c.gridy = 0;
        panel.add(_autoSystemName, c);
*/        
        
        
        
        
        
        
        
//            eto1.setVisible(true);
//            eto1.setDefaultNamedBean(((jmri.implementation.LsDecSignalHead) curS).getGreen().getBean());
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
        ActionTurnout action = new ActionTurnout(systemName);
        // nbhm.getNamedBeanHandle(to1.getDisplayName(), t1);
        
//        Turnout t1 = getTurnoutFromPanel(to1, "SignalHead:" + systemNameTextField.getText() + ":Green");
        
//                    if (t1 == null) {
//                        addTurnoutMessage(v1Border.getTitle(), to1.getDisplayName());
//                    }
        
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @Nonnull String userName) {
        System.out.format("System name: %s, user name: %s%n", systemName, userName);
        ActionTurnout action = new ActionTurnout(systemName, userName);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }
    
    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        
    }
    
    
    /**
     * Create Turnout object for the action
     *
     * @param reference Turnout application description
     * @return The new output as Turnout object
     */
    protected Turnout getTurnoutFromPanel(String reference) {
        if (turnoutBeanPanel == null) {
            return null;
        }
        turnoutBeanPanel.setReference(reference); // pass turnout application description to be put into turnout Comment
        try {
            return (Turnout) turnoutBeanPanel.getNamedBean();
        } catch (jmri.JmriException ex) {
            log.warn("skipping creation of turnout not found for " + reference);
            return null;
        }
    }
    
    private void noTurnoutMessage(String s1, String s2) {
        log.warn("Could not provide turnout " + s2);
//        String msg = Bundle.getMessage("WarningNoTurnout", new Object[]{s1, s2});
//        JOptionPane.showMessageDialog(editFrame, msg,
//                Bundle.getMessage("WarningTitle"), JOptionPane.ERROR_MESSAGE);
    }
    
    @Override
    public void dispose() {
        if (turnoutBeanPanel != null) {
            turnoutBeanPanel.dispose();
        }
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ActionTurnoutSwing.class);
    
}
