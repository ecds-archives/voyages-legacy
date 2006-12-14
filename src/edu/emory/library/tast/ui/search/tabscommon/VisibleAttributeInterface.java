package edu.emory.library.tast.ui.search.tabscommon;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;

public interface VisibleAttributeInterface {

	public static final String DATE_ATTRIBUTE = "DateAttribute";

	public static final String DICTIONARY_ATTRIBUTE = "DictionaryAttribute";

	public static final String STRING_ATTRIBUTE = "StringAttribute";

	public static final String NUMERIC_ATTRIBUTE = "NumericAttribute";
	
	public static final String BOOLEAN_ATTRIBUTE = "BooleanAttribute";
	
	public abstract Attribute[] getAttributes();

	public abstract String getUserLabel();

	public abstract String getName();

	public abstract String getUserLabelOrName();

	public abstract String encodeToString();

	public abstract String getType();
	
	public boolean isDate();

	public abstract boolean isInUserCategory(UserCategory category);

}