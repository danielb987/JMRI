package jmri.jmrit.logixng.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import jmri.InstanceManager;
import jmri.Manager;
import jmri.NamedBean;
import jmri.ProvidingManager;
import jmri.jmrit.logixng.LogixNG_InstanceManager;
import jmri.jmrit.logixng.NullNamedBeanInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A default implementation of the LogixNG_InstanceManager.
 */
public class TemplateInstanceManager implements LogixNG_InstanceManager {

    private final HashMap<Class<?>, NullNamedBeanInitializer> initializers = new HashMap<>();
    
    {
        ServiceLoader.load(NullNamedBeanInitializer.class).forEach((provider) -> {
            provider.getInitalizes().forEach((cls) -> {
                initializers.put(cls, provider);
                log.debug("Using {} to provide default instance of {}", provider.getClass().getName(), cls.getName());
            });
        });
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")  // Needed due to limitations of Java templates
    public <M extends Manager, N extends NamedBean> N get(
            Class<M> type, Class<N> clazz, String name) {
        
        return (N) InstanceManager.getDefault(type).getNamedBean(name);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")  // Needed due to limitations of Java templates
    public <M extends Manager, N extends NamedBean> N provide(
            Class<M> type, Class<N> clazz, String name) {
        
        // Step 1: Try provide() if it's a providing manager
        // Step 2: Try get()
        // Step 3: Try to create it ourself
        
        if (type.isInstance(ProvidingManager.class)) {
            try {
                return ((ProvidingManager<N>)InstanceManager.getDefault(type))
                        .provide(name);
            } catch (IllegalArgumentException e) {
                log.warn("TemplateInstanceManager.provide() : "+e.getLocalizedMessage(), e);
            }
        }
        
        N nb = (N) InstanceManager.getDefault(type).getNamedBean(name);
        if (nb != null) {
            return nb;
        }
        
        NullNamedBeanInitializer initializer = initializers.get(clazz);
        if (initializer != null) {
            try {
                nb = (N) initializer.create(clazz, name);
                InstanceManager.getDefault(type).register(nb);
                return nb;
            } catch (IllegalArgumentException e) {
                log.warn("TemplateInstanceManager.provide() : "+e.getLocalizedMessage(), e);
            }
        }
        
        return null;
    }


    private final static Logger log = LoggerFactory.getLogger(TemplateInstanceManager.class);

}
