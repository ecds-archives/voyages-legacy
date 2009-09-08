package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class TonType extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "tonType", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "tonType"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(TonType.class, sess);
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(TonType.class, sess, orderBy);
	}
	
	public static TonType loadById(Session sess, long tonId)
	{
		return (TonType) Dictionary.loadById(TonType.class, sess, tonId);
	}

	public static TonType loadById(Session sess, String tonId)
	{
		return (TonType) Dictionary.loadById(TonType.class, sess, tonId);
	}

}