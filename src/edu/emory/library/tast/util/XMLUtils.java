package edu.emory.library.tast.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Node;

public class XMLUtils {
	
	public static final String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public static StringBuffer appendAttribute(StringBuffer buffer, String attribute, Object value) {
		if (value == null) {
			buffer.append(attribute).append("=\"\" ");
		} else {
			buffer.append(attribute).append("=\"").append(value).append("\" ");
		}
		return buffer;
	}
	
	public static String getXMLProperty(Node node, String propertyName) {
		Node attr = node.getAttributes().getNamedItem(propertyName);
		if (attr != null && !"".equals(attr.getNodeValue())) {
			return attr.getNodeValue();
		} else {
			return null;
		}
	}
	
	public static String encodeSet(String setName, Set set) {
		Iterator iter = set.iterator();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<set ");
		appendAttribute(buffer, "name", setName);
		buffer.append("values=\"");
		boolean first = true;
		while (iter.hasNext()) {
			if (!first) {
				buffer.append(",");
			}
			first = false;
			buffer.append(iter.next());
		}
		buffer.append("\"/>\n");
		return buffer.toString();
	}
	
	public static Set restoreSetOfIntegers(Node node) {
		if (!node.getNodeName().equals("set")) {
			throw new RuntimeException("Only set tag is supported!");
		}
		String values = getXMLProperty(node, "values");
		String[] vals = values.split(",");
		Set out = new HashSet();
		for (int i = 0; i < vals.length; i++) {
			out.add(new Integer(vals[i]));
		}
		return out;
	}
	
	public static Set restoreSetOfLongs(Node node) {
		if (!node.getNodeName().equals("set")) {
			throw new RuntimeException("Only set tag is supported!");
		}
		String values = getXMLProperty(node, "values");
		String[] vals = values.split(",");
		Set out = new HashSet();
		for (int i = 0; i < vals.length; i++) {
			out.add(new Long(vals[i]));
		}
		return out;
	}
	
	public static Set restoreSetOfStrings(Node node) {
		if (!node.getNodeName().equals("set")) {
			throw new RuntimeException("Only set tag is supported!");
		}
		String values = getXMLProperty(node, "values");
		String[] vals = values.split(",");
		Set out = new HashSet();
		for (int i = 0; i < vals.length; i++) {
			out.add(vals[i]);
		}
		return out;
	}
	
	public static String encodeArray(String[] array) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<set values=\"");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				buffer.append(",");
			}
			buffer.append(array[i]);
		}
		return buffer.toString();
	}
	
	public static String[] decodeIntegerArray(String value) {
		String[] vals = value.split(",");
		return vals;
	}
}
