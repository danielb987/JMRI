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
        ExpressionNode exprNode = t.parseExpression("");
        Assert.assertTrue("expression node is null", null == exprNode);
        exprNode = t.parseExpression("134");
        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("abc");
        Assert.assertTrue("expression matches", "Identifier:abc".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("\"a little string\"");
        Assert.assertTrue("expression matches", "String:a little string".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("123*1233");
        Assert.assertTrue("expression matches", "(Number:123)*(Number:1233)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("123+2123");
        Assert.assertTrue("expression matches", "(Number:123)+(Number:2123)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("123*1233");
        Assert.assertTrue("expression matches", "(Number:123)*(Number:1233)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12+45*12");
        Assert.assertTrue("expression matches", "(Number:12)+((Number:45)*(Number:12))".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12*45+34");
        Assert.assertTrue("expression matches", "((Number:12)*(Number:45))+(Number:34)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12-23/43");
        Assert.assertTrue("expression matches", "(Number:12)-((Number:23)/(Number:43))".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12/23-43");
        Assert.assertTrue("expression matches", "((Number:12)/(Number:23))-(Number:43)".equals(exprNode.getDefinitionString()));
        
        exprNode = t.parseExpression("12 < 2");
        Assert.assertTrue("expression matches", "(Number:12)<(Number:2)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12 <= 2");
        Assert.assertTrue("expression matches", "(Number:12)<=(Number:2)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12 > 2");
        Assert.assertTrue("expression matches", "(Number:12)>(Number:2)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12 >= 2");
        Assert.assertTrue("expression matches", "(Number:12)>=(Number:2)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12 == 2");
        Assert.assertTrue("expression matches", "(Number:12)==(Number:2)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("12 != 2");
        Assert.assertTrue("expression matches", "(Number:12)!=(Number:2)".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("!12 < 2");
//        Assert.assertTrue("expression matches", "(Number:12)<(Number:2)".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("!(12 < 2)");
//        Assert.assertTrue("expression matches", "(Number:12)<(Number:2)".equals(exprNode.getDefinitionString()));
        
        exprNode = t.parseExpression("2 <= 3");
        Assert.assertTrue("expression matches", "(Number:2)<=(Number:3)".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= 3 && 3");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= 3 && 3 > 4");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("4 && 2");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("4 || 2");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= 3 && 3 > 4 || 2");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= 3 && 3 > 4 || 2 < 3");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("(2 <= 3) && 3 > 4 || 2 < 3");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= (3 && 3) > 4 || 2 < 3");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= 3 && (3 > 4) || 2 < 3");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("2 <= 3 && (3 > 4) || (2 < 3)");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
//        exprNode = t.parseExpression("(2 <= 3) && (3 > 4) || 2 < 3");
//        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
        
        exceptionIsThrown.set(false);
        try {
            t.parseExpression("12+31*(23-1)+((((9*2+3)-2)/23");
        } catch (InvalidSyntaxException e) {
            System.err.format("Error message: %s%n", e.getMessage());
            Assert.assertTrue("exception message matches", "invalid syntax".equals(e.getMessage()));
            exceptionIsThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", exceptionIsThrown.get());

        exprNode = t.parseExpression("12+31*(23-1)+21*((((9*2+3)-2)/23+3)/3+4)");
        Assert.assertTrue("expression matches", "((Number:12)+((Number:31)*((Number:23)-(Number:1))))+((Number:21)*((((((((Number:9)*(Number:2))+(Number:3))-(Number:2))/(Number:23))+(Number:3))/(Number:3))+(Number:4)))".equals(exprNode.getDefinitionString()));
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
