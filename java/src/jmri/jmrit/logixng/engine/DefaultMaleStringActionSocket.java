package jmri.jmrit.logixng.engine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.logixng.Category;
import javax.annotation.Nonnull;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.MaleStringActionSocket;
import jmri.jmrit.logixng.StringAction;

/**
 * Every StringAction has an DefaultMaleStringActionSocket as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultMaleStringActionSocket implements MaleStringActionSocket {

    private final StringAction _stringAction;
    private Lock _lock = Lock.NONE;
    private DebugConfig _debugConfig = null;
    
    
    public DefaultMaleStringActionSocket(@Nonnull StringAction stringAction) {
        _stringAction = stringAction;
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
        return _stringAction.getCategory();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    /**
     * Set a string value.
     */
    public void setValue(String value) {
        _stringAction.setValue(value);
    }

    @Override
    public String getShortDescription() {
        return _stringAction.getShortDescription();
    }

    @Override
    public String getLongDescription() {
        return _stringAction.getLongDescription();
    }

    @Override
    public FemaleSocket getChild(int index)
            throws IllegalArgumentException, UnsupportedOperationException {
        return _stringAction.getChild(index);
    }

    @Override
    public int getChildCount() {
        return _stringAction.getChildCount();
    }

    @Override
    public String getUserName() {
        return _stringAction.getUserName();
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        _stringAction.setUserName(s);
    }

    @Override
    public String getSystemName() {
        return _stringAction.getSystemName();
    }

    @Override
    public String getDisplayName() {
        return _stringAction.getDisplayName();
    }

    @Override
    public String getFullyFormattedDisplayName() {
        return _stringAction.getFullyFormattedDisplayName();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        _stringAction.addPropertyChangeListener(l, name, listenerRef);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        _stringAction.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        _stringAction.removePropertyChangeListener(l);
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        _stringAction.updateListenerRef(l, newName);
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        _stringAction.vetoableChange(evt);
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        return _stringAction.getListenerRef(l);
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        return _stringAction.getListenerRefs();
    }

    @Override
    public int getNumPropertyChangeListeners() {
        return _stringAction.getNumPropertyChangeListeners();
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        return _stringAction.getPropertyChangeListenersByReference(name);
    }

    @Override
    public void dispose() {
        _stringAction.dispose();
    }

    @Override
    public void setState(int s) throws JmriException {
        _stringAction.setState(s);
    }

    @Override
    public int getState() {
        return _stringAction.getState();
    }

    @Override
    public String describeState(int state) {
        return _stringAction.describeState(state);
    }

    @Override
    public String getComment() {
        return _stringAction.getComment();
    }

    @Override
    public void setComment(String comment) {
        _stringAction.setComment(comment);
    }

    @Override
    public void setProperty(String key, Object value) {
        _stringAction.setProperty(key, value);
    }

    @Override
    public Object getProperty(String key) {
        return _stringAction.getProperty(key);
    }

    @Override
    public void removeProperty(String key) {
        _stringAction.removeProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return _stringAction.getPropertyKeys();
    }

    @Override
    public String getBeanType() {
        return _stringAction.getBeanType();
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        return _stringAction.compareSystemNameSuffix(suffix1, suffix2, n2);
    }

    @Override
    public String getConfiguratorClassName() {
        return _stringAction.getConfiguratorClassName();
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
        return new StringActionDebugConfig();
    }



    public class StringActionDebugConfig implements MaleSocket.DebugConfig {
        
        // If true, the socket is not executing the action.
        // It's useful if you want to test the LogixNG without affecting the
        // layout (turnouts, sensors, and so on).
        public boolean dontExecute = false;
        
    }

}
