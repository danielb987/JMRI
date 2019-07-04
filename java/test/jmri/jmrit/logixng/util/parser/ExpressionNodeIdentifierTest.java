package jmri.jmrit.logixng.util.parser;

import java.util.HashMap;
import java.util.Map;
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
public class ExpressionNodeIdentifierTest {

    @Test
    public void testCtor() throws IdentifierNotExistsException {
        Token token = new Token();
        token._string = "abc";
        Map<String, Variable> variables = new HashMap<>();
        variables.put("abc", new Variable() {
            @Override
            public String getName() {
                return "abc";
            }

            @Override
            public Object getValue() {
                return "0";
            }
        });
        ExpressionNodeIdentifier t = new ExpressionNodeIdentifier(token, variables);
        Assert.assertNotNull("not null", t);
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
