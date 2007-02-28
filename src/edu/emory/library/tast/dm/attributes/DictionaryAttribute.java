package edu.emory.library.tast.dm.attributes;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.spss.LogWriter;
import edu.emory.library.tast.spss.STSchemaVariable;

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
	
	public abstract NumericAttribute getItAttribute(); 
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_NUMERIC;
	}
	
	public Object importParse(Session sess, String value, LogWriter log, int recordNo) 
	{
		
		if (value == null || value.length() == 0)
			return null;
		
		long id = 0;
		
		try
		{
			id = Long.parseLong(value);
		}
		catch (NumberFormatException nfe)
		{
			log.logWarn(
					"Variable " + getName() + ", " +
					"record " + recordNo + ": " +
					"values '" + value + "' is expected to be an integer. " +
					"Imported as NULL (MISSING).");
			return null;
		}
		
		Object obj = loadObjectById(sess, id);
		if (obj == null)
		{
			log.logWarn(
					"Variable " + getName() + ", " +
					"record " + recordNo + ": " +
					"label for code '" + value + "' is not defined. " +
					"Imported as NULL (MISSING).");
			return null;
		}

		return obj;

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
