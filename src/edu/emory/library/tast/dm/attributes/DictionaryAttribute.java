package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;

public abstract class DictionaryAttribute extends Attribute
{
	
	private String dictionary;

	public DictionaryAttribute(String name, String objType) {
		this(name, objType, null);
	}
	
	public DictionaryAttribute(String name, String objectType, String string) {
		super(name, objectType);
		this.setUserLabel(string);
	}
	
	public DictionaryAttribute(Node xmlNode, String objectType) {
		super(xmlNode, objectType);
		this.dictionary = this.parseAttribute(xmlNode, "dictionary");
	}

	protected long parseId(String value) throws InvalidNumberOfValuesException, InvalidNumberException
	{
		if (value == null || value.length() == 0)
		{
			return 0;
		}
		else
		{
			try
			{
				return Long.parseLong(value);
			}
			catch (NumberFormatException nfe)
			{
				throw new InvalidNumberException();
			}
		}
	}

	public String getTypeDisplayName()
	{
		return "List " + dictionary;
	}
	
	public String getDictionary() {
		return dictionary;
	}

	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}
	
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

	public Object getValueToCondition(Object value) {
		return value;
	}

}
