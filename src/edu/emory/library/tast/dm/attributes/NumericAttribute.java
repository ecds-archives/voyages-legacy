package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tas.spss.STSchemaVariable;
import edu.emory.library.tast.spss.LogWriter;

public class NumericAttribute extends ImportableAttribute
{
	
	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_LONG = 1;
	public final static int TYPE_FLOAT = 2;
	
	private int type;
	private boolean percentage = false;

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
	
	public Object importParse(Session sess, String value, LogWriter log, int recordNo)
	{
		
		if (value == null)
			return null;

		value = value.trim();
		if (value.length() == 0)
			return null;

		switch (type)
		{

		case TYPE_INTEGER:
		case TYPE_LONG:
			
			double dblValue = 0;
			try
			{
				dblValue = Double.parseDouble(value);
			}
			catch (NumberFormatException nfe)
			{
				log.logWarn(
						"Variable " + getName() + ", " +
						"record " + recordNo + ": " +
						"value '" + value + "' is not an number. " +
						"Imported as NULL (MISSING).");
				return null;
			}
			
			double roundedValue = Math.rint(dblValue);
			if (roundedValue != dblValue)
			{
				log.logWarn(
						"Variable " + getName() + ", " +
						"record " + recordNo + ": " +
						"value '" + value + "' expected to be an integer. " +
						"Imported, but rounded to integer.");
			}

			if (type == TYPE_INTEGER)
			{
				try
				{
					return new Integer((int)roundedValue);
				}
				catch (NumberFormatException nfe)
				{
					log.logWarn(
							"Variable " + getName() + ", " +
							"record " + recordNo + ": " +
							"value '" + value + "' too large for int. " +
							"Imported as NULL (MISSING).");
					return null;
				}
			}

			if (type == TYPE_LONG)
			{
				try
				{
					return new Long((long)roundedValue);
				}
				catch (NumberFormatException nfe)
				{
					log.logWarn(
							"Variable " + getName() + ", " +
							"record " + recordNo + ": " +
							"value '" + value + "' too large for long. " +
							"Imported as NULL (MISSING).");
					return null;
				}
			}

		case TYPE_FLOAT:

			try
			{
				return new Float(value);
			}
			catch (NumberFormatException nfe)
			{
				log.logWarn(
						"Variable " + getName() + ", " +
						"record " + recordNo + ": " +
						"value '" + value + "' is not an decimal number. " +
						"Imported as NULL (MISSING).");
				return null;
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
