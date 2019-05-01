package jmri.jmrit.logixng.template;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import jmri.Conditional;
import jmri.Logix;

/**
 * A null logix.
 */
public class NullLogix extends AbstractNullNamedBean implements Logix {

    static final ResourceBundle rbm = ResourceBundle.getBundle("jmri.implementation.ImplementationBundle");

    /**
     * Create a new NullLogix instance using only a system name.
     *
     * @param sys the system name for this bean; must not be null and must
     *            be unique within the layout
     */
    public NullLogix(@Nonnull String sys) {
        super(sys);
    }

    @Override
    public String getBeanType() {
        return rbm.getString("BeanNameLogix");
    }

    @Override
    public void setEnabled(boolean state) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean getEnabled() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getNumConditionals() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void swapConditional(int nextInOrder, int row) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getConditionalByNumberOrder(int order) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addConditional(String systemName, int order) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addConditional(String systemName, Conditional conditional) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Conditional getConditional(String systemName) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String[] deleteConditional(String systemName) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void calculateConditionals() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void activateLogix() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void deActivateLogix() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setGuiNames() {
        throw new UnsupportedOperationException("Not supported.");
    }

}
