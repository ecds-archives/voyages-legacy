package edu.emory.library.tast.dm.attributes;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tas.spss.STSchemaVariable;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.MissingDictionaryValueException;

public abstract class DictionaryAttribute extends ImportableAttribute
{
	
	public DictionaryAttribute(String name, String objType)
	{
		this(name, objType, null);
	}
	
	public DictionaryAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_NUMERIC;
	}
	
	public Object importParse(Session sess, String value) throws MissingDictionaryValueException, InvalidNumberException 
	{
		if (value == null || value.length() == 0)
		{
			return null;
		}
		else
		{
			long id = 0;
			try
			{
				id = Long.parseLong(value);
			}
			catch (NumberFormatException nfe)
			{
				throw new InvalidNumberException();
			}
			Object obj = loadObjectById(sess, id);
			if (obj == null) throw new MissingDictionaryValueException();
			return obj;
		}
	}

	public abstract Dictionary loadObjectById(Session sess, long id);
	public abstract List loadAllObjects(Session sess);

	/*
	protected long parseId(String value) throws InvalidNumberException
	{
		if (value == null || value.length() == 0)
		{
			return 0;
		}
		else
		{
			try
			{
				return Long.parseLong(value);
			}
			catch (NumberFormatException nfe)
			{
				throw new InvalidNumberException();
			}
		}
	}
	*/

	public boolean isOuterjoinable() {
		return true;
	}
	
	public String getHQLSelectPath(Map bindings) {
		if (!bindings.containsKey(this.getObjectType())) {
			return this.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(bindings.get(this.getObjectType()));
		buffer.append(".");
		buffer.append(this.getName());
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}

	public String getHQLParamName() {
		return this.getName();
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		if (!bindings.containsKey(this.getObjectType())) {
			return this.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(bindings.get(this.getObjectType()));
		buffer.append(".");
		buffer.append(this.getName());
		return buffer.toString();
	}

	public Object getValueToCondition(Object value) {
		return value;
	}
	
	public abstract Attribute getAttribute(String name);

}
