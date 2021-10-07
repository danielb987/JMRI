package jmri.jmrit.logixng.implementation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import jmri.*;
import jmri.jmrit.logixng.*;
import jmri.managers.AbstractManager;
import jmri.util.*;

/**
 * Class providing the basic logic of the Type_Manager interface.
 * 
 * @author Dave Duchamp       Copyright (C) 2007
 * @author Daniel Bergqvist   Copyright (C) 2021
 */
public class DefaultTypeManager extends AbstractManager<Type>
        implements TypeManager {

    DecimalFormat paddedNumber = new DecimalFormat("0000");

    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getXMLOrder() {
        return LOGIXNG_TYPES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char typeLetter() {
        return 'Q';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValidity validSystemNameFormat(String systemName) {
        return LogixNG_Manager.validSystemNameFormat(
                getSubSystemNamePrefix(), systemName);
//        if (systemName.matches(getSubSystemNamePrefix()+"(:AUTO:)?\\d+")) {
//            return NameValidity.VALID;
//        } else {
//            return NameValidity.INVALID;
//        }
    }

    /*.*
     * {@inheritDoc}
     *./
    @Override
    public Type newCSVTable(String systemName, String userName, String fileName)
            throws IllegalArgumentException {
        
        // Check that Type does not already exist
        Type x;
        if (userName != null && !userName.equals("")) {
            x = getByUserName(userName);
            if (x != null) {
                return null;
            }
        }
        x = getBySystemName(systemName);
        if (x != null) {
            return null;
        }
        // Check if system name is valid
        if (this.validSystemNameFormat(systemName) != NameValidity.VALID) {
            throw new IllegalArgumentException("SystemName " + systemName + " is not in the correct format");
        }
        try {
            // Type does not exist, create a new Type
            x = AbstractType.loadTableFromCSV_File(systemName, userName, fileName, true);
        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
            log.error("Cannot load table due to I/O error", ex);
            return null;
        }
        // save in the maps
        register(x);
        
        // Keep track of the last created auto system name
        updateAutoNumber(systemName);
        
        return x;
    }

    /*.*
     * {@inheritDoc}
     *./
    @Override
    public Type newInternalTable(String systemName, String userName, int numRows, int numColumns)
            throws IllegalArgumentException {
        
        // Check that Type does not already exist
        Type x;
        if (userName != null && !userName.equals("")) {
            x = getByUserName(userName);
            if (x != null) {
                return null;
            }
        }
        x = getBySystemName(systemName);
        if (x != null) {
            return null;
        }
        // Check if system name is valid
        if (this.validSystemNameFormat(systemName) != NameValidity.VALID) {
            throw new IllegalArgumentException("SystemName " + systemName + " is not in the correct format");
        }
        // Table does not exist, create a new Type
        x = new DefaultInternalType(systemName, userName, numRows, numColumns);
        // save in the maps
        register(x);
        
        // Keep track of the last created auto system name
        updateAutoNumber(systemName);
        
        return x;
    }

    /*.*
     * {@inheritDoc}
     *./
    @Override
    public AnonymousTable newAnonymousTable(int numRows, int numColumns)
            throws IllegalArgumentException {
        
        // Check that Type does not already exist
        // Type does not exist, create a new Type
        return new DefaultAnonymousTable(numRows, numColumns);
    }
    
    /*.*
     * {@inheritDoc}
     *./
    @Override
    public Type loadTableFromCSVData(
            @Nonnull String sys, @CheckForNull String user, @Nonnull String text)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException {
        return AbstractType.loadTableFromCSV_Text(sys, user, text, true);
    }
    
    /*.*
     * {@inheritDoc}
     *./
    @Override
    public Type loadTableFromCSV(
            @Nonnull String sys, @CheckForNull String user,
            @Nonnull String fileName)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException, IOException {
        return AbstractType.loadTableFromCSV_File(sys, user, fileName, true);
    }
    
    /*.*
     * {@inheritDoc}
     *./
    @Override
    public Type loadTableFromCSV(
            @Nonnull String sys, @CheckForNull String user,
            @Nonnull File file)
            throws NamedBean.BadUserNameException, NamedBean.BadSystemNameException, IOException {
        return AbstractType.loadTableFromCSV_File(sys, user, file, true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType(String name) {
        Type x = getByUserName(name);
        if (x != null) {
            return x;
        }
        return getBySystemName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getByUserName(String name) {
        return _tuser.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getBySystemName(String name) {
        return _tsys.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBeanTypeHandled(boolean plural) {
        return Bundle.getMessage(plural ? "BeanNameTypes" : "BeanNameType");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteType(Type x) {
        // delete the Type
        deregister(x);
        x.dispose();
    }

    /** {@inheritDoc} */
    @Override
    public void printTree(PrintWriter writer, String indent) {
        printTree(Locale.getDefault(), writer, indent);
    }

    /** {@inheritDoc} */
    @Override
    public void printTree(Locale locale, PrintWriter writer, String indent) {
        writer.println("Not implemented yet");
/*        
        for (Type namedTable : getNamedBeanSet()) {
            if (namedTable instanceof DefaultCsvType) {
                DefaultCsvType csvTable = (DefaultCsvType)namedTable;
                writer.append(String.format(
                        "Named table: System name: %s, User name: %s, File name: %s, Num rows: %d, Num columns: %d",
                        csvTable.getSystemName(), csvTable.getUserName(),
                        csvTable.getFileName(), csvTable.numRows(), csvTable.numColumns()));
            } if (namedTable != null) {
                writer.append(String.format(
                        "Named table: System name: %s, User name: %s, Num rows: %d, Num columns: %d",
                        namedTable.getSystemName(), namedTable.getUserName(),
                        namedTable.numRows(), namedTable.numColumns()));
            } else {
                throw new NullPointerException("namedTable is null");
            }
            writer.println();
            writer.println();
        }
*/
    }
    
    static volatile DefaultTypeManager _instance = null;

    @InvokeOnGuiThread  // this method is not thread safe
    static public DefaultTypeManager instance() {
        if (!ThreadingUtil.isGUIThread()) {
            LoggingUtil.warnOnce(log, "instance() called on wrong thread");
        }
        
        if (_instance == null) {
            _instance = new DefaultTypeManager();
        }
        return (_instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Type> getNamedBeanClass() {
        return Type.class;
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DefaultTypeManager.class);

}
