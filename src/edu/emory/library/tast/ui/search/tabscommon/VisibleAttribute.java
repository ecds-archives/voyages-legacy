package edu.emory.library.tast.ui.search.tabscommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;

public class VisibleAttribute {

	public static final int ATTRIBUTE_TABLE_TAB = 1;

	public static final int ATTRIBUTE_CHART_TAB = 2;

	private static HashMap visibleAttributes = new HashMap();

	private Attribute[] attributes;

	private String name;

	private UserCategory category;

	private String userLabel;

	private int[] validity;

	public static VisibleAttribute[] loadVisibleAttributes(int tabType) {
		if (visibleAttributes.isEmpty()) {
			loadConfig();
		}
		return (VisibleAttribute[]) (((Map) visibleAttributes.get(new Integer(
				tabType)))).values().toArray(new VisibleAttribute[] {});
	}

	private static void loadConfig() {
		try {
			Document document = new XMLConfiguration("table-attributes.xml")
					.getDocument();
			Node mainNode = document.getFirstChild();
			if (mainNode != null) {
				if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList attrs = mainNode.getChildNodes();
					for (int j = 0; j < attrs.getLength(); j++) {
						if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
							VisibleAttribute attr = VisibleAttribute
									.fromXML(attrs.item(j));
							for (int i = 0; i < attr.getValidity().length; i++) {
								Integer val = new Integer(attr.getValidity()[i]);
								Map attrMap = (Map) visibleAttributes.get(val);
								if (attrMap == null) {
									attrMap = new HashMap();
									visibleAttributes.put(val, attrMap);
								}
								attrMap.put(attr.getName(), attr);
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

	private static VisibleAttribute fromXML(Node node) {

		String name = node.getAttributes().getNamedItem("id").getNodeValue();
		String validity = node.getAttributes().getNamedItem("tabs")
				.getNodeValue();
		String category = node.getAttributes().getNamedItem("userCategory")
				.getNodeValue();
		String userLabel = node.getAttributes().getNamedItem("userLabel")
				.getNodeValue();
		String[] tabsStr = validity.split(",");
		int[] tabs = new int[tabsStr.length];
		for (int i = 0; i < tabsStr.length; i++) {
			String string = tabsStr[i];
			tabs[i] = Integer.parseInt(string);
		}

		List attributesList = new ArrayList();
		NodeList attrsList = node.getChildNodes();
		for (int j = 0; j < attrsList.getLength(); j++) {
			Node attrsListChild = attrsList.item(j);
			if (attrsListChild != null) {
				NodeList attrs = attrsListChild.getChildNodes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					if (attr.getNodeType() == Node.ELEMENT_NODE) {
						String attrName = attr.getAttributes().getNamedItem(
								"name").getNodeValue();
						attributesList.add(Voyage.getAttribute(attrName));
					}
				}
			}
		}
		VisibleAttribute attr = new VisibleAttribute(name, tabs,
				(Attribute[]) attributesList.toArray(new Attribute[] {}));
		attr.setCategory(UserCategory.parse(category));
		attr.setUserLabel(userLabel);
		return attr;
	}

	public VisibleAttribute(String name, int[] validity, Attribute[] attributes) {
		this.validity = validity;
		this.name = name;
		this.attributes = attributes;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public UserCategory getCategory() {
		return category;
	}

	public void setCategory(UserCategory category) {
		this.category = category;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public int[] getValidity() {
		return validity;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getUserLabelOrName();
	}
	
	public String getUserLabelOrName() {
		return (this.userLabel == null || "".equals(this.userLabel)) ? this.name
				: this.userLabel;
	}

	public String encodeToString() {
		return "Attribute_" + this.getName();
	}

	public Integer getType() {
		return this.attributes[0].getType();
	}

	public static VisibleAttribute getAttributeForTable(String string) {
		if (visibleAttributes.isEmpty()) {
			loadConfig();
		}
		HashMap map = (HashMap) visibleAttributes.get(new Integer(
				VisibleAttribute.ATTRIBUTE_TABLE_TAB));
		return (VisibleAttribute) map.get(string);
	}

	public static VisibleAttribute getAttribute(String id) {
		Iterator iter = visibleAttributes.values().iterator();
		while (iter.hasNext()) {
			Map attrMap = (Map) iter.next();
			Iterator attrMapIter = attrMap.values().iterator();
			while (attrMapIter.hasNext()) {
				VisibleAttribute attr = (VisibleAttribute) attrMapIter.next();
				if (attr.getName().equals(id)) {
					return attr;
				}
			}
		}
		return null;
	}

}
