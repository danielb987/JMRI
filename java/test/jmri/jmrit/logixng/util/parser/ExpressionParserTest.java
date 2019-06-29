package jmri.jmrit.logixng.util.parser;

import jmri.jmrit.logixng.util.parser.ExpressionParser;
import jmri.jmrit.logixng.util.parser.ExpressionParser.Function;
import jmri.jmrit.logixng.util.parser.ExpressionParser.OperatorInfo;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionParser
 * 
 * @author Daniel Bergqvist 2019
 */
public class ExpressionParserTest {

    @Test
    public void testCtor() {
        ExpressionParser t = new ExpressionParser();
        Assert.assertNotNull("not null", t);
    }
    
    @Test
    public void testParseAndCalculate() {
        ExpressionParser<Float> parser = new ExpressionParser<>();
        Assert.assertNotNull("not null", parser);
        
//        parser.addUnaryOperator("-", negateOperator);
        parser.addBinaryOperator("+", addOperator);
        parser.addBinaryOperator("-", subtractOperator);
        parser.addBinaryOperator("*", multiplyOperator);
        parser.addBinaryOperator("/", divideOperator);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
/*    
    private final OperatorInfo<Float> negateOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 + param2;
            }

            @Override
            public int getPriority() {
                return 1;
            }
        };
*/    
    private final OperatorInfo<Float> addOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 + param2;
            }

            @Override
            public int getPriority() {
                return 1;
            }
        };
    
    private final OperatorInfo<Float> subtractOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 - param2;
            }

            @Override
            public int getPriority() {
                return 1;
            }
        };
    
    private final OperatorInfo<Float> multiplyOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 * param2;
            }

            @Override
            public int getPriority() {
                return 2;
            }
        };
    
    private final OperatorInfo<Float> divideOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 / param2;
            }

            @Override
            public int getPriority() {
                return 2;
            }
        };
    
    
}
