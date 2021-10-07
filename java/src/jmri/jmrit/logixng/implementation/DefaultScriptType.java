package jmri.jmrit.logixng.implementation;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jmri.JmriException;
import jmri.NamedBean.BadUserNameException;
import jmri.NamedBean.BadSystemNameException;
import jmri.Reference;
import jmri.script.JmriScriptEngineManager;

/**
 * The default implementation of a Script Type.
 * 
 * @author Daniel Bergqvist 2021
 */
public class DefaultScriptType extends AbstractType {

    private String _fileName;
    private String _script;
    
    /**
     * Create a new type.
     * @param sys the system name
     * @param user the user name or null if no user name
     * @param className the class name
     * @param fileName the jython script file name
     * @param script the jython script
     */
    public DefaultScriptType(
            @Nonnull String sys, @CheckForNull String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    public String getFileName() {
        return _fileName;
    }
    
    public void setFileName(String fileName) throws JmriException {
        this._fileName = fileName;
    }

    public String getScript() {
        return _script;
    }
    
    public void setScript(String script) throws JmriException {
        this._script = script;
    }
    
    @Override
    public TypeEnum getTypeEnum() {
        return TypeEnum.Script;
    }
    
    @Override
    public Object getNewInstance() throws JmriException {
        JmriScriptEngineManager scriptEngineManager = jmri.script.JmriScriptEngineManager.getDefault();
        
        Bindings bindings = new SimpleBindings();
        
        Reference<Class<?>> classRef = new Reference<>();
        bindings.put("theClass", classRef);
        
        if ((_fileName == null) && ((_script == null) || _script.isEmpty())) {
            throw new JmriException("No script is given");
        }
        if (_fileName != null) {
            try (InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(jmri.util.FileUtil.getExternalFilename(_fileName)),
                    StandardCharsets.UTF_8)) {
                scriptEngineManager.getEngineByName(JmriScriptEngineManager.PYTHON)
                        .eval(reader);
            } catch (IOException | ScriptException e) {
                throw new JmriException("Cannot execute script", e);
            }
        } else {
            try {
                String theScript = String.format("import jmri%n") + _script;
                scriptEngineManager.getEngineByName(JmriScriptEngineManager.PYTHON)
                        .eval(theScript, bindings);
            } catch (ScriptException e) {
                throw new JmriException("Cannot execute script", e);
            }
        }
        return classRef.get();
    }
    
}
