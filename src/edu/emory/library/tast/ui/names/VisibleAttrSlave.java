package edu.emory.library.tast.ui.names;

import java.util.HashMap;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttrEstimate;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;

public class VisibleAttrSlave {
	private static HashMap visibleAttributes = new HashMap();

	static {
		VisibleAttrEstimate attr = new VisibleAttrEstimate("id", new Attribute[] {Estimate.getAttribute("id")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_id"));
		visibleAttributes.put("id", attr);
		
		attr = new VisibleAttrEstimate("voyageId", new Attribute[] {Estimate.getAttribute("voyageId")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_voyageid"));
		visibleAttributes.put("voyageId", attr);

		attr = new VisibleAttrEstimate("name", new Attribute[] {Estimate.getAttribute("name")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_name"));
		visibleAttributes.put("name", attr);
		
		attr = new VisibleAttrEstimate("shipname", new Attribute[] {Estimate.getAttribute("shipname")});
		attr.setUserLabel(TastResource.getText("components_names_attributes_shipname"));
		visibleAttributes.put("shipname", attr);
		
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
