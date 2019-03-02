package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.Category;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import javax.annotation.Nonnull;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.MaleStringExpressionSocket;
import jmri.jmrit.logixng.StringExpression;

/**
 * Every StringExpression has an DefaultMaleStringExpressionSocket as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultMaleStringExpressionSocket implements MaleStringExpressionSocket {

    private final StringExpression _stringExpression;
    private Lock _lock = Lock.NONE;
    private DebugConfig _debugConfig = null;


    public DefaultMaleStringExpressionSocket(@Nonnull StringExpression stringExpression) {
        _stringExpression = stringExpression;
    }

    @Override
    public Base getParent() {
        return _stringExpression.getParent();
    }

    @Override
    public void setParent(Base parent) {
        _stringExpression.setParent(parent);
    }

    /** {@inheritDoc} */
    @Override
    public Lock getLock() {
        return _lock;
    }

    /** {@inheritDoc} */
    @Override
    public void setLock(Lock lock) {
        _lock = lock;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return _stringExpression.getCategory();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public String evaluate() {
        return _stringExpression.evaluate();
    }

    @Override
    public int getState() {
        return NamedBean.UNKNOWN;
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return _stringExpression.getChild(index);
    }

    @Override
    public int getChildCount() {
        return _stringExpression.getChildCount();
    }

    @Override
    public String getShortDescription() {
        return _stringExpression.getShortDescription();
    }

    @Override
    public String getLongDescription() {
        return _stringExpression.getLongDescription();
    }

    @Override
    public String getUserName() {
        return _stringExpression.getUserName();
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        _stringExpression.setUserName(s);
    }

    @Override
    public String getSystemName() {
        return _stringExpression.getSystemName();
    }

    @Override
    public String getDisplayName() {
        return _stringExpression.getDisplayName();
    }

    @Override
    public String getFullyFormattedDisplayName() {
        return _stringExpression.getFullyFormattedDisplayName();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        _stringExpression.addPropertyChangeListener(l, name, listenerRef);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        _stringExpression.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        _stringExpression.removePropertyChangeListener(l);
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        _stringExpression.updateListenerRef(l, newName);
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        _stringExpression.vetoableChange(evt);
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        return _stringExpression.getListenerRef(l);
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        return _stringExpression.getListenerRefs();
    }

    @Override
    public int getNumPropertyChangeListeners() {
        return _stringExpression.getNumPropertyChangeListeners();
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        return _stringExpression.getPropertyChangeListenersByReference(name);
    }

    @Override
    public void dispose() {
        _stringExpression.dispose();
    }

    @Override
    public void setState(int s) throws JmriException {
        // Do nothing
    }

    @Override
    public String describeState(int state) {
        return Bundle.getMessage("BeanStateUnknown");
    }

    @Override
    public String getComment() {
        return _stringExpression.getComment();
    }

    @Override
    public void setComment(String comment) {
        _stringExpression.setComment(comment);
    }

    @Override
    public void setProperty(String key, Object value) {
        _stringExpression.setProperty(key, value);
    }

    @Override
    public Object getProperty(String key) {
        return _stringExpression.getProperty(key);
    }

    @Override
    public void removeProperty(String key) {
        _stringExpression.removeProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return _stringExpression.getPropertyKeys();
    }

    @Override
    public String getBeanType() {
        return _stringExpression.getBeanType();
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        return _stringExpression.compareSystemNameSuffix(suffix1, suffix2, n2);
    }

    @Override
    public String getConfiguratorClassName() {
        return _stringExpression.getConfiguratorClassName();
    }

    /** {@inheritDoc} */
    @Override
    public void setDebugConfig(DebugConfig config) {
        _debugConfig = config;
    }

    /** {@inheritDoc} */
    @Override
    public DebugConfig getDebugConfig() {
        return _debugConfig;
    }

    /** {@inheritDoc} */
    @Override
    public DebugConfig createDebugConfig() {
        return new StringExpressionDebugConfig();
    }



    public class StringExpressionDebugConfig implements MaleSocket.DebugConfig {
        
        // If true, the socket is not executing the action.
        // It's useful if you want to test the LogixNG without affecting the
        // layout (turnouts, sensors, and so on).
        public boolean dontExecute = false;
        
    }

}
