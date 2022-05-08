package jmri.jmrit.logixng.configurexml;

import java.util.*;

import jmri.*;

import org.jdom2.Element;

/**
 * Imports/Exports an item in LogixNG, for example an action or an expression.
 *
 * @author Daniel Berqvist (C) 2022
 */
public interface ImportExportItemXml {

    public void addNamedBeansToExport(
            Object o,
            Map<Manager<? extends NamedBean>, Map<String,NamedBeanToExport>> map);

    public boolean export(Element e);


    /**
     * Add a named bean to be exported
     * @param bean          the bean
     * @param manager       the manager for the bean. InstanceManager.getDefault(TurnoutManager.class)
     * @param managerClass  the class. TurnoutManager.class
     * @param map           the map to add the bean to
     */
    public static void addNameBean(
            NamedBean bean,
            Manager<? extends NamedBean> manager,
            Class<? extends Manager> managerClass,
            Map<Manager<? extends NamedBean>, Map<String,NamedBeanToExport>> map) {

        Map<String,NamedBeanToExport> subMap = map.get(manager);
        if (subMap == null) {
            subMap = new HashMap<>();
            map.put(manager, subMap);
        }
        NamedBeanToExport data = subMap.get(bean.getSystemName());
        if (data == null) {
            data = new NamedBeanToExport(bean, manager, managerClass);
            subMap.put(bean.getSystemName(), data);
        }
    }


    public static class NamedBeanToExport {
        private final NamedBean _namedBean;
        private final Manager<? extends NamedBean> _manager;
        private final Class<? extends Manager> _managerClass;

        public NamedBeanToExport(NamedBean bean, Manager<? extends NamedBean> manager, Class<? extends Manager> managerClass) {
            _namedBean = bean;
            _manager = manager;
            _managerClass = managerClass;
        }

        public NamedBean getNamedBean() {
            return _namedBean;
        }

        public Manager<? extends NamedBean> getManager() {
            return _manager;
        }

        public Class<? extends Manager> getManagerClass() {
            return _managerClass;
        }
    }

    public static class NamedBeanToImport {
        private final String _origSystemName;
        private final String _origUserName;
        private final Manager<? extends NamedBean> _manager;
        private NamedBeanHandle<? extends NamedBean> _beanHandle;

        public NamedBeanToImport(String origSystemName, String origUserName, String managerClass) {
            throw new RuntimeException("Daniel");
        }
    }

}
