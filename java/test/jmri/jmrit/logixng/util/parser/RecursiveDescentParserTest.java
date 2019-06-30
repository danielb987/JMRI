package jmri.jmrit.logixng.util.parser;

//import jmri.jmrit.logixng.util.parser.RecursiveDescentParser.Function;
//import jmri.jmrit.logixng.util.parser.RecursiveDescentParser.OperatorInfo;
import java.util.concurrent.atomic.AtomicBoolean;
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
public class RecursiveDescentParserTest {

    @Test
    public void testCtor() {
        RecursiveDescentParser t = new RecursiveDescentParser();
        Assert.assertNotNull("not null", t);
    }
    
    
    @Test
    public void testDaniel() throws InvalidSyntaxException {
        
        AtomicBoolean exceptionIsThrown = new AtomicBoolean();
        
        RecursiveDescentParser t = new RecursiveDescentParser();
        t.parseExpression("");
        t.parseExpression("134");
        t.parseExpression("abc");
        t.parseExpression("\"a little string\"");
        t.parseExpression("123+2123");
        t.parseExpression("123*1233");
        t.parseExpression("12+45*12");
        t.parseExpression("12*45+34");
        t.parseExpression("12-23/43");
        t.parseExpression("12/23-43");
        
        t.parseExpression("12 < 2");
        t.parseExpression("12 <= 2");
        t.parseExpression("12 > 2");
        t.parseExpression("12 >= 2");
        t.parseExpression("12 == 2");
        t.parseExpression("12 != 2");
        t.parseExpression("!12 < 2");
//        t.parseExpression("!(12 < 2)");
        
        exceptionIsThrown.set(false);
        try {
            t.parseExpression("12+31*(23-1)+((((9*2+3)-2)/23");
        } catch (InvalidSyntaxException e) {
//            System.err.format("Error message: %s%n", e.getMessage());
            Assert.assertTrue("exception message matches", "invalid syntax at index 2. Token RIGHT_PARENTHESIS expected".equals(e.getMessage()));
            exceptionIsThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", exceptionIsThrown.get());
        t.parseExpression("12+31*(23-1)+21*((((9*2+3)-2)/23+3)/3+4)");
    }
    
    
/*    
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
*/    
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
/*    
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
*/    
    
}
