package edu.emory.library.tast.database.tabscommon;

import java.util.HashMap;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class VisibleAttrEstimate implements VisibleAttributeInterface {

	private static HashMap visibleAttributes = new HashMap();

	static {
		VisibleAttrEstimate attr = new VisibleAttrEstimate("nation", 
				new Attribute[] {new SequenceAttribute(
						new Attribute[] {Estimate.getAttribute("nation"), EstimatesNation.getAttribute("name")})});
		attr.setUserLabel(TastResource.getText("components_estimate_nationcarrierattr"));
		visibleAttributes.put("nation", attr);
		
		attr = new VisibleAttrEstimate("year", new Attribute[] {Estimate.getAttribute("year")});
		attr.setUserLabel(TastResource.getText("components_estimate_yearattr"));
		visibleAttributes.put("year", attr);
		
		attr = new VisibleAttrEstimate("impRegion", 
				new Attribute[] {new SequenceAttribute(
						new Attribute[] {Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("name")})});
		attr.setUserLabel(TastResource.getText("components_estimate_impregionattr"));
		visibleAttributes.put("impRegion", attr);
		
		attr = new VisibleAttrEstimate("expRegion", 
				new Attribute[] {new SequenceAttribute(
						new Attribute[] {Estimate.getAttribute("expRegion"), EstimatesExportRegion.getAttribute("name")})});
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
	
	public static VisibleAttributeInterface[] getAllAttributes() {
		return (VisibleAttributeInterface[])visibleAttributes.values().toArray(new VisibleAttributeInterface[] {});
	}

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
