package jmri.jmrit.logixng.digitalactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketFactory;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.DigitalActionManager;

/**
 * An action that can hold everything but doesn't do anything.
 * It is used to hold parts of the LogixNG tree that is not currently in use.
 This allows moving operations in the tree.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class HoldAnything extends AbstractDigitalAction {

    private final List<MultipleSockets> _multipleSockets = new ArrayList<>();
    
    /**
     * Create a new instance of ActionMany and generate a new system name.
     * @param newLogix the LogixNG that this action is related to
     */
    public HoldAnything(LogixNG newLogix)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
        
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(newLogix));
        init();
    }

    public HoldAnything(String sys)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
        
        super(sys);
        init();
    }

    public HoldAnything(String sys, String user)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
        
        super(sys, user);
        init();
    }
    
    private void init() {
        for (FemaleSocketFactory factory :
                InstanceManager.getDefault(LogixNG_Manager.class).getFemaleSocketFactories()) {
            
            _multipleSockets.add(new MultipleSockets(factory));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.COMMON;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // Do nothing
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        int i = index;
        
        for (MultipleSockets multipleSockets : _multipleSockets) {
            if (i < multipleSockets._femaleSockets.size()) {
                return multipleSockets._femaleSockets.get(i);
            }
            i -= multipleSockets._femaleSockets.size();
        }
        
        throw new IllegalArgumentException(String.format("index %d out of range", index));
    }

    @Override
    public int getChildCount() {
        return _multipleSockets.size();
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("HoldAnything_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("HoldAnything_Long");
    }

    
    
    private static class MultipleSockets implements MaleSocket, FemaleSocketListener {

        private final FemaleSocketFactory _femaleSocketFactory;
        private final List<FemaleSocket> _femaleSockets = new ArrayList<>();
        private Lock _lock = Lock.NONE;
        private DebugConfig _debugConfig = null;
        
        private MultipleSockets(FemaleSocketFactory femaleSocketFactory) {
            _femaleSocketFactory = femaleSocketFactory;
            _femaleSockets.add(femaleSocketFactory.create());
        }
        
        /** {@inheritDoc} */
        @Override
        public String getSystemName() {
            throw new RuntimeException("This method should never be called");
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

        @Override
        public String getConfiguratorClassName() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getShortDescription() {
            return "Multiple sockets for " + _femaleSocketFactory.getClass().getName();
        }

        @Override
        public String getLongDescription() {
            return "Multiple sockets for " + _femaleSocketFactory.getClass().getName();
        }

        @Override
        public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
            return _femaleSockets.get(index);
        }

        @Override
        public int getChildCount() {
            return _femaleSockets.size();
        }

        @Override
        public Category getCategory() {
            return Category.OTHER;
        }

        @Override
        public boolean isExternal() {
            return false;
        }

        @Override
        public void connected(FemaleSocket socket) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void disconnected(FemaleSocket socket) {
            throw new UnsupportedOperationException("Not supported yet.");
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
            return new ActionDebugConfig();
        }



        public class ActionDebugConfig implements MaleSocket.DebugConfig {

        }

    }
    
}
