package jmri.jmrit.logixng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A LogixNG digitalaction.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalAction extends Base {

    /**
     * Determines whether this action supports enable execution for this
     * digital action. An action for which execution is disabled will evaluate
     * its expressions, if it has that, but not execute any actions.
     * <p>
     * Note that enable execution for LogixNG is the equivalent of enable for Logix.
     * 
     * @return true if execution is enbaled for the digital action, false otherwise
     */
    default public boolean supportsEnableExecution() {
        return false;
    }
    
    /**
     * Enables or disables execution for this digital action. An action for
     * which execution is disabled will evaluate its expressions, if it has that,
     * but not execute any actions.
     * <p>
     * Note that enable execution for LogixNG is the equivalent of enable for Logix.
     * 
     * @param b if true, enables execution, otherwise disables execution
     */
    default public void setEnableExecution(boolean b) {
        log.error("This digital action does not supports the method setEnableExecution()");
        throw new UnsupportedOperationException("This digital action does not supports the method setEnableExecution()");
    }
    
    /**
     * Determines whether execution is enabled for this digital action. An
     * action for which execution is disabled will evaluate its expressions,
     * if it has that, but not execute any actions.
     * <p>
     * Note that EnableExecution for LogixNG is the equivalent of enable for Logix.
     * 
     * @return true if execution is enbaled for the digital action, false otherwise
     */
    default public boolean isExecutionEnabled() {
        log.error("This digital action does not supports the method isExecutionEnabled()");
        throw new UnsupportedOperationException("This digital action does not supports the method isExecutionEnabled()");
    }
    
    /**
     * Start execution of this DigitalActionBean.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeStart();
    
    /**
     * Continue execution of this DigitalActionBean if it last time returned true.
     * This method is called if Type == TRIGGER_ACTION, the previous call to
     * one of the execute???() methods returned True and the expression is
     * still True.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeContinue();
    
    /**
     * Restart the execute of this DigitalActionBean.
     * This method is called if Type == TRIGGER_ACTION and the expression has
     * become False and then True again.
     * 
     * If a parent action is restarted, it must restart all its children.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeRestart();
    
    /**
     * Abort this action.
     * This method is called when the LogixNG expression evaluates to False,
     * but the last call to executeStart() returned True.
     */
    public void abort();
    
    final static Logger log = LoggerFactory.getLogger(DigitalAction.class);
    
}
