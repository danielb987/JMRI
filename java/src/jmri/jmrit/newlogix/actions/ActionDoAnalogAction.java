package jmri.jmrit.newlogix.actions;

import jmri.InstanceManager;
import jmri.jmrit.newlogix.AnalogActionManager;
import jmri.jmrit.newlogix.AnalogExpressionManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.FemaleAnalogActionSocket;
import jmri.jmrit.newlogix.FemaleAnalogExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleAnalogActionSocket;
import jmri.jmrit.newlogix.MaleAnalogExpressionSocket;

/**
 * Executes an analog action with the result of an analog expression.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionDoAnalogAction
        extends AbstractAction
        implements FemaleSocketListener {

    private final FemaleAnalogExpressionSocket _analogExpressionSocket;
    private final FemaleAnalogActionSocket _analogActionSocket;
    
    public ActionDoAnalogAction(String sys) {
        super(sys);
        _analogExpressionSocket = InstanceManager.getDefault(AnalogExpressionManager.class)
                .createFemaleAnalogExpressionSocket(this, "E1");
        _analogActionSocket = InstanceManager.getDefault(AnalogActionManager.class)
                .createFemaleAnalogActionSocket(this, "A1");
    }
    
    public ActionDoAnalogAction(String sys, String user) {
        super(sys, user);
        _analogExpressionSocket = InstanceManager.getDefault(AnalogExpressionManager.class)
                .createFemaleAnalogExpressionSocket(this, "E1");
        _analogActionSocket = InstanceManager.getDefault(AnalogActionManager.class)
                .createFemaleAnalogActionSocket(this, "A1");
    }
    
    public ActionDoAnalogAction(
            String sys,
            String expressionSocketName, String actionSocketName,
            MaleAnalogExpressionSocket expression, MaleAnalogActionSocket action) {
        
        super(sys);
        _analogExpressionSocket = InstanceManager.getDefault(AnalogExpressionManager.class)
                .createFemaleAnalogExpressionSocket(this, expressionSocketName, expression);
        _analogActionSocket = InstanceManager.getDefault(AnalogActionManager.class)
                .createFemaleAnalogActionSocket(this, actionSocketName, action);
    }
    
    public ActionDoAnalogAction(
            String sys, String user,
            String expressionSocketName, String actionSocketName, 
            MaleAnalogExpressionSocket expression, MaleAnalogActionSocket action) {
        
        super(sys, user);
        _analogExpressionSocket = InstanceManager.getDefault(AnalogExpressionManager.class)
                .createFemaleAnalogExpressionSocket(this, expressionSocketName, expression);
        _analogActionSocket = InstanceManager.getDefault(AnalogActionManager.class)
                .createFemaleAnalogActionSocket(this, actionSocketName, action);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        float result = _analogExpressionSocket.evaluate();
        
        _analogActionSocket.setValue(result);

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // We should never be here since executeStart() always return false.
        throw new UnsupportedOperationException("Not supported.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        return executeStart();
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // Do nothing
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _analogExpressionSocket;
                
            case 1:
                return _analogActionSocket;
                
            default:
                throw new IllegalArgumentException(
                        String.format("index has invalid value: %d", index));
        }
    }

    @Override
    public int getChildCount() {
        return 2;
    }

    @Override
    public void connected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("ActionDoAnalogAction_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("ActionDoAnalogAction", _analogExpressionSocket.getName(), _analogActionSocket.getName());
    }

}
