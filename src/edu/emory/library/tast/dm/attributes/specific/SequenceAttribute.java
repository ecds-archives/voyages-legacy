package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.util.EmptyMap;
import edu.emory.library.tast.util.HibernateUtil;

public class SequenceAttribute extends Attribute {

	private Attribute[] attributes;
	
	public SequenceAttribute(Attribute[] attributeSeq) {
		super("SequentialAttribute", null);
		this.attributes = attributeSeq;
	}
	
	public String getTypeDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOuterjoinable() {
		boolean joinable = false;
		for (int i = 0; i < attributes.length; i++) {
			joinable = joinable || attributes[i].isOuterjoinable(); 
		}
		return joinable;
	}
	
	public String getHQLSelectPath(Map bindings) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < this.attributes.length; i++) {
			if (i != 0) {
				buffer.append(".");
			}
			buffer.append(attributes[i].getHQLSelectPath(bindings));
		}
		
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}

	public String getHQLOuterJoinPath(Map bindings) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < this.attributes.length; i++) {
			if (attributes[i].getHQLOuterJoinPath(bindings) == null) {
				continue;
			}
			if (i != 0) {
				buffer.append(".");
			}
			buffer.append(attributes[i].getHQLOuterJoinPath(bindings));
		}
		
		return buffer.toString().equals("") ? null : buffer.toString();
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		
		String path = getHQLOuterJoinPath(new EmptyMap());
		String alias = (String) existingJoins.get(path);
		
		if (alias == null)
		{
			
			// the name of the first attribute is the name of the first column
			String prevTableAlias = masterTable;
			
			for (int i = 0; i < attributes.length - 1; i++)
			{
				
				// get the name of the table and the column
				DictionaryAttribute dictAttr = (DictionaryAttribute) attributes[i];
				String table = HibernateUtil.getTableName(dictAttr.getDictionayClass());
				String column = dictAttr.getName();
				
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
				sqlFrom.append(prevTableAlias).append(".").append(column).append(prevTableAlias.startsWith("ports_") || prevTableAlias.startsWith("regions_") ? "_id" : "");
				sqlFrom.append("\n");
				
				// remember for the next round
				prevTableAlias = tableAlias;
				
			}
			
			alias = prevTableAlias;
			existingJoins.put(path, alias);
		
		}
		
		// the alias of the last table
		return alias + "." + attributes[attributes.length - 1].getName();
		
	}
	
}
