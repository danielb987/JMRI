package jmri.jmrix.loconet.pr3;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.jmrix.loconet.nodes.swing.ConfigNodes;
import jmri.util.SystemType;

/**
 * Definition of objects to handle configuring a PR3 layout connection via a
 * PR2Adapter object.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003, 2008, 2010
 */
public class ConnectionConfig extends jmri.jmrix.AbstractSerialConnectionConfig {

    private final JPanel panel = new JPanel();
    private final ConfigNodes configNodes = new ConfigNodes();

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     * 
     * @param p   Serial port adapter for the connection
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

    /**
     * Get the connection type name.
     *
     * @return Connection type name
     */
    @Override
    public String name() {
        return "LocoNet PR3"; // NOI18N
    }

    /**
     * Is Option List 2 Advanced?
     * @return boolean, always false 
     */
    public boolean isOptList2Advanced() {
        return false;
    }

    @Override
    protected String[] getPortFriendlyNames() {
        if (SystemType.isWindows()) {
            return new String[]{"Communications Port"}; // NOI18N
        }
        return new String[]{};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new PR3Adapter();
        }
    }

}
