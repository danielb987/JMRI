package jmri.jmrit.logixng.util.parser;

import java.util.List;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Tokenizer
 * 
 * @author Daniel Bergqvist 2019
 */
public class TokenizerTest {

    @Test
    public void testCtor() {
        Tokenizer t = new Tokenizer();
        Assert.assertNotNull("not null", t);
    }
    
    @Test
    public void testTokenType() {
        // Test precedence functions
        
        Assert.assertTrue("Precedence function is correct", TokenType.BOOLEAN_AND.hasLowerPrecedence(TokenType.EQUAL));
        Assert.assertTrue("Precedence function is correct", !TokenType.EQUAL.hasLowerPrecedence(TokenType.BOOLEAN_AND));
        Assert.assertTrue("Precedence function is correct", TokenType.EQUAL.hasHigherPrecedence(TokenType.BOOLEAN_AND));
        Assert.assertTrue("Precedence function is correct", !TokenType.BOOLEAN_AND.hasHigherPrecedence(TokenType.EQUAL));
        Assert.assertTrue("Precedence function is correct", TokenType.EQUAL.hasSamePrecedence(TokenType.NOT_EQUAL));
        Assert.assertTrue("Precedence function is correct", !TokenType.EQUAL.hasSamePrecedence(TokenType.BOOLEAN_AND));
        Assert.assertTrue("Precedence function is correct", TokenType.NOT_EQUAL.hasSamePrecedence(TokenType.EQUAL));
        Assert.assertTrue("Precedence function is correct", !TokenType.BOOLEAN_AND.hasSamePrecedence(TokenType.EQUAL));
        
        // Test precedence
        Assert.assertTrue("Precedence is correct", TokenType.BOOLEAN_OR.hasLowerPrecedence(TokenType.BOOLEAN_AND));
        Assert.assertTrue("Precedence is correct", TokenType.BOOLEAN_AND.hasLowerPrecedence(TokenType.BINARY_OR));
        Assert.assertTrue("Precedence is correct", TokenType.BINARY_OR.hasLowerPrecedence(TokenType.BINARY_XOR));
        Assert.assertTrue("Precedence is correct", TokenType.BINARY_XOR.hasLowerPrecedence(TokenType.BINARY_AND));
        Assert.assertTrue("Precedence is correct", TokenType.BINARY_AND.hasLowerPrecedence(TokenType.EQUAL));
        
        Assert.assertTrue("Precedence is correct", TokenType.EQUAL.hasSamePrecedence(TokenType.NOT_EQUAL));
        Assert.assertTrue("Precedence is correct", TokenType.EQUAL.hasLowerPrecedence(TokenType.LESS));
        
        Assert.assertTrue("Precedence is correct", TokenType.LESS.hasSamePrecedence(TokenType.LESS_THAN));
        Assert.assertTrue("Precedence is correct", TokenType.LESS.hasSamePrecedence(TokenType.GREATER));
        Assert.assertTrue("Precedence is correct", TokenType.LESS.hasSamePrecedence(TokenType.GREATER_THAN));
        Assert.assertTrue("Precedence is correct", TokenType.LESS.hasLowerPrecedence(TokenType.SHIFT_LEFT));
        
        Assert.assertTrue("Precedence is correct", TokenType.SHIFT_LEFT.hasSamePrecedence(TokenType.SHIFT_RIGHT));
        Assert.assertTrue("Precedence is correct", TokenType.SHIFT_LEFT.hasLowerPrecedence(TokenType.ADD));
        
        Assert.assertTrue("Precedence is correct", TokenType.ADD.hasSamePrecedence(TokenType.SUBTRACKT));
        Assert.assertTrue("Precedence is correct", TokenType.ADD.hasLowerPrecedence(TokenType.MULTIPLY));
        
        Assert.assertTrue("Precedence is correct", TokenType.MULTIPLY.hasSamePrecedence(TokenType.DIVIDE));
        Assert.assertTrue("Precedence is correct", TokenType.MULTIPLY.hasSamePrecedence(TokenType.MODULO));
        Assert.assertTrue("Precedence is correct", TokenType.MULTIPLY.hasLowerPrecedence(TokenType.BINARY_NOT));
        
        Assert.assertTrue("Precedence is correct", TokenType.MULTIPLY.hasLowerPrecedence(TokenType.BINARY_NOT));
        
        Assert.assertTrue("Precedence is correct", TokenType.BINARY_NOT.hasSamePrecedence(TokenType.BOOLEAN_NOT));
        Assert.assertTrue("Precedence is correct", TokenType.BINARY_NOT.hasLowerPrecedence(TokenType.LEFT_PARENTHESIS));
        
        Assert.assertTrue("Precedence is correct", TokenType.LEFT_PARENTHESIS.hasSamePrecedence(TokenType.RIGHT_PARENTHESIS));
        Assert.assertTrue("Precedence is correct", TokenType.LEFT_PARENTHESIS.hasSamePrecedence(TokenType.LEFT_SQUARE_BRACKET));
        Assert.assertTrue("Precedence is correct", TokenType.LEFT_PARENTHESIS.hasSamePrecedence(TokenType.RIGHT_SQUARE_BRACKET));
        Assert.assertTrue("Precedence is correct", TokenType.LEFT_PARENTHESIS.hasSamePrecedence(TokenType.LEFT_CURLY_BRACKET));
        Assert.assertTrue("Precedence is correct", TokenType.LEFT_PARENTHESIS.hasSamePrecedence(TokenType.RIGHT_CURLY_BRACKET));
        Assert.assertTrue("Precedence is correct", TokenType.LEFT_PARENTHESIS.hasLowerPrecedence(TokenType.COMMA));
        
        Assert.assertTrue("Precedence is correct", TokenType.COMMA.hasSamePrecedence(TokenType.DOT_DOT));
        Assert.assertTrue("Precedence is correct", TokenType.COMMA.hasLowerPrecedence(TokenType.IDENTIFIER));
        
        Assert.assertTrue("Precedence is correct", TokenType.IDENTIFIER.hasSamePrecedence(TokenType.NUMBER));
        Assert.assertTrue("Precedence is correct", TokenType.IDENTIFIER.hasSamePrecedence(TokenType.STRING));
    }
    
    private void checkFirstToken(
            List<Token> tokens,
            TokenType tokenType, String string) {
        
        Assert.assertTrue("list is not empty", tokens.size() > 0);
        System.out.format("Type: %s, String: '%s'%n", tokens.get(0).getTokenType(), tokens.get(0).getString());
        Assert.assertTrue("token type matches", tokens.get(0).getTokenType() == tokenType);
        Assert.assertTrue("string matches", string.equals(tokens.get(0).getString()));
        
        tokens.remove(0);
    }
    
    @Test
    public void testGetTokens() throws InvalidSyntaxException {
        
        List<Token> tokens;
        
        tokens = Tokenizer.getTokens("");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("R1ABC");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1ABC");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("R1ABC");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1ABC");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("321");
        checkFirstToken(tokens, TokenType.NUMBER, "321");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("32.221");
        checkFirstToken(tokens, TokenType.NUMBER, "32.221");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("321 353");
        checkFirstToken(tokens, TokenType.NUMBER, "321");
        checkFirstToken(tokens, TokenType.NUMBER, "353");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("321   353");
        checkFirstToken(tokens, TokenType.NUMBER, "321");
        checkFirstToken(tokens, TokenType.NUMBER, "353");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("321354");
        checkFirstToken(tokens, TokenType.NUMBER, "321354");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("(");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens(")");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("(R1)");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("&&");
        checkFirstToken(tokens, TokenType.BOOLEAN_AND, "&");    // The second & is eaten by the parser and not included in the _string.
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("R1 && R2");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.BOOLEAN_AND, "&");    // The second & is eaten by the parser and not included in the _string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R2");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("R1(x)");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("R1[x]");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("{x,y,z}[a]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("{x,y,z}[a..b]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the _string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("{x,y,z}[a..b,c]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the _string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "c");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("{x,y,z}[a,b..c]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the _string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "c");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("{x,y,z}[a,b..c,d,e,f..g]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the _string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "c");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "d");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "e");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "f");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the _string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "g");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("(R1(x))");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = Tokenizer.getTokens("R1(x)*(y+21.2)-2.12/R12");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        checkFirstToken(tokens, TokenType.MULTIPLY, "*");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.ADD, "+");
        checkFirstToken(tokens, TokenType.NUMBER, "21.2");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        checkFirstToken(tokens, TokenType.SUBTRACKT, "-");
        checkFirstToken(tokens, TokenType.NUMBER, "2.12");
        checkFirstToken(tokens, TokenType.DIVIDE, "/");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R12");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
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
    
}
