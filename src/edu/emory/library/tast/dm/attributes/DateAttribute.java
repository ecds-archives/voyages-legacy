package edu.emory.library.tast.dm.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;
import edu.emory.library.tast.spss.STSchemaVariable;

public class DateAttribute extends ImportableAttribute
{
	
	private DateFormat importDateFormat = new SimpleDateFormat("d/M/y");
	
	public DateAttribute(String name, String objectType)
	{
		super(name, objectType);
	}
	
	public DateAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{

		if (value == null || value.length() == 0)
			return null;

		try
		{
			return importDateFormat.parse(value);
		}
		catch (ParseException e)
		{
			throw new InvalidDateException();
		}

	}

	public String getTypeDisplayName() {
		return "Date";
	}

	public boolean isOuterjoinable() {
		return false;
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
		return null;
	}

	public Object getValueToCondition(Object value) {
		return value;
	}
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_DATE;
	}

}
