package jmri.jmrit.logixng.actions.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import jmri.BasicRosterEntry;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle.ShortOrLongType;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle.ThrottleType;
import jmri.jmrit.logixng.actions.ActionListenOnThrottle.ThrottleReference;
import jmri.util.swing.JComboBoxUtil;

/**
 * Table model for ListenOnBeans named beans
 *
 * @author Daniel Bergqvist Copyright 2020
 */
public class ListenOnThrottleTableModel extends AbstractTableModel {

    // COLUMN_DUMMY is "hidden" but used when the panel is closed to
    // ensure that the last edited cell is saved.
    public static final int COLUMN_THROTTLE_TYPE = 0;
    public static final int COLUMN_ADDRESS = COLUMN_THROTTLE_TYPE + 1;
    public static final int COLUMN_SHORT_OR_LONG = COLUMN_ADDRESS + 1;
    public static final int COLUMN_DATA = COLUMN_SHORT_OR_LONG + 1;
    public static final int COLUMN_DELETE = COLUMN_DATA + 1;
    public static final int COLUMN_DUMMY = COLUMN_DELETE + 1;

    private final List<ThrottleReference> _throttleReference = new ArrayList<>();


    public ListenOnThrottleTableModel(Collection<ThrottleReference> namedBeanReference) {
        if (namedBeanReference != null) {
            for (ThrottleReference ref : namedBeanReference) {
                _throttleReference.add(new ThrottleReference(ref));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount() {
        return _throttleReference.size();
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount() {
        return COLUMN_DUMMY+1;
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(int col) {
        switch (col) {
            case COLUMN_THROTTLE_TYPE:
                return Bundle.getMessage("ActionListenOnThrottle_ColumnThrottleType");
            case COLUMN_ADDRESS:
                return Bundle.getMessage("ActionListenOnThrottle_ColumnAddress");
            case COLUMN_SHORT_OR_LONG:
                return Bundle.getMessage("ActionListenOnThrottle_ColumnShortOrLong");
            case COLUMN_DATA:
                return Bundle.getMessage("ActionListenOnThrottle_ColumnData");
            case COLUMN_DELETE:
                return "";  // no label
            case COLUMN_DUMMY:
                return "";  // no label
            default:
                throw new IllegalArgumentException("Invalid column");
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case COLUMN_THROTTLE_TYPE:
                return ThrottleType.class;
            case COLUMN_ADDRESS:
                return ThrottleReference.class;
            case COLUMN_SHORT_OR_LONG:
                return ShortOrLongType.class;
            case COLUMN_DATA:
                return String.class;
            case COLUMN_DELETE:
                return JButton.class;
            case COLUMN_DUMMY:
                return String.class;
            default:
                throw new IllegalArgumentException("Invalid column");
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        ThrottleReference ref = _throttleReference.get(rowIndex);

        switch (columnIndex) {
            case COLUMN_THROTTLE_TYPE:
                ThrottleType oldType = ref.getType();
                ref.setType((ThrottleType) value);
                if (oldType != ref.getType()) {
//                    ref.setName("");
//                    // When the type changes, the name is set to null, so the name cell needs to be updated
//                    fireTableCellUpdated(rowIndex, COLUMN_ADDRESS);
                }
                break;
            case COLUMN_ADDRESS:
                if (ref.getType() == ThrottleType.Roster) {
                    ref.setRosterEntry((BasicRosterEntry)value);
                } else {
                    ref.setAddress((int)value);
                }
                break;
            case COLUMN_SHORT_OR_LONG:
                ref.setShortOrLongType((ShortOrLongType) value);
                break;
            case COLUMN_DATA:
                ref.setData((String) value);
                break;
            case COLUMN_DELETE:
                delete(rowIndex);
                break;
            case COLUMN_DUMMY:
                break;
            default:
                throw new IllegalArgumentException("Invalid column");
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= _throttleReference.size()) throw new IllegalArgumentException("Invalid row");

        switch (columnIndex) {
            case COLUMN_THROTTLE_TYPE:
                return _throttleReference.get(rowIndex).getType();
            case COLUMN_ADDRESS:
                return _throttleReference.get(rowIndex);
            case COLUMN_SHORT_OR_LONG:
                return _throttleReference.get(rowIndex).getShortOrLongType();
            case COLUMN_DATA:
                return _throttleReference.get(rowIndex).getData();
            case COLUMN_DELETE:
                return Bundle.getMessage("ButtonDelete");  // NOI18N
            case COLUMN_DUMMY:
                return "";
            default:
                throw new IllegalArgumentException("Invalid column");
        }
    }

    public void setColumnsForComboBoxes(JTable table) {
        JComboBox<ThrottleType> beanTypeComboBox = new JComboBox<>();
        table.setRowHeight(beanTypeComboBox.getPreferredSize().height);
        table.getColumnModel().getColumn(COLUMN_THROTTLE_TYPE)
                .setPreferredWidth((beanTypeComboBox.getPreferredSize().width) + 4);
    }

    public void add() {
        int row = _throttleReference.size();
        _throttleReference.add(new ThrottleReference(ThrottleType.Address, 0, ShortOrLongType.Unknown, null, ""));
        fireTableRowsInserted(row, row);
    }

    private void delete(int row) {
        _throttleReference.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public List<ThrottleReference> getReferences() {
        return _throttleReference;
    }


    public static class CellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            if (column == COLUMN_THROTTLE_TYPE) {
                if (value == null) value = ThrottleType.Address;

                if (! (value instanceof ThrottleType)) {
                    throw new IllegalArgumentException("value is not an ThrottleType: " + value.getClass().getName());
                }
                setText(((ThrottleType) value).toString());
            } else {
                throw new RuntimeException("Unknown column: "+Integer.toString(column));
            }
            return this;
        }
    }


    public static class ThrottleTypeCellEditor extends AbstractCellEditor
            implements TableCellEditor, ActionListener {

        private ThrottleType _beanType;

        @Override
        public Object getCellEditorValue() {
            return this._beanType;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {

            if (value == null) value = ThrottleType.Address;

            if (! (value instanceof ThrottleType)) {
                throw new IllegalArgumentException("value is not an ThrottleType: " + value.getClass().getName());
            }

            JComboBox<ThrottleType> returnValueTypeComboBox = new JComboBox<>();

            for (ThrottleType type : ThrottleType.values()) {
                returnValueTypeComboBox.addItem(type);
            }
            JComboBoxUtil.setupComboBoxMaxRows(returnValueTypeComboBox);

            returnValueTypeComboBox.setSelectedItem(value);
            returnValueTypeComboBox.addActionListener(this);

            return returnValueTypeComboBox;
        }

        @Override
        @SuppressWarnings("unchecked")  // Not possible to check that event.getSource() is instanceof JComboBox<ThrottleType>
        public void actionPerformed(ActionEvent event) {
            if (! (event.getSource() instanceof JComboBox)) {
                throw new IllegalArgumentException("value is not an JComboBox: " + event.getSource().getClass().getName());
            }
            JComboBox<ThrottleType> returnValueTypeComboBox =
                    (JComboBox<ThrottleType>) event.getSource();
            _beanType = returnValueTypeComboBox.getItemAt(returnValueTypeComboBox.getSelectedIndex());

        }

    }


    public NamedBeanCellEditor getNamedBeanCellEditor() {
        return new NamedBeanCellEditor();
    }


    public class NamedBeanCellEditor extends AbstractCellEditor
            implements TableCellEditor, ActionListener {

        private ThrottleReference _namedBeanRef;

        @Override
        public Object getCellEditorValue() {
            return this._namedBeanRef;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})    // The actual types are not known by this class.
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {

            if ((value != null) && (! (value instanceof ThrottleReference))) {
                throw new IllegalArgumentException("value is not a ThrottleReference: " + value.getClass().getName());
            }
/*
            _namedBeanRef = _namedBeanReference.get(row);
            Manager manager = _namedBeanRef.getType().getManager();
            NamedBean selection = _namedBeanRef.getHandle() != null ? _namedBeanRef.getHandle().getBean() : null;
            NamedBeanComboBox<NamedBean> namedBeanComboBox =
                    new NamedBeanComboBox<>(manager, selection, NamedBean.DisplayOptions.DISPLAYNAME);
            namedBeanComboBox.setAllowNull(true);
            JComboBoxUtil.setupComboBoxMaxRows(namedBeanComboBox);

            namedBeanComboBox.addActionListener(this);

            return namedBeanComboBox;
*/
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")  // Not possible to check that event.getSource() is instanceof JComboBox<ThrottleType>
        public void actionPerformed(ActionEvent event) {
            if (! (event.getSource() instanceof JComboBox)) {
                throw new IllegalArgumentException("value is not an JComboBox: " + event.getSource().getClass().getName());
            }
//            NamedBeanComboBox<NamedBean> namedBeanComboBox = (NamedBeanComboBox<NamedBean>) event.getSource();
//            int index = namedBeanComboBox.getSelectedIndex();
//            NamedBean namedBean = (index != -1) ? namedBeanComboBox.getItemAt(index) : null;
//            _namedBeanRef.setName(namedBean);
        }

    }

}
