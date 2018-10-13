package jmri.jmrit.newlogix;

import java.util.Set;
import jmri.InstanceInitializer;
import jmri.implementation.AbstractInstanceInitializer;
import org.openide.util.lookup.ServiceProvider;

/**
 * Initialize the preferences for NewLogix
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
@ServiceProvider(service = InstanceInitializer.class)
public class NewLogixPreferencesInstanceInitializer extends AbstractInstanceInitializer {

    @Override
    public <T> Object getDefault(Class<T> type) throws IllegalArgumentException {
        if (type == NewLogixPreferences.class) {
            return new NewLogixPreferences();
        }
        return super.getDefault(type);
    }
    
    @Override
    public Set<Class<?>> getInitalizes() {
        Set<Class<?>> set = super.getInitalizes();
        set.add(NewLogixPreferences.class);
        return set;
    }

}
