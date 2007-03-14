package edu.emory.library.tast.slaves;

import java.util.HashMap;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.database.tabscommon.VisibleAttrEstimate;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class VisibleAttrSlave {
	private static HashMap visibleAttributes = new HashMap();

	static {
		VisibleAttrEstimate attr = new VisibleAttrEstimate("id", new Attribute[] {Slave.getAttribute("id")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_id"));
		visibleAttributes.put("id", attr);
		
		attr = new VisibleAttrEstimate("voyageId", new Attribute[] {Slave.getAttribute("voyageId")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_voyageid"));
		visibleAttributes.put("voyageId", attr);

		attr = new VisibleAttrEstimate("name", new Attribute[] {Slave.getAttribute("name")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_name"));
		visibleAttributes.put("name", attr);
		
		attr = new VisibleAttrEstimate("shipname", new Attribute[] {Slave.getAttribute("shipname")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_shipname"));
		visibleAttributes.put("shipname", attr);
		
		attr = new VisibleAttrEstimate("age", new Attribute[] {Slave.getAttribute("age")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_age"));
		visibleAttributes.put("age", attr);
		
		attr = new VisibleAttrEstimate("height", new Attribute[] {Slave.getAttribute("height")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_height"));
		visibleAttributes.put("height", attr);
		
		attr = new VisibleAttrEstimate("datearr", new Attribute[] {Slave.getAttribute("datearr")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_datearr"));
		visibleAttributes.put("datearr", attr);
		
		attr = new VisibleAttrEstimate("source", new Attribute[] {Slave.getAttribute("source")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_source"));
		visibleAttributes.put("source", attr);
		
		attr = new VisibleAttrEstimate("country", new Attribute[] {Slave.getAttribute("country")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_country"));
		visibleAttributes.put("country", attr);

		attr = new VisibleAttrEstimate("sexage", new Attribute[] {Slave.getAttribute("sexage")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_sexage"));
		visibleAttributes.put("sexage", attr);
		
		attr = new VisibleAttrEstimate("majselpt", new Attribute[] {Slave.getAttribute("majselpt")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_majselpt"));
		visibleAttributes.put("majselpt", attr);
		
		attr = new VisibleAttrEstimate("majbuypt", new Attribute[] {Slave.getAttribute("majbuypt")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_majbuypt"));
		visibleAttributes.put("majbuypt", attr);
		
	}
	
	private Attribute[] attributes;

	private String name;

	private UserCategory userCategory;

	private String userLabel;
	
	public static VisibleAttributeInterface[] getAllAttributes() {
		return (VisibleAttributeInterface[])visibleAttributes.values().toArray(new VisibleAttributeInterface[] {});
	}

	public VisibleAttrSlave(String name, Attribute[] attributes) {
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
		}
	}

	public static VisibleAttributeInterface getAttributeForTable(String string) {
		return (VisibleAttributeInterface) visibleAttributes.get(string);
	}
	
	public boolean isDate() {
		return true;
	}

	public boolean isInUserCategory(UserCategory category) {
		return true;
	}
}
