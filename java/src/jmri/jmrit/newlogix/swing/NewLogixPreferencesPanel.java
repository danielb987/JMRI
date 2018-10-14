package jmri.jmrit.newlogix.swing;

import jmri.jmrit.newlogix.NewLogixPreferences;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import org.openide.util.lookup.ServiceProvider;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import jmri.InstanceManager;
import jmri.swing.JTitledSeparator;
import jmri.swing.PreferencesPanel;

/**
 * Preferences panel for NewLogix
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
@ServiceProvider(service = PreferencesPanel.class)
public class NewLogixPreferencesPanel extends JPanel implements PreferencesPanel {
    
    private NewLogixPreferences preferences;
//    private jmri.web.server.WebServerPreferences apreferences;
    
    JCheckBox _startNewLogixOnLoadCheckBox;
    JCheckBox _allowDebugModeCheckBox;
    
    public NewLogixPreferencesPanel() {
        preferences = InstanceManager.getDefault(NewLogixPreferences.class);
        initGUI();
        setGUI();
    }

    private void initGUI() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JTitledSeparator(Bundle.getMessage("TitleStartupSettingsPanel")));
        add(getStartupPanel());
        add(new JTitledSeparator(Bundle.getMessage("TitleTimeDiagramColorsPanel")));
        add(getTimeDiagramColorsPanel());
//        add(new JTitledSeparator(Bundle.getMessage("TitleNetworkPanel")));
//        add(networkPanel());
//        add(new JTitledSeparator(Bundle.getMessage("TitleControllersPanel")));
//        add(allowedControllers());
        
//        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//        add(new JTitledSeparator(Bundle.getMessage("TitleRailroadNamePreferences")));
//        add(getNewLogixPanel());
    }

    private void setGUI() {
        _startNewLogixOnLoadCheckBox.setSelected(preferences.getStartNewLogixOnStartup());
    }

    /**
     * set the local prefs to match the GUI Local prefs are independent from the
     * singleton instance prefs.
     *
     * @return true if set, false if values are unacceptable.
     */
    private boolean setValues() {
        boolean didSet = true;
        preferences.setStartNewLogixOnStartup(_startNewLogixOnLoadCheckBox.isSelected());
        return didSet;
    }
    
    private JPanel getStartupPanel() {
        JPanel panel = new JPanel();

        _startNewLogixOnLoadCheckBox = new JCheckBox(Bundle.getMessage("LabelStartNewLogixOnLoad"));
        _startNewLogixOnLoadCheckBox.setToolTipText(Bundle.getMessage("ToolTipStartNewLogixOnLoad"));

        _allowDebugModeCheckBox = new JCheckBox(Bundle.getMessage("LabelAllowDebugMode"));
        _allowDebugModeCheckBox.setToolTipText(Bundle.getMessage("ToolTipLabelAllowDebugMode"));

        JPanel gridPanel = new JPanel(new GridLayout(0, 1));
        
        gridPanel.add(_startNewLogixOnLoadCheckBox);
        gridPanel.add(_allowDebugModeCheckBox);
        
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.add(gridPanel);

        return panel;
    }

    private JPanel getTimeDiagramColorsPanel() {
        return new JPanel();
    }
    
    @Override
    public String getPreferencesItem() {
        return "NEWLOGIX"; // NOI18N
    }

    @Override
    public String getPreferencesItemText() {
        return Bundle.getMessage("MenuNewLogix"); // NOI18N
    }

    @Override
    public String getTabbedPreferencesTitle() {
        return getPreferencesItemText();
    }

    @Override
    public String getLabelKey() {
        return null;
    }

    @Override
    public JComponent getPreferencesComponent() {
        return this;
    }

    @Override
    public boolean isPersistant() {
        return false;
    }

    @Override
    public String getPreferencesTooltip() {
        return null;
    }

    @Override
    public void savePreferences() {
        if (setValues()) {
            preferences.save();
        }
    }

    @Override
    public boolean isDirty() {
        return preferences.isDirty();
    }

    @Override
    public boolean isRestartRequired() {
        return preferences.isRestartRequired();
    }

    @Override
    public boolean isPreferencesValid() {
        return true; // no validity checking performed
    }

}
