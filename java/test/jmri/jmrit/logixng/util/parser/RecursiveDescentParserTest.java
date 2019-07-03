package jmri.jmrit.logixng.util.parser;

//import jmri.jmrit.logixng.util.parser.RecursiveDescentParser.Function;
//import jmri.jmrit.logixng.util.parser.RecursiveDescentParser.OperatorInfo;
import java.util.HashMap;
import java.util.Map;
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
        RecursiveDescentParser t = new RecursiveDescentParser(null);
        Assert.assertNotNull("not null", t);
    }
    
    
    @Test
    public void testParseAndCalculate() throws InvalidSyntaxException {
        
        AtomicBoolean exceptionIsThrown = new AtomicBoolean();
        Map<String, Variable> _variables = new HashMap<>();
        
        _variables.put("abc", new MyVariable("abc", "ABC"));
        
        RecursiveDescentParser t = new RecursiveDescentParser(_variables);
        ExpressionNode exprNode = t.parseExpression("");
        Assert.assertTrue("expression node is null", null == exprNode);
        exprNode = t.parseExpression("134");
        Assert.assertTrue("expression matches", "Number:134".equals(exprNode.getDefinitionString()));
        System.err.format("calculate: '%s', %s%n", exprNode.calculate(), exprNode.calculate().getClass().getName());
        System.err.format("expected: '%s', %s%n", ((Object)134d), ((Object)134d).getClass().getName());
        Assert.assertTrue("calculate is correct", ((Object)134d).equals(exprNode.calculate()));
        exprNode = t.parseExpression("abc");
        Assert.assertTrue("expression matches", "Identifier:abc".equals(exprNode.getDefinitionString()));
        Assert.assertTrue("calculate is correct", "ABC".equals(exprNode.calculate()));
        exprNode = t.parseExpression("\"a little string\"");
        Assert.assertTrue("expression matches", "String:a little string".equals(exprNode.getDefinitionString()));
        System.err.format("calculate: '%s', %s%n", exprNode.calculate(), exprNode.calculate().getClass().getName());
        System.err.format("expected: '%s', %s%n", ((Object)134d), ((Object)134d).getClass().getName());
        Assert.assertTrue("calculate is correct", "a little string".equals(exprNode.calculate()));
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
        
        exprNode = t.parseExpression("abc()");
        Assert.assertTrue("expression matches", "Function:abc()".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("abc(x)");
        Assert.assertTrue("expression matches", "Function:abc(Identifier:x)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("abc(x,y,z)");
        Assert.assertTrue("expression matches", "Function:abc(Identifier:x,Identifier:y,Identifier:z)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("abc(x*2+5)");
        Assert.assertTrue("expression matches", "Function:abc(((Identifier:x)*(Number:2))+(Number:5))".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("abc((x))");
        Assert.assertTrue("expression matches", "Function:abc(Identifier:x)".equals(exprNode.getDefinitionString()));
        exprNode = t.parseExpression("abc(x*(2+3),23,\"Abc\",2)");
        Assert.assertTrue("expression matches", "Function:abc((Identifier:x)*((Number:2)+(Number:3)),Number:23,String:Abc,Number:2)".equals(exprNode.getDefinitionString()));
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
    
    
    private static class MyVariable implements Variable {
        
        private final String _name;
        private final Object _value;
        
        private MyVariable(String name, Object value) {
            _name = name;
            _value = value;
        }

        @Override
        public String getName() {
            return _name;
        }

        @Override
        public Object getValue() {
            return _value;
        }
    }
}
