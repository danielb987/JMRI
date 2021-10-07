package jmri.jmrit.logixng.implementation;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import jmri.JmriException;
import jmri.NamedBean;
import jmri.NamedBean.BadUserNameException;
import jmri.NamedBean.BadSystemNameException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Type;

/**
 * The abstract implementation of a Type
 * 
 * @author Daniel Bergqvist 2021
 */
public abstract class AbstractType extends AbstractNamedBean implements Type {

    private int _state = NamedBean.UNKNOWN;
    private String _className;
    protected final ClassLoader _classLoader = AbstractType.class.getClassLoader();
    
    /**
     * Create a new type.
     * @param sys the system name
     * @param user the user name or null if no user name
     */
    public AbstractType(
            @Nonnull String sys, @CheckForNull String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys,user);
    }
    
    @Override
    public String getClassName() {
        return _className;
    }

    @Override
    public void setClassName(String name) throws JmriException {
        _className = name;
    }

    @Override
    public void setState(int s) throws JmriException {
        _state = s;
    }
    
    @Override
    public int getState() {
        return _state;
    }
    
    @Override
    public String getBeanType() {
        return Bundle.getMessage("BeanNameType");
    }
    
}
