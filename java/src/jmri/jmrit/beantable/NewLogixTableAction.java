package jmri.jmrit.beantable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import jmri.InstanceManager;
import jmri.Manager;
import jmri.NamedBean;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.NewLogixManager;
import jmri.UserPreferencesManager;
import jmri.jmrit.newlogix.tools.swing.NewLogixEditor;
import jmri.jmrit.sensorgroup.SensorGroupFrame;
import jmri.util.FileUtil;
import jmri.util.JmriJFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Swing action to create and register a NewLogix Table.
 * <p>
 * Also contains the panes to create, edit, and delete a NewLogix.
 * <p>
 * Most of the text used in this GUI is in BeanTableBundle.properties, accessed
 * via Bundle.getMessage().
 * <p>
 * Two additional action and variable name selection methods have been added:
 * <ol>
 *     <li>Single Pick List
 *     <li>Combo Box Selection
 * </ol>
 * The traditional tabbed Pick List with text entry is the default method.
 * The Options menu has been expanded to list the 3 methods.
 * Mar 27, 2017 - Dave Sand
 *
 * @author Dave Duchamp Copyright (C) 2007
 * @author Pete Cressman Copyright (C) 2009, 2010, 2011
 * @author Matthew Harris copyright (c) 2009
 * @author Dave Sand copyright (c) 2017
 * @author Daniel Bergqvist copyright (c) 2019
 */
public class NewLogixTableAction extends AbstractTableAction<NewLogix> {

    /**
     * Create a NewLogixManager instance.
     *
     * @param s the Action title, not the title of the resulting frame. Perhaps
     *          this should be changed?
     */
    public NewLogixTableAction(String s) {
        super(s);
        // set up managers - no need to use InstanceManager since both managers are
        // Default only (internal). We use InstanceManager to get managers for
        // compatibility with other facilities.
        _newLogixManager = InstanceManager.getNullableDefault(NewLogixManager.class);
        // disable ourself if there is no NewLogix manager
        if (_newLogixManager == null) {
            setEnabled(false);
        }
    }

    /**
     * Create a NewLogixManager instance with default title.
     */
    public NewLogixTableAction() {
        this(Bundle.getMessage("TitleNewLogixTable"));
    }

    // ------------ Methods for NewLogix Table Window ------------

    /**
     * Create the JTable DataModel, along with the changes (overrides of
     * BeanTableDataModel) for the specific case of a NewLogix table.
     */
    @Override
    protected void createModel() {
        m = new BeanTableDataModel<NewLogix>() {
            // overlay the state column with the edit column
            static public final int ENABLECOL = VALUECOL;
            static public final int EDITCOL = DELETECOL;
            protected String enabledString = Bundle.getMessage("ColumnHeadEnabled");  // NOI18N

            @Override
            public String getColumnName(int col) {
                if (col == EDITCOL) {
                    return "";  // no heading on "Edit"
                }
                if (col == ENABLECOL) {
                    return enabledString;
                }
                return super.getColumnName(col);
            }

            @Override
            public Class<?> getColumnClass(int col) {
                if (col == EDITCOL) {
                    return String.class;
                }
                if (col == ENABLECOL) {
                    return Boolean.class;
                }
                return super.getColumnClass(col);
            }

            @Override
            public int getPreferredWidth(int col) {
                // override default value for SystemName and UserName columns
                if (col == SYSNAMECOL) {
                    return new JTextField(12).getPreferredSize().width;
                }
                if (col == USERNAMECOL) {
                    return new JTextField(17).getPreferredSize().width;
                }
                if (col == EDITCOL) {
                    return new JTextField(12).getPreferredSize().width;
                }
                if (col == ENABLECOL) {
                    return new JTextField(5).getPreferredSize().width;
                }
                return super.getPreferredWidth(col);
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == EDITCOL) {
                    return true;
                }
                if (col == ENABLECOL) {
                    return true;
                }
                return super.isCellEditable(row, col);
            }

            @Override
            public Object getValueAt(int row, int col) {
                if (col == EDITCOL) {
                    return Bundle.getMessage("ButtonSelect");  // NOI18N
                } else if (col == ENABLECOL) {
                    NewLogix newLogix = (NewLogix) getValueAt(row, SYSNAMECOL);
                    if (newLogix == null) {
                        return null;
                    }
                    return Boolean.valueOf(newLogix.getEnabled());
                } else {
                    return super.getValueAt(row, col);
                }
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == EDITCOL) {
                    // set up to edit
                    String sName = ((NewLogix) getValueAt(row, SYSNAMECOL)).getSystemName();
                    if (Bundle.getMessage("ButtonEdit").equals(value)) {  // NOI18N
                        editPressed(sName);

                    } else if (Bundle.getMessage("BrowserButton").equals(value)) {  // NOI18N
                        conditionalRowNumber = row;
                        browserPressed(sName);

                    } else if (Bundle.getMessage("ButtonCopy").equals(value)) {  // NOI18N
                        copyPressed(sName);

                    } else if (Bundle.getMessage("ButtonDelete").equals(value)) {  // NOI18N
                        deletePressed(sName);
                    }
                } else if (col == ENABLECOL) {
                    // alternate
                    NewLogix x = (NewLogix) getValueAt(row, SYSNAMECOL);
                    boolean v = x.getEnabled();
                    x.setEnabled(!v);
                } else {
                    super.setValueAt(value, row, col);
                }
            }

            /**
             * Delete the bean after all the checking has been done.
             * <p>
             * Deletes the NewLogix.
             *
             * @param bean of the NewLogix to delete
             */
            @Override
            void doDelete(NewLogix bean) {
                bean.setEnabled(false);
                // delete the NewLogix
                _newLogixManager.deleteNewLogix(bean);
            }

            @Override
            protected boolean matchPropertyName(java.beans.PropertyChangeEvent e) {
                if (e.getPropertyName().equals(enabledString)) {
                    return true;
                }
                return super.matchPropertyName(e);
            }

            @Override
            public Manager<NewLogix> getManager() {
                return InstanceManager.getDefault(jmri.jmrit.newlogix.NewLogixManager.class);
            }

            @Override
            public NewLogix getBySystemName(String name) {
                return InstanceManager.getDefault(jmri.jmrit.newlogix.NewLogixManager.class).getBySystemName(
                        name);
            }

            @Override
            public NewLogix getByUserName(String name) {
                return InstanceManager.getDefault(jmri.jmrit.newlogix.NewLogixManager.class).getByUserName(
                        name);
            }

            @Override
            protected String getMasterClassName() {
                return getClassName();
            }

            @Override
            public void configureTable(JTable table) {
                table.setDefaultRenderer(Boolean.class, new EnablingCheckboxRenderer());
                table.setDefaultRenderer(JComboBox.class, new jmri.jmrit.symbolicprog.ValueRenderer());
                table.setDefaultEditor(JComboBox.class, new jmri.jmrit.symbolicprog.ValueEditor());
                super.configureTable(table);
            }

            /**
             * Replace delete button with comboBox to edit/delete/copy/select
             * NewLogix.
             *
             * @param table name of the NewLogix JTable holding the column
             */
            @Override
            protected void configDeleteColumn(JTable table) {
                JComboBox<String> editCombo = new JComboBox<String>();
                editCombo.addItem(Bundle.getMessage("ButtonSelect"));  // NOI18N
                editCombo.addItem(Bundle.getMessage("ButtonEdit"));  // NOI18N
                editCombo.addItem(Bundle.getMessage("BrowserButton"));  // NOI18N
                editCombo.addItem(Bundle.getMessage("ButtonCopy"));  // NOI18N
                editCombo.addItem(Bundle.getMessage("ButtonDelete"));  // NOI18N
                TableColumn col = table.getColumnModel().getColumn(BeanTableDataModel.DELETECOL);
                col.setCellEditor(new DefaultCellEditor(editCombo));
            }

            // Not needed - here for interface compatibility
            @Override
            public void clickOn(NewLogix t) {
            }

            @Override
            public String getValue(String s) {
                return "";
            }

            @Override
            protected String getBeanType() {
                return Bundle.getMessage("BeanNameLogix");  // NOI18N
            }
        };
    }

    /**
     * Set title of NewLogix table.
     */
    @Override
    protected void setTitle() {
        f.setTitle(Bundle.getMessage("TitleNewLogixTable"));
    }

    /**
     * Insert 2 table specific menus.
     * <p>
     * Accounts for the Window and Help menus, which are already added to the
     * menu bar as part of the creation of the JFrame, by adding the new menus 2
     * places earlier unless the table is part of the ListedTableFrame, which
     * adds the Help menu later on.
     *
     * @param f the JFrame of this table
     */
    @Override
    public void setMenuBar(BeanTableFrame f) {
        loadSelectionMode();
        loadEditorMode();

        JMenu menu = new JMenu(Bundle.getMessage("MenuOptions"));  // NOI18N
        menu.setMnemonic(KeyEvent.VK_O);
        javax.swing.JMenuBar menuBar = f.getJMenuBar();
        int pos = menuBar.getMenuCount() - 1;  // count the number of menus to insert the TableMenus before 'Window' and 'Help'
        int offset = 1;
        log.debug("setMenuBar number of menu items = " + pos);  // NOI18N
        for (int i = 0; i <= pos; i++) {
            if (menuBar.getComponent(i) instanceof JMenu) {
                if (((JMenu) menuBar.getComponent(i)).getText().equals(Bundle.getMessage("MenuHelp"))) {  // NOI18N
                    offset = -1;  // correct for use as part of ListedTableAction where the Help Menu is not yet present
                }
            }
        }

        ButtonGroup enableButtonGroup = new ButtonGroup();
        JRadioButtonMenuItem r = new JRadioButtonMenuItem(Bundle.getMessage("EnableAll"));  // NOI18N
        r.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAll(true);
            }
        });
        enableButtonGroup.add(r);
        r.setSelected(true);
        menu.add(r);

        r = new JRadioButtonMenuItem(Bundle.getMessage("DisableAll"));  // NOI18N
        r.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAll(false);
            }
        });
        enableButtonGroup.add(r);
        menu.add(r);

        menu.addSeparator();

        ButtonGroup modeButtonGroup = new ButtonGroup();
        r = new JRadioButtonMenuItem(Bundle.getMessage("UseMultiPick"));  // NOI18N
        r.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setSelectionMode(SelectionMode.USEMULTI);
            }
        });
        modeButtonGroup.add(r);
        menu.add(r);
        r.setSelected(_selectionMode == SelectionMode.USEMULTI);

        r = new JRadioButtonMenuItem(Bundle.getMessage("UseSinglePick"));  // NOI18N
        r.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setSelectionMode(SelectionMode.USESINGLE);
            }
        });
        modeButtonGroup.add(r);
        menu.add(r);
        r.setSelected(_selectionMode == SelectionMode.USESINGLE);

        r = new JRadioButtonMenuItem(Bundle.getMessage("UseComboNameBoxes"));  // NOI18N
        r.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setSelectionMode(SelectionMode.USECOMBO);
            }
        });
        modeButtonGroup.add(r);
        menu.add(r);
        r.setSelected(_selectionMode == SelectionMode.USECOMBO);

        menu.addSeparator();

        ButtonGroup viewButtonGroup = new ButtonGroup();
        r = new JRadioButtonMenuItem(Bundle.getMessage("TreeEdit"));  // NOI18N
        r.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setEditorMode(EditMode.TREEEDIT);
            }
        });
        viewButtonGroup.add(r);
        menu.add(r);
        r.setSelected(_editMode == EditMode.TREEEDIT);

        menuBar.add(menu, pos + offset);

        menu = new JMenu(Bundle.getMessage("MenuTools"));  // NOI18N
        menu.setMnemonic(KeyEvent.VK_T);

        JMenuItem item = new JMenuItem(Bundle.getMessage("OpenPickListTables"));  // NOI18N
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPickListTable();
            }
        });
        menu.add(item);

        menuBar.add(menu, pos + offset + 1);  // add this menu to the right of the previous
    }

    /**
     * Get the saved mode selection, default to the tranditional tabbed pick
     * list.
     * <p>
     * During the menu build process, the corresponding menu item is set to
     * selected.
     *
     * @since 4.7.3
     */
    void loadSelectionMode() {
        Object modeName = InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                getProperty(getClassName(), "Selection Mode");      // NOI18N
        if (modeName == null) {
            _selectionMode = SelectionMode.USEMULTI;
        } else {
            String currentMode = (String) modeName;
            switch (currentMode) {
                case "USEMULTI":        // NOI18N
                    _selectionMode = SelectionMode.USEMULTI;
                    break;
                case "USESINGLE":       // NOI18N
                    _selectionMode = SelectionMode.USESINGLE;
                    break;
                case "USECOMBO":        // NOI18N
                    _selectionMode = SelectionMode.USECOMBO;
                    break;
                default:
                    log.warn("Invalid NewLogix selection mode value, '{}', returned", currentMode);  // NOI18N
                    _selectionMode = SelectionMode.USEMULTI;
            }
        }
    }

    /**
     * Save the mode selection. Called by menu item change events.
     *
     * @since 4.7.3
     * @param newMode The SelectionMode enum constant
     */
    void setSelectionMode(SelectionMode newMode) {
        _selectionMode = newMode;
        InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                setProperty(getClassName(), "Selection Mode", newMode.toString());  // NOI18N
    }

    /**
     * Get the saved mode selection, default to the tree editor.
     * <p>
     * During the menu build process, the corresponding menu item is set to
     * selected.
     *
     * @since 4.9.x
     */
    void loadEditorMode() {
        Object modeName = InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                getProperty(getClassName(), "Edit Mode");      // NOI18N
        if (modeName == null) {
            _editMode = EditMode.TREEEDIT;
        } else {
            String currentMode = (String) modeName;
            switch (currentMode) {
                case "TREEEDIT":       // NOI18N
                    _editMode = EditMode.TREEEDIT;
                    break;
                default:
                    log.warn("Invalid edit mode value, '{}', returned", currentMode);  // NOI18N
                    _editMode = EditMode.TREEEDIT;
            }
        }
    }

    /**
     * Save the view mode selection. Called by menu item change events.
     *
     * @since 4.9.x
     * @param newMode The ViewMode enum constant
     */
    void setEditorMode(EditMode newMode) {
        _editMode = newMode;
        InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                setProperty(getClassName(), "Edit Mode", newMode.toString());  // NOI18N
    }

    /**
     * Open a new Pick List to drag Actions from to form NewLogix.
     */
    private void openPickListTable() {
        if (_pickTables == null) {
            _pickTables = new jmri.jmrit.picker.PickFrame(Bundle.getMessage("TitlePickList"));  // NOI18N
        } else {
            _pickTables.setVisible(true);
        }
        _pickTables.toFront();
    }

    void enableAll(boolean enable) {
        for (NewLogix x : _newLogixManager.getNamedBeanSet()) {
            x.setEnabled(enable);
        }
    }

    @Override
    protected String helpTarget() {
        return "package.jmri.jmrit.beantable.LogixTable";  // NOI18N
    }

    // ------------ variable definitions ------------

    NewLogixManager _newLogixManager = null;  // set when LogixAction is created

    NewLogixEditor _treeEdit = null;

    boolean _showReminder = false;
    jmri.jmrit.picker.PickFrame _pickTables;

    // Current focus variables
    NewLogix _curNewLogix = null;
    int conditionalRowNumber = 0;

    // Add NewLogix Variables
    JmriJFrame addNewLogixFrame = null;
    JTextField _systemName = new JTextField(20);
    JTextField _addUserName = new JTextField(20);
    JCheckBox _autoSystemName = new JCheckBox(Bundle.getMessage("LabelAutoSysName"));   // NOI18N
    JLabel _sysNameLabel = new JLabel(Bundle.getMessage("BeanNameLogix") + " " + Bundle.getMessage("ColumnSystemName") + ":");  // NOI18N
    JLabel _userNameLabel = new JLabel(Bundle.getMessage("BeanNameLogix") + " " + Bundle.getMessage("ColumnUserName") + ":");   // NOI18N
    String systemNameAuto = this.getClass().getName() + ".AutoSystemName";      // NOI18N
    JButton create;

    // Edit NewLogix Variables
    private boolean _inEditMode = false;
    private boolean _inCopyMode = false;

    /**
     * Input selection names.
     *
     * @since 4.7.3
     */
    public enum SelectionMode {
        /**
         * Use the traditional text field, with the tabbed Pick List available
         * for drag-n-drop
         */
        USEMULTI,
        /**
         * Use the traditional text field, but with a single Pick List that
         * responds with a click
         */
        USESINGLE,
        /**
         * Use combo boxes to select names instead of a text field.
         */
        USECOMBO;
    }
    SelectionMode _selectionMode;

    /**
     * NewLogix edit view mode
     */
    public enum EditMode {
        /**
         * Use the tree based mode for editing NewLogix
         */
        TREEEDIT;
    }
    EditMode _editMode;

    // ------------ Methods for Add NewLogix Window ------------

    /**
     * Respond to the Add button in NewLogix table Creates and/or initialize the
     * Add NewLogix pane.
     *
     * @param e The event heard
     */
    @Override
    protected void addPressed(ActionEvent e) {
        // possible change
        if (!checkFlags(null)) {
            return;
        }
        _showReminder = true;
        // make an Add NewLogix Frame
        if (addNewLogixFrame == null) {
            JPanel panel5 = makeAddNewLogixFrame("TitleAddNewLogix", "AddNewLogixMessage");  // NOI18N
            // Create NewLogix
            create = new JButton(Bundle.getMessage("ButtonCreate"));  // NOI18N
            panel5.add(create);
            create.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createPressed(e);
                }
            });
            create.setToolTipText(Bundle.getMessage("NewLogixCreateButtonHint"));  // NOI18N
        }
        addNewLogixFrame.pack();
        addNewLogixFrame.setVisible(true);
        _autoSystemName.setSelected(false);
        InstanceManager.getOptionalDefault(UserPreferencesManager.class).ifPresent((prefMgr) -> {
            _autoSystemName.setSelected(prefMgr.getSimplePreferenceState(systemNameAuto));
        });
    }

    /**
     * Create or copy NewLogix frame.
     *
     * @param titleId   property key to fetch as title of the frame (using Bundle)
     * @param messageId part 1 of property key to fetch as user instruction on
     *                  pane, either 1 or 2 is added to form the whole key
     * @return the button JPanel
     */
    JPanel makeAddNewLogixFrame(String titleId, String messageId) {
        addNewLogixFrame = new JmriJFrame(Bundle.getMessage(titleId));
        addNewLogixFrame.addHelpMenu(
                "package.jmri.jmrit.beantable.LogixAddEdit", true);     // NOI18N
        addNewLogixFrame.setLocation(50, 30);
        Container contentPane = addNewLogixFrame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel p;
        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.EAST;
        p.add(_sysNameLabel, c);
        c.gridy = 1;
        p.add(_userNameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
        p.add(_systemName, c);
        c.gridy = 1;
        p.add(_addUserName, c);
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;  // text field will expand
        c.gridy = 0;
        p.add(_autoSystemName, c);
        _addUserName.setToolTipText(Bundle.getMessage("NewLogixUserNameHint"));    // NOI18N
        _systemName.setToolTipText(Bundle.getMessage("NewLogixSystemNameHint"));   // NOI18N
        contentPane.add(p);
        // set up message
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        JPanel panel31 = new JPanel();
        panel31.setLayout(new FlowLayout());
        JLabel message1 = new JLabel(Bundle.getMessage(messageId + "1"));  // NOI18N
        panel31.add(message1);
        JPanel panel32 = new JPanel();
        JLabel message2 = new JLabel(Bundle.getMessage(messageId + "2"));  // NOI18N
        panel32.add(message2);
        panel3.add(panel31);
        panel3.add(panel32);
        contentPane.add(panel3);

        // set up create and cancel buttons
        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout());
        // Cancel
        JButton cancel = new JButton(Bundle.getMessage("ButtonCancel"));    // NOI18N
        panel5.add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAddPressed(e);
            }
        });
        cancel.setToolTipText(Bundle.getMessage("CancelLogixButtonHint"));      // NOI18N

        addNewLogixFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelAddPressed(null);
            }
        });
        contentPane.add(panel5);

        _autoSystemName.addItemListener(
                new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                autoSystemName();
            }
        });
        return panel5;
    }

    /**
     * Enable/disable fields for data entry when user selects to have system
     * name automatically generated.
     */
    void autoSystemName() {
        if (_autoSystemName.isSelected()) {
            _systemName.setEnabled(false);
            _sysNameLabel.setEnabled(false);
        } else {
            _systemName.setEnabled(true);
            _sysNameLabel.setEnabled(true);
        }
    }

    /**
     * Respond to the Cancel button in Add NewLogix window.
     * <p>
     * Note: Also get there if the user closes the Add NewLogix window.
     *
     * @param e The event heard
     */
    void cancelAddPressed(ActionEvent e) {
        addNewLogixFrame.setVisible(false);
        addNewLogixFrame.dispose();
        addNewLogixFrame = null;
        _inCopyMode = false;
        if (f != null) {
            f.setVisible(true);
        }
    }

    /**
     * Respond to the Copy NewLogix button in Add NewLogix window.
     * <p>
     * Provides a pane to set new properties of the copy.
     *
     * @param sName system name of NewLogix to be copied
     */
    void copyPressed(String sName) {
        if (!checkFlags(sName)) {
            return;
        }

        Runnable t = new Runnable() {
            @Override
            public void run() {
                JPanel panel5 = makeAddNewLogixFrame("TitleCopyNewLogix", "CopyNewLogixMessage");    // NOI18N
                // Create NewLogix
                JButton create = new JButton(Bundle.getMessage("ButtonCopy"));  // NOI18N
                panel5.add(create);
                create.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        copyNewLogixPressed(e);
                    }
                });
                addNewLogixFrame.pack();
                addNewLogixFrame.setVisible(true);
                _autoSystemName.setSelected(false);
                InstanceManager.getOptionalDefault(UserPreferencesManager.class).ifPresent((prefMgr) -> {
                    _autoSystemName.setSelected(prefMgr.getSimplePreferenceState(systemNameAuto));
                });
            }
        };
        log.debug("copyPressed started for {}", sName);  // NOI18N
        javax.swing.SwingUtilities.invokeLater(t);
        _inCopyMode = true;
        _newLogixSysName = sName;
    }

    String _newLogixSysName;

    /**
     * Copy the NewLogix as configured in the Copy set up pane.
     *
     * @param e the event heard
     */
    void copyNewLogixPressed(ActionEvent e) {
/*        
        String uName = _addUserName.getText().trim();
        if (uName.length() == 0) {
            uName = null;
        }
        NewLogix targetNewLogix;
        if (_autoSystemName.isSelected()) {
            if (!checkNewLogixUserName(uName)) {
                return;
            }
            targetNewLogix = _newLogixManager.createNewLogix(uName);
        } else {
            if (!checkNewLogixSysName()) {
                return;
            }
            String sName = _systemName.getText().trim();
            // check if a NewLogix with this name already exists
            boolean createLogix = true;
            targetNewLogix = _newLogixManager.getBySystemName(sName);
            if (targetNewLogix != null) {
                int result = JOptionPane.showConfirmDialog(f,
                        Bundle.getMessage("ConfirmLogixDuplicate", sName, _newLogixSysName), // NOI18N
                        Bundle.getMessage("QuestionTitle"), JOptionPane.YES_NO_OPTION,    // NOI18N
                        JOptionPane.QUESTION_MESSAGE);
                if (JOptionPane.NO_OPTION == result) {
                    return;
                }
                createLogix = false;
                String userName = targetNewLogix.getUserName();
                if (userName != null && userName.length() > 0) {
                    _addUserName.setText(userName);
                    uName = userName;
                }
            } else if (!checkNewLogixUserName(uName)) {
                return;
            }
            if (createLogix) {
                // Create the new NewLogix
                targetNewLogix = _newLogixManager.createNewLogix(sName, uName);
                if (targetNewLogix == null) {
                    // should never get here unless there is an assignment conflict
                    log.error("Failure to create NewLogix with System Name: {}", sName);  // NOI18N
                    return;
                }
            } else if (targetNewLogix == null) {
                log.error("Error targetLogix is null!");  // NOI18N
                return;
            } else {
                targetNewLogix.setUserName(uName);
            }
        }
        NewLogix srcLogic = _newLogixManager.getBySystemName(_newLogixSysName);
        for (int i = 0; i < srcLogic.getNumConditionals(); i++) {
            String cSysName = srcLogic.getConditionalByNumberOrder(i);
            copyConditionalToLogix(cSysName, srcLogic, targetNewLogix);
        }
        cancelAddPressed(null);
*/
    }

    /**
     * Check and warn if a string is already in use as the user name of a NewLogix.
     *
     * @param uName the suggested name
     * @return true if not in use
     */
    boolean checkNewLogixUserName(String uName) {
        // check if a NewLogix with the same user name exists
        if (uName != null && uName.trim().length() > 0) {
            NewLogix x = _newLogixManager.getByUserName(uName);
            if (x != null) {
                // NewLogix with this user name already exists
                JOptionPane.showMessageDialog(addNewLogixFrame,
                        Bundle.getMessage("NewLogixError3"), Bundle.getMessage("ErrorTitle"), // NOI18N
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /**
     * Check validity of NewLogix system name.
     * <p>
     * Fixes name if it doesn't start with "IQ:".
     *
     * @return false if name has length &lt; 1 after displaying a dialog
     */
    boolean checkNewLogixSysName() {
        String sName = InstanceManager.getDefault(NewLogixManager.class).normalizeSystemName(_systemName.getText());
        if ((sName.length() < 1)) {
            // Entered system name is blank or too short
            JOptionPane.showMessageDialog(addNewLogixFrame,
                    Bundle.getMessage("NewLogixError8"), Bundle.getMessage("ErrorTitle"), // NOI18N
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ((sName.length() < 3) || (sName.charAt(0) != 'I')
                || (sName.charAt(1) != 'Q')
                || (sName.charAt(2) != ':')) {
            // System name does not begin with IQ:, prefix IQ: to it
            String s = sName;
            sName = "IQ:" + s;  // NOI18N
        }
        _systemName.setText(sName);
        return true;
    }

    /**
     * Check if another NewLogix editing session is currently open or no system
     * name is provided.
     *
     * @param sName system name of NewLogix to be copied
     * @return true if a new session may be started
     */
    boolean checkFlags(String sName) {
        if (_inEditMode) {
            // Already editing a NewLogix, ask for completion of that edit
            JOptionPane.showMessageDialog(null,
                    Bundle.getMessage("NewLogixError32", _curNewLogix.getSystemName()),
                    Bundle.getMessage("ErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            if (_treeEdit != null) {
                _treeEdit.toFront();
//                _treeEdit.bringToFront();
            }
            return false;
        }

        if (_inCopyMode) {
            // Already editing a NewLogix, ask for completion of that edit
            JOptionPane.showMessageDialog(null,
                    Bundle.getMessage("NewLogixError31", _newLogixSysName),
                    Bundle.getMessage("ErrorTitle"), // NOI18N
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (sName != null) {
            // check if a NewLogix with this name exists
            NewLogix x = _newLogixManager.getBySystemName(sName);
            if (x == null) {
                // NewLogix does not exist, so cannot be edited
                log.error("No NewLogix with system name: " + sName);
                JOptionPane.showMessageDialog(null,
                        Bundle.getMessage("NewLogixError5"),
                        Bundle.getMessage("ErrorTitle"), // NOI18N
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /**
     * Respond to the Create NewLogix button in Add NewLogix window.
     *
     * @param e The event heard
     */
    void createPressed(ActionEvent e) {
        // possible change
        _showReminder = true;
        String sName = "";
        String uName = _addUserName.getText().trim();
        if (uName.length() == 0) {
            uName = null;
        }
        if (_autoSystemName.isSelected()) {
            if (!checkNewLogixUserName(uName)) {
                return;
            }
            _curNewLogix = _newLogixManager.createNewLogix(uName);
            sName = _curNewLogix.getSystemName();
        } else {
            if (!checkNewLogixSysName()) {
                return;
            }
            // Get validated system name
            sName = _systemName.getText();
            // check if a NewLogix with this name already exists
            NewLogix x = null;
            try {
                x = _newLogixManager.getBySystemName(sName);
            } catch (Exception ex) {
                // user input no good
                handleCreateException(sName);
                return;  // without creating
            }
            if (x != null) {
                // NewLogix already exists
                JOptionPane.showMessageDialog(addNewLogixFrame, Bundle.getMessage("NewLogixError1"),
                        Bundle.getMessage("ErrorTitle"), // NOI18N
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!checkNewLogixUserName(uName)) {
                return;
            }
            // Create the new NewLogix
            _curNewLogix = _newLogixManager.createNewLogix(sName, uName);
            if (_curNewLogix == null) {
                // should never get here unless there is an assignment conflict
                log.error("Failure to create NewLogix with System Name: {}", sName);  // NOI18N
                return;
            }
        }
        cancelAddPressed(null);
        // create the Edit NewLogix Window
        editPressed(sName);
        InstanceManager.getOptionalDefault(UserPreferencesManager.class).ifPresent((prefMgr) -> {
            prefMgr.setSimplePreferenceState(systemNameAuto, _autoSystemName.isSelected());
        });
    }

    void handleCreateException(String sysName) {
        JOptionPane.showMessageDialog(addNewLogixFrame,
                Bundle.getMessage("ErrorLogixAddFailed", sysName), // NOI18N
                Bundle.getMessage("ErrorTitle"), // NOI18N
                JOptionPane.ERROR_MESSAGE);
    }

    // ------------ Methods for Edit NewLogix Pane ------------

    /**
     * Respond to the Edit button pressed in NewLogix table.
     *
     * @param sName system name of NewLogix to be edited
     */
    void editPressed(String sName) {
        _curNewLogix = _newLogixManager.getBySystemName(sName);
        if (!checkFlags(sName)) {
            return;
        }

        if (sName.equals(SensorGroupFrame.logixSysName)) {
            // Sensor group message
            JOptionPane.showMessageDialog(null,
                    Bundle.getMessage("NewLogixWarn8", SensorGroupFrame.logixUserName, SensorGroupFrame.logixSysName),
                    Bundle.getMessage("WarningTitle"), // NOI18N
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create a new NewLogix edit view, add the listener.
        if (_editMode == EditMode.TREEEDIT) {
            _treeEdit = new NewLogixEditor(sName);
            _treeEdit.initComponents();
            _treeEdit.setVisible(true);
            _inEditMode = true;
            
            _treeEdit.addNewLogixEventListener(new NewLogixEditor.NewLogixEventListener() {
                @Override
                public void newLogixEventOccurred() {
                    String lgxName = sName;
                    _treeEdit.logixData.forEach((key, value) -> {
                        if (key.equals("Finish")) {                  // NOI18N
                            _treeEdit = null;
                            _inEditMode = false;
                            _curNewLogix.setEnabled(true);
                            f.setVisible(true);
                        } else if (key.equals("Delete")) {           // NOI18N
                            deletePressed(value);
                        } else if (key.equals("chgUname")) {         // NOI18N
                            NewLogix x = _newLogixManager.getBySystemName(lgxName);
                            x.setUserName(value);
                            m.fireTableDataChanged();
                        }
                    });
                }
            });
        }
    }

    /**
     * Display reminder to save.
     */
    void showSaveReminder() {
        if (_showReminder) {
            if (InstanceManager.getNullableDefault(jmri.UserPreferencesManager.class) != null) {
                InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                        showInfoMessage(Bundle.getMessage("ReminderTitle"), Bundle.getMessage("ReminderSaveString", Bundle.getMessage("MenuItemNewLogixTable")), // NOI18N
                                getClassName(),
                                "remindSaveLogix");  // NOI18N
            }
        }
    }

    @Override
    public void setMessagePreferencesDetails() {
        HashMap<Integer, String> options = new HashMap<>(3);
        options.put(0x00, Bundle.getMessage("DeleteAsk"));      // NOI18N
        options.put(0x01, Bundle.getMessage("DeleteNever"));    // NOI18N
        options.put(0x02, Bundle.getMessage("DeleteAlways"));   // NOI18N
        jmri.InstanceManager.getDefault(jmri.UserPreferencesManager.class).setMessageItemDetails(getClassName(), "delete", Bundle.getMessage("DeleteNewLogix"), options, 0x00);  // NOI18N
        jmri.InstanceManager.getDefault(jmri.UserPreferencesManager.class).setPreferenceItemDetails(getClassName(), "remindSaveNewLogix", Bundle.getMessage("HideSaveReminder"));  // NOI18N
        super.setMessagePreferencesDetails();
    }

    /**
     * Respond to the Delete combo selection NewLogix window delete request.
     *
     * @param sName system name of bean to be deleted
     */
    void deletePressed(String sName) {
        if (!checkFlags(sName)) {
            return;
        }
        
        final NewLogix x = _newLogixManager.getBySystemName(sName);
        final jmri.UserPreferencesManager p;
        p = jmri.InstanceManager.getNullableDefault(jmri.UserPreferencesManager.class);
        if (p != null && p.getMultipleChoiceOption(getClassName(), "delete") == 0x02) {     // NOI18N
            if (x != null) {
                _newLogixManager.deleteNewLogix(x);
//                deleteSourceWhereUsed();
            }
        } else {
            final JDialog dialog = new JDialog();
            String msg;
            dialog.setTitle(Bundle.getMessage("QuestionTitle"));     // NOI18N
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            JPanel container = new JPanel();
            container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            msg = Bundle.getMessage("ConfirmLogixDelete", sName);    // NOI18N
            JLabel question = new JLabel(msg);
            question.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(question);

            final JCheckBox remember = new JCheckBox(Bundle.getMessage("MessageRememberSetting"));  // NOI18N
            remember.setFont(remember.getFont().deriveFont(10f));
            remember.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton yesButton = new JButton(Bundle.getMessage("ButtonYes"));    // NOI18N
            JButton noButton = new JButton(Bundle.getMessage("ButtonNo"));      // NOI18N
            JPanel button = new JPanel();
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.add(yesButton);
            button.add(noButton);
            container.add(button);

            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //there is no point in remebering this the user will never be
                    //able to delete a bean!
                    /*if(remember.isSelected()){
                     setDisplayDeleteMsg(0x01);
                     }*/
                    dialog.dispose();
                }
            });

            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (p != null && remember.isSelected()) {
                        p.setMultipleChoiceOption(getClassName(), "delete", 0x02);  // NOI18N
                    }
                    if (x != null) {
                        _newLogixManager.deleteNewLogix(x);
//                        deleteSourceWhereUsed();
                    }
                    dialog.dispose();
                }
            });
            container.add(remember);
            container.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.setAlignmentY(Component.CENTER_ALIGNMENT);
            dialog.getContentPane().add(container);
            dialog.pack();
            dialog.setModal(true);
            dialog.setVisible(true);
        }

        f.setVisible(true);
    }

    /**
     * Create Variable and Action editing pane center part.
     *
     * @param comp  Field or comboBox to include on sub pane
     * @param label property key for label
     * @param hint  property key for tooltip for this sub pane
     * @return JPanel containing interface
     */
    JPanel makeEditPanel(JComponent comp, String label, String hint) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel p = new JPanel();
        p.add(new JLabel(Bundle.getMessage(label)));
        panel.add(p);
        if (hint != null) {
            panel.setToolTipText(Bundle.getMessage(hint));
        }
        comp.setMaximumSize(comp.getPreferredSize());  // override for text fields
        panel.add(comp);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    /**
     * Format time to hh:mm given integer hour and minute.
     *
     * @param hour   value for time hours
     * @param minute value for time minutes
     * @return Formatted time string
     */
    public static String formatTime(int hour, int minute) {
        String s = "";
        String t = Integer.toString(hour);
        if (t.length() == 2) {
            s = t + ":";
        } else if (t.length() == 1) {
            s = "0" + t + ":";
        }
        t = Integer.toString(minute);
        if (t.length() == 2) {
            s = s + t;
        } else if (t.length() == 1) {
            s = s + "0" + t;
        }
        if (s.length() != 5) {
            // input error
            s = "00:00";
        }
        return s;
    }

    @Override
    public String getClassDescription() {
        return Bundle.getMessage("TitleNewLogixTable");        // NOI18N
    }

    @Override
    protected String getClassName() {
        return NewLogixTableAction.class.getName();
    }

// ------------ Methods for Conditional Browser Window ------------
    /**
     * Respond to the Browse button pressed in NewLogix table.
     *
     * @param sName The selected NewLogix system name
     */
    void browserPressed(String sName) {
        // NewLogix was found, create the window
        _curNewLogix = _newLogixManager.getBySystemName(sName);
        makeBrowserWindow();
    }

    /**
     * Create and initialize the conditionals browser window.
     */
    void makeBrowserWindow() {
        JmriJFrame condBrowserFrame = new JmriJFrame(Bundle.getMessage("BrowserTitle"), false, true);   // NOI18N
        condBrowserFrame.addHelpMenu("package.jmri.jmrit.beantable.LogixAddEdit", true);            // NOI18N

        Container contentPane = condBrowserFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // NewLogix header information
        JPanel topPanel = new JPanel();
        String tStr = Bundle.getMessage("BrowserLogix") + " " + _curNewLogix.getSystemName() + "    " // NOI18N
                + _curNewLogix.getUserName() + "    "
                + (Boolean.valueOf(_curNewLogix.getEnabled())
                        ? Bundle.getMessage("BrowserEnabled") // NOI18N
                        : Bundle.getMessage("BrowserDisabled"));  // NOI18N
        topPanel.add(new JLabel(tStr));
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Build the conditionals listing
        JScrollPane scrollPane = null;
//        JTextArea textContent = buildConditionalListing();
        JTextArea textContent = new JTextArea();
        scrollPane = new JScrollPane(textContent);
        contentPane.add(scrollPane);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        JButton helpBrowse = new JButton(Bundle.getMessage("MenuHelp"));   // NOI18N
        bottomPanel.add(helpBrowse, BorderLayout.WEST);
        helpBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(condBrowserFrame,
                        Bundle.getMessage("BrowserHelpText"),   // NOI18N
                        Bundle.getMessage("BrowserHelpTitle"),  // NOI18N
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JButton saveBrowse = new JButton(Bundle.getMessage("BrowserSaveButton"));   // NOI18N
        saveBrowse.setToolTipText(Bundle.getMessage("BrowserSaveButtonHint"));      // NOI18N
        bottomPanel.add(saveBrowse, BorderLayout.EAST);
        saveBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBrowserPressed();
            }
        });
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        condBrowserFrame.pack();
        condBrowserFrame.setVisible(true);
    }  // makeBrowserWindow

    JFileChooser userFileChooser = new JFileChooser(FileUtil.getUserFilesPath());

    /**
     * Save the NewLogix browser window content to a text file.
     */
    void saveBrowserPressed() {
        userFileChooser.setApproveButtonText(Bundle.getMessage("BrowserSaveDialogApprove"));  // NOI18N
        userFileChooser.setDialogTitle(Bundle.getMessage("BrowserSaveDialogTitle"));  // NOI18N
        userFileChooser.rescanCurrentDirectory();
        // Default to newLogix system name.txt
        userFileChooser.setSelectedFile(new File(_curNewLogix.getSystemName() + ".txt"));  // NOI18N
        int retVal = userFileChooser.showSaveDialog(null);
        if (retVal != JFileChooser.APPROVE_OPTION) {
            log.debug("Save browser content stopped, no file selected");  // NOI18N
            return;  // give up if no file selected or cancel pressed
        }
        File file = userFileChooser.getSelectedFile();
        log.debug("Save browser content to '{}'", file);  // NOI18N

        if (file.exists()) {
            Object[] options = {Bundle.getMessage("BrowserSaveDuplicateReplace"),  // NOI18N
                    Bundle.getMessage("BrowserSaveDuplicateAppend"),  // NOI18N
                    Bundle.getMessage("ButtonCancel")};               // NOI18N
            int selectedOption = JOptionPane.showOptionDialog(null,
                    Bundle.getMessage("BrowserSaveDuplicatePrompt", file.getName()), // NOI18N
                    Bundle.getMessage("BrowserSaveDuplicateTitle"),   // NOI18N
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (selectedOption == 2 || selectedOption == -1) {
                log.debug("Save browser content stopped, file replace/append cancelled");  // NOI18N
                return;  // Cancel selected or dialog box closed
            }
            if (selectedOption == 0) {
                FileUtil.delete(file);  // Replace selected
            }
        }

        // Create the file content
        String tStr = Bundle.getMessage("BrowserLogix") + " " + _curNewLogix.getSystemName() + "    "  // NOI18N
                + _curNewLogix.getUserName() + "    "
                + (_curNewLogix.getEnabled()
                        ? Bundle.getMessage("BrowserEnabled")    // NOI18N
                        : Bundle.getMessage("BrowserDisabled"));  // NOI18N
//        JTextArea textContent = buildConditionalListing();
        JTextArea textContent = new JTextArea();
        try {
            // ADD NewLogix Header inforation first
            FileUtil.appendTextToFile(file, tStr);
            FileUtil.appendTextToFile(file, textContent.getText());
        } catch (IOException e) {
            log.error("Unable to write browser content to '{}', exception: '{}'", file, e);  // NOI18N
        }
    }

    private final static Logger log = LoggerFactory.getLogger(NewLogixTableAction.class);

}
