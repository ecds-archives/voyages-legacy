/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.spss.LogWriter;
import edu.emory.library.tast.spss.STSchemaVariable;

public class NumericAttribute extends ImportableAttribute
{
	
	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_LONG = 1;
	public final static int TYPE_FLOAT = 2;
	public final static int TYPE_DOUBLE = 3;
	
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
				float floatValue = Float.parseFloat(value);
				if (percentage) floatValue *= 100;
				return new Float(floatValue);
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
		case TYPE_DOUBLE:

			try
			{
				double doubleValue = Double.parseDouble(value);
				if (percentage) doubleValue *= 100;
				return new Double(doubleValue);
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
		case TYPE_DOUBLE:
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
	
	public boolean isPercentage()
	{
		return percentage;
	}

	public void setPercentage(boolean percentage)
	{
		this.percentage = percentage;
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		return masterTable + "." + getName();
	}

}
