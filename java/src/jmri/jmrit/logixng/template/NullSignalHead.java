package jmri.jmrit.logixng.template;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import jmri.SignalHead;

/**
 * A null signal head.
 */
public class NullSignalHead extends AbstractNullNamedBean implements SignalHead {

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

    @Override
    public int getAppearance() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setAppearance(int newAppearance) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getAppearanceName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getAppearanceName(int appearance) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean getLit() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setLit(boolean newLit) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean getHeld() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setHeld(boolean newHeld) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int[] getValidStates() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String[] getValidStateNames() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isCleared() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isShowingRestricting() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isAtStop() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
 }
