package jmri.jmrit.newlogix.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import javax.annotation.Nonnull;
import jmri.jmrit.newlogix.ExpressionManager;
import jmri.InstanceManagerAutoDefault;
import jmri.InvokeOnGuiThread;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.NewLogixExpressionFactory;
import jmri.jmrit.newlogix.NewLogixPluginFactory;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.managers.AbstractManager;

/**
 * Class providing the basic logic of the ExpressionManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultExpressionManager extends AbstractManager<MaleExpressionSocket>
        implements ExpressionManager, InstanceManagerAutoDefault {

    private final Map<Category, List<Class<? extends Expression>>> expressionClassList = new HashMap<>();
    int lastAutoExpressionRef = 0;
    
    // This is for testing only!!!
    // This number needs to be saved and restored.
    DecimalFormat paddedNumber = new DecimalFormat("0000");

    
    public DefaultExpressionManager() {
        super();
        
        for (Category category : Category.values()) {
            expressionClassList.put(category, new ArrayList<>());
        }
        
        System.out.format("Read expressions%n");
        for (NewLogixExpressionFactory expressionFactory : ServiceLoader.load(NewLogixExpressionFactory.class)) {
            expressionFactory.getExpressionClasses().forEach((entry) -> {
                System.out.format("Add expression: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                expressionClassList.get(entry.getKey()).add(entry.getValue());
            });
        }
        
        System.out.format("Read plugin expressions%n");
        for (NewLogixPluginFactory expressionFactory : ServiceLoader.load(NewLogixPluginFactory.class)) {
            System.out.format("Read plugin factory: %s%n", expressionFactory.getClass().getName());
            expressionFactory.getExpressionClasses().forEach((entry) -> {
                System.out.format("Add expression plugin: %s, %s%n", entry.getKey().name(), entry.getValue().getName());
                expressionClassList.get(entry.getKey()).add(entry.getValue());
            });
        }
    }

    protected MaleExpressionSocket createMaleExpressionSocket(Expression expression) {
        return new DefaultMaleExpressionSocket(expression);
    }
    
    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param expression the bean
     */
    @Override
    public MaleExpressionSocket register(@Nonnull Expression expression)
            throws IllegalArgumentException {
        
        // Check if system name is valid
        if (this.validSystemNameFormat(expression.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + expression.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        // save in the maps
        MaleExpressionSocket maleSocket = createMaleExpressionSocket(expression);
        register(maleSocket);
        return maleSocket;
    }
    
    @Override
    public int getXMLOrder() {
        return NEWLOGIXS;
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
        if (systemName.matches("IQ\\:[AM]\\:\\d+:[AM]\\:E\\d+")) {
            return NameValidity.VALID;
        } else {
            return NameValidity.INVALID;
        }
    }

    @Override
    public String getNewSystemName(NewLogix newLogix) {
        int nextAutoNewLogixRef = lastAutoExpressionRef + 1;
        StringBuilder b = new StringBuilder(newLogix.getSystemName());
        b.append(":A:E");
        String nextNumber = paddedNumber.format(nextAutoNewLogixRef);
        b.append(nextNumber);
        return b.toString();
    }

    @Override
    public FemaleExpressionSocket createFemaleExpressionSocket(
            FemaleSocketListener listener, String socketName) {
        return new DefaultFemaleExpressionSocket(listener, socketName);
    }

    @Override
    public FemaleExpressionSocket createFemaleExpressionSocket(
            FemaleSocketListener listener, String socketName,
            MaleExpressionSocket maleSocket) {
        
        FemaleExpressionSocket socket =
                new DefaultFemaleExpressionSocket(listener, socketName, maleSocket);
        
        return socket;
    }
/*
    @Override
    public void addExpression(Expression expression) throws IllegalArgumentException {
        // Check if system name is valid
        if (this.validSystemNameFormat(expression.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + expression.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        // save in the maps
        register(expression);
    }

    @Override
    public Expression getExpression(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Expression getByUserName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Expression getBySystemName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteExpression(Expression x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/    
    static DefaultExpressionManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultExpressionManager instance() {
        if (log.isDebugEnabled()) {
            if (!ThreadingUtil.isGUIThread()) {
                Log4JUtil.warnOnce(log, "instance() called on wrong thread");
            }
        }
        
        if (_instance == null) {
            _instance = new DefaultExpressionManager();
        }
        return (_instance);
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultExpressionManager.class);
}
