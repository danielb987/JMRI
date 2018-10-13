package jmri.jmrit.newlogix;

import java.util.prefs.Preferences;
import jmri.beans.PreferencesBean;
import jmri.profile.ProfileManager;
import jmri.profile.ProfileUtils;

/**
 * Preferences for NewLogix
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class NewLogixPreferences extends PreferencesBean {

    public static final String START_NEW_LOGIX_ON_LOAD = "startNewLogixOnStartup";
    public static final String ALLOW_DEBUG_MODE = "allowDebugMode";
    
    private boolean startNewLogixOnLoad = false;
    private boolean allowDebugMode = false;
    
    public NewLogixPreferences() {
        super(ProfileManager.getDefault().getActiveProfile());
        Preferences sharedPreferences = ProfileUtils.getPreferences(
                super.getProfile(), this.getClass(), true);
        this.readPreferences(sharedPreferences);
    }

    private void readPreferences(Preferences sharedPreferences) {
        this.startNewLogixOnLoad = sharedPreferences.getBoolean(START_NEW_LOGIX_ON_LOAD, this.startNewLogixOnLoad);
        this.allowDebugMode = sharedPreferences.getBoolean(ALLOW_DEBUG_MODE, this.allowDebugMode);
/*        
        this.allowRemoteConfig = sharedPreferences.getBoolean(ALLOW_REMOTE_CONFIG, this.allowRemoteConfig);
        this.clickDelay = sharedPreferences.getInt(CLICK_DELAY, this.clickDelay);
        this.simple = sharedPreferences.getBoolean(SIMPLE, this.simple);
        this.railroadName = sharedPreferences.get(RAILROAD_NAME, this.railroadName);
        this.readonlyPower = sharedPreferences.getBoolean(READONLY_POWER, this.readonlyPower);
        this.refreshDelay = sharedPreferences.getInt(REFRESH_DELAY, this.refreshDelay);
        this.useAjax = sharedPreferences.getBoolean(USE_AJAX, this.useAjax);
        this.disableFrames = sharedPreferences.getBoolean(DISABLE_FRAME_SERVER, this.disableFrames);
        this.redirectFramesToPanels = sharedPreferences.getBoolean(REDIRECT_FRAMES, this.redirectFramesToPanels);
        try {
            Preferences frames = sharedPreferences.node(DISALLOWED_FRAMES);
            if (frames.keys().length != 0) {
                this.disallowedFrames.clear();
                for (String key : frames.keys()) { // throws BackingStoreException
                    String frame = frames.get(key, null);
                    if (frame != null && !frame.trim().isEmpty()) {
                        this.disallowedFrames.add(frame);
                    }
                }
            }
        } catch (BackingStoreException ex) {
            // this is expected if sharedPreferences have not been written previously,
            // so do nothing.
        }
        this.port = sharedPreferences.getInt(PORT, this.port);
        this.useZeroConf = sharedPreferences.getBoolean(USE_ZERO_CONF, this.useZeroConf);
*/
        this.setIsDirty(false);
    }

    public boolean compareValuesDifferent(NewLogixPreferences prefs) {
        if (getStartNewLogixOnStartup() != prefs.getStartNewLogixOnStartup()) {
            return true;
        }
        if (getAllowDebugMode() != prefs.getAllowDebugMode()) {
            return true;
        }
/*        
        if (isUseAjax() != prefs.isUseAjax()) {
            return true;
        }
        if (this.allowRemoteConfig() != prefs.allowRemoteConfig()) {
            return true;
        }
        if (this.isReadonlyPower() != prefs.isReadonlyPower()) {
            return true;
        }
        if (!(Arrays.equals(getDisallowedFrames(), prefs.getDisallowedFrames()))) {
            return true;
        }
        if (getPort() != prefs.getPort()) {
            return true;
        }
        return !getRailroadName().equals(prefs.getRailroadName());
*/
        return false;
    }

    public void apply(NewLogixPreferences prefs) {
        setStartNewLogixOnStartup(prefs.getStartNewLogixOnStartup());
        setAllowDebugMode(prefs.getAllowDebugMode());
/*        
        setUseAjax(prefs.isUseAjax());
        this.setAllowRemoteConfig(prefs.allowRemoteConfig());
        this.setReadonlyPower(prefs.isReadonlyPower());
        setDisallowedFrames(prefs.getDisallowedFrames());
        setPort(prefs.getPort());
        setRailroadName(prefs.getRailroadName());
*/        
    }

    public void save() {
        Preferences sharedPreferences = ProfileUtils.getPreferences(this.getProfile(), this.getClass(), true);
        sharedPreferences.putBoolean(START_NEW_LOGIX_ON_LOAD, this.getStartNewLogixOnStartup());
        sharedPreferences.putBoolean(ALLOW_DEBUG_MODE, this.getAllowDebugMode());
/*        
        sharedPreferences.putInt(PORT, this.getPort());
        sharedPreferences.putBoolean(USE_ZERO_CONF, this.isUseZeroConf());
        sharedPreferences.putInt(CLICK_DELAY, this.getClickDelay());
        sharedPreferences.putInt(REFRESH_DELAY, this.getRefreshDelay());
        sharedPreferences.putBoolean(USE_AJAX, this.isUseAjax());
        sharedPreferences.putBoolean(SIMPLE, this.isSimple());
        sharedPreferences.putBoolean(ALLOW_REMOTE_CONFIG, this.allowRemoteConfig());
        sharedPreferences.putBoolean(READONLY_POWER, this.isReadonlyPower());
        sharedPreferences.put(RAILROAD_NAME, getRailroadName());
        sharedPreferences.putBoolean(DISABLE_FRAME_SERVER, this.isDisableFrames());
        sharedPreferences.putBoolean(REDIRECT_FRAMES, this.redirectFramesToPanels);
        try {
            Preferences node = sharedPreferences.node(DISALLOWED_FRAMES);
            this.disallowedFrames.stream().forEach((frame) -> {
                node.put(Integer.toString(this.disallowedFrames.indexOf(frame)), frame);
            });
            if (this.disallowedFrames.size() < node.keys().length) {
                for (int i = node.keys().length - 1; i >= this.disallowedFrames.size(); i--) {
                    node.remove(Integer.toString(i));
                }
            }
            sharedPreferences.sync();
            setIsDirty(false);  //  Resets only when stored
        } catch (BackingStoreException ex) {
            log.error("Exception while saving web server preferences", ex);
        }
*/
    }
    
    public void setStartNewLogixOnStartup(boolean value) {
        startNewLogixOnLoad = value;
    }

    public boolean getStartNewLogixOnStartup() {
        return startNewLogixOnLoad;
    }

    public void setAllowDebugMode(boolean value) {
        allowDebugMode = value;
    }

    public boolean getAllowDebugMode() {
        return allowDebugMode;
    }

}
