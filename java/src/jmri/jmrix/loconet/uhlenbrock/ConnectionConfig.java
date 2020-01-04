package jmri.jmrix.loconet.uhlenbrock;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import jmri.jmrix.loconet.nodes.swing.ConfigNodes;

/**
 * Definition of objects to handle configuring an Uhlenbrock serial port layout
 * connection via an IntelliboxAdapter object.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003
 */
public class ConnectionConfig extends jmri.jmrix.AbstractSerialConnectionConfig {

    private final JPanel panel = new JPanel();
    private final ConfigNodes configNodes = new ConfigNodes();

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     *
     * @param p  a @link jmri.jmrix.SerialPortAdapter} object
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
        return "Intellibox-II (USB)";
    } // NOI18N

    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new UhlenbrockAdapter();
        }
    }

}
