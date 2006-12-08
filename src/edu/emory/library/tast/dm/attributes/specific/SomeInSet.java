package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;

public class SomeInSet extends Attribute {

	private Attribute attribute;
	
	public SomeInSet(Attribute attr) {
		super("SomeInSet", null);
		this.attribute = attr;
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		return attribute.getHQLOuterJoinPath(bindings);
	}

	public String getHQLParamName() {
		return attribute.getHQLParamName();
	}

	public String getHQLSelectPath(Map bindings) {
		return attribute.getHQLSelectPath(bindings);
	}

	public String getHQLWherePath(Map bindings) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("some elements(");
		buffer.append(attribute.getHQLWherePath(bindings));
		buffer.append(")");
		return buffer.toString();
	}

	public String getTypeDisplayName() {
		return null;
	}

	public Object getValueToCondition(Object value) {
		return attribute.getValueToCondition(value);
	}

	public boolean isOuterjoinable() {
		return attribute.isOuterjoinable();
	}

}
