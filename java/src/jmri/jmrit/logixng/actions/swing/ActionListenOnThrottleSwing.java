package jmri.jmrit.logixng.actions.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.table.TableColumn;

import jmri.InstanceManager;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle;
import jmri.jmrit.logixng.actions.NamedBeanType;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle.ThrottleReference;
import jmri.util.table.ButtonEditor;
import jmri.util.table.ButtonRenderer;

/**
 * Configures an ActionListenOnThrottle object with a Swing JPanel.
 *
 * @author Daniel Bergqvist Copyright 2021
 */
public class ActionListenOnThrottleSwing extends AbstractDigitalActionSwing {

    private JTable _listenOnBeansTable;
    private ListenOnThrottleTableModel _listenOnBeansTableModel;
    private JCheckBox _listenOnSpeed;
    private JCheckBox _listenOnIsForward;
    private JCheckBox _listenOnFunction;
    private JTextField _localVariableEvent;
    private JTextField _localVariableLocoAddress;
    private JTextField _localVariableRoster;
    private JTextField _localVariableSpeed;
    private JTextField _localVariableIsForward;
    private JTextField _localVariableFunction;
    private JTextField _localVariableFunctionState;
    private JTextField _localVariableUserData;


    @Override
    protected void createPanel(@CheckForNull Base object, @Nonnull JPanel buttonPanel) {
        if ((object != null) && (! (object instanceof ActionListenOnThrottle))) {
            throw new IllegalArgumentException("object is not a ActionListenOnThrottle: " + object.getClass().getName());
        }
        ActionListenOnThrottle listenOnBeans = (ActionListenOnThrottle)object;

        panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        _listenOnBeansTable = new JTable();

        if (listenOnBeans != null) {
            List<ThrottleReference> namedBeanReference
                    = new ArrayList<>(listenOnBeans.getReferences());

            namedBeanReference.sort((o1, o2) -> {
                if (o1.getType() == null) return o2.getType() == null ? 0 : -1;
                if (o2.getType() == null) return 1;

                int result = o1.getType().toString().compareTo(o2.getType().toString());
                if (result == 0) {
//                    if (o1.getAddress() == null) return o2.getAddress() == null ? 0 : -1;
//                    if (o2.getAddress() == null) return 1;
//                    result = o1.getAddress().compareTo(o2.getAddress());
                }
                return result;
            });
            _listenOnBeansTableModel = new ListenOnThrottleTableModel(namedBeanReference);
        } else {
            _listenOnBeansTableModel = new ListenOnThrottleTableModel(null);
        }

        _listenOnBeansTable.setModel(_listenOnBeansTableModel);
        _listenOnBeansTable.setDefaultRenderer(NamedBeanType.class,
                new ListenOnThrottleTableModel.CellRenderer());
        _listenOnBeansTable.setDefaultEditor(NamedBeanType.class,
                new ListenOnThrottleTableModel.ThrottleTypeCellEditor());
        _listenOnBeansTable.setDefaultEditor(ActionListenOnThrottle.ThrottleReference.class,
                _listenOnBeansTableModel.getNamedBeanCellEditor());
        _listenOnBeansTableModel.setColumnsForComboBoxes(_listenOnBeansTable);
        _listenOnBeansTable.setDefaultRenderer(JButton.class, new ButtonRenderer());
        _listenOnBeansTable.setDefaultEditor(JButton.class, new ButtonEditor(new JButton()));

        JButton testButton = new JButton("XXXXXX");  // NOI18N
        _listenOnBeansTable.setRowHeight(testButton.getPreferredSize().height);
        TableColumn deleteColumn = _listenOnBeansTable.getColumnModel()
                .getColumn(ListenOnThrottleTableModel.COLUMN_DUMMY);
        deleteColumn.setMinWidth(testButton.getPreferredSize().width);
        deleteColumn.setMaxWidth(testButton.getPreferredSize().width);
        deleteColumn.setResizable(false);

        // The dummy column is used to be able to force update of the
        // other columns when the panel is closed.
        TableColumn dummyColumn = _listenOnBeansTable.getColumnModel()
                .getColumn(ListenOnThrottleTableModel.COLUMN_DUMMY);
        dummyColumn.setMinWidth(0);
        dummyColumn.setPreferredWidth(0);
        dummyColumn.setMaxWidth(0);

        JScrollPane scrollpane = new JScrollPane(_listenOnBeansTable);
        scrollpane.setPreferredSize(new Dimension(400, 200));
        panel.add(scrollpane);

        _listenOnSpeed = new JCheckBox(Bundle.getMessage("ActionListenOnThrottleSwing_ListenOnSpeed"));
        _listenOnSpeed.setSelected(true);
        panel.add(_listenOnSpeed);

        _listenOnIsForward = new JCheckBox(Bundle.getMessage("ActionListenOnThrottleSwing_ListenOnIsForward"));
        _listenOnIsForward.setSelected(true);
        panel.add(_listenOnIsForward);

        _listenOnFunction = new JCheckBox(Bundle.getMessage("ActionListenOnThrottleSwing_ListenOnFunction"));
        _listenOnFunction.setSelected(true);
        panel.add(_listenOnFunction);

        JPanel localVariableEventPanel = new JPanel();
        localVariableEventPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableEvent")));
        _localVariableEvent = new JTextField(20);
        localVariableEventPanel.add(_localVariableEvent);
        panel.add(localVariableEventPanel);

        JPanel localVariableLocoAddressPanel = new JPanel();
        localVariableLocoAddressPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableLocoAddress")));
        _localVariableLocoAddress = new JTextField(20);
        localVariableLocoAddressPanel.add(_localVariableLocoAddress);
        panel.add(localVariableLocoAddressPanel);

        JPanel localVariableRosterPanel = new JPanel();
        localVariableRosterPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableRoster")));
        _localVariableRoster = new JTextField(20);
        localVariableRosterPanel.add(_localVariableRoster);
        panel.add(localVariableRosterPanel);

        JPanel localVariableSpeedPanel = new JPanel();
        localVariableSpeedPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableSpeed")));
        _localVariableSpeed = new JTextField(20);
        localVariableSpeedPanel.add(_localVariableSpeed);
        panel.add(localVariableSpeedPanel);

        JPanel localVariableIsForwardPanel = new JPanel();
        localVariableIsForwardPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableIsForward")));
        _localVariableIsForward = new JTextField(20);
        localVariableIsForwardPanel.add(_localVariableIsForward);
        panel.add(localVariableIsForwardPanel);

        JPanel localVariableFunctionPanel = new JPanel();
        localVariableFunctionPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableFunction")));
        _localVariableFunction = new JTextField(20);
        localVariableFunctionPanel.add(_localVariableFunction);
        panel.add(localVariableFunctionPanel);

        JPanel localVariableFunctionStatePanel = new JPanel();
        localVariableFunctionStatePanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableFunctionState")));
        _localVariableFunctionState = new JTextField(20);
        localVariableFunctionStatePanel.add(_localVariableFunctionState);
        panel.add(localVariableFunctionStatePanel);

        JPanel localVariableUserDataPanel = new JPanel();
        localVariableUserDataPanel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_LocalVariableUserData")));
        _localVariableUserData = new JTextField(20);
        localVariableUserDataPanel.add(_localVariableUserData);
        panel.add(localVariableUserDataPanel);

        panel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_Warning1")));
        panel.add(new JLabel(Bundle.getMessage("ActionListenOnThrottleSwing_Warning2")));

        if (listenOnBeans != null) {
            _listenOnSpeed.setSelected(listenOnBeans.getListenOnSpeed());
            _listenOnIsForward.setSelected(listenOnBeans.getListenOnIsForward());
            _listenOnFunction.setSelected(listenOnBeans.getListenOnFunction());
            _localVariableEvent.setText(listenOnBeans.getLocalVariableEvent());
            _localVariableLocoAddress.setText(listenOnBeans.getLocalVariableLocoAddress());
            _localVariableRoster.setText(listenOnBeans.getLocalVariableRoster());
            _localVariableSpeed.setText(listenOnBeans.getLocalVariableSpeed());
            _localVariableIsForward.setText(listenOnBeans.getLocalVariableIsForward());
            _localVariableFunction.setText(listenOnBeans.getLocalVariableFunction());
            _localVariableFunctionState.setText(listenOnBeans.getLocalVariableFunctionState());
            _localVariableUserData.setText(listenOnBeans.getLocalVariableUserData());
        }

        // Add parameter
        JButton add = new JButton(Bundle.getMessage("ActionListenOnThrottle_TableAddReference"));
        buttonPanel.add(add);
        add.addActionListener((ActionEvent e) -> {
            _listenOnBeansTableModel.add();
        });
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate(@Nonnull List<String> errorMessages) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @CheckForNull String userName) {
        ActionListenOnThrottle action = new ActionListenOnThrottle(systemName, userName);
        updateObject(action);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        if (! (object instanceof ActionListenOnThrottle)) {
            throw new IllegalArgumentException("object is not a ActionListenOnThrottle: " + object.getClass().getName());
        }
        ActionListenOnThrottle listenOnBeans = (ActionListenOnThrottle)object;

        // Do this to force update of the table
        _listenOnBeansTable.editCellAt(0, 2);

        listenOnBeans.clearReferences();

        for (ThrottleReference reference : _listenOnBeansTableModel.getReferences()) {
            listenOnBeans.addReference(reference);
        }

        listenOnBeans.setListenOnSpeed(_listenOnSpeed.isSelected());
        listenOnBeans.setListenOnIsForward(_listenOnIsForward.isSelected());
        listenOnBeans.setListenOnFunction(_listenOnFunction.isSelected());
        listenOnBeans.setLocalVariableEvent(_localVariableEvent.getText());
        listenOnBeans.setLocalVariableLocoAddress(_localVariableLocoAddress.getText());
        listenOnBeans.setLocalVariableRoster(_localVariableRoster.getText());
        listenOnBeans.setLocalVariableSpeed(_localVariableSpeed.getText());
        listenOnBeans.setLocalVariableIsForward(_localVariableIsForward.getText());
        listenOnBeans.setLocalVariableFunction(_localVariableFunction.getText());
        listenOnBeans.setLocalVariableFunctionState(_localVariableFunctionState.getText());
        listenOnBeans.setLocalVariableUserData(_localVariableUserData.getText());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Bundle.getMessage("ActionListenOnThrottle_Short");
    }

    @Override
    public void dispose() {
    }

}
