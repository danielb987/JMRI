package jmri.jmrit.logixng.implementation;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import jmri.JmriException;
import jmri.NamedBean.BadUserNameException;
import jmri.NamedBean.BadSystemNameException;

import org.openide.util.Exceptions;

/**
 * The default implementation of a Existing Class Type.
 * 
 * @author Daniel Bergqvist 2021
 */
public class DefaultExistingClassType extends AbstractType {

    private Class<?> _typeClass;
    
    /**
     * Create a type.
     * @param sys the system name
     * @param user the user name or null if no user name
     */
    public DefaultExistingClassType(
            @Nonnull String sys, @CheckForNull String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    @Override
    public Object getNewInstance() throws JmriException {
        try {
            return _typeClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            throw new JmriException("Cannot create instance", ex);
        }
    }
    
    @Override
    public void setClassName(String name) throws JmriException {
        super.setClassName(name);
        loadClass();
    }
    
    private void loadClass() throws JmriException {
        try {
            _typeClass = _classLoader.loadClass(getClassName());
        } catch (ClassNotFoundException e) {
            throw new JmriException("Cannot load class "+getClassName(), e);
        }
    }
    
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.ExistingClass;
    }
    
}
