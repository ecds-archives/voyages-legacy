package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Resistance;

public class ResistanceAttribute extends DictionaryAttribute
{
	
	public ResistanceAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public ResistanceAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Resistance.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Resistance.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Resistance.loadAll(sess);
	}

	public NumericAttribute getItAttribute()
	{
		return (NumericAttribute) Resistance.getAttribute("id");
	}

}