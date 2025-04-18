package jmri.jmrit.operations.rollingstock;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.MessageFormat;

import javax.swing.*;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jmri.InstanceManager;
import jmri.jmrit.operations.OperationsFrame;
import jmri.jmrit.operations.OperationsXml;
import jmri.jmrit.operations.rollingstock.cars.*;
import jmri.jmrit.operations.setup.Control;
import jmri.jmrit.operations.setup.Setup;
import jmri.jmrit.operations.trains.trainbuilder.TrainCommon;
import jmri.util.swing.JmriJOptionPane;

/**
 * Frame for editing a rolling stock attribute.
 *
 * @author Daniel Boudreau Copyright (C) 2020
 */
public abstract class RollingStockAttributeEditFrame extends OperationsFrame implements java.beans.PropertyChangeListener {

    // labels
    public JLabel textAttribute = new JLabel();
    JLabel textSep = new JLabel();
    public JLabel quanity = new JLabel("0");

    // major buttons
    public JButton addButton = new JButton(Bundle.getMessage("Add"));
    public JButton deleteButton = new JButton(Bundle.getMessage("ButtonDelete"));
    public JButton replaceButton = new JButton(Bundle.getMessage("Replace"));

    // combo box
    public JComboBox<String> comboBox;

    // text box
    public JTextField addTextBox = new JTextField(Control.max_len_string_attibute);

    // ROAD and OWNER are the only two attributes shared between Cars and Engines
    public static final String ROAD = "Road";
    public static final String OWNER = "Owner";
    public static final String TYPE = "Type"; // cars and engines have different types
    public static final String LENGTH = "Length"; // cars and engines have different lengths

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("MS_CANNOT_BE_FINAL") // needs access in subpackage
    protected static boolean showDialogBox = true;
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("MS_CANNOT_BE_FINAL") // needs access in subpackage
    protected static boolean showQuanity = false;

    // property change
    public static final String DISPOSE = "dispose"; // NOI18N

    public RollingStockAttributeEditFrame() {
    }

    public String _attribute; // used to determine which attribute is being edited

    public void initComponents(String attribute, String name) {
        getContentPane().removeAll();

        // track which attribute is being edited
        _attribute = attribute;
        loadCombobox();
        comboBox.setSelectedItem(name);

        // general GUI config
        getContentPane().setLayout(new GridBagLayout());

        textAttribute.setText(attribute);
        quanity.setVisible(showQuanity);

        // row 1
        addItem(textAttribute, 2, 1);
        // row 2
        addItem(addTextBox, 2, 2);
        addItem(addButton, 3, 2);

        // row 3
        addItem(quanity, 1, 3);
        addItem(comboBox, 2, 3);
        addItem(deleteButton, 3, 3);

        // row 4
        addItem(replaceButton, 3, 4);

        addButtonAction(addButton);
        addButtonAction(deleteButton);
        addButtonAction(replaceButton);
        
        addComboBoxAction(comboBox);
        
        updateAttributeQuanity();

        deleteButton.setToolTipText(
                Bundle.getMessage("TipDeleteAttributeName", attribute));
        addButton.setToolTipText(
                Bundle.getMessage("TipAddAttributeName", attribute));
        replaceButton.setToolTipText(
                Bundle.getMessage("TipReplaceAttributeName", attribute));

        initMinimumSize(new Dimension(Control.panelWidth400, Control.panelHeight250));
    }

    // add, delete, or replace button
    @Override
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "GUI ease of use")
    public void buttonActionPerformed(java.awt.event.ActionEvent ae) {
        log.debug("edit frame button activated");
        if (ae.getSource() == addButton) {
            if (!checkItemName(Bundle.getMessage("canNotAdd"))) {
                return;
            }
            addAttributeName(addTextBox.getText().trim());
        }
        if (ae.getSource() == deleteButton) {
            deleteAttributeName((String) comboBox.getSelectedItem());
        }
        if (ae.getSource() == replaceButton) {
            if (!checkItemName(Bundle.getMessage("canNotReplace"))) {
                return;
            }
            String newItem = addTextBox.getText().trim();
            String oldItem = (String) comboBox.getSelectedItem();
            if (JmriJOptionPane.showConfirmDialog(this,
                    Bundle.getMessage("replaceMsg", oldItem, newItem),
                    Bundle.getMessage("replaceAll"), JmriJOptionPane.YES_NO_OPTION) != JmriJOptionPane.YES_OPTION) {
                return;
            }
            if (newItem.equals(oldItem)) {
                return;
            }
            // don't show dialog, save current state
            boolean oldShow = showDialogBox;
            showDialogBox = false;
            addAttributeName(newItem);
            showDialogBox = oldShow;
            replaceItem(oldItem, newItem);
            deleteAttributeName(oldItem);
        }
    }

    protected boolean checkItemName(String errorMessage) {
        String itemName = addTextBox.getText().trim();
        if (itemName.isEmpty()) {
            return false;
        }
        // hyphen feature needs at least one character to work properly
        if (itemName.contains(TrainCommon.HYPHEN)) {
            String[] s = itemName.split(TrainCommon.HYPHEN);
            if (s.length == 0) {
                JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("HyphenFeature"),
                        MessageFormat.format(errorMessage, new Object[] { _attribute }), JmriJOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (_attribute.equals(LENGTH)) {
            if (convertLength(itemName).equals(FAILED)) {
                return false;
            }
        }
        if (_attribute.equals(ROAD)) {
            if (!OperationsXml.checkFileName(itemName)) { // NOI18N
                JmriJOptionPane.showMessageDialog(this,
                        Bundle.getMessage("NameResChar") + NEW_LINE + Bundle.getMessage("ReservedChar"),
                        MessageFormat.format(errorMessage, new Object[] { _attribute }), JmriJOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        String[] item = { itemName };
        if (_attribute.equals(TYPE)) {
            // can't have the " & " as part of the type name
            if (itemName.contains(CarLoad.SPLIT_CHAR)) {
                JmriJOptionPane.showMessageDialog(this,
                        Bundle.getMessage("carNameNoAndChar",
                                CarLoad.SPLIT_CHAR),
                        MessageFormat.format(errorMessage, new Object[] { _attribute }), JmriJOptionPane.ERROR_MESSAGE);
                return false;
            }
            item = itemName.split(TrainCommon.HYPHEN);
        }
        if (item[0].length() > Control.max_len_string_attibute) {
            JmriJOptionPane.showMessageDialog(this,
                    Bundle.getMessage("rsAttributeName",
                            Control.max_len_string_attibute),
                    MessageFormat.format(errorMessage, new Object[] { _attribute }), JmriJOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    protected void deleteAttributeName(String deleteItem) {
        log.debug("delete attribute {}", deleteItem);
        if (_attribute.equals(ROAD)) {
            // purge train and locations by using replace
            InstanceManager.getDefault(CarRoads.class).replaceName(deleteItem, null);
        }
        if (_attribute.equals(OWNER)) {
            InstanceManager.getDefault(CarOwners.class).deleteName(deleteItem);
        }
    }

    protected void addAttributeName(String addItem) {
        if (_attribute.equals(ROAD)) {
            InstanceManager.getDefault(CarRoads.class).addName(addItem);
        }
        if (_attribute.equals(OWNER)) {
            InstanceManager.getDefault(CarOwners.class).addName(addItem);
        }
    }

    protected void replaceItem(String oldItem, String newItem) {
        if (_attribute.equals(ROAD)) {
            InstanceManager.getDefault(CarRoads.class).replaceName(oldItem, newItem);
        }
        if (_attribute.equals(OWNER)) {
            InstanceManager.getDefault(CarOwners.class).replaceName(oldItem, newItem);
        }
    }

    protected void loadCombobox() {
        if (_attribute.equals(ROAD)) {
            comboBox = InstanceManager.getDefault(CarRoads.class).getComboBox();
            InstanceManager.getDefault(CarRoads.class).addPropertyChangeListener(this);
        }
        if (_attribute.equals(OWNER)) {
            comboBox = InstanceManager.getDefault(CarOwners.class).getComboBox();
            InstanceManager.getDefault(CarOwners.class).addPropertyChangeListener(this);
        }
    }

    public static final String FAILED = "failed";

    public String convertLength(String addItem) {
        // convert from inches to feet if needed
        if (addItem.endsWith("\"")) { // NOI18N
            addItem = addItem.substring(0, addItem.length() - 1);
            try {
                double inches = Double.parseDouble(addItem);
                int feet = (int) (inches * Setup.getScaleRatio() / 12);
                addItem = Integer.toString(feet);
            } catch (NumberFormatException e) {
                JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("CanNotConvertFeet"),
                        Bundle.getMessage("ErrorRsLength"), JmriJOptionPane.ERROR_MESSAGE);
                return FAILED;
            }
            addTextBox.setText(addItem);
        }
        if (addItem.endsWith("cm")) { // NOI18N
            addItem = addItem.substring(0, addItem.length() - 2);
            try {
                double cm = Double.parseDouble(addItem);
                int meter = (int) (cm * Setup.getScaleRatio() / 100);
                addItem = Integer.toString(meter);
            } catch (NumberFormatException e) {
                JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("CanNotConvertMeter"),
                        Bundle.getMessage("ErrorRsLength"), JmriJOptionPane.ERROR_MESSAGE);
                return FAILED;
            }
            addTextBox.setText(addItem);
        }
        // confirm that length is a number and less than 10000 feet
        try {
            int length = Integer.parseInt(addItem);
            if (length < 0) {
                log.error("length ({}) has to be a positive number", addItem);
                JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("ErrorRsLength"),
                        Bundle.getMessage("canNotAdd", _attribute),
                        JmriJOptionPane.ERROR_MESSAGE);
                return FAILED;
            }
            if (addItem.length() > Control.max_len_string_length_name) {
                JmriJOptionPane.showMessageDialog(this,
                        Bundle.getMessage("RsAttribute",
                                Control.max_len_string_length_name),
                        Bundle.getMessage("canNotAdd", _attribute),
                        JmriJOptionPane.ERROR_MESSAGE);
                return FAILED;
            }
        } catch (NumberFormatException e) {
            log.error("length ({}) is not an integer", addItem);
            JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("ErrorRsLength"),
                    Bundle.getMessage("canNotAdd",  _attribute),
                    JmriJOptionPane.ERROR_MESSAGE);
            return FAILED;
        }
        return addItem;
    }
    
    @Override
    protected void comboBoxActionPerformed(java.awt.event.ActionEvent ae) {
        log.debug("Combo box action");
        updateAttributeQuanity();
    }
    
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "GUI ease of use")
    public void toggleShowQuanity() {
        showQuanity = !showQuanity;
        quanity.setVisible(showQuanity);
        updateAttributeQuanity();
    }
    
    protected boolean deleteUnused = false;
    protected boolean cancel = false;

    public void deleteUnusedAttributes() {
        if (!showQuanity) {
            toggleShowQuanity();
        }
        deleteUnused = true;
        cancel = false;
        int items = comboBox.getItemCount() - 1;
        for (int i = items; i >= 0; i--) {
            comboBox.setSelectedIndex(i);
        }
        deleteUnused = false; // done
        comboBox.setSelectedIndex(0);
    }
    
    protected void confirmDelete(String item) {
        if (!cancel) {
            int results = JmriJOptionPane.showOptionDialog(this,
                    MessageFormat
                            .format(Bundle.getMessage("ConfirmDeleteAttribute"), new Object[] { _attribute, item }),
                    Bundle.getMessage("DeleteAttribute?"), JmriJOptionPane.DEFAULT_OPTION,
                    JmriJOptionPane.QUESTION_MESSAGE, null, new Object[] { Bundle.getMessage("ButtonYes"),
                            Bundle.getMessage("ButtonNo"), Bundle.getMessage("ButtonCancel") },
                    Bundle.getMessage("ButtonYes"));
            if (results == 0 ) { // array position 0
                deleteAttributeName((String) comboBox.getSelectedItem());
            }
            if (results == 2 || results == JmriJOptionPane.CLOSED_OPTION) { // array position 2 or Dialog closed
                cancel = true;
            }
        }
    }
    
    protected abstract void updateAttributeQuanity();

    @Override
    public void dispose() {
        InstanceManager.getDefault(CarRoads.class).removePropertyChangeListener(this);
        InstanceManager.getDefault(CarOwners.class).removePropertyChangeListener(this);
        firePropertyChange(DISPOSE, _attribute, null);
        super.dispose();
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (e.getPropertyName().equals(CarRoads.CARROADS_CHANGED_PROPERTY)) {
            InstanceManager.getDefault(CarRoads.class).updateComboBox(comboBox);
        }
        if (e.getPropertyName().equals(CarOwners.CAROWNERS_CHANGED_PROPERTY)) {
            InstanceManager.getDefault(CarOwners.class).updateComboBox(comboBox);
        }
        comboBox.setSelectedItem(addTextBox.getText().trim()); // has to be the last line for propertyChange
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RollingStockAttributeEditFrame.class);
}
