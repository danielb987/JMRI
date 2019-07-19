package jmri.jmrit.logixng;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.NamedBean;
import jmri.util.junit.annotations.ToDo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Base class for testing FemaleStringExpressionSocket classes
 * 
 * @author Daniel Bergqvist 2018
 */
public abstract class FemaleSocketTestBase {

    protected AtomicBoolean flag;
    protected AtomicBoolean errorFlag;
    protected MaleSocket maleSocket;
    protected MaleSocket otherMaleSocket;
    protected FemaleSocket femaleSocket;
    
    abstract protected boolean hasSocketBeenSetup();
    
    @Test
    public void testSetup() throws SocketAlreadyConnectedException {
        Assert.assertFalse("not connected", femaleSocket.isConnected());

        // Check that we can call setup() even if the socket is not connected.
        femaleSocket.setup();
        
        femaleSocket.connect(maleSocket);
        Assert.assertTrue("is connected", femaleSocket.isConnected());
        Assert.assertFalse("not setup", hasSocketBeenSetup());
        femaleSocket.setup();
        Assert.assertTrue("is setup", hasSocketBeenSetup());
    }
    
    @Test
    public void testConnect() {
        
        // Test connect male socket
        flag.set(false);
        errorFlag.set(false);
        try {
            femaleSocket.connect(maleSocket);
        } catch (SocketAlreadyConnectedException ex) {
            errorFlag.set(true);
        }
        
        Assert.assertTrue("Socket is connected", flag.get());
        Assert.assertFalse("No error", errorFlag.get());
        
        // Test connect male socket when already connected
        flag.set(false);
        errorFlag.set(false);
        try {
            femaleSocket.connect(otherMaleSocket);
        } catch (SocketAlreadyConnectedException ex) {
            errorFlag.set(true);
        }
        
        Assert.assertFalse("Socket was not connected again", flag.get());
        Assert.assertTrue("Socket already connected error", errorFlag.get());
    }
    
    @Test
    public void testDisconnect() throws SocketAlreadyConnectedException {
        
        // Ensure the socket is connected before this test.
        if (!femaleSocket.isConnected()) {
            femaleSocket.connect(maleSocket);
        }
        
        // Test disconnect male socket
        flag.set(false);
        femaleSocket.disconnect();
        
        Assert.assertTrue("Socket is disconnected", flag.get());
        
        // Test connect male socket
        flag.set(false);
        errorFlag.set(false);
        femaleSocket.disconnect();
        
        Assert.assertFalse("Socket is not disconnected again", flag.get());
    }
    
    private boolean setName_verifyException(String newName, String expectedExceptionMessage) {
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        try {
            femaleSocket.setName(newName);
        } catch (IllegalArgumentException ex) {
            hasThrown.set(true);
            Assert.assertTrue("Error message is correct", ex.getMessage().equals(expectedExceptionMessage));
        }
        return hasThrown.get();
    }
    
    @Test
    public void testSetName() {
        // Both letters and digits is OK
        femaleSocket.setName("X12");
        Assert.assertTrue("name matches", "X12".equals(femaleSocket.getName()));
        
        // Only letters is OK
        femaleSocket.setName("Xyz");
        Assert.assertTrue("name matches", "Xyz".equals(femaleSocket.getName()));
        
        // Both letters and digits in random order is OK as long as the first
        // character is a letter
        femaleSocket.setName("X1b2c3Y");
        Assert.assertTrue("name matches", "X1b2c3Y".equals(femaleSocket.getName()));
        
        // The name must start with a letter, not a digit
        Assert.assertTrue("exception is thrown", setName_verifyException("123", "the name is not valid: 123"));
        
        // The name must not contain any spaces
        Assert.assertTrue("exception is thrown", setName_verifyException(" A123", "the name is not valid:  A123"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A1 23", "the name is not valid: A1 23"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A123 ", "the name is not valid: A123 "));
        
        // The name must not contain any character that's not a letter nor a digit
        Assert.assertTrue("exception is thrown", setName_verifyException("A12!3", "the name is not valid: A12!3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A+123", "the name is not valid: A+123"));
        Assert.assertTrue("exception is thrown", setName_verifyException("=A123", "the name is not valid: =A123"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12*3", "the name is not valid: A12*3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A123/", "the name is not valid: A123/"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12(3", "the name is not valid: A12(3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12)3", "the name is not valid: A12)3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12[3", "the name is not valid: A12[3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12]3", "the name is not valid: A12]3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12{3", "the name is not valid: A12{3"));
        Assert.assertTrue("exception is thrown", setName_verifyException("A12}3", "the name is not valid: A12}3"));
    }
    
}
