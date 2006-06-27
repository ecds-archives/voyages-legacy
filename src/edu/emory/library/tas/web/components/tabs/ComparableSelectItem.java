package edu.emory.library.tas.web.components.tabs;

import javax.faces.model.SelectItem;

public class ComparableSelectItem extends SelectItem implements Comparable {

	private static final long serialVersionUID = 1L;

	public ComparableSelectItem(Object o) {
		super(o);
	}
	
	public ComparableSelectItem(Object string, String string2) {
		super(string, string2);
	}
	
	public ComparableSelectItem(Object o, String string1, String string2) {
		super(o, string1, string2);
	}
	
	public ComparableSelectItem(Object o, String string1, String string2, boolean b) {
		super(o, string1, string2, b);
	}

	public int compareTo(Object arg0) {
		if (!(arg0 instanceof ComparableSelectItem)) {
			return 0;
		}
		ComparableSelectItem that = (ComparableSelectItem)arg0;
		return this.getLabel().toLowerCase().compareTo(that.getLabel().toLowerCase());
	}

	
}
