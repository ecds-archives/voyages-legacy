package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.TonType;

public class TonTypeAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "tonType";
	
	public TonTypeAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public TonTypeAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return TonType.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return TonType.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return TonType.loadAll(sess);
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) TonType.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return TonType.class;
	}
	
}