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

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;

public class VisibleAttrEstimate implements VisibleAttributeInterface {

	private static HashMap visibleAttributes = new HashMap();

	static {
		VisibleAttrEstimate attr = new VisibleAttrEstimate("nation", 
				new Attribute[] {new SequenceAttribute(
						new Attribute[] {Estimate.getAttribute("nation"), Nation.getAttribute("name")})});
		attr.setUserLabel(TastResource.getText("components_estimate_nationcarrierattr"));
		visibleAttributes.put("nation", attr);
		
		attr = new VisibleAttrEstimate("year", new Attribute[] {Estimate.getAttribute("year")});
		attr.setUserLabel(TastResource.getText("components_estimate_yearattr"));
		visibleAttributes.put("year", attr);
		
		attr = new VisibleAttrEstimate("impRegion", 
				new Attribute[] {new SequenceAttribute(
						new Attribute[] {Estimate.getAttribute("impRegion"), Region.getAttribute("name")})});
		attr.setUserLabel(TastResource.getText("components_estimate_impregionattr"));
		visibleAttributes.put("impRegion", attr);
		
		attr = new VisibleAttrEstimate("expRegion", 
				new Attribute[] {new SequenceAttribute(
						new Attribute[] {Estimate.getAttribute("expRegion"), Region.getAttribute("name")})});
		attr.setUserLabel(TastResource.getText("components_estimate_expregionattr"));
		visibleAttributes.put("expRegion", attr);
		
		attr = new VisibleAttrEstimate("slavExported", new Attribute[] {Estimate.getAttribute("slavExported")});
		attr.setUserLabel(TastResource.getText("components_estimate_exportedattr"));
		visibleAttributes.put("slavExported", attr);
		
		attr = new VisibleAttrEstimate("slavImported", new Attribute[] {Estimate.getAttribute("slavImported")});
		attr.setUserLabel(TastResource.getText("components_estimate_importedattr"));
		visibleAttributes.put("slavImported", attr);
	}
	
	private Attribute[] attributes;

	private String name;

	private UserCategory userCategory;

	private String userLabel;

//	public static VisibleAttributeInterface[] loadVisibleAttributes(int tabType) {
//		if (visibleAttributes.isEmpty()) {
//			loadConfig();
//		}
//		return (VisibleAttributeInterface[]) (((Map) visibleAttributes.get(new Integer(
//				tabType)))).values().toArray(new VisibleAttributeInterface[] {});
//	}

//	private static void loadConfig() {
//		try {
//			Document document = new XMLConfiguration("table-attributes.xml")
//					.getDocument();
//			Node mainNode = document.getFirstChild();
//			if (mainNode != null) {
//				if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
//					NodeList attrs = mainNode.getChildNodes();
//					for (int j = 0; j < attrs.getLength(); j++) {
//						if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
//							VisibleAttributeInterface attr = VisibleAttribute
//									.fromXML(attrs.item(j));
//							for (int i = 0; i < attr.getValidity().length; i++) {
//								Integer val = new Integer(attr.getValidity()[i]);
//								Map attrMap = (Map) visibleAttributes.get(val);
//								if (attrMap == null) {
//									attrMap = new HashMap();
//									visibleAttributes.put(val, attrMap);
//								}
//								attrMap.put(attr.getName(), attr);
//							}
//						}
//					}
//				}
//			}
//		} catch (ConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static VisibleAttributeInterface[] getAllAttributes() {
		return (VisibleAttributeInterface[])visibleAttributes.values().toArray(new VisibleAttributeInterface[] {});
	}

//	private static VisibleAttributeInterface fromXML(Node node) {
//
//		String name = node.getAttributes().getNamedItem("id").getNodeValue();
//		String validity = node.getAttributes().getNamedItem("tabs")
//				.getNodeValue();
//		String category = node.getAttributes().getNamedItem("userCategory")
//				.getNodeValue();
//		String userLabel = node.getAttributes().getNamedItem("userLabel")
//				.getNodeValue();
//		String[] tabsStr = validity.split(",");
//		int[] tabs = new int[tabsStr.length];
//		for (int i = 0; i < tabsStr.length; i++) {
//			String string = tabsStr[i];
//			tabs[i] = Integer.parseInt(string);
//		}
//
//		List attributesList = new ArrayList();
//		NodeList attrsList = node.getChildNodes();
//		for (int j = 0; j < attrsList.getLength(); j++) {
//			Node attrsListChild = attrsList.item(j);
//			if (attrsListChild != null) {
//				NodeList attrs = attrsListChild.getChildNodes();
//				for (int i = 0; i < attrs.getLength(); i++) {
//					Node attr = attrs.item(i);
//					if (attr.getNodeType() == Node.ELEMENT_NODE) {
//						String attrName = attr.getAttributes().getNamedItem(
//								"name").getNodeValue();
//						attributesList.add(Voyage.getAttribute(attrName));
//					}
//				}
//			}
//		}
//		VisibleAttribute attr = new VisibleAttribute(name, tabs,
//				(Attribute[]) attributesList.toArray(new Attribute[] {}));
//		attr.setUserCategory(UserCategory.parse(category));
//		attr.setUserLabel(userLabel);
//		return attr;
//	}

	public VisibleAttrEstimate(String name, Attribute[] attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getAttributes()
	 */
	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getUserCategory()
	 */
	public UserCategory getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(UserCategory category) {
		this.userCategory = category;
	}

	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getUserLabel()
	 */
	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}


	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getName()
	 */
	public String getName() {
		return name;
	}

	public String toString() {
		return this.getUserLabelOrName();
	}
	
	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getUserLabelOrName()
	 */
	public String getUserLabelOrName() {
		return (this.userLabel == null || "".equals(this.userLabel)) ? this.name
				: this.userLabel;
	}

	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#encodeToString()
	 */
	public String encodeToString() {
		return "Attribute_" + this.getName();
	}

	/* (non-Javadoc)
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getType()
	 */
	public String getType() {
		if (this.attributes[0] instanceof DateAttribute) {
			return "DateAttribute";
		} else if (this.attributes[0] instanceof StringAttribute) {
			return "StringAttribute";
		} else if (this.attributes[0] instanceof DictionaryAttribute) {
			return "DictionaryAttribute";
		} else if (this.attributes[0] instanceof NumericAttribute) {
			return "NumericAttribute";
		} else {
			return "Unknown";
			//throw new RuntimeException("Not supported attribute type! " + this.attributes[0]);
		}
	}

	public static VisibleAttributeInterface getAttributeForTable(String string) {
		return (VisibleAttributeInterface) visibleAttributes.get(string);
	}
	
	public boolean isDate() {
		return true;
	}

//	public static VisibleAttributeInterface getAttribute(String id) {
////		if (visibleAttributes.isEmpty()) {
////			loadConfig();
////		}
//		Iterator iter = visibleAttributes.values().iterator();
//		while (iter.hasNext()) {
//			Map attrMap = (Map) iter.next();
//			Iterator attrMapIter = attrMap.values().iterator();
//			while (attrMapIter.hasNext()) {
//				VisibleAttributeInterface attr = (VisibleAttributeInterface) attrMapIter.next();
//				if (attr.getName().equals(id)) {
//					return attr;
//				}
//			}
//		}
//		return null;
//	}

	public boolean isInUserCategory(UserCategory category) {
		// TODO Auto-generated method stub
		return true;
	}

}
