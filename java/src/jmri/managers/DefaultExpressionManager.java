package jmri.managers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import jmri.ExpressionManager;
import jmri.InstanceManagerAutoDefault;
import jmri.InvokeOnGuiThread;
import jmri.NewLogix;
import jmri.NewLogixCategory;
import jmri.NewLogixExpression;
import jmri.NewLogixExpressionFactory;
import jmri.NewLogixPluginFactory;
import jmri.util.Log4JUtil;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class providing the basic logic of the ExpressionManager interface.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class DefaultExpressionManager extends AbstractManager<NewLogixExpression>
        implements ExpressionManager, InstanceManagerAutoDefault {

    private final Map<NewLogixCategory, List<Class<? extends NewLogixExpression>>> expressionClassList = new HashMap<>();
    int lastAutoExpressionRef = 0;
    
    DecimalFormat paddedNumber = new DecimalFormat("0000");

    
    public DefaultExpressionManager() {
        super();
        
        for (NewLogixCategory category : NewLogixCategory.values()) {
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

    @Override
    public int getXMLOrder() {
        return NEWLOGIXS;
    }

    @Override
    public String getBeanTypeHandled() {
        return Bundle.getMessage("BeanNameAction");
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
    public void addExpression(NewLogixExpression expression) throws IllegalArgumentException {
        // Check if system name is valid
        if (this.validSystemNameFormat(expression.getSystemName()) != NameValidity.VALID) {
            log.warn("SystemName " + expression.getSystemName() + " is not in the correct format");
            throw new IllegalArgumentException("System name is invalid");
        }
        // save in the maps
        register(expression);
    }

    @Override
    public NewLogixExpression getExpression(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NewLogixExpression getByUserName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NewLogixExpression getBySystemName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteExpression(NewLogixExpression x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
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
