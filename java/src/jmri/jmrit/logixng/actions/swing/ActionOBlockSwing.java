package jmri.jmrit.logixng.actions.swing;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.swing.*;

import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.OBlockManager;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.actions.ActionOBlock;
import jmri.jmrit.logixng.actions.ActionOBlock.DirectOperation;
import jmri.jmrit.logixng.swing.SwingConfiguratorInterface;
import jmri.jmrit.logixng.util.LogixNG_SelectEnum;
import jmri.jmrit.logixng.util.swing.LogixNG_SelectEnumSwing;
import jmri.jmrit.logixng.util.swing.LogixNG_SelectNamedBeanSwing;
import jmri.jmrit.logixng.util.swing.LogixNG_SelectStringSwing;

/**
 * Configures an ActionOBlock object with a Swing JPanel.
 *
 * @author Daniel Bergqvist  Copyright 2021
 * @author Dave Sand         Copyright 2021
 */
public class ActionOBlockSwing extends AbstractDigitalActionSwing {

    private LogixNG_SelectNamedBeanSwing<OBlock> _selectNamedBeanSwing;
    private LogixNG_SelectEnumSwing<DirectOperation> _selectOperationSwing;
    private LogixNG_SelectStringSwing _selectOBlockValueSwing;
    private LogixNG_SelectNamedBeanSwing<Memory> _selectMemorySwing;

    private JTabbedPane _tabbedPaneData;
    private JPanel _panelOBlockValue;
    private JPanel _panelMemoryBean;


    public ActionOBlockSwing() {
    }

    public ActionOBlockSwing(JDialog dialog) {
        super.setJDialog(dialog);
    }

    @Override
    protected void createPanel(@CheckForNull Base object, @Nonnull JPanel buttonPanel) {
        ActionOBlock action = (ActionOBlock)object;

        _selectNamedBeanSwing = new LogixNG_SelectNamedBeanSwing<>(
                InstanceManager.getDefault(OBlockManager.class), getJDialog(), this);

        _selectOperationSwing = new LogixNG_SelectEnumSwing<>(getJDialog(), this);
        _selectOBlockValueSwing = new LogixNG_SelectStringSwing(getJDialog(), this);
        _selectMemorySwing = new LogixNG_SelectNamedBeanSwing<>(
                InstanceManager.getDefault(MemoryManager.class), getJDialog(), this);

        panel = new JPanel();
        _tabbedPaneData = new JTabbedPane();

        JPanel tabbedPaneNamedBean;
        JPanel tabbedPaneOperation;

        if (action != null) {
            tabbedPaneNamedBean = _selectNamedBeanSwing.createPanel(action.getSelectNamedBean());
            tabbedPaneOperation = _selectOperationSwing.createPanel(action.getSelectEnum(), DirectOperation.values());
            _panelOBlockValue = _selectOBlockValueSwing.createPanel(action.getSelectOBlockValue());
            _panelMemoryBean = _selectMemorySwing.createPanel(action.getSelectMemoryNamedBean());
        } else {
            tabbedPaneNamedBean = _selectNamedBeanSwing.createPanel(null);
            tabbedPaneOperation = _selectOperationSwing.createPanel(null, DirectOperation.values());
            _panelOBlockValue = _selectOBlockValueSwing.createPanel(null);
            _panelMemoryBean = _selectMemorySwing.createPanel(null);
        }
//        _tabbedPaneData.add(_panelOBlockValue, Bundle.getMessage("ActionOBlock_DataPane_OBlockValue"));
//        _tabbedPaneData.add(_panelMemoryBean, Bundle.getMessage("ActionOBlock_DataPane_Memory"));

        if (action != null) {
            LogixNG_SelectEnum<DirectOperation> selectEnum = action.getSelectEnum();
            if (selectEnum.getEnum() != null) {
                switch (selectEnum.getEnum()) {
                    case GetBlockWarrant:
                    case GetBlockValue:
                        _panelMemoryBean.setVisible(true);
                        break;
                    case SetValue:
                        _panelOBlockValue.setVisible(true);
                        break;
                    default:
                }
            }
        }

        setDataPanelState();

        _selectOperationSwing.addAddressingListener((evt) -> { setDataPanelState(); });
        _selectOperationSwing.addEnumListener((evt) -> { setDataPanelState(); });

        JComponent[] components = new JComponent[]{
            tabbedPaneNamedBean,
            tabbedPaneOperation,
            _tabbedPaneData};

        List<JComponent> componentList = SwingConfiguratorInterface.parseMessage(
                Bundle.getMessage("ActionOBlock_Components"), components);

        for (JComponent c : componentList) panel.add(c);
    }

    private void setDataPanelState() {
        var selection = _tabbedPaneData.getSelectedComponent();

        _tabbedPaneData.removeAll();

        if (!_selectOperationSwing.isDirectAddressing()) {
            _panelOBlockValue.setVisible(true);
            _panelMemoryBean.setVisible(true);
            _tabbedPaneData.add(_panelOBlockValue, Bundle.getMessage("ActionOBlock_DataPane_OBlockValue"));
            _tabbedPaneData.add(_panelMemoryBean, Bundle.getMessage("ActionOBlock_DataPane_Memory"));
            if (selection != null) _tabbedPaneData.setSelectedComponent(selection);
            else _tabbedPaneData.setSelectedComponent(_panelOBlockValue);
        } else if (_selectOperationSwing.isEnumSelectedOrIndirectAddressing(DirectOperation.SetValue)) {
            _panelOBlockValue.setVisible(true);
            _tabbedPaneData.add(_panelOBlockValue, Bundle.getMessage("ActionOBlock_DataPane_OBlockValue"));
            _tabbedPaneData.setSelectedComponent(_panelOBlockValue);
        } else if (_selectOperationSwing.isEnumSelectedOrIndirectAddressing(DirectOperation.GetBlockWarrant) ||
                _selectOperationSwing.isEnumSelectedOrIndirectAddressing(DirectOperation.GetBlockValue)) {
            _panelMemoryBean.setVisible(true);
            _tabbedPaneData.add(_panelMemoryBean, Bundle.getMessage("ActionOBlock_DataPane_Memory"));
            _tabbedPaneData.setSelectedComponent(_panelMemoryBean);
        }

        getJDialog().pack();
    }


    /** {@inheritDoc} */
    @Override
    public boolean validate(@Nonnull List<String> errorMessages) {
        ActionOBlock action = new ActionOBlock("IQDA1", null);
        _selectNamedBeanSwing.validate(action.getSelectNamedBean(), errorMessages);
        _selectOperationSwing.validate(action.getSelectEnum(), errorMessages);
        _selectOBlockValueSwing.validate(action.getSelectOBlockValue(), errorMessages);
        validateDataSection(errorMessages);
        return errorMessages.isEmpty();
    }

    private void validateDataSection(List<String> errorMessages) {
        if (_selectOBlockValueSwing.isDirectAddressing()) {
            if (_selectOperationSwing.isEnumSelectedOrIndirectAddressing(DirectOperation.GetBlockWarrant)
                    || _selectOperationSwing.isEnumSelectedOrIndirectAddressing(DirectOperation.GetBlockValue)) {
                if (_selectMemorySwing.isDirectAddressing() && _selectMemorySwing.getBean() == null) {
//                if (_panelMemoryBean.isEmpty() || _panelMemoryBean.getNamedBean() == null) {
                    errorMessages.add(Bundle.getMessage("ActionWarrant_ErrorMemory"));
                }
            } else if (_selectOperationSwing.isEnumSelectedOrIndirectAddressing(DirectOperation.SetValue)) {
                if (_selectOBlockValueSwing.getValue().isEmpty()) {
                    errorMessages.add(Bundle.getMessage("ActionWarrant_ErrorValue"));
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket createNewObject(@Nonnull String systemName, @CheckForNull String userName) {
        ActionOBlock action = new ActionOBlock(systemName, userName);
        updateObject(action);
        return InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }

    /** {@inheritDoc} */
    @Override
    public void updateObject(@Nonnull Base object) {
        if (! (object instanceof ActionOBlock)) {
            throw new IllegalArgumentException("object must be an ActionOBlock but is a: "+object.getClass().getName());
        }
        ActionOBlock action = (ActionOBlock) object;
        _selectNamedBeanSwing.updateObject(action.getSelectNamedBean());

        // Center section
        _selectOperationSwing.updateObject(action.getSelectEnum());

        // Right section
        _selectOBlockValueSwing.updateObject(action.getSelectOBlockValue());
        _selectMemorySwing.updateObject(action.getSelectMemoryNamedBean());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Bundle.getMessage("ActionOBlock_Short");
    }

    @Override
    public void dispose() {
        _selectNamedBeanSwing.dispose();
        _selectOperationSwing.dispose();
        _selectOBlockValueSwing.dispose();
    }


//     private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionOBlockSwing.class);

}
