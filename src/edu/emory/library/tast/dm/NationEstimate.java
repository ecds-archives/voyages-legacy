package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class NationEstimate extends Dictionary {
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Nation", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Nation"));
		attributes.put("order", new NumericAttribute("order", "Nation", NumericAttribute.TYPE_INTEGER));
	}
}
