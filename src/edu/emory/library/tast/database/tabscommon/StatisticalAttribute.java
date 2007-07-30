package edu.emory.library.tast.database.tabscommon;

import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;

public class StatisticalAttribute implements VisibleAttributeInterface {

	private String id;
	private String label;
	private String type;
	private String function;
	private Attribute[] attributes;
	
	public StatisticalAttribute(String id, String label, String type, String function, String[] attributes) {
		this.id = id;
		this.label = label;
		this.type = type;
		this.function = function;
		this.attributes = new Attribute[attributes.length];
		for (int i = 0; i < attributes.length; i++) {
			if (isConst(attributes[i])) {
				this.attributes[i] = new DirectValueAttribute(attributes[i].replaceAll("'", ""));
			} else {
				this.attributes[i] = Voyage.getAttribute(attributes[i]);
			}
		}
	}
	
	private boolean isConst(String attr) {
		if (attr.startsWith("'") && attr.endsWith("'")) {
			return true;
		}
		return attr.matches("[\\d]*\\.?[\\d]*");
	}
	
	public String encodeToString() {
		return "Statistical attribute: " + this.id;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public String getName() {
		return id;
	}

	public String getType() {
		return this.type;
	}

	public String getUserLabel() {
		return label;
	}

	public String getUserLabelOrName() {
		return this.label != null ? this.label : this.id;
	}

	public boolean isDate() {
		return false;
	}

	public boolean isInUserCategory(UserCategory category) {
		return false;
	}

	public String getFunction() {
		return function;
	}

	public Attribute getQueryAttribute() {
		return new FunctionAttribute(this.function, this.attributes);
	}

	public String getFormat() {
		return null;
	}

}
