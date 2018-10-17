package jmri.jmrit.newlogix.swing;

import java.awt.Dimension;
import jmri.jmrit.newlogix.NewLogixPreferences;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataListener;
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
        add(new JTitledSeparator(Bundle.getMessage("TitlePluginClassesPanel")));
        add(getPluginClassesPanel());
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

    class ClassListModel implements ListModel<String> {
        
        Set<ListDataListener> listeners = new HashSet<>();
        
        @Override
        public int getSize() {
            return 5;
        }

        @Override
        public String getElementAt(int index) {
            return String.format("Element no %d", index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }
    }

    private JPanel getJarFilePanel(String jarFileName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        ClassListModel expressionListModel = new ClassListModel();
        JList<String> expressionsList = new JList<>(expressionListModel);
        panel.add(expressionsList);
        
        JPanel addRemoveExpressionButtonPanel = new JPanel();
        addRemoveExpressionButtonPanel.setLayout(
                new BoxLayout(addRemoveExpressionButtonPanel, BoxLayout.X_AXIS));
        JButton addExpressionButton = new JButton(
                Bundle.getMessage("LabelButtonAddExpressionPlugin"));
        JButton removeExpressionButton = new JButton(
                Bundle.getMessage("LabelButtonRemoveExpressionPlugin"));
        addRemoveExpressionButtonPanel.add(addExpressionButton);
        addRemoveExpressionButtonPanel.add(removeExpressionButton);
        panel.add(addRemoveExpressionButtonPanel);
        
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(3,50));
        
        ClassListModel actionListModel = new ClassListModel();
        JList<String> actionList = new JList<>(actionListModel);
        panel.add(actionList);
        
        JPanel addRemoveActionButtonPanel = new JPanel();
        addRemoveActionButtonPanel.setLayout(
                new BoxLayout(addRemoveActionButtonPanel, BoxLayout.X_AXIS));
        JButton addActionButton = new JButton(
                Bundle.getMessage("LabelButtonAddActionPlugin"));
        JButton removeActionButton = new JButton(
                Bundle.getMessage("LabelButtonRemoveActionPlugin"));
        addRemoveActionButtonPanel.add(addActionButton);
        addRemoveActionButtonPanel.add(removeActionButton);
        panel.add(addRemoveActionButtonPanel);
        
        return panel;
    }
    
    private JPanel getPluginClassesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JPanel jarPanel1 = getJarFilePanel("File 1");
        JPanel jarPanel2 = getJarFilePanel("File 2");
//        JPanel jarPanel1 = new JPanel();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("JAR file 1", jarPanel1);
        tabbedPane.addTab("JAR file 2", jarPanel2);
        
        panel.add(tabbedPane);
        
        JPanel addRemoveButtonsPanel = new JPanel();
        addRemoveButtonsPanel.setLayout(
                new BoxLayout(addRemoveButtonsPanel, BoxLayout.X_AXIS));
        JButton addJarFile = new JButton(Bundle.getMessage("LabelButtonAddJarFile"));
        JButton removeJarFile = new JButton(Bundle.getMessage("LabelButtonRemoveJarFile"));
        addRemoveButtonsPanel.add(addJarFile);
        addRemoveButtonsPanel.add(removeJarFile);
        panel.add(addRemoveButtonsPanel);
        
        return panel;
/*        
        //Lay out the label and scroll pane from top to bottom.
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(labelText);
        ...
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(setButton);
        
        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
*/
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
