package jmri.jmrit.logixng.util.parser.functions;

import java.util.ArrayList;
import java.util.Collections;
import jmri.jmrit.logixng.util.parser.expressionnode.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.jmrit.logixng.util.parser.Token;
import jmri.jmrit.logixng.util.parser.TokenType;
import jmri.jmrit.logixng.util.parser.Variable;
import jmri.jmrit.logixng.util.parser.WrongNumberOfParametersException;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ParsedExpression
 * 
 * @author Daniel Bergqvist 2019
 */
public class MathFunctionsTest {

    ExpressionNode expr12_34;
    ExpressionNode expr25_46;
    ExpressionNode expr12;
    ExpressionNode expr23;
    
    
    private List<ExpressionNode> getParameterList(ExpressionNode... exprNodes) {
        List<ExpressionNode> list = new ArrayList<>();
        Collections.addAll(list, exprNodes);
        return list;
    }
    
    @Test
    public void testIntFunction() throws ParserException {
        MathFunctions.IntFunction intFunction = new MathFunctions.IntFunction();
        Assert.assertEquals("strings matches", "int", intFunction.getName());
        Assert.assertEquals("strings matches", "int", new MathFunctions.IntFunction().getName());
        
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        
        // Test unsupported token type
        hasThrown.set(false);
        try {
            intFunction.calculate(getParameterList());
        } catch (WrongNumberOfParametersException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
        
        Assert.assertEquals("strings are equal", 12, intFunction.calculate(getParameterList(expr12_34)));
        
        // Test unsupported token type
        hasThrown.set(false);
        try {
            intFunction.calculate(getParameterList(expr12_34, expr25_46));
        } catch (WrongNumberOfParametersException e) {
            hasThrown.set(true);
        }
    }
    
    @Test
    public void testLongFunction() throws ParserException {
        MathFunctions.LongFunction longFunction = new MathFunctions.LongFunction();
        Assert.assertEquals("strings matches", "long", longFunction.getName());
        Assert.assertEquals("strings matches", "long", new MathFunctions.LongFunction().getName());
        
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        
        // Test unsupported token type
        hasThrown.set(false);
        try {
            longFunction.calculate(getParameterList());
        } catch (WrongNumberOfParametersException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
        
        Assert.assertEquals("strings are equal", 12l, longFunction.calculate(getParameterList(expr12_34)));
        
        // Test unsupported token type
        hasThrown.set(false);
        try {
            longFunction.calculate(getParameterList(expr12_34, expr25_46));
        } catch (WrongNumberOfParametersException e) {
            hasThrown.set(true);
        }
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        expr12_34 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "12.34", 0));
        expr25_46 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "25.46", 0));
        expr12 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "12", 0));
        expr23 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "23", 0));
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
