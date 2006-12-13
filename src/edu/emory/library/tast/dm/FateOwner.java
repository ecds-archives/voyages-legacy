package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class FateOwner extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Fate", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Fate"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(FateOwner.class, sess);
	}
	
	public static FateOwner loadById(Session sess, long rigId)
	{
		return (FateOwner) Dictionary.loadById(FateOwner.class, sess, rigId);
	}

}