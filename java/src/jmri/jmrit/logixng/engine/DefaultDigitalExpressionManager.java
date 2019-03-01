package jmri.jmrit.logixng.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import javax.annotation.Nonnull;
import jmri.InstanceManagerAutoDefault;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.logixng.Category;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.managers.AbstractManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNGPluginFactory;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionFactory;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;

/**
 * Class providing the basic logic of the DigitalExpressionManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultDigitalExpressionManager extends AbstractManager<MaleDigitalExpressionSocket>
        implements DigitalExpressionManager, InstanceManagerAutoDefault {

    private final Map<Category, List<Class<? extends DigitalExpression>>> expressionClassList = new HashMap<>();
    int lastAutoExpressionRef = 0;
    
    // This is for testing only!!!
    // This number needs to be saved and restored.
    DecimalFormat paddedNumber = new DecimalFormat("0000");

    
    public DefaultDigitalExpressionManager() {
        super();
        
        for (Category category : Category.values()) {
            expressionClassList.put(category, new ArrayList<>());
        }
        
        System.out.format("Read expressions%n");
        for (DigitalExpressionFactory expressionFactory : ServiceLoader.load(DigitalExpressionFactory.class)) {
            expressionFactory.getExpressionClasses().forEach((entry) -> {
                System.out.format("Add expression: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                expressionClassList.get(entry.getKey()).add(entry.getValue());
            });
        }
        
        System.out.format("Read plugin expressions%n");
        for (LogixNGPluginFactory expressionFactory : ServiceLoader.load(LogixNGPluginFactory.class)) {
            System.out.format("Read plugin factory: %s%n", expressionFactory.getClass().getName());
            expressionFactory.getExpressionClasses().forEach((entry) -> {
                System.out.format("Add expression plugin: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                expressionClassList.get(entry.getKey()).add(entry.getValue());
            });
        }
    }

    protected MaleDigitalExpressionSocket createMaleExpressionSocket(DigitalExpression expression) {
        return new DefaultMaleDigitalExpressionSocket(expression);
    }
    
    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param expression the bean
     */
    @Override
    public MaleDigitalExpressionSocket register(@Nonnull DigitalExpression expression)
            throws IllegalArgumentException {
        
        // Check if system name is valid
        if (this.validSystemNameFormat(expression.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + expression.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        // save in the maps
        MaleDigitalExpressionSocket maleSocket = createMaleExpressionSocket(expression);
        register(maleSocket);
        return maleSocket;
    }
    
    @Override
    public int getXMLOrder() {
        return LOGIXNGS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameExpression");
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
        // E - DigitalExpression
        // \d+ - The DigitalExpression ID number
        if (systemName.matches("IQ\\:(A\\:)?\\d+:(A\\:)?E\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public String getNewSystemName(LogixNG newLogix) {
        int nextAutoLogixNGRef = lastAutoExpressionRef + 1;
        StringBuilder b = new StringBuilder(newLogix.getSystemName());
        b.append(":A:E");
        String nextNumber = paddedNumber.format(nextAutoLogixNGRef);
        b.append(nextNumber);
        return b.toString();
    }

    @Override
    public FemaleDigitalExpressionSocket createFemaleExpressionSocket(
            FemaleSocketListener listener, String socketName) {
        return new DefaultFemaleDigitalExpressionSocket(listener, socketName);
    }

    @Override
    public FemaleDigitalExpressionSocket createFemaleExpressionSocket(
            FemaleSocketListener listener, String socketName,
            MaleDigitalExpressionSocket maleSocket) {
        
        FemaleDigitalExpressionSocket socket =
                new DefaultFemaleDigitalExpressionSocket(listener, socketName, maleSocket);
        
        return socket;
    }
/*
    @Override
    public void addExpression(DigitalExpression expression) throws IllegalArgumentException {
        // Check if system name is valid
        if (this.validSystemNameFormat(expression.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + expression.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        // save in the maps
        register(expression);
    }

    @Override
    public DigitalExpression getExpression(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DigitalExpression getByUserName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DigitalExpression getBySystemName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteExpression(DigitalExpression x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/    
    static DefaultDigitalExpressionManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultDigitalExpressionManager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new DefaultDigitalExpressionManager();
        }
        return (_instance);
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultDigitalExpressionManager.class);
}
