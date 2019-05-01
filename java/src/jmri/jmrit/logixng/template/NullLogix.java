package jmri.jmrit.logixng.template;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;

/**
 * A null logix.
 */
public class NullLogix extends AbstractNullNamedBean {

    static final ResourceBundle rbm = ResourceBundle.getBundle("jmri.implementation.ImplementationBundle");

    /**
     * Create a new NullLogix instance using only a system name.
     *
     * @param sys the system name for this bean; must not be null and must
     *            be unique within the layout
     */
    public NullLogix(@Nonnull String sys) {
        super(sys);
        jmri.Logix a;
    }

    @Override
    public String getBeanType() {
        return rbm.getString("BeanNameLogix");
    }

}
