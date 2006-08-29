package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class DirectValueAttribute extends Attribute {

	String value;
	
	public DirectValueAttribute(String value) {
		super(value, null);
		this.value = value;
	}

	public String getTypeDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOuterjoinable() {
		return false;
	}

	public Object parse(String[] values, int options)
			throws InvalidNumberOfValuesException, InvalidNumberException,
			InvalidDateException, StringTooLongException {
		return null;
	}

	public String getHQLSelectPath(Map bindings) {
		return value;
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}
	
	public String getHQLParamName() {
		return this.value;
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		return null;
	}

	public Object getValueToCondition(Object value) {
		return value;
	}
}
