package edu.emory.library.tast.ui.search.tabscommon;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;

public interface VisibleAttributeInterface {

	public abstract Attribute[] getAttributes();

	public abstract UserCategory getUserCategory();

	public abstract String getUserLabel();

	public abstract String getName();

	public abstract String getUserLabelOrName();

	public abstract String encodeToString();

	public abstract String getType();

}