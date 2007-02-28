package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.spss.LogWriter;
import edu.emory.library.tast.spss.STSchemaVariable;

public class StringAttribute extends ImportableAttribute
{
	
	public StringAttribute(String name, String objectType)
	{
		super(name, objectType);
	}
	
	public StringAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}

	public StringAttribute(String name, String objectType, String importName, int maxImportLength)
	{
		super(name, objectType, importName);
		setMaxImportLength(maxImportLength);
	}

	public Object importParse(Session sess, String value, LogWriter log, int recordNo)
	{
		
		if (value == null)
			return null;

		if (isImportLengthLimited() && value.length() > getMaxImportLength())
		{
			log.logWarn(
					"Variable " + getName() + ", " +
					"record " + recordNo + ": " +
					"string '" + value + "' too long. " +
					"Expected maximal length is " + getMaxImportLength() + ". " +
					"Imported as NULL (MISSING).");
			return null;
		}

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
