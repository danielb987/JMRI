package jmri.jmrit.newlogix.engine;

import jmri.jmrit.newlogix.Category;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Set;
import javax.annotation.Nonnull;
import jmri.JmriException;
import jmri.NamedBean;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.MaleExpressionSocket;

/**
 * Every Expression has an DefaultMaleExpressionSocket as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultMaleExpressionSocket implements MaleExpressionSocket {

    private final Expression _expression;
    private boolean lastEvaluationResult = false;
    
    public DefaultMaleExpressionSocket(@Nonnull Expression expression) {
        _expression = expression;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return _expression.getCategory();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        lastEvaluationResult = _expression.evaluate();
        return lastEvaluationResult;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _expression.reset();
    }

    @Override
    public int getState() {
        return lastEvaluationResult ? Expression.TRUE : Expression.FALSE;
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getShortDescription() {
        return _expression.getShortDescription();
    }

    @Override
    public String getLongDescription() {
        return _expression.getLongDescription();
    }

    @Override
    public String getUserName() {
        return _expression.getUserName();
    }

    @Override
    public void setUserName(String s) throws BadUserNameException {
        _expression.setUserName(s);
    }

    @Override
    public String getSystemName() {
        return _expression.getSystemName();
    }

    @Override
    public String getDisplayName() {
        return _expression.getDisplayName();
    }

    @Override
    public String getFullyFormattedDisplayName() {
        return _expression.getFullyFormattedDisplayName();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l, String name, String listenerRef) {
        _expression.addPropertyChangeListener(l, name, listenerRef);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        _expression.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        _expression.removePropertyChangeListener(l);
    }

    @Override
    public void updateListenerRef(PropertyChangeListener l, String newName) {
        _expression.updateListenerRef(l, newName);
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        _expression.vetoableChange(evt);
    }

    @Override
    public String getListenerRef(PropertyChangeListener l) {
        return _expression.getListenerRef(l);
    }

    @Override
    public ArrayList<String> getListenerRefs() {
        return _expression.getListenerRefs();
    }

    @Override
    public int getNumPropertyChangeListeners() {
        return _expression.getNumPropertyChangeListeners();
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListenersByReference(String name) {
        return _expression.getPropertyChangeListenersByReference(name);
    }

    @Override
    public void dispose() {
        _expression.dispose();
    }

    @Override
    public void setState(int s) throws JmriException {
        _expression.setState(s);
    }

    @Override
    public String describeState(int state) {
        return _expression.describeState(state);
    }

    @Override
    public String getComment() {
        return _expression.getComment();
    }

    @Override
    public void setComment(String comment) {
        _expression.setComment(comment);
    }

    @Override
    public void setProperty(String key, Object value) {
        _expression.setProperty(key, value);
    }

    @Override
    public Object getProperty(String key) {
        return _expression.getProperty(key);
    }

    @Override
    public void removeProperty(String key) {
        _expression.removeProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return _expression.getPropertyKeys();
    }

    @Override
    public String getBeanType() {
        return _expression.getBeanType();
    }

    @Override
    public int compareSystemNameSuffix(String suffix1, String suffix2, NamedBean n2) {
        return _expression.compareSystemNameSuffix(suffix1, suffix2, n2);
    }

    @Override
    public String getConfiguratorClassName() {
        return _expression.getConfiguratorClassName();
    }

}
