package edu.emory.library.tast.dm.attributes;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.spss.LogWriter;
import edu.emory.library.tast.spss.STSchemaVariable;
import edu.emory.library.tast.util.HibernateUtil;

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
	public abstract Class getDictionayClass();

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

	public abstract Attribute getAttribute(String name);
	
	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{

		// get the name of the table
		String table = HibernateUtil.getTableName(this.getDictionayClass().getCanonicalName());
		
		// get a new index for this table
		int thisIdx = 0;
		Integer lastIdx = (Integer)tablesIndexes.get(table);
		if (lastIdx != null) thisIdx = lastIdx.intValue() + 1; 
		tablesIndexes.put(table, new Integer(thisIdx));
		
		// create an alias for it using the index
		String tableAlias = table + "_" + thisIdx;

		// INNER JOIN table tableAlias ON tableAlias.id = prevTableAlias.prevColumn
		sqlFrom.append("    ");
		sqlFrom.append("LEFT JOIN ");
		sqlFrom.append(table).append(" ").append(tableAlias);
		sqlFrom.append(" ON ");
		sqlFrom.append(tableAlias).append(".").append("id");
		sqlFrom.append(" = ");					
		sqlFrom.append(masterTable).append(".").append(getName());
		sqlFrom.append("\n");		
		
		// name of the column from the 'dictionary' is always 'name'
		return tableAlias + ".name";

	}

}
