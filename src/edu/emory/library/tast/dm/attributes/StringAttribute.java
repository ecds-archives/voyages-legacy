package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import edu.emory.library.tas.spss.STSchemaVariable;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class StringAttribute extends ImportableAttribute
{
	
	public StringAttribute(String name, String objectType)
	{
		super(name, objectType);
	}
	
	public StringAttribute(String name, String objectType, String importName, int maxImportLength)
	{
		super(name, objectType, importName);
		setMaxImportLength(maxImportLength);
	}

	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		
		if (value == null)
			return null;

		if (isImportLengthLimited() && value.length() > getMaxImportLength())
			throw new StringTooLongException();

		return value;

	}

	public String getTypeDisplayName() {
		return "Text";
	}
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_STRING;
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
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("'").append(value.toString()).append("'");
		return value;
	}
}
