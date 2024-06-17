package jmri.jmrit.logixng.configurexml;

import jmri.configurexml.JmriConfigureXmlException;
import jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML;

import org.jdom2.Element;

/**
 * Base class for LogixNG xml classes.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public abstract class StoreAndLoadXml extends AbstractNamedBeanManagerConfigXML {

// cd /path/to/your/folder
// sed -i 's/foo/bar/g' *

// sed -i 's/jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML/jmri.jmrit.logixng.configurexml.StoreAndLoadXml/g' *

/*

Substitute "Some...\n...Thing" by the contents of file "new" in one or more input files

perl -i -p0e 's/Some.*?thing\n/`cat new`/se' input.txt ...

    -i to change input.txt directly
    -p0 slurp input file file and print it in the end
    s/regexp/.../s in regexp . is .|\n
    s/.../exp/e replace by eval(exp)
    new -- a file containing the replacement text (This is completely...different text)
    if useful you can expand the original text s/Some text\n...\n...thing\n/...


perl -i -p0e 's/     \* \@return Element containing the complete info\n     \*\/\n    \@Override\n    public Element store\(Object o\) \{/`cat \/home\/daniel\/Dokument\/GitHub\/replace.txt`/se' ReturnXml.java

*/

    @Override
    public Element store(Object o) {
        return store(o, new ExportData());
    }

    @Override
    public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {
        return load(shared, new ImportData());
    }

    public Element store(Object o, ExportData exportData) {
        return store(o);    // Temporary until all classes are refactorized
    }

    public boolean load(Element shared, ImportData importData) throws JmriConfigureXmlException {
        return load(shared, (Element)null);  // Temporary until all classes are refactorized
    }

    /**
     * Data when doing an export.
     */
    public static class ExportData {
    }

    /**
     * Data when doing an import.
     */
    public static class ImportData {
    }

}
