package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class NumericAttribute extends Attribute {
	
	public static final String ATTR_TYPE_NAME = "Numeric";
	
	public final static int TYPE_INTEGER = 0;

	public final static int TYPE_LONG = 1;

	public final static int TYPE_FLOAT = 5;
	
	public final static int TYPE_UNKNOWN = -1;
	
	private Integer type;

	public NumericAttribute(String name, String objectType) {
		super(name, objectType);
		this.type = new Integer(TYPE_UNKNOWN);
	}
	
	public NumericAttribute(Node xmlNode, String objectType) {
		super(xmlNode, objectType);
		this.type = new Integer(this.parseAttribute(xmlNode, "type"));
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		if (type == null) {
			type = new Integer(-1);
		} else {
			this.type = type;
		}
	}
	
	public Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException {
		
		String value;
		switch (getType().intValue()) {

		case TYPE_INTEGER:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Integer(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_LONG:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Long(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_FLOAT:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Float(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}
		}
		return null;
	}

	public String getTypeDisplayName() {
		switch (type.intValue()) {
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
}
