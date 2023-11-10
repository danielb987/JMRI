package jmri.jmrit.logixng.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import jmri.*;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.OBlockManager;
import jmri.jmrit.logix.Warrant;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.util.*;
import jmri.jmrit.logixng.util.parser.*;
import jmri.util.ThreadingUtil;

/**
 * This action triggers an OBlock.
 *
 * @author Daniel Bergqvist Copyright 2021
 * @author Dave Sand Copyright 2021
 */
public class ActionOBlock extends AbstractDigitalAction
        implements PropertyChangeListener {

    private final LogixNG_SelectNamedBean<OBlock> _selectNamedBean =
            new LogixNG_SelectNamedBean<>(
                    this, OBlock.class, InstanceManager.getDefault(OBlockManager.class), this);

    private final LogixNG_SelectEnum<DirectOperation> _selectEnum =
            new LogixNG_SelectEnum<>(this, DirectOperation.values(), DirectOperation.Deallocate, this);

    private final LogixNG_SelectNamedBean<Memory> _selectMemoryNamedBean =
            new LogixNG_SelectNamedBean<>(
                    this, Memory.class, InstanceManager.getDefault(MemoryManager.class), this);

    private final LogixNG_SelectString _selectOBlockValue =
            new LogixNG_SelectString(this, this);


    public ActionOBlock(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    @Override
    public Base getDeepCopy(Map<String, String> systemNames, Map<String, String> userNames) throws ParserException {
        DigitalActionManager manager = InstanceManager.getDefault(DigitalActionManager.class);
        String sysName = systemNames.get(getSystemName());
        String userName = userNames.get(getSystemName());
        if (sysName == null) sysName = manager.getAutoSystemName();
        ActionOBlock copy = new ActionOBlock(sysName, userName);
        copy.setComment(getComment());
        _selectNamedBean.copy(copy._selectNamedBean);
        _selectMemoryNamedBean.copy(copy._selectMemoryNamedBean);
        _selectEnum.copy(copy._selectEnum);
        _selectOBlockValue.copy(copy._selectOBlockValue);
        return manager.registerAction(copy);
    }

    public LogixNG_SelectNamedBean<OBlock> getSelectNamedBean() {
        return _selectNamedBean;
    }

    public LogixNG_SelectNamedBean<Memory> getSelectMemoryNamedBean() {
        return _selectMemoryNamedBean;
    }

    public LogixNG_SelectEnum<DirectOperation> getSelectEnum() {
        return _selectEnum;
    }

    public LogixNG_SelectString getSelectOBlockValue() {
        return _selectOBlockValue;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws JmriException {
        final ConditionalNG conditionalNG = getConditionalNG();

        OBlock oblock = _selectNamedBean.evaluateNamedBean(conditionalNG);

        if (oblock == null) return;

        DirectOperation oper = _selectEnum.evaluateEnum(conditionalNG);

        // Variables used in lambda must be effectively final
        DirectOperation theOper = oper;

        ThreadingUtil.runOnLayoutWithJmriException(() -> {
            switch (theOper) {
                case Deallocate:
                    oblock.deAllocate(null);
                    break;
                case SetValue:
                    oblock.setValue(_selectOBlockValue.evaluateValue(conditionalNG));
                    break;
                case SetError:
                    oblock.setError(true);
                    break;
                case ClearError:
                    oblock.setError(false);
                    break;
                case SetOutOfService:
                    oblock.setOutOfService(true);
                    break;
                case ClearOutOfService:
                    oblock.setOutOfService(false);
                    break;
                case GetBlockWarrant:
                    Memory memory = _selectMemoryNamedBean.evaluateNamedBean(conditionalNG);
                    if (memory != null) {
                        Warrant w = oblock.getWarrant();
                        if (w != null) {
                            memory.setValue(w.getDisplayName());
                        } else {
                            memory.setValue("unallocated");
                        }
                    } else {
                        throw new JmriException("Memory for GetBlockWarrant is null for oblock - " + oblock.getDisplayName());  // NOI18N
                    }
                    break;
                case GetBlockValue:
                    memory = _selectMemoryNamedBean.evaluateNamedBean(conditionalNG);
                    if (memory != null) {
                        Object obj = oblock.getValue();
                        if (obj instanceof String) {
                            memory.setValue(obj);
                        } else {
                            memory.setValue("");
                        }
                    } else {
                        throw new JmriException("Memory for GetBlockValue is null for oblock - " + oblock.getDisplayName());  // NOI18N
                    }
                    break;
                default:
                    throw new IllegalArgumentException("invalid oper state: " + theOper.name());
            }
        });
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription(Locale locale) {
        return Bundle.getMessage(locale, "ActionOBlock_Short");
    }

    @Override
    public String getLongDescription(Locale locale) {
        String namedBean = _selectNamedBean.getDescription(locale);
        String state = _selectEnum.getDescription(locale);
        String getLocationMemory = _selectMemoryNamedBean.getDescription(locale);
        String _oblockValue = _selectOBlockValue.getDescription(locale);

        if (_selectEnum.isDirectAddressing()) {
            if (_selectEnum.getEnum() != null) {
                switch (_selectEnum.getEnum()) {
                    case SetValue:
                        return Bundle.getMessage(locale, "ActionOBlock_Long_Value", namedBean, _oblockValue);
                    case GetBlockWarrant:
                        return Bundle.getMessage(locale, "ActionOBlock_Long_GetWarrant", namedBean, getLocationMemory);
                    case GetBlockValue:
                        return Bundle.getMessage(locale, "ActionOBlock_Long_GetTrain", namedBean, getLocationMemory);
                    default:
                        // Fall thru and handle it in the end of the method
                }
            }
        }

        return Bundle.getMessage(locale, "ActionOBlock_Long", namedBean, state);
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
        _selectNamedBean.registerListeners();
        _selectEnum.registerListeners();
        _selectMemoryNamedBean.addPropertyChangeListener("value", this);
        _selectOBlockValue.registerListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        _selectNamedBean.unregisterListeners();
        _selectEnum.unregisterListeners();
        _selectMemoryNamedBean.removePropertyChangeListener("value", this);
        _selectOBlockValue.unregisterListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }

    public enum DirectOperation {
        Deallocate(Bundle.getMessage("ActionOBlock_Deallocate")),
        SetValue(Bundle.getMessage("ActionOBlock_SetValue")),
        SetError(Bundle.getMessage("ActionOBlock_SetError")),
        ClearError(Bundle.getMessage("ActionOBlock_ClearError")),
        SetOutOfService(Bundle.getMessage("ActionOBlock_SetOutOfService")),
        ClearOutOfService(Bundle.getMessage("ActionOBlock_ClearOutOfService")),
        GetBlockWarrant(Bundle.getMessage("ActionOBlock_GetBlockWarrant")),
        GetBlockValue(Bundle.getMessage("ActionOBlock_GetValue"));

        private final String _text;

        private DirectOperation(String text) {
            this._text = text;
        }

        @Override
        public String toString() {
            return _text;
        }

    }

    /** {@inheritDoc} */
    @Override
    public void getUsageDetail(int level, NamedBean bean, List<NamedBeanUsageReport> report, NamedBean cdl) {
        _selectNamedBean.getUsageDetail(level, bean, report, cdl, this, LogixNG_SelectNamedBean.Type.Action);
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getConditionalNG().execute();
    }

//    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionOBlock.class);

}
