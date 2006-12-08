package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tas.spss.STSchemaVariable;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class NumericAttribute extends ImportableAttribute
{
	
	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_LONG = 1;
	public final static int TYPE_FLOAT = 5;
	
	private int type;

	public NumericAttribute(String name, String objectType, int numericalType)
	{
		super(name, objectType);
		this.type = numericalType;
	}
	
	public NumericAttribute(String name, String objectType, int numericalType, String importName)
	{
		super(name, objectType, importName);
		this.type = numericalType;
	}
	
	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_NUMERIC;
	}
	
	public Object importParse(Session sess, String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		
		if (value == null)
			return null;

		value = value.trim();
		if (value.length() == 0)
			return null;

		switch (type)
		{

		case TYPE_INTEGER:

			try {
				return new Integer(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_LONG:

			try {
				return new Long(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_FLOAT:

			try {
				return new Float(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}
		}
		return null;
	}

	public String getTypeDisplayName() {
		switch (type) {
		case TYPE_INTEGER:
			return "Integer";
		case TYPE_LONG:
			return "Integer";
		case TYPE_FLOAT:
			return "Decimal";
		default:
			return null;
		}
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
}
