package jmri.jmrit.logixng.configurexml;

import java.util.HashMap;
import java.util.Map;

import jmri.Manager;
import jmri.NamedBean;

import org.jdom2.Element;

/**
 * Supports import/export of LogixNG
 * @author Daniel Bergqvist (C) 2022
 */
public interface ImportExportLogixNG {

    public enum WhatToDo {

        Keep(Bundle.getMessage("ImportExportLogixNG_WhatToDo_Keep")),
        DoNotCreate(Bundle.getMessage("ImportExportLogixNG_WhatToDo_DoNotCreate")),
        Create(Bundle.getMessage("ImportExportLogixNG_WhatToDo_Create")),
        CreateWithNewName(Bundle.getMessage("ImportExportLogixNG_WhatToDo_CreateWithNewName"));

        private final String _description;

        private WhatToDo(String description) {
            _description = description;
        }

        @Override
        public String toString() {
            return _description;
        }
    }

    public class ReferredNamedBean {
        private final Class<? extends NamedBean> _managerClass;
        private final Manager<? extends NamedBean> _manager;
        private final String _beanName;
        private final String _systemName;
        private final String _userName;
        private WhatToDo _whatToDo = WhatToDo.Keep;
        private boolean _convertToInternal;

        public ReferredNamedBean(
                Class<? extends NamedBean> managerClass,
                Manager<? extends NamedBean> manager,
                String beanName,
                String systemName,
                String userName) {
            _managerClass = managerClass;
            _manager = manager;
            _beanName = beanName;
            _systemName = systemName;
            _userName = userName;
        }
    }

    public class ReferredItems {

        // For now, this class has only referredNamedBeans, but later more
        // things might be added. Therefore this is in it's own class.

        private final Map<Manager<? extends NamedBean>, Map<String, ReferredNamedBean>> referredNamedBeans =
                new HashMap<>();

        public void addReferredNamedBean(
                Class<? extends NamedBean> managerClass,
                Manager<? extends NamedBean> manager,
                String beanName,
                String systemName,
                String userName) {

            Map<String, ReferredNamedBean> referredBeans = referredNamedBeans.get(manager);
            if (referredBeans == null) {
                referredBeans = new HashMap<>();
                referredNamedBeans.put(manager, referredBeans);
            }
            ReferredNamedBean referredNamedBean = referredBeans.get(beanName);
            if (referredNamedBean == null) {
                referredNamedBean = new ReferredNamedBean(managerClass, manager, beanName, systemName, userName);
                referredBeans.put(beanName, referredNamedBean);
            }
        }
    }

    public void doExport(Element element, ReferredItems referredItems);

    public void checkReferredItems(Element element, ReferredItems referredItems);

    public void doImport(Element element, ReferredItems referredItems);

}
