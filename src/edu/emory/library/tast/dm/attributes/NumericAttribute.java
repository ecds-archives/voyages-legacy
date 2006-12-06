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
		this(name, objectType, null);
	}
	
	public NumericAttribute(String name, String objectType, String string) {
		super(name, objectType);
		this.type = new Integer(TYPE_UNKNOWN);
		this.setUserLabel(string);
	}
	
	public NumericAttribute(Node xmlNode, String objectType) {

		super(xmlNode, objectType);
		
		String attrNumType = this.parseAttribute(xmlNode, "attrNumType");
		if ("Integer".equals(attrNumType))
			this.type = new Integer(TYPE_INTEGER);
		else if ("Long".equals(attrNumType))
			this.type = new Integer(TYPE_LONG);
		else if ("Float".equals(attrNumType))
			this.type = new Integer(TYPE_FLOAT);
		else
			this.type = new Integer(TYPE_UNKNOWN);
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
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		
		if (value == null)
			return null;

		value = value.trim();
		if (value.length() == 0)
			return null;

		switch (getType().intValue())
		{

		case TYPE_INTEGER:

			try {
				return new Integer(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_LONG:

			try {
				return new Long(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_FLOAT:

			try {
				return new Float(value);
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

	public Object getValueToCondition(Object value) {
		return value;
	}
}
