package jmri.jmrit.newlogix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import jmri.NewLogixAction;
import jmri.NewLogixExpression;
import jmri.jmrit.newlogix.swing.PluginConfiguratorInterface;

/**
 * Manages plugin classes for NewLogix.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class PluginManager {

    public enum ClassType {
        EXPRESSION,
        EXPRESSION_NOT_PLUGIN,
        ACTION,
        ACTION_NOT_PLUGIN,
        CONFIGURATOR,
        OTHER
    }
    
    public static class ClassDefinition {
        private boolean _enabled;
        private final ClassType _type;
        private final String _name;
        
        public ClassDefinition(boolean enabled, ClassType type, String name) {
            _enabled = enabled;
            _type = type;
            _name = name;
        }
        
        public boolean getEnabled() {
            return _enabled;
        }
        
        public void setEnabled(boolean enabled) {
            _enabled = enabled;
        }
        
        public ClassType getType() {
            return _type;
        }
        
        public String getName() {
            return _name;
        }
        
    }
    
    public class JarFile {
        private final String _filename;
        private final List<String> classNameList = new ArrayList<>();
        private final List<ClassDefinition> classList = new ArrayList<>();
        
        public JarFile(String name) {
            _filename = name;
            
            try {
                loadJarFile();
            } catch (Exception e) {
                // This needs to be handled in a better way.
                e.printStackTrace();
            }
        }
        
        private void loadClassesInJarFile() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            File file = new File(_filename);

            // Convert the file to the URL format
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};

            // ------ Load this folder into Class loader - Remove comment?

            // Load this jar file into Class loader
            try (URLClassLoader cl = new URLClassLoader(urls)) {
                for (String className : classNameList) {
                    // Load the class se.bergqvist.jmri_newlogix_plugin.ExpressionXor
                    Class cls = cl.loadClass(className);
                    
//                    System.out.format("%s extends %s%n", cls.getName(), cls.getSuperclass());
//                    System.out.format("AA: %b%n", cls.isAssignableFrom(NewLogixAction.class));
//                    System.out.format("BB: %b%n", NewLogixAction.class.isAssignableFrom(cls));
//                    System.out.format("EE: %b%n", NewLogixExpression.class.isAssignableFrom(cls));
//                    System.out.format("CCC: %b%n", cls.newInstance() instanceof NewLogixAction);
                    
//                    for (Class<?> temp : cls.getInterfaces()) {
//                        System.out.format("%s implements %s%n", cls.getName(), temp.getName());
//                    }

//                    if (cls.newInstance() instanceof NewLogixExpression) {
//                        System.out.format("AAA: Class %s is an Expression%n", cls.getName());
//                    } else if (cls.isInstance(NewLogixExpression.class)) {
//                    if (cls.isInstance(NewLogixExpression.class)) {
//                    ClassType type = ClassType.OTHER;
                    ClassType type;
                    if (ExpressionPluginInterface.class.isAssignableFrom(cls)) {
                        System.out.format("Class %s is an Expression%n", cls.getName());
                        type = ClassType.EXPRESSION;
//                    } else if (cls.isInstance(NewLogixAction.class)) {
                    } else if (ActionPluginInterface.class.isAssignableFrom(cls)) {
                        System.out.format("Class %s is an Action%n", cls.getName());
                        type = ClassType.ACTION;
                    } else if (PluginConfiguratorInterface.class.isAssignableFrom(cls)) {
                        System.out.format("Class %s is a plugin configurator%n", cls.getName());
                        type = ClassType.CONFIGURATOR;
                    } else if (NewLogixExpression.class.isAssignableFrom(cls)) {
                        System.out.format("Class %s is an Expression but not a plugin%n", cls.getName());
                        type = ClassType.EXPRESSION_NOT_PLUGIN;
//                    } else if (cls.isInstance(NewLogixAction.class)) {
                    } else if (NewLogixAction.class.isAssignableFrom(cls)) {
                        System.out.format("Class %s is an Action but not a plugin%n", cls.getName());
                        type = ClassType.ACTION_NOT_PLUGIN;
                    } else {
                        System.out.format("Class %s is an unknown class%n", cls.getName());
                        type = ClassType.OTHER;
                    }
                    
                    classList.add(new ClassDefinition(false, type, className));
                }
/*
                // Load the class se.bergqvist.jmri_newlogix_plugin.ExpressionXor
                Class cls = cl.loadClass("se.bergqvist.jmri_newlogix_plugin.ExpressionXor");

                // Print the location from where this class was loaded
                ProtectionDomain pDomain = cls.getProtectionDomain();
                CodeSource cSource = pDomain.getCodeSource();
                URL urlfrom = cSource.getLocation();
                System.out.format("Class from: %s%n", urlfrom.getFile());

//                cls.newInstance();
*/
                cl.close();
            }
        }
        
        private void loadJarFile() throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//            String jarFileName = "F:\\Projekt\\Java\\GitHub\\JMRI_NewLogixPlugins\\dist\\JMRI_NewLogixPlugins.jar";

//            List<String> classList = new ArrayList<>();
            try (JarInputStream jarFile = new JarInputStream(new FileInputStream(_filename))) {
                JarEntry jarEntry;

                while (true) {
                    jarEntry = jarFile.getNextJarEntry();
                    if (jarEntry == null) {
                        break;
                    }
                    if ((jarEntry.getName().endsWith(".class"))) {
                        String className = jarEntry.getName().replaceAll("/", "\\.");
                        String myClass = className.substring(0, className.lastIndexOf('.'));
                        classNameList.add(myClass);
                        classList.add(new ClassDefinition(false, ClassType.EXPRESSION, myClass));
                    }
                }
            }
            
            loadClassesInJarFile();
        }
        
        public void setEnabled(boolean enabled) {
            
        }
    }
    
//    private final NewLogixPreferences _preferences;
    private final List<JarFile> jarFileList = new ArrayList<>();
    
    public PluginManager() {
    }
    
//    public PluginManager(NewLogixPreferences preferences) {
//        _preferences = preferences;
//    }
    
    /**
     * Init the plugin manager.
     * This needs to be after the preferences has been read.
     * 
     * @param filename the filename of the jar file
     * @return the JarFile object
     */
    public JarFile addJarFile(String filename) {
        JarFile jarFile = new JarFile(filename);
        jarFileList.add(jarFile);
        return jarFile;
    }
}
