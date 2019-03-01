package jmri.jmrit.logixng.engine.configurexml;

import java.lang.reflect.Field;
import java.util.List;
import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.jmrit.logixng.engine.DefaultDigitalExpressionManager;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;

/**
 * Provides the functionality for configuring ExpressionManagers
 * <P>
 *
 * @author Dave Duchamp Copyright (c) 2007
 * @author Daniel Bergqvist Copyright (c) 2018
 */
public class DefaultExpressionManagerXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public DefaultExpressionManagerXml() {
    }

    private DigitalExpression getExpression(DigitalExpression expression) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field f = expression.getClass().getDeclaredField("_expression");
        f.setAccessible(true);
        return (DigitalExpression) f.get(expression);
    }
    
    /**
     * Default implementation for storing the contents of a LogixManager
     *
     * @param o Object to store, of type LogixManager
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        Element expressions = new Element("logixngExpressions");
        setStoreElementClass(expressions);
        DigitalExpressionManager tm = (DigitalExpressionManager) o;
        if (tm != null) {
            for (DigitalExpression expression : tm.getNamedBeanSet()) {
                log.debug("expression system name is " + expression.getSystemName());  // NOI18N
                try {
                    Element e = jmri.configurexml.ConfigXmlManager.elementFromObject(getExpression(expression));
                    if (e != null) {
                        expressions.addContent(e);
                    }
                } catch (Exception e) {
                    log.error("Error storing action: {}", e, e);
                }
            }
        }
        return (expressions);
    }

    /**
     * Subclass provides implementation to create the correct top element,
     * including the type information. Default implementation is to use the
     * local class here.
     *
     * @param expressions The top-level element being created
     */
    public void setStoreElementClass(Element expressions) {
        expressions.setAttribute("class", this.getClass().getName());  // NOI18N
    }

    @Override
    public void load(Element element, Object o) {
        log.error("Invalid method called");  // NOI18N
    }

    /**
     * Create a DigitalExpressionManager object of the correct class, then register and
 fill it.
     *
     * @param sharedExpression  Shared top level Element to unpack.
     * @param perNodeExpression Per-node top level Element to unpack.
     * @return true if successful
     */
    @Override
    public boolean load(Element sharedExpression, Element perNodeExpression) {
        // create the master object
        replaceExpressionManager();
        // load individual sharedLogix
        loadExpressions(sharedExpression);
        return true;
    }

    /**
     * Utility method to load the individual Logix objects. If there's no
     * additional info needed for a specific logix type, invoke this with the
     * parent of the set of Logix elements.
     *
     * @param expressions Element containing the Logix elements to load.
     */
    public void loadExpressions(Element expressions) {
/*        
        List<Element> expressionList = expressions.getChildren("expression");  // NOI18N
        if (log.isDebugEnabled()) {
            log.debug("Found " + expressionList.size() + " expressions");  // NOI18N
        }
        DigitalExpressionManager tm = InstanceManager.getDefault(jmri.DigitalExpressionManager.class);

        for (int i = 0; i < expressionList.size(); i++) {

            String sysName = getSystemName(expressionList.get(i));
            if (sysName == null) {
                log.warn("unexpected null in systemName " + expressionList.get(i));  // NOI18N
                break;
            }

            String userName = getUserName(expressionList.get(i));

            String yesno = "";
            if (expressionList.get(i).getAttribute("enabled") != null) {  // NOI18N
                yesno = expressionList.get(i).getAttribute("enabled").getValue();  // NOI18N
            }
            if (log.isDebugEnabled()) {
                log.debug("create expression: (" + sysName + ")("  // NOI18N
                        + (userName == null ? "<null>" : userName) + ")");  // NOI18N
            }

            DigitalExpression x = tm.createNewExpression(sysName, userName);
            if (x != null) {
                // load common part
                loadCommon(x, expressionList.get(i));

                // set enabled/disabled if attribute was present
                if ((yesno != null) && (!yesno.equals(""))) {
                    if (yesno.equals("yes")) {  // NOI18N
                        x.setEnabled(true);
                    } else if (yesno.equals("no")) {  // NOI18N
                        x.setEnabled(false);
                    }
                }
/*                
                // load conditionals, if there are any
                List<Element> logixConditionalList = expressionList.get(i).getChildren("logixConditional");  // NOI18N
                if (logixConditionalList.size() > 0) {
                    // add conditionals
                    for (int n = 0; n < logixConditionalList.size(); n++) {
                        if (logixConditionalList.get(n).getAttribute("systemName") == null) {  // NOI18N
                            log.warn("unexpected null in systemName " + logixConditionalList.get(n)  // NOI18N
                                    + " " + logixConditionalList.get(n).getAttributes());
                            break;
                        }
                        String cSysName = logixConditionalList.get(n)
                                .getAttribute("systemName").getValue();  // NOI18N
                        int cOrder = Integer.parseInt(logixConditionalList.get(n)
                                .getAttribute("order").getValue());  // NOI18N
                        // add conditional to logix
                        x.addConditional(cSysName, cOrder);
                    }
                }
*./                
            }
        }
*/
    }

    /**
     * Replace the current LogixManager, if there is one, with one newly created
     * during a load operation. This is skipped if they are of the same absolute
     * type.
     */
    protected void replaceExpressionManager() {
        if (InstanceManager.getDefault(jmri.LogixManager.class).getClass().getName()
                .equals(DefaultDigitalExpressionManager.class.getName())) {
            return;
        }
        // if old manager exists, remove it from configuration process
        if (InstanceManager.getNullableDefault(jmri.LogixManager.class) != null) {
            ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
            if (cmOD != null) {
                cmOD.deregister(InstanceManager.getDefault(jmri.LogixManager.class));
            }

        }

        // register new one with InstanceManager
        DefaultDigitalExpressionManager pManager = DefaultDigitalExpressionManager.instance();
        InstanceManager.store(pManager, DigitalExpressionManager.class);
        // register new one for configuration
        ConfigureManager cmOD = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cmOD != null) {
            cmOD.registerConfig(pManager, jmri.Manager.LOGIXS);
        }
    }

    @Override
    public int loadOrder() {
        return InstanceManager.getDefault(jmri.jmrit.logixng.DigitalExpressionManager.class).getXMLOrder();
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultExpressionManagerXml.class);
}
