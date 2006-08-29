package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class StringAttribute extends Attribute {
	
	public static final String ATTR_TYPE_NAME = "String";
	
	private Integer length = new Integer(-1);

	public StringAttribute(String name, String objectType) {
		super(name, objectType);
	}
	
	public StringAttribute(Node xmlNode, String objectType) {
		super(xmlNode, objectType);
		String len = this.parseAttribute(xmlNode, "length");
		if (len != null) {
			this.length = new Integer(len);
		}
	}
	
	public Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException {
		if (values.length != 1 || values[0] == null)
			throw new InvalidNumberOfValuesException();

		String value = values[0].trim();
		if (value.length() == 0)
			return null;

		if (length.intValue() != -1 && value.length() > length.intValue())
			throw new StringTooLongException();

		return value;
	}

	public String getTypeDisplayName() {
		return "Text";
	}
	
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		if (length != null)
			this.length = length;
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
