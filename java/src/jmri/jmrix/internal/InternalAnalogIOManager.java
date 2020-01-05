package jmri.jmrix.internal;

import javax.annotation.Nonnull;
import jmri.AnalogIO;
import jmri.NamedBean;
import jmri.implementation.AbstractAnalogIO;
import jmri.util.PreferNumericComparator;

/**
 * Implement a AnalogIOManager for "Internal" (virtual) lights.
 *
 * @author Bob Jacobsen Copyright (C) 2009
 * @author Daniel Bergqvist Copyright (C) 2020
 */
public class InternalAnalogIOManager extends jmri.managers.AbstractAnalogIOManager {

    public InternalAnalogIOManager(InternalSystemConnectionMemo memo) {
        super(memo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public InternalSystemConnectionMemo getMemo() {
        return (InternalSystemConnectionMemo) memo;
    }

}
