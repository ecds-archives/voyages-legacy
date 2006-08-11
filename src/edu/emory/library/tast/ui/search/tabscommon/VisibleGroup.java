package edu.emory.library.tast.ui.search.tabscommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.ui.search.query.searchables.UserCategory;

public class VisibleGroup {

	private static HashMap visibleGroups = new HashMap();

	private String name;

	private VisibleAttribute[] visibleAttributes;

	private String userLabel;

	public static VisibleGroup[] loadAllGroups() {
		if (visibleGroups.isEmpty()) {
			readConfigFile();
		}
		return (VisibleGroup[]) visibleGroups.values().toArray(
				new VisibleGroup[] {});
	}

	public static VisibleGroup loadVisibleGroup(String groupName) {
		if (visibleGroups.isEmpty()) {
			readConfigFile();
		}
		return (VisibleGroup) visibleGroups.get(groupName);
	}

	private static void readConfigFile() {
		try {
			Document document = new XMLConfiguration("attribute-groups.xml")
					.getDocument();
			NodeList mainNodes = document.getChildNodes();
			for (int m = 0; m < mainNodes.getLength(); m++) {
				Node mainNode = mainNodes.item(m);
				if (mainNode.getNodeName().equals("groups")
						&& mainNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList groups = mainNode.getChildNodes();
					for (int j = 0; j < groups.getLength(); j++) {
						if (groups.item(j).getNodeType() == Node.ELEMENT_NODE) {
							resolveAttributes(groups, j);
						}
					}
				}
			}
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void resolveAttributes(NodeList groups, int j) {
		Node group = groups.item(j);
		String groupName = group.getAttributes()
				.getNamedItem("id").getNodeValue();
		String userLabel = group.getAttributes()
				.getNamedItem("userLabel").getNodeValue();
		List visibleAttributes = new ArrayList();

		NodeList groupItems = group.getChildNodes();
		for (int k = 0; k < groupItems.getLength(); k++) {
			Node attrsNode = groupItems.item(k);
			if (attrsNode.getNodeType() == Node.ELEMENT_NODE
					&& attrsNode.getNodeName().equals(
							"table-attributes")) {
				NodeList attrs = attrsNode.getChildNodes();
				for (int l = 0; l < attrs.getLength(); l++) {
					Node attr = attrs.item(l);
					if (attr.getNodeType() == Node.ELEMENT_NODE) {
						String id = attr.getAttributes()
								.getNamedItem("id")
								.getNodeValue();
						VisibleAttribute vAttr = VisibleAttribute
								.getAttribute(id);
						visibleAttributes.add(vAttr);
					}
				}
			}
		}

		visibleGroups.put(groupName, new VisibleGroup(
								groupName,
								userLabel,
								(VisibleAttribute[]) visibleAttributes
										.toArray(new VisibleAttribute[] {})));
	}

	public VisibleGroup(String name, String userLabel, VisibleAttribute[] attrs) {
		this.name = name;
		this.visibleAttributes = attrs;
		this.userLabel = userLabel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VisibleAttribute[] getVisibleAttributes() {
		return visibleAttributes;
	}

	public void setVisibleAttributes(VisibleAttribute[] visibleAttributes) {
		this.visibleAttributes = visibleAttributes;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public int noOfAttributesInCategory(UserCategory category) {
		int num = 0;
		for (int i = 0; i < this.visibleAttributes.length; i++) {
			if (category == this.visibleAttributes[i].getCategory()) {
				num++;
			}
		}
		return num;
	}
	
	public String toString() {
		return this.userLabel;
	}

}
