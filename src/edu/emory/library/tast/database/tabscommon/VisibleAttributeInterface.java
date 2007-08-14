package edu.emory.library.tast.database.tabscommon;

import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.attributes.Attribute;

/**
 * Instances of this interface are used in components.
 * Provide mapping from datamodel attribute to properties presented to users in web interface.
 * Each visible attribute has user label which is visible
 * in the header of table component. It also has name, which 
 * is optional. VisibleAttributeInterface should keep list of attributes
 * which are attributes of objects presented in table. 
 *
 */
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

	public abstract Attribute getQueryAttribute();
	
	public String getFormat();

}