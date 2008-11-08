package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;

public class CaseNullToZeroAttribute extends Attribute {
	
	private Attribute attribute;
	
	/**
	 * case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end
	 */
	
	public CaseNullToZeroAttribute(Attribute attribute) {
		super(attribute.getName(), null);
		this.attribute = attribute;
	}
	
	public String getTypeDisplayName() {
		return null;
	}

	public boolean isOuterjoinable() {
		return attribute.isOuterjoinable();
	}
	
	public String getHQLSelectPath(Map bindings) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("case when ");
		buffer.append(attribute.getHQLSelectPath(bindings));
		buffer.append(" is null then 0 else ");
		buffer.append(attribute.getHQLSelectPath(bindings));
		buffer.append(" end");
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		return attribute.getHQLOuterJoinPath(bindings);
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		return null;
	}

}
