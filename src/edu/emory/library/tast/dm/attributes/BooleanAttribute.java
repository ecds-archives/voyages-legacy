package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tas.spss.STSchemaVariable;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class BooleanAttribute extends ImportableAttribute
{
	
	public static final String ATTR_TYPE_NAME = "Boolean";

	public BooleanAttribute(String name, String objectType)
	{
		super(name, objectType);
	}

	public BooleanAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public String getHQLOuterJoinPath(Map bindings)
	{
		return null;
	}

	public String getHQLParamName()
	{
		return null;
	}

	public String getHQLSelectPath(Map bindings)
	{
		return null;
	}

	public String getHQLWherePath(Map bindings)
	{
		return null;
	}

	public String getTypeDisplayName()
	{
		return null;
	}

	public Object getValueToCondition(Object value)
	{
		return null;
	}

	public boolean isOuterjoinable()
	{
		return false;
	}

	public Object importParse(Session sess, String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return new Boolean(value.equals("TRUE") || value.equals("1"));
	}

	public int getImportType()
	{
		return STSchemaVariable.TYPE_NUMERIC;
	}
	
}