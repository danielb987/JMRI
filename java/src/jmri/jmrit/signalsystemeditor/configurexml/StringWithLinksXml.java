/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmri.jmrit.signalsystemeditor.configurexml;

import java.util.List;

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;

import jmri.jmrit.signalsystemeditor.StringWithLinks;

/**
 * Load and store StringWithLinks
 *
 * @author Daniel Bergqvist (C) 2022
 */
public class StringWithLinksXml {

    public static StringWithLinks load(Element element) {
        StringWithLinks swl = new StringWithLinks();
        List<String> _strings = swl.getStrings();
        List<StringWithLinks.Link> _links = swl.getLinks();
        for (Content content : element.getContent()) {
            if (content.getCType() == Content.CType.Text) {
                int stringsSize = _strings.size();
                if (stringsSize > _links.size()) {
                    _strings.set(stringsSize-1, _strings.get(stringsSize-1)+content.getValue());
                } else {
                    _strings.add(content.getValue());
                }
            } else if (content.getCType() == Content.CType.Element) {
                Element e = (Element) content;
                if ("a".equals(e.getName())) {
                    if (_strings.size() <= _links.size()) {
                        _strings.add("");
                    }
                    _links.add(new StringWithLinks.Link(e.getText(), e.getAttributeValue("href")));
                } else {
                    throw new RuntimeException("Unkown tag: " + e.getName());
                }
            } else {
                throw new RuntimeException("Unkown CType: " + content.getCType().name());
            }
        }
        return swl;
    }

    public static Element store(StringWithLinks stringWithLinks, String tagName) {
        Element element = new Element(tagName);
        List<String> _strings = stringWithLinks.getStrings();
        List<StringWithLinks.Link> _links = stringWithLinks.getLinks();
        int i=0;
        while (i < _strings.size() || i < _links.size()) {
            if (i < _strings.size()) {
                element.addContent(_strings.get(i));
            }
            if (i < _links.size()) {
                Element link = new Element("a");
                link.setText(_links.get(i).getName());
                link.setAttribute("href", _links.get(i).getHRef());
                element.addContent(link);
            }
            i++;
        }
        return element;
    }

}
