package jmri.jmrit.logixng;

/**
 * A LogixNG male socket.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface MaleSocket extends Base {

    /**
     * Set the debug configuration for this male socket.
     * <P>
     * Each implementation of MaleSocket has their own implementation of
     * DebugConfig. Use reflection to get the proper class
     * &lt;package-name&gt;.debug.&lt;ClassName&gt;Debug that returns a JPanel
     * that can configure debugging for this male socket.
     * 
     * @param config the new configuration or null to turn off debugging
     */
    public void setDebugConfig(DebugConfig config);

    /**
     * Set the debug configuration for this male socket.
     * 
     * @return the new configuration or null if debugging is turned off for this male socket
     */
    public DebugConfig getDebugConfig();

    /**
     * Debug configuration for this male socket.
     */
    public interface DebugConfig {
    }

}
