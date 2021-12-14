package jmri.jmrit.logixng.expressions;

import java.util.HashSet;
import java.util.Set;

import jmri.jmrit.logixng.AnalogExpressionFactory;
import jmri.jmrit.logixng.Category;

import org.openide.util.lookup.ServiceProvider;

/**
 * The factory for DigitalAction classes.
 */
@ServiceProvider(service = AnalogExpressionFactory.class)
public class AnalogFactory implements AnalogExpressionFactory {

    @Override
    public Set<ClassInfo> getClasses() {
        Set<ClassInfo> analogExpressionClasses = new HashSet<>();
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, AnalogExpressionAnalogIO.class));
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, AnalogExpressionConstant.class));
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, AnalogExpressionMemory.class));
        analogExpressionClasses.add(new ClassInfo(Category.COMMON, AnalogFormula.class));
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, TimeSinceMidnight.class));
        return analogExpressionClasses;
    }

}
