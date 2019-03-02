package jmri.jmrit.logixng.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ServiceLoader;
import javax.annotation.Nonnull;
import jmri.InvokeOnGuiThread;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.StringAction;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleStringActionSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleStringActionSocket;
import jmri.managers.AbstractManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNGPluginFactory;
import jmri.jmrit.logixng.StringActionFactory;

/**
 * Class providing the basic logic of the ActionManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultStringActionManager extends AbstractManager<MaleStringActionSocket>
        implements StringActionManager {

    private final Map<Category, List<Class<? extends StringAction>>> actionClassList = new HashMap<>();
    private int lastAutoActionRef = 0;
    
    // This is for testing only!!!
    // This number needs to be saved and restored.
    DecimalFormat paddedNumber = new DecimalFormat("0000");

    
    public DefaultStringActionManager() {
        super();
        
        for (Category category : Category.values()) {
            actionClassList.put(category, new ArrayList<>());
        }
        
        for (StringActionFactory actionFactory : ServiceLoader.load(StringActionFactory.class)) {
            actionFactory.getStringActionClasses().forEach((entry) -> {
                System.out.format("Add action: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                actionClassList.get(entry.getKey()).add(entry.getValue());
            });
        }
        
//        for (LogixNGPluginFactory actionFactory : ServiceLoader.load(LogixNGPluginFactory.class)) {
//            actionFactory.getStringActionClasses().forEach((entry) -> {
//                System.out.format("Add action plugin: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
//                actionClassList.get(entry.getKey()).add(entry.getValue());
//            });
//        }
    }

    protected MaleStringActionSocket createMaleActionSocket(StringAction action) {
        return new DefaultMaleStringActionSocket(action);
    }
    
    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param action the bean
     */
    @Override
    public MaleStringActionSocket register(@Nonnull StringAction action)
            throws IllegalArgumentException {
        
        // Check if system name is valid
        if (this.validSystemNameFormat(action.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + action.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        MaleStringActionSocket maleSocket = createMaleActionSocket(action);
        register(maleSocket);
        return maleSocket;
    }
    
    @Override
    public int getXMLOrder() {
        return LOGIXNGS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameStringAction");
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
        // Q - LogixNG
        // :
        // Optional: A: - Automatic (if the system name is created by the software and not by the user
        // \d+ - The LogixNG ID number
        // :
        // Optional: A: - Automatic (if the system name is created by the software and not by the user
        // A - StringAction
        // \d+ - The StringAction ID number
        if (systemName.matches("IQA?\\d+:SAA?\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public String getNewSystemName(LogixNG newLogix) {
        int nextAutoLogixNGRef = ++lastAutoActionRef;
        StringBuilder b = new StringBuilder(newLogix.getSystemName());
        b.append(":SAA");
        String nextNumber = paddedNumber.format(nextAutoLogixNGRef);
        b.append(nextNumber);
        return b.toString();
    }

    @Override
    public FemaleStringActionSocket createFemaleStringActionSocket(
            Base parent, FemaleSocketListener listener, String socketName) {
        return new DefaultFemaleStringActionSocket(parent, listener, socketName);
    }

    @Override
    public FemaleStringActionSocket createFemaleStringActionSocket(
            Base parent,
            FemaleSocketListener listener,
            String socketName,
            MaleStringActionSocket maleSocket){
        
        FemaleStringActionSocket socket =
                new DefaultFemaleStringActionSocket(parent, listener, socketName, maleSocket);
        
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
    static DefaultStringActionManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultStringActionManager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new DefaultStringActionManager();
        }
        return (_instance);
    }
    
    private final static Logger log = LoggerFactory.getLogger(DefaultStringActionManager.class);
}
