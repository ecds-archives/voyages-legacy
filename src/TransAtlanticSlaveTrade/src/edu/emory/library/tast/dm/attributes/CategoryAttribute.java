package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Port;

public class CategoryAttribute extends DictionaryAttribute
{
	
	public CategoryAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public CategoryAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return ImageCategory.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return ImageCategory.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return ImageCategory.loadAll(sess);
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) Port.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return ImageCategory.class;
	}
	
}