package jmri.jmrix.openlcb.swing.lccpro;

import java.awt.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

import jmri.*;
import jmri.jmrix.can.CanSystemConnectionMemo;
import jmri.util.swing.JmriJOptionPane;

import org.openlcb.*;
import org.openlcb.cdi.impl.ConfigRepresentation;

/**
 * Do a backup of a specified node.
 *
 * Not truly an Action, though it might be some day
 * @author Bob Jacobsen   (c) 2026
 */


class NodeBackupAction {

    private ConfigRepresentation _cdi;
    private String _name;
    
    static JFileChooser directoryChooser = new JFileChooser();
    static final String PREFNAME = "backupDirectory";
    
    static {
        directoryChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.setDialogTitle(Bundle.getMessage("ChooseADirectory"));
        directoryChooser.setSelectedFile(new File("."));
        
        var prefsMgr = InstanceManager.getDefault(UserPreferencesManager.class);
        var dirPref = prefsMgr.getProperty(NodeBackupAction.class.getName(), PREFNAME);
        if (dirPref != null) {
            directoryChooser.setSelectedFile(new File(dirPref.toString()));
        }

    }
    
    static public void showOpenDialog(Component here) {
        var response = directoryChooser.showOpenDialog(here);
        
        if (response == JFileChooser.APPROVE_OPTION) {
            String directory = directoryChooser.getSelectedFile().getAbsolutePath();
            var prefsMgr = InstanceManager.getDefault(UserPreferencesManager.class);
            prefsMgr.setProperty(NodeBackupAction.class.getName(), PREFNAME, directory);
        }
    }
    
    public void doBackup(MimicNodeStore.NodeMemo nodememo, CanSystemConnectionMemo memo, String name) {
        var node = nodememo.getNodeID();
        _name = name;
        log.info("Backup {} '{}' ", node.toString(), _name);    
        
        var iface = memo.get(OlcbInterface.class);
        
        _cdi = iface.getConfigForNode(node);
               
        if (_cdi.getRoot() != null) {
            storeCdiData();
        } else {
            JmriJOptionPane.showMessageDialogNonModal(null,
                    Bundle.getMessage("MessageCdiLoad", _name, node),
                    Bundle.getMessage("TitleCdiLoad", name),
                    JmriJOptionPane.INFORMATION_MESSAGE,
                    null);
            _cdi.addPropertyChangeListener(new CdiListener());
        }
    }
    
    private class CdiListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            log.debug("CdiListener event = {}", propertyName);

            if (propertyName.equals("UPDATE_CACHE_COMPLETE")) {
                Window[] windows = Window.getWindows();
                for (Window window : windows) {
                    if (window instanceof JDialog) {
                        JDialog dialog = (JDialog) window;
                        if (Bundle.getMessage("TitleCdiLoad", _name).equals(dialog.getTitle())) {
                            dialog.dispose();
                            break;
                        }
                    }
                }
                storeCdiData();
            }
        }
    }
    
    protected void storeCdiData() {
        log.debug("Load complete");
        var filename = org.openlcb.cdi.swing.CdiPanel
                        .fileNameGenerator.generateFileName(_cdi, _name);
        String directory = directoryChooser.getSelectedFile().getAbsolutePath();
        String storeName = directory+File.separator+filename;
        log.info("Storing to file {}", storeName);
        try {
            org.openlcb.cdi.cmd.BackupConfig.writeConfigToFile(storeName, _cdi);
        } catch (IOException e) {
            log.error("Error writing file! ", e);
        }
        
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NodeBackupAction.class);
}
