package edu.emory.library.tast.database.listing.formatters;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

public class BooleanAttributeFormatter extends AbstractAttributeFormatter
{

	public String format(VisibleAttributeInterface attr, Object object)
	{
		if (object == null) return "";
		return ((Boolean)object).booleanValue() ? "Yes" : "No";
	}

}
