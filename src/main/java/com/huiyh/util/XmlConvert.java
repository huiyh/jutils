package com.huiyh.util;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alany on 2018/7/10.
 */
public class XmlConvert {

    public static Map<String, Object> xmlToMap(String xmlStr) throws JDOMException, IOException {
        if (StringUtils.isEmpty(xmlStr)) {
            return null;
        }
        Element rootElement = parseXml(xmlStr);

        HashMap<String, Object> map = new HashMap<>();
        addToParent(map, rootElement.getName(), parseElement(rootElement));
        return map;
    }

    static void addToParent(Map<String, Object> parent, String name, Object child) {
        Object value = parent.get(name);
        if (value == null) {
            parent.put(name, child);
            return;
        }

        if (value instanceof List) {
            ((List) value).add(child);
            return;
        }

        ArrayList<Object> list = new ArrayList<>();
        list.add(value);
        list.add(child);
        parent.put(name, list);
    }

    static Object parseElement(Element element) {
        HashMap<String, Object> map = new HashMap<>();
        List<Attribute> attributes = element.getAttributes();
        for (Attribute attribute : attributes) {
            addToParent(map, attribute.getName(), attribute.getValue());
        }

        List<Element> children = element.getChildren();
        for (Element child : children) {
            addToParent(map, child.getName(), parseElement(child));
        }

        if (map.isEmpty()) {
            return element.getValue();
        }
        return map;
    }

    public static JSONObject xmlToJson(String xmlStr) throws JDOMException, IOException {
        if (StringUtils.isEmpty(xmlStr)) {
            return null;
        }
        Element rootElement = parseXml(xmlStr);

        JSONObject jsonObject = new JSONObject();
        addToParent(jsonObject, rootElement.getName(), parseElement2Json(rootElement));
        return jsonObject;
    }


    static void addToParent(JSONObject parent, String name, Object child) {
        Object value = parent.opt(name);
        if (value == null) {
            parent.put(name, child);
            return;
        }

        if (value instanceof JSONArray) {
            ((JSONArray) value).put(child);
            return;
        }

        JSONArray list = new JSONArray();
        list.put(value);
        list.put(child);
        parent.put(name, list);
    }

    static JSONObject parseElement2Json(Element element) {
        JSONObject jsonObject = new JSONObject();
        List<Attribute> attributes = element.getAttributes();
        for (Attribute attribute : attributes) {
            addToParent(jsonObject, attribute.getName(), attribute.getValue());
        }

        List<Element> children = element.getChildren();
        for (Element child : children) {
            addToParent(jsonObject, child.getName(), parseElement2Json(child));
        }
        return jsonObject;
    }


    private static Element parseXml(String xmlStr) throws JDOMException, IOException {
        xmlStr = xmlStr.replaceAll("\\\n", "");
        byte[] xml = xmlStr.getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(is);
        return document.getRootElement();
    }

}