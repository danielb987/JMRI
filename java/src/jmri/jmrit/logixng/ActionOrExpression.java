package jmri.jmrit.logixng;

import java.util.List;

import jmri.NamedBean;
import jmri.NamedBeanHandle;
import jmri.jmrit.logixng.util.LogixNG_SelectNamedBean;

/**
 * A LogixNG action or expression.
 *
 * @author Daniel Bergqvist Copyright 2024
 */
public interface ActionOrExpression {

    default void getNamedBeans(List<NamedBeanHandle<? extends NamedBean>> list) {
        // Do nothing by default
    }

    default void replaceNamedBean(NamedBeanHandle<? extends NamedBean> oldBean,
            NamedBeanHandle<? extends NamedBean> newBean) {
        // Do nothing by default
    }

    default void getSelectNamedBeans(List<LogixNG_SelectNamedBean<? extends NamedBean>> list) {
        // Do nothing by default
    }

}
