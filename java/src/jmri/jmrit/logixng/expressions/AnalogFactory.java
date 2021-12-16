package jmri.jmrit.logixng.expressions;

import java.util.*;

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
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, AnalogExpressionAnalogIO.class, Bundle.getMessage("AnalogExpressionAnalogIO_Description"), Bundle.getMessage("AnalogExpressionAnalogIO_Description", Locale.US)));
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, AnalogExpressionConstant.class, Bundle.getMessage("AnalogExpressionConstant_Description"), Bundle.getMessage("AnalogExpressionConstant_Description", Locale.US)));
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, AnalogExpressionMemory.class, Bundle.getMessage("AnalogExpressionMemory_Description"), Bundle.getMessage("AnalogExpressionMemory_Description", Locale.US)));
        analogExpressionClasses.add(new ClassInfo(Category.COMMON, AnalogFormula.class, Bundle.getMessage("AnalogFormula_Description"), Bundle.getMessage("AnalogFormula_Description", Locale.US)));
        analogExpressionClasses.add(new ClassInfo(Category.ITEM, TimeSinceMidnight.class, Bundle.getMessage("TimeSinceMidnight_Description"), Bundle.getMessage("TimeSinceMidnight_Description", Locale.US)));
        return analogExpressionClasses;
    }

}
