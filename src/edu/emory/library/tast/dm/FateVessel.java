package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class FateVessel extends Dictionary
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
		return Dictionary.loadAll(FateVessel.class, sess);
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(FateVessel.class, sess, orderBy);
	}
	
	public static FateVessel loadById(Session sess, long rigId)
	{
		return (FateVessel) Dictionary.loadById(FateVessel.class, sess, rigId);
	}

}