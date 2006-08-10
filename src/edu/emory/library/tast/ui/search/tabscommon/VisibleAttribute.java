package edu.emory.library.tast.ui.search.tabscommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.dm.attributes.Attribute;


public class VisibleAttribute {
	
	public static final int ATTRIBUTE_TABLE_TAB = 1;
	public static final int ATTRIBUTE_CHART_TAB = 2;
	
	private static HashMap visibleAttributes = new HashMap(); 
	
	private Attribute[] attributes;
	
	public static VisibleAttribute[] loadVisibleAttributes(int tabType) {
		if (visibleAttributes.isEmpty()) {
			try {
				Document document = new XMLConfiguration("table-attributes.xml").getDocument(); 
				Node mainNode = document.getFirstChild();
				if (mainNode != null) {
					NodeList objects = mainNode.getChildNodes();
					for (int i = 0; i < objects.getLength(); i++) {
						Node node = objects.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							NodeList attrs = node.getChildNodes();
							List attrsList = new ArrayList();
							for (int j = 0; j < attrs.getLength(); j++) {
								if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
									attrsList.add(Attribute.fromXML(attrs.item(j)));
								}
							}
						}
					}
				}
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public VisibleAttribute(Attribute[] attributes) {
		this.attributes = attributes;
	}
	
	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	
}
