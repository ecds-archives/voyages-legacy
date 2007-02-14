package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Category;
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
		return Category.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Category.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Category.loadAll(sess);
	}
	
	public NumericAttribute getItAttribute()
	{
		return (NumericAttribute) Port.getAttribute("id");
	}
	
}