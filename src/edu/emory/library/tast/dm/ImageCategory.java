package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class ImageCategory extends Dictionary {
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "ImageCategory", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "ImageCategory"));
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(ImageCategory.class, sess, orderBy);
	}

	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(ImageCategory.class, sess, "name");
	}
	
	public static ImageCategory loadById(Session sess, long catId)
	{
		return (ImageCategory) Dictionary.loadById(ImageCategory.class, sess, catId);
	}

	public static ImageCategory loadById(Session sess, String portId)
	{
		return (ImageCategory) Dictionary.loadById(ImageCategory.class, sess, portId);
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
}
