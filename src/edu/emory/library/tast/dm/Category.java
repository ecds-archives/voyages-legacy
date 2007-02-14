package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Category extends Dictionary {
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Category", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Category"));
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Category.class, sess, orderBy);
	}

	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Category.class, sess, null);
	}
	
	public static Category loadById(Session sess, long portId)
	{
		return (Category) Dictionary.loadById(Category.class, sess, portId);
	}

	public static Category loadById(Session sess, String portId)
	{
		return (Category) Dictionary.loadById(Category.class, sess, portId);
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
}
