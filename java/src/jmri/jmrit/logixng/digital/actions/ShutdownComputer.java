package jmri.jmrit.logixng.digital.actions;

import apps.AppsBase;
import java.io.IOException;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.util.SystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This action sets the state of a turnout.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ShutdownComputer extends AbstractDigitalAction {

    private ShutdownComputer _template;
    private int _seconds;
    
    public ShutdownComputer(String sys, int seconds)
            throws BadUserNameException {
        super(sys);
//        jmri.InstanceManager.turnoutManagerInstance().addVetoableChangeListener(this);
    }

    public ShutdownComputer(String sys, String user, int seconds)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
//        jmri.InstanceManager.turnoutManagerInstance().addVetoableChangeListener(this);
    }
    
    private ShutdownComputer(ShutdownComputer template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ShutdownComputer(this, sys);
    }
    
    public void setSeconds(int seconds) {
        _seconds = seconds;
    }
    
    public int getSeconds() {
        return _seconds;
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.EXRAVAGANZA;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean executeStart() {
        String time = (_seconds == 0) ? "now" : Integer.toString(_seconds);
        try {
            if (SystemType.isLinux() || SystemType.isUnix() || SystemType.isMacOSX()) {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("shutdown -h " + time);
//                Process proc = runtime.exec("shutdown -s -t " + time);
            } else if (SystemType.isWindows()) {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("shutdown.exe -s -t " + time);
//                Process proc = runtime.exec("shutdown -s -t " + time);
            } else {
                throw new UnsupportedOperationException("Unknown OS: "+SystemType.getOSName());
            }
        } catch (SecurityException | IOException e) {
            log.error("Shutdown failed", e);  // NOI18N
            return false;
        }
        
        // Quit program
        AppsBase.handleQuit();
        
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // We should normally never be here.
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        // We should normally never be here.
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // The executeStart() metod never return True from this action and
        // therefore abort() should never be called.
        throw new RuntimeException("ShutDown doesn't support abort()");
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("ShutdownComputer_Short");
    }

    @Override
    public String getLongDescription() {
        return Bundle.getMessage("ShutdownComputer_Long", _seconds);
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Do nothing
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ShutdownComputer.class);
}
