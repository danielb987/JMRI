package jmri.jmrix.loconet.usb_dcs240;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import jmri.jmrix.loconet.nodes.swing.ConfigNodes;
import jmri.util.SystemType;

/**
 * Definition of objects to handle configuring a DCS240 USB layout connection via a
 * PR2Adapter object.
 * <p>
 * Copied from loconet.pr3.ConnectionConfig.java
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003, 2008, 2010
 * @author B. Milhaupt Copyright (C) 2019
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
        details.add(configNodes.getButton());
    }

    @Override
    public String name() {
        return "DCS240 USB Interface"; // NOI18N
    }

    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new UsbDcs240Adapter();
        }
    }
}
