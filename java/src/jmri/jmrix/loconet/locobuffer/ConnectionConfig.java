package jmri.jmrix.loconet.locobuffer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.nodes.swing.ConfigNodes;

/**
 * Definition of objects to handle configuring a LocoBuffer layout connection
 * via a LocoBufferAdapter object.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003, 2010
 */
public class ConnectionConfig extends jmri.jmrix.AbstractSerialConnectionConfig {

    private final JPanel panel = new JPanel();
    private final ConfigNodes configNodes = new ConfigNodes();

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     * @param p the SerialPortAdapter to associate with this connection
     */
    public ConnectionConfig(jmri.jmrix.SerialPortAdapter p) {
        super(p);
    }

    /**
     * Ctor for a connection configuration with no preexisting adapter.
     * {@link #setInstance()} will fill the adapter member.
     */
    public ConnectionConfig() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadDetails(JPanel details) {
        panel.removeAll();
        super.loadDetails(panel);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.add(panel);
        
        details.add(configNodes.getButton(
                (LocoNetSystemConnectionMemo)adapter.getSystemConnectionMemo()));
    }

    @Override
    public String name() {
        return "LocoNet LocoBuffer"; // NOI18N
    }

    public boolean isOptList2Advanced() {
        return false;
    }

    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new LocoBufferAdapter();
        }
    }

}
