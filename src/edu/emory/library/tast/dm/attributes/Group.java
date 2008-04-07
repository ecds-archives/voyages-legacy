package edu.emory.library.tast.dm.attributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.database.tabscommon.StatisticalAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

public class Group {

	private static final String ATTRIBUTE_GROUPS_XML = "/attribute-groups.xml";

	private static Group[] groups = null;

	private static Map groupsByIds = null;

	private String id;

	private String userLabel;

	private SearchableAttribute[] searchableAttributesAll;

	private Map searchableAttributesbyUserCategories = new HashMap();

	private VisibleAttributeInterface[] visibleAttributesAll;

	private Map visibleAttributesbyUserCategories = new HashMap();
	
	private VisibleAttributeInterface[] statisticalAttributesAll;
	
	private static Map statisticalAttributes = new HashMap();

	public Group(String id, String userLabel, SearchableAttribute[] all,
			VisibleAttributeInterface[] visible, VisibleAttributeInterface[] statistical) {
		this.id = id;
		this.userLabel = userLabel;
		this.searchableAttributesAll = all;
		if (this.searchableAttributesAll == null) {
			this.searchableAttributesAll = new SearchableAttribute[] {};
		}
		this.visibleAttributesAll = visible;
		if (this.visibleAttributesAll == null) {
			this.visibleAttributesAll = new VisibleAttributeInterface[] {};
		}
		this.statisticalAttributesAll = statistical;
		if (this.statisticalAttributesAll == null) {
			this.statisticalAttributesAll = new VisibleAttributeInterface[] {};
		}
		splitByUserCategories();
	}

	private void splitByUserCategories() {

		if (searchableAttributesbyUserCategories == null)
			searchableAttributesbyUserCategories = new HashMap();
		else
			searchableAttributesbyUserCategories.clear();

		if (visibleAttributesbyUserCategories == null)
			visibleAttributesbyUserCategories = new HashMap();
		else
			visibleAttributesbyUserCategories.clear();

		UserCategory allCategories[] = UserCategory.getAllCategories();
		List selected = new ArrayList();

		for (int i = 0; i < allCategories.length; i++) {

			UserCategory category = allCategories[i];
			selected.clear();

			for (int j = 0; j < searchableAttributesAll.length; j++) {
				SearchableAttribute attr = searchableAttributesAll[j];
				if (attr != null && attr.isInUserCategory(category))
					selected.add(attr);
			}

			SearchableAttribute[] selectedArr = new SearchableAttribute[selected
					.size()];
			selected.toArray(selectedArr);

			searchableAttributesbyUserCategories.put(category, selectedArr);

		}

		for (int i = 0; i < allCategories.length; i++) {

			UserCategory category = allCategories[i];
			selected.clear();

			for (int j = 0; j < visibleAttributesAll.length; j++) {
				VisibleAttributeInterface attr = visibleAttributesAll[j];
				if (attr != null && attr.isInUserCategory(category))
					selected.add(attr);
			}

			VisibleAttribute[] selectedArr = new VisibleAttribute[selected
					.size()];
			selected.toArray(selectedArr);

			visibleAttributesbyUserCategories.put(category, selectedArr);

		}

	}

	public String getId() {
		return id;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public SearchableAttribute[] getAllSearchableAttributes() {
		return searchableAttributesAll;
	}

	public int getNoOfAllSearchableAttributes() {
		return searchableAttributesAll.length;
	}

	public SearchableAttribute[] getSearchableAttributesInUserCategory(
			UserCategory category) {
		return (SearchableAttribute[]) searchableAttributesbyUserCategories
				.get(category);
	}

	public int getNoOfSearchableAttributesInUserCategory(UserCategory category) {
		return getSearchableAttributesInUserCategory(category).length;
	}

	public boolean hasSearchableAttributesInUserCategory(UserCategory category) {
		return getNoOfSearchableAttributesInUserCategory(category) > 0;
	}

	public VisibleAttributeInterface[] getAllVisibleAttributes() {
		return visibleAttributesAll;
	}
	
	public VisibleAttributeInterface[] getAllStatisticalAttributes() {
		return statisticalAttributesAll;
	}

	public VisibleAttributeInterface[] getVisibleAttributesInUserCategory(
			UserCategory category) {
		return (VisibleAttributeInterface[]) visibleAttributesbyUserCategories
				.get(category);
	}

	public int getNoOfVisibleAttributesInUserCategory(UserCategory category) {
		return getVisibleAttributesInUserCategory(category).length;
	}

	public boolean hasVisibleAttributesInUserCategory(UserCategory category) {
		return getNoOfVisibleAttributesInUserCategory(category) > 0;
	}

	private static void loadGroups() throws ParserConfigurationException,
			SAXException, IOException {

		// we have a DTD and we want to get rid of whitespace
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);

		// load main document
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream str = Group.class.getResourceAsStream(ATTRIBUTE_GROUPS_XML);
		Document document = builder.parse(str);

		NodeList xmlGroups = document.getDocumentElement().getChildNodes();
		groups = new Group[xmlGroups.getLength()];
		groupsByIds = new HashMap();

		// main loop over <searchable-attribute>
		for (int i = 0; i < xmlGroups.getLength(); i++) {
			Node xmlGroup = xmlGroups.item(i);

			// main properties
			String id = xmlGroup.getAttributes().getNamedItem("id")
					.getNodeValue();
			String userLabel = xmlGroup.getAttributes().getNamedItem(
					"userLabel").getNodeValue();

			// check id uniqueness
			if (groupsByIds.containsKey(id))
				throw new RuntimeException("duplicate group id '" + id + "'");

			// locate <searchable-attributes>
			NodeList xmlAttrTypes = xmlGroup.getChildNodes();
			NodeList xmlAttrs = null;
			List visibleAttrs = new ArrayList();
			List statisticalAttrs = new ArrayList();
			SearchableAttribute[] searchableAttrs = null;

			for (int k = 0; k < xmlAttrTypes.getLength(); k++) {
				Node xmlAttrType = xmlAttrTypes.item(k);
				if ("searchable-attributes".equals(xmlAttrType.getNodeName())) {
					xmlAttrs = xmlAttrType.getChildNodes();
					searchableAttrs = new SearchableAttribute[xmlAttrs
							.getLength()];
					for (int j = 0; j < xmlAttrs.getLength(); j++) {
						Node xmlAttr = xmlAttrs.item(j);
						if (xmlAttr.getNodeType() != Node.COMMENT_NODE) {
							String attrId = xmlAttr.getAttributes()
									.getNamedItem("id").getNodeValue();
							SearchableAttribute attr = (SearchableAttribute) Searchables
									.getById(attrId);
							if (attr == null)
								throw new RuntimeException(
										"group '"
												+ id
												+ "' contains a nonexistent attribute '"
												+ attrId + "'");
							searchableAttrs[j] = attr;
						}
					}
				}
				if ("table-attributes".equals(xmlAttrType.getNodeName())) {
					xmlAttrs = xmlAttrType.getChildNodes();
					for (int j = 0; j < xmlAttrs.getLength(); j++) {
						Node xmlAttr = xmlAttrs.item(j);
						if (xmlAttr.getNodeType() != Node.COMMENT_NODE && xmlAttr.getNodeName().equals("table-attribute")) {
							String attrId = xmlAttr.getAttributes()
									.getNamedItem("id").getNodeValue();
							VisibleAttributeInterface attr = VisibleAttribute
									.getAttribute(attrId);
							if (attr == null)
								throw new RuntimeException(
										"group '"
												+ id
												+ "' contains a nonexistent attribute '"
												+ attrId + "'");
							visibleAttrs.add(attr);
							statisticalAttrs.add(attr);
							statisticalAttributes.put(attr.getName(), attr);
						}
						if (xmlAttr.getNodeType() != Node.COMMENT_NODE && xmlAttr.getNodeName().equals("statistical-attribute")) {
							String attrId = xmlAttr.getAttributes().getNamedItem("id").getNodeValue();
							String function = xmlAttr.getAttributes().getNamedItem("function").getNodeValue();
							String attribute = xmlAttr.getAttributes().getNamedItem("attribute").getNodeValue();
							String label = xmlAttr.getAttributes().getNamedItem("label").getNodeValue();
							String type = xmlAttr.getAttributes().getNamedItem("type").getNodeValue();
							String[] attributes = attribute.split(",");
							
							VisibleAttributeInterface attr = new StatisticalAttribute(attrId, label, type, function, attributes);
							statisticalAttrs.add(attr);
							statisticalAttributes.put(attr.getName(), attr);
						}
					}
				}

			}
			Group group = new Group(id, userLabel, searchableAttrs,
					(VisibleAttributeInterface[])visibleAttrs.toArray(new VisibleAttributeInterface[] {}),
					(VisibleAttributeInterface[])statisticalAttrs.toArray(new VisibleAttributeInterface[] {}));

			// add it to our collection
			groups[i] = group;
			groupsByIds.put(id, group);

		}

	}

	public static Group getGroupById(String groupId) {
		if (groups == null) {
			try {
				loadGroups();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (Group) groupsByIds.get(groupId);
	}

	public static Group[] getGroups() {
		if (groups == null) {
			try {
				loadGroups();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return groups;
	}

	public String toString() {
		return this.userLabel == null ? this.id : this.userLabel;
	}
	
	public static VisibleAttributeInterface getStatisticalAttribute(String name) {
		return (VisibleAttributeInterface) statisticalAttributes.get(name);
	}

}