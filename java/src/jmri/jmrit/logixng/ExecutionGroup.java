package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * An execution group is a group of LogixNGs that are executed together.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ExecutionGroup extends Base, NamedBean {

    /**
     * Execute all the LogixNGs in this execution group.
     */
    public void execute();

}
