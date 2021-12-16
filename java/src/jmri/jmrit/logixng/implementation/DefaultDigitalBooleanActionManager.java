package jmri.jmrit.logixng.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import jmri.InstanceManager;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.logixng.*;
import jmri.util.LoggingUtil;
import jmri.util.ThreadingUtil;

/**
 * Class providing the basic logic of the DigitalBooleanActionManager interface.
 * 
 * @author Dave Duchamp       Copyright (C) 2007
 * @author Daniel Bergqvist   Copyright (C) 2018
 */
public class DefaultDigitalBooleanActionManager extends AbstractBaseManager<MaleDigitalBooleanActionSocket>
        implements DigitalBooleanActionManager {

    private final Map<Category, List<Class<? extends Base>>> actionClassList = new HashMap<>();
    private final Map<String, Class<? extends Base>> actionClassMap = new HashMap<>();
    private final Map<String, Class<? extends Base>> actionEnglishClassMap = new HashMap<>();
    private MaleSocket _lastRegisteredBean;

    
    public DefaultDigitalBooleanActionManager() {
        InstanceManager.getDefault(LogixNG_Manager.class).registerManager(this);
        
        for (DigitalBooleanActionFactory actionFactory : ServiceLoader.load(DigitalBooleanActionFactory.class)) {
            actionFactory.init();
        }
        
        for (Category category : Category.values()) {
            actionClassList.put(category, new ArrayList<>());
        }
        
        for (DigitalBooleanActionFactory actionFactory : ServiceLoader.load(DigitalBooleanActionFactory.class)) {
            actionFactory.getClasses().forEach((entry) -> {
//                System.out.format("Add action: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                actionClassList.get(entry.category).add(entry.clazz);
                
                if (actionClassMap.containsKey(entry.description)) {
                    throw new RuntimeException("Duplicate items: "+entry.description);
                }
                actionClassMap.put(entry.description, entry.clazz);
                
                if (actionEnglishClassMap.containsKey(entry.englishDescription)) {
                    throw new RuntimeException("Duplicate items: "+entry.englishDescription);
                }
                actionEnglishClassMap.put(entry.englishDescription, entry.clazz);
            });
        }
        
        for (MaleDigitalBooleanActionSocketFactory maleSocketFactory : ServiceLoader.load(MaleDigitalBooleanActionSocketFactory.class)) {
            _maleSocketFactories.add(maleSocketFactory);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends MaleSocket> getMaleSocketClass() {
        return DefaultMaleDigitalBooleanActionSocket.class;
    }

    protected MaleDigitalBooleanActionSocket createMaleActionSocket(DigitalBooleanActionBean action) {
        MaleDigitalBooleanActionSocket socket = new DefaultMaleDigitalBooleanActionSocket(this, action);
        action.setParent(socket);
        return socket;
    }

    /** {@inheritDoc} */
    @Override
    public MaleSocket getLastRegisteredMaleSocket() {
        return _lastRegisteredBean;
    }
    
    /** {@inheritDoc} */
    @Override
    public MaleDigitalBooleanActionSocket registerBean(MaleDigitalBooleanActionSocket maleSocket) {
        MaleDigitalBooleanActionSocket bean = super.registerBean(maleSocket);
        _lastRegisteredBean = maleSocket;
        return bean;
    }
    
    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleDigitalBooleanActionSocket for the action.
     *
     * @param action the bean
     */
    @Override
    public MaleDigitalBooleanActionSocket registerAction(@Nonnull DigitalBooleanActionBean action)
            throws IllegalArgumentException {
        
        if (action instanceof MaleDigitalBooleanActionSocket) {
            throw new IllegalArgumentException("registerAction() cannot register a MaleDigitalBooleanActionSocket. Use the method register() instead.");
        }
        
        // Check if system name is valid
        if (this.validSystemNameFormat(action.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + action.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException(String.format("System name is invalid: %s", action.getSystemName()));
        }
        
        // Keep track of the last created auto system name
        updateAutoNumber(action.getSystemName());
        
        // save in the maps
        MaleDigitalBooleanActionSocket maleSocket = createMaleActionSocket(action);
        return registerBean(maleSocket);
    }
    
    @Override
    public int getXMLOrder() {
        return LOGIXNG_DIGITAL_BOOLEAN_ACTIONS;
    }

    @Override
    public char typeLetter() {
        return 'Q';
    }

    @Override
    public FemaleDigitalBooleanActionSocket createFemaleSocket(
            Base parent, FemaleSocketListener listener, String socketName) {
        return new DefaultFemaleDigitalBooleanActionSocket(parent, listener, socketName);
    }

    @Override
    public Map<Category, List<Class<? extends Base>>> getActionClasses() {
        return actionClassList;
    }

    @Override
    public Class<? extends Base> getClassByDescription(String descr) {
        return actionClassMap.get(descr);
    }

    @Override
    public Class<? extends Base> getClassByEnglishDescription(String descr) {
        return actionEnglishClassMap.get(descr);
    }

    /** {@inheritDoc} */
    @Override
    public String getBeanTypeHandled(boolean plural) {
        return Bundle.getMessage(plural ? "BeanNameDigitalBooleanActions" : "BeanNameDigitalBooleanAction");
    }

    /** {@inheritDoc} */
    @Override
    public void deleteDigitalBooleanAction(MaleDigitalBooleanActionSocket x) {
        // delete the MaleDigitalBooleanActionSocket
        deregister(x);
        x.dispose();
    }
    
    static volatile DefaultDigitalBooleanActionManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultDigitalBooleanActionManager instance() {
        if (!ThreadingUtil.isGUIThread()) {
            LoggingUtil.warnOnce(log, "instance() called on wrong thread");
        }
        
        if (_instance == null) {
            _instance = new DefaultDigitalBooleanActionManager();
        }
        return (_instance);
    }

    @Override
    public Class<MaleDigitalBooleanActionSocket> getNamedBeanClass() {
        return MaleDigitalBooleanActionSocket.class;
    }

    @Override
    protected MaleDigitalBooleanActionSocket castBean(MaleSocket maleSocket) {
        return (MaleDigitalBooleanActionSocket)maleSocket;
    }


    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DefaultDigitalBooleanActionManager.class);

}
