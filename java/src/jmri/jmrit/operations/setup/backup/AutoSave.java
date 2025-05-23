package jmri.jmrit.operations.setup.backup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jmri.InstanceManager;
import jmri.jmrit.operations.OperationsXml;
import jmri.jmrit.operations.setup.Setup;
import jmri.jmrit.operations.trains.TrainManager;

/**
 * Auto Save. When enabled will automatically save operation files.
 *
 * @author Daniel Boudreau Copyright (C) 2012
 */
public class AutoSave {

    static Thread autoSave = null;

    public static synchronized void start() {
        if (Setup.isAutoSaveEnabled() && autoSave == null) {
            autoSave = jmri.util.ThreadingUtil.newThread(() -> {
                saveFiles();
            });
            autoSave.setName("Operations Auto Save"); // NOI18N
            autoSave.start();
        }
    }

    public static synchronized void stop() {
        if (autoSave != null) {
            autoSave.interrupt();
            try {
                autoSave.join();
            } catch (InterruptedException e) {
                // Do nothing
            } catch (NullPointerException e) {
                // Do nothing
            }
            autoSave = null;
        }
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings( value="SLF4J_FORMAT_SHOULD_BE_CONST",
            justification="I18N of Info Message")
    private static void saveFiles() {
        while (true) {
            synchronized (autoSave) {
                if (!Setup.isAutoSaveEnabled()) {
                    break;
                }
                try {
                    autoSave.wait(60000); // check every minute
                } catch (InterruptedException e) {
                    break; // stop was called
                }
                if (OperationsXml.areFilesDirty()) {
                    log.debug("Detected dirty operation files");
                    try {
                        autoSave.wait(60000); // wait another minute before
                                              // saving
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                    if (!Setup.isAutoSaveEnabled()) {
                        break;
                    }
                    if (InstanceManager.getDefault(TrainManager.class).isAnyTrainBuilding()) {
                        log.debug("Detected trains being built");
                        continue;
                    }
                    if (OperationsXml.areFilesDirty()) {
                        OperationsXml.save();
                        log.info(Bundle.getMessage("InfoFilesSaved"));
                    }
                }
            }
        }
        autoSave = null; // done
    }

    private final static Logger log = LoggerFactory.getLogger(AutoSave.class);
}
