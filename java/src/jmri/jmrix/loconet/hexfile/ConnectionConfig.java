package jmri.jmrix.loconet.hexfile;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.nodes.swing.ConfigNodes;

/**
 * Definition of objects to handle configuring a layout connection via a LocoNet
 * hexfile emulator.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003
 */
public class ConnectionConfig extends jmri.jmrix.AbstractSimulatorConnectionConfig {

    private final JPanel panel = new JPanel();
    private final ConfigNodes configNodes = new ConfigNodes();

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     *
     * @param p a {@link jmri.jmrix.SerialPortAdapter} object
     */
    public ConnectionConfig(jmri.jmrix.SerialPortAdapter p) {
        super(p);
    }

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
        return "LocoNet Simulator"; // NOI18N
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new LnHexFilePort();
        }
    }

}
