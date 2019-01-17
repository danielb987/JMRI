package jmri.managers;

import java.util.Objects;
import jmri.*;
import jmri.util.com.dictiography.collections.IndexedTreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a DigitalIOManager that can serve as a proxy for any
 * manager that provides NamedBeans that implements the DigitalIO interface.
 *
 * @author	Daniel Bergqvist Copyright (C) 2018
 */
public class ProxyDigitalIOManager implements DigitalIOManager, Manager.ManagerDataListener<DigitalIO> {

    @Override
    public void addManager(ProvidingManager<? extends DigitalIO> m) {
        Objects.requireNonNull(m, "Can only add non-null manager");
        // check for already present
        for (Manager<? extends DigitalIO> check : mgrs) {
            if (m == check) { // can't use contains(..) because of Comparator.equals is on the prefix
                // already present, complain and skip
                log.warn("Manager already present: {}", m); // NOI18N
                return;
            }
        }
        mgrs.add(m);

//        if (defaultManager == null) defaultManager = m;  // 1st one is default

//        propertyVetoListenerList.stream().forEach((l) -> {
//            m.addVetoableChangeListener(l);
//        });
//        propertyListenerList.stream().forEach((l) -> {
//            m.addPropertyChangeListener(l);
//        });

        m.addDataListener(this);
        updateOrderList();
        recomputeNamedBeanSet();

        if (log.isDebugEnabled()) {
            log.debug("added manager " + m.getClass());
        }
    }

    private final IndexedTreeSet<Manager<? extends DigitalIO>> mgrs =
            new IndexedTreeSet<>(new java.util.Comparator<Manager<? extends DigitalIO>>(){
        @Override
        public int compare(Manager<? extends DigitalIO> e1, Manager<? extends DigitalIO> e2) { return e1.getSystemPrefix().compareTo(e2.getSystemPrefix()); }
    });

    // initialize logging
    private final static Logger log = LoggerFactory.getLogger(ProxyDigitalIOManager.class);

}
