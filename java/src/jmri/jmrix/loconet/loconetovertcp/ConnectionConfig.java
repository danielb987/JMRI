package jmri.jmrix.loconet.loconetovertcp;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import jmri.jmrix.loconet.nodes.swing.ConfigNodes;


/**
 * Definition of objects to handle configuring a LocoNetOverTcp layout
 * connection via a LnTcpDriverAdapter object.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003
 * @author Stephen Williams Copyright (C) 2008
 *
 */
public class ConnectionConfig extends jmri.jmrix.AbstractNetworkConnectionConfig {

    private final JPanel panel = new JPanel();
    private final ConfigNodes configNodes = new ConfigNodes();

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     * @param p the NetworkPortAdapter to associate with this connection
     */
    public ConnectionConfig(jmri.jmrix.NetworkPortAdapter p) {
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
        return Bundle.getMessage("ConnectionTitle");
    }

    public boolean isOptList1Advanced() {
        return false;
    }

    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new LnTcpDriverAdapter();
            adapter.setPort(1234);
        }
    }

}
