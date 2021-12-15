package jmri.jmrit.logixng.expressions;

import java.util.HashSet;
import java.util.Set;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.StringExpressionFactory;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for DigitalAction classes.
 */
@ServiceProvider(service = StringExpressionFactory.class)
public class StringFactory implements StringExpressionFactory {

    @Override
    public Set<ClassInfo> getClasses() {
        Set<ClassInfo> stringExpressionClasses = new HashSet<>();
        stringExpressionClasses.add(new ClassInfo(Category.ITEM, StringExpressionConstant.class, Bundle.getMessage("StringExpressionConstant_Description")));
        stringExpressionClasses.add(new ClassInfo(Category.ITEM, StringExpressionMemory.class, Bundle.getMessage("StringExpressionMemory_Description")));
        stringExpressionClasses.add(new ClassInfo(Category.COMMON, StringFormula.class, Bundle.getMessage("StringFormula_Description")));
        return stringExpressionClasses;
    }

}
