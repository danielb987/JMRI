package jmri.jmrit.logixng.template;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;

/**
 * A null signal head.
 */
public class NullSignalHead extends AbstractNullNamedBean {

    static final ResourceBundle rbm = ResourceBundle.getBundle("jmri.implementation.ImplementationBundle");

    /**
     * Create a new NullSignalHead instance using only a system name.
     *
     * @param sys the system name for this bean; must not be null and must
     *            be unique within the layout
     */
    public NullSignalHead(@Nonnull String sys) {
        super(sys);
    }

    @Override
    public String getBeanType() {
        return rbm.getString("BeanNameSignalHead");
    }
    
 }
