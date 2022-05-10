package jmri.jmrit.logixng.configurexml;

import java.util.Map;

import jmri.Manager;
import jmri.NamedBean;
import jmri.jmrit.logixng.configurexml.ImportExportItemXml.NamedBeanToExport;

import org.jdom2.Element;

/**
 * Import/export LogixNG data.
 *
 * @author Daniel Berqvist (C) 2022
 */
public interface ImportExportManagerXml {

    public void addNamedBeansToExport(
            Object o,
            Map<Manager<? extends NamedBean>, Map<String,NamedBeanToExport>> map);

    public Element export(Object o);

}
