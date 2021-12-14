package jmri.jmrix.loconet.logixng;

import java.util.HashSet;
import java.util.Set;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.DigitalExpressionFactory;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for LogixNG LocoNet classes.
 */
@ServiceProvider(service = DigitalExpressionFactory.class)
public class ExpressionFactory implements DigitalExpressionFactory {

    @Override
    public void init() {
        CategoryLocoNet.registerCategory();
    }
    
    @Override
    public Set<ClassInfo> getExpressionClasses() {
        Set<ClassInfo> expressionClasses = new HashSet<>();
        
        // We don't want to add these classes if we don't have a LocoNet connection
        if (CategoryLocoNet.hasLocoNet()) {
            expressionClasses.add(new ClassInfo(CategoryLocoNet.LOCONET, ExpressionSlotUsage.class));
        }
        
        return expressionClasses;
    }
    
    
    
    public static final class LocoNet extends Category {
        
        public LocoNet() {
            super("LOCONET", Bundle.getMessage("MenuLocoNet"), 1100);
        }
    }

}
