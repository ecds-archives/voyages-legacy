package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Country extends Dictionary {

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Country", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Country"));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}

}
