package jmri.jmrit.logixng;

import java.util.concurrent.atomic.AtomicBoolean;
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
public class FemaleSocketTestBase {

    protected AtomicBoolean flag;
    protected AtomicBoolean errorFlag;
    protected MaleSocket maleSocket;
    protected MaleSocket otherMaleSocket;
    protected FemaleSocket femaleSocket;
    
    @Test
    @ToDo("Implement proper testing of FemaleSocket.setup()")
    public void testSetup() {
        // Do proper testing later
        if (femaleSocket.isConnected()) {
            femaleSocket.disconnect();
        }
        femaleSocket.setup();
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
    
}
