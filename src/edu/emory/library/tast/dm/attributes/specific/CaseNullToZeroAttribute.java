package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

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

	public Object parse(String[] values, int options)
			throws InvalidNumberOfValuesException, InvalidNumberException,
			InvalidDateException, StringTooLongException {
		return null;
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

	public String getHQLParamName() {
		return this.attribute.getName();
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		return attribute.getHQLOuterJoinPath(bindings);
	}

	public Object getValueToCondition(Object value) {
		return attribute.getValueToCondition(value);
	}
}
