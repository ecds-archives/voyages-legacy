package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

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

	public Object parse(String[] values, int options)
			throws InvalidNumberOfValuesException, InvalidNumberException,
			InvalidDateException, StringTooLongException {
		// TODO Auto-generated method stub
		return null;
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

	public String getHQLParamName() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < this.attributes.length; i++) {
			if (i > 0) {
				buffer.append("_");
			}
			buffer.append(attributes[i].getHQLParamName());
		}
		return buffer.toString();
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < this.attributes.length; i++) {
			if (attributes[i].getHQLOuterJoinPath(bindings) == null) {
				continue;
			}
			if (i != 0) {
				buffer.append(", ");
			}
			buffer.append(attributes[i].getHQLOuterJoinPath(bindings));
		}
		
		return buffer.toString().equals("") ? null : buffer.toString();
	}
}
