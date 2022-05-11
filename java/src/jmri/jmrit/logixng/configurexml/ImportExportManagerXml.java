package jmri.jmrit.logixng.configurexml;

import java.util.Map;

import jmri.Manager;
import jmri.NamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.configurexml.ImportExportItemXml.NamedBeanToExport;

import org.jdom2.Element;

/**
 * Import/export LogixNG data.
 *
 * @author Daniel Berqvist (C) 2022
 */
public interface ImportExportManagerXml {

    /**
     * Add all the named beans that's used by the items that are to be exported.
     *
     * @param ancestor  the ancestor to the items to be exported
     * @param o         the manager
     * @param map       the map with the manager and map with beans to export
     */
    public void addNamedBeansToExport(
            Base ancestor,
            Object o,
            Map<Manager<? extends NamedBean>, Map<String,NamedBeanToExport>> map);

    public Element export(Object o);

}
