package jmri.jmrit.newlogix;

import javax.annotation.CheckForNull;

/**
 * A NewLogix female expression socket.
 * A NewLogixExpression or a NewLogixAction that has children must not use
 * these directly but instead use a FemaleSocket.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface FemaleSocket extends Base {

    /**
     * Connect the male socket to this female socket.
     * @param socket the socket to connect
     */
    public void connect(MaleSocket socket);

    /**
     * Disconnect the current connected male socket from this female socket.
     */
    public void disconnect();
    
    /**
     * Get the connected socket.
     * @return the male socket or null if not connected
     */
    public MaleSocket getConnectedSocket();
    
    /**
     * Is a male socket connected to this female socket?
     * @return true if connected
     */
    public boolean isConnected();
    
    /**
     * Is a particular male socket compatible with this female socket?
     * @param socket the male socket
     * @return true if the male socket can be connected to this female socket
     */
    public boolean isCompatible(MaleSocket socket);
    
    /**
     * Set the name of this socket.
     * @param name the name
     */
    public void setName(String name);
    
    /**
     * Get the name of this socket.
     * @return the name
     */
    @CheckForNull
    public String getName();
    
}
