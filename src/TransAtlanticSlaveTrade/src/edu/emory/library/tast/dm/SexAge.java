package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class SexAge extends Dictionary {
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "SexAge", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "SexAge"));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}

	public static SexAge loadById(Session session, int id) {
		return (SexAge)loadById(SexAge.class, session, id);
	}
}
