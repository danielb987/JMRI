package jmri.jmrit.newlogix.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ServiceLoader;
import javax.annotation.Nonnull;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.newlogix.NewLogix;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.newlogix.AnalogAction;
import jmri.jmrit.newlogix.AnalogActionManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.FemaleAnalogActionSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.MaleAnalogActionSocket;
import jmri.jmrit.newlogix.NewLogixAnalogActionFactory;
import jmri.jmrit.newlogix.NewLogixPluginFactory;
import jmri.managers.AbstractManager;

/**
 * Class providing the basic logic of the ActionManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultAnalogActionManager extends AbstractManager<MaleAnalogActionSocket>
        implements AnalogActionManager {

    private final Map<Category, List<Class<? extends AnalogAction>>> actionClassList = new HashMap<>();
    private int lastAutoActionRef = 0;
    
    // This is for testing only!!!
    // This number needs to be saved and restored.
    DecimalFormat paddedNumber = new DecimalFormat("0000");

    
    public DefaultAnalogActionManager() {
        super();
        
        for (Category category : Category.values()) {
            actionClassList.put(category, new ArrayList<>());
        }
        
        for (NewLogixAnalogActionFactory actionFactory : ServiceLoader.load(NewLogixAnalogActionFactory.class)) {
            actionFactory.getAnalogActionClasses().forEach((entry) -> {
                System.out.format("Add action: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                actionClassList.get(entry.getKey()).add(entry.getValue());
            });
        }
        
//        for (NewLogixPluginFactory actionFactory : ServiceLoader.load(NewLogixPluginFactory.class)) {
//            actionFactory.getAnalogActionClasses().forEach((entry) -> {
//                System.out.format("Add action plugin: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
//                actionClassList.get(entry.getKey()).add(entry.getValue());
//            });
//        }
    }

    protected MaleAnalogActionSocket createMaleActionSocket(AnalogAction action) {
        return new DefaultMaleAnalogActionSocket(action);
    }
    
    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param action the bean
     */
    @Override
    public MaleAnalogActionSocket register(@Nonnull AnalogAction action)
            throws IllegalArgumentException {
        
        // Check if system name is valid
        if (this.validSystemNameFormat(action.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + action.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        MaleAnalogActionSocket maleSocket = createMaleActionSocket(action);
        register(maleSocket);
        return maleSocket;
    }
    
    @Override
    public int getXMLOrder() {
        return NEWLOGIXS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameAnalogAction");
    }

    @Override
    public String getSystemPrefix() {
        return "I";
    }

    @Override
    public char typeLetter() {
        return 'Q';
    }

    /**
     * Test if parameter is a properly formatted system name.
     *
     * @param systemName the system name
     * @return enum indicating current validity, which might be just as a prefix
     */
    @Override
    public NameValidity validSystemNameFormat(String systemName) {
        // I - Internal
        // Q - NewLogix
        // :
        // Optional: A: - Automatic (if the system name is created by the software and not by the user
        // \d+ - The NewLogix ID number
        // :
        // Optional: A: - Automatic (if the system name is created by the software and not by the user
        // A - AnalogAction
        // \d+ - The AnalogAction ID number
        if (systemName.matches("IQ\\:(A\\:)?\\d+:(A\\:)?AA\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public String getNewSystemName(NewLogix newLogix) {
        int nextAutoNewLogixRef = ++lastAutoActionRef;
        StringBuilder b = new StringBuilder(newLogix.getSystemName());
        b.append(":A:A");
        String nextNumber = paddedNumber.format(nextAutoNewLogixRef);
        b.append(nextNumber);
        return b.toString();
    }

    @Override
    public FemaleAnalogActionSocket createFemaleAnalogActionSocket(
            FemaleSocketListener listener, String socketName) {
        return new DefaultFemaleAnalogActionSocket(listener, socketName);
    }

    @Override
    public FemaleAnalogActionSocket createFemaleAnalogActionSocket(
            FemaleSocketListener listener, String socketName,
            MaleAnalogActionSocket maleSocket){
        
        FemaleAnalogActionSocket socket =
                new DefaultFemaleAnalogActionSocket(listener, socketName, maleSocket);
        
        return socket;
    }
/*
    @Override
    public void addAction(Action action) throws IllegalArgumentException {
        // Check if system name is valid
        if (this.validSystemNameFormat(action.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + action.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        // save in the maps
        register(action);
    }
/*
    @Override
    public Action getAction(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action getByUserName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action getBySystemName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteAction(Action x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/    
    static DefaultAnalogActionManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultAnalogActionManager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new DefaultAnalogActionManager();
        }
        return (_instance);
    }
    
    private final static Logger log = LoggerFactory.getLogger(DefaultAnalogActionManager.class);
}
