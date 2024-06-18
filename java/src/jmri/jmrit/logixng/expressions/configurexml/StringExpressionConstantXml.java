package jmri.jmrit.logixng.expressions.configurexml;

import jmri.InstanceManager;
import jmri.configurexml.JmriConfigureXmlException;
import jmri.jmrit.logixng.StringExpressionManager;
import jmri.jmrit.logixng.expressions.StringExpressionConstant;

import org.jdom2.Element;

/**
 * Handle XML configuration for ActionLightXml objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2004, 2008, 2010
 * @author Daniel Bergqvist Copyright (C) 2019
 */
public class StringExpressionConstantXml extends jmri.jmrit.logixng.configurexml.StoreAndLoadXml {

    public StringExpressionConstantXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleLightSignalHead
     * @param exportData export data
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o, ExportData exportData) {

        StringExpressionConstant p = (StringExpressionConstant) o;

        Element element = new Element("StringExpressionConstant");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));

        storeCommon(p, element);

        element.addContent(new Element("value").addContent(p.getValue()));
        
        return element;
    }
    
    @Override
    public boolean load(Element shared, ImportData importData) throws JmriConfigureXmlException {     // Test class that inherits this class throws exception
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        StringExpressionConstant h;
        h = new StringExpressionConstant(sys, uname);

        loadCommon(h, shared);

        Element valueElement = shared.getChild("value");
        h.setValue(valueElement.getTextTrim());

        InstanceManager.getDefault(StringExpressionManager.class).registerExpression(h);
        return true;
    }
    
//    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnalogExpressionConstantXml.class);
}
