package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

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
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Country.class, sess);
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Country.class, sess, orderBy);
	}

	public static Country loadById(Session sess, long countryId)
	{
		return (Country) Dictionary.loadById(Country.class, sess, countryId);
	}

	public static Country loadById(Session sess, String countryId)
	{
		return (Country) Dictionary.loadById(Country.class, sess, countryId);
	}

}
