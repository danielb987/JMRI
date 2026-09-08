package jmri;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jmri.jmrit.XmlFile;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.jupiter.api.*;


/**
 * Test to collect all the jar files in the lib folder for NetBeans ant.
 *
 * @author Daniel Bergqvist Copyright (C) 2026
 */
public class UpdateNetBeansJars {

    @Test
    public void test() throws JmriException, JDOMException, IOException {

        Map<String, List<String>> libs = new HashMap<>();

        XmlFile xmlFile = new XmlFile();
        Element root = xmlFile.rootFromName("build.xml");
        System.out.format("Root: %s%n", root);

        for (var element : root.getChildren()) {
//            System.out.format("    Element: %s%n", element);

            if (element.getName().toLowerCase().equals("path")) {
                System.out.format("    Element: %s, %s%n", element.getName(), element.getAttribute("id").getValue());

                var list = libs.computeIfAbsent(element.getAttribute("id").getValue(), (k) -> new ArrayList<>());

//                if (1==0)
                for (var element2 : element.getChildren()) {
//                    System.out.format("        Element2: %s%n", element2.getName());

                    if (element2.getAttribute("location") != null) {
                        String location = element2.getAttribute("location").getValue();
                        location = location.replace("${libdir}/", "lib/");
//                        System.out.format("        Element2: %s, Location: %s%n", element2.getName(), location);
                        if (location.startsWith("${")) {
                            System.out.format("        Element2: %s, Location: %s%n", element2.getName(), location);
                            location = location.replace("${jartarget}", "target");
                            location = location.replace("${target}", "target/classes");
                            location = location.replace("${testtarget}", "target/test-classes");
                            System.out.format("        Element2: %s, Location: %s%n", element2.getName(), location);
//                        } else {
//                            list.add(location);
                        }
                        list.add(location);
                    } else if (element2.getAttribute("path") != null) {
                        String path = element2.getAttribute("path").getValue();
                        if (path.equals("${cp.prepend}")) {
                            System.out.format("            Ignore ${cp.prepend}%n");
                            continue;
                        }
                        if (path.equals("${cp.append}")) {
                            System.out.format("            Ignore ${cp.append}%n");
                            continue;
                        }
                        System.out.format("        Element2: %s, Path: %s%n", element2.getName(), path);
                        Pattern p = Pattern.compile("\\$\\{(.+)\\}");
                        Matcher m = p.matcher(path);
                        if (m.matches()) {
                            System.out.format("            Does match: %s%n", m.group(1));
                            var otherList = libs.get(m.group(1));
                            if (otherList == null) {
                                System.out.format("Error: %s is not found%n", m.group(1));
                                Assert.fail(String.format("path \"%s\" is not found%n", m.group(1)));
                            }
                            list.addAll(otherList);
                        } else {
                            System.out.format("            Does not match%n");
                        }
                    } else if (element2.getAttribute("refid") != null) {
                        System.out.format("        Element2: %s, Refid: %s%n", element2.getName(), element2.getAttribute("refid").getValue());
                        String refid = element2.getAttribute("refid").getValue();
                        var otherList = libs.get(refid);
                        list.addAll(otherList);
                    } else if (element2.getAttribute("dir") != null) {
                        System.out.format("        Element2: %s, Dir: %s%n", element2.getName(), element2.getAttribute("dir").getValue());
                    } else {
                        System.out.format("        Element2: %s, Unknown data%n", element2.getName());
                        for (var attr : element2.getAttributes()) {
                            System.out.format("        Element2: %s,   %s: %s%n", element2.getName(), attr.getName(), attr.getValue());
                        }
                        Assert.fail("Unknown data");
                    }
                }
            } else if (element.getName().toLowerCase().equals("pathconvert")) {
                System.out.format("    Element: %s, %s, %s%n", element.getName(), element.getAttribute("property").getValue(), element.getAttribute("refid").getValue());
                String property = element.getAttribute("property").getValue();
                String refid = element.getAttribute("refid").getValue();
                var list = libs.computeIfAbsent(property, (k) -> new ArrayList<>());
                var otherList = libs.get(refid);
                list.addAll(otherList);
            }
        }

        for (var entry : libs.entrySet()) {
            String libraries = String.join(":", entry.getValue());
            System.out.format("%s: %s%n", entry.getKey(), libraries);
        }

//        assertEquals( "RelativeAA", AnalogIO.AbsoluteOrRelative.RELATIVE.toString(),
//            "String value is Relative");
    }

    @BeforeEach
    public void setUp() {
          jmri.util.JUnitUtil.setUp();
    }

    @AfterEach
    public void tearDown() {
          jmri.util.JUnitUtil.tearDown();
    }

//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(XmlFile.class);

}
