package jmri.jmrit.logixng.util.parser.functions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jmri.jmrit.logixng.util.parser.CalculateException;
import jmri.jmrit.logixng.util.parser.ExpressionNode;
import jmri.jmrit.logixng.util.parser.Function;
import jmri.jmrit.logixng.util.parser.FunctionFactory;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.jmrit.logixng.util.parser.WrongNumberOfParametersException;
import jmri.util.TypeConversionUtil;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of mathematical functions.
 * 
 * @author Daniel Bergqvist 2019
 */
@ServiceProvider(service = FunctionFactory.class)
public class MathFunctions implements FunctionFactory {

    @Override
    public Set<Function> getFunctions() {
        Set<Function> functionClasses = new HashSet<>();
        functionClasses.add(new IntFunction());
        functionClasses.add(new RandomFunction());
        functionClasses.add(new SinFunction());
        return functionClasses;
    }
    
    
    
    public class IntFunction implements Function {

        @Override
        public String getName() {
            return "int";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws ParserException {
            if (parameterList.size() != 1) {
                throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters2", getName(), 1));
            }
            return (int) TypeConversionUtil.convertToLong(parameterList.get(0).calculate());
        }
        
    }
    
    public class LongFunction implements Function {

        @Override
        public String getName() {
            return "long";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws ParserException {
            if (parameterList.size() != 1) {
                throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters2", getName(), 1));
            }
            return TypeConversionUtil.convertToLong(parameterList.get(0).calculate());
        }
        
    }
    
    public class RandomFunction implements Function {

        @Override
        public String getName() {
            return "random";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws CalculateException {
            return Math.random();
        }
        
    }
    
    public class SinFunction implements Function {

        @Override
        public String getName() {
            return "sin";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws ParserException {
            if (parameterList.size() == 1) {
                double param = TypeConversionUtil.convertToDouble(parameterList.get(0).calculate(), false);
                return Math.sin(param);
            } else if (parameterList.size() == 2) {
                double param0 = TypeConversionUtil.convertToDouble(parameterList.get(0).calculate(), false);
                Object param1 = parameterList.get(1).calculate();
                if (param1 instanceof String) {
                    switch ((String)param1) {
                        case "rad":
                            return Math.sin(param0);
                        case "deg":
                            return Math.sin(Math.toRadians(param0));
                        default:
                            throw new CalculateException(Bundle.getMessage("IllegalParameter", 2, param1, getName()));
                    }
                } else if (param1 instanceof Number) {
                    double p1 = TypeConversionUtil.convertToDouble(param1, false);
                    double angle = param0 * p1 / 2 / Math.PI;
                    return Math.sin(angle);
                } else {
                    throw new CalculateException(Bundle.getMessage("IllegalParameter", 2, param1, getName()));
                }
//            } else {
                
            }
            throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters1", getName()));
        }
        
    }
    
}
