package jmri.jmrit.logixng.template;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;

/**
 * A null turnout.
 */
public class NullOBlock extends AbstractNullNamedBean {

    static final ResourceBundle rbm = ResourceBundle.getBundle("jmri.jmrit.logix.WarrantBundle");

    /**
     * Create a new NullOBlock instance using only a system name.
     *
     * @param sys the system name for this bean; must not be null and must
     *            be unique within the layout
     */
    public NullOBlock(@Nonnull String sys) {
        super(sys);
        jmri.Block a;
    }

    @Override
    public String getBeanType() {
        return rbm.getString("BeanNameOBlock");
    }

}
