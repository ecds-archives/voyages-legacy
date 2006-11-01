package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class DictionaryAttribute extends Attribute {
	
	public static final String ATTR_TYPE_NAME = "Dictionary";
	
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

	public Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException {
		String value;
		
		if (values.length != 1 || values[0] == null)
			throw new InvalidNumberOfValuesException();

		value = values[0].trim();
		if (value.length() == 0)
			return null;

		Integer remoteId = null;
		try {
			remoteId = new Integer(value);
		} catch (NumberFormatException nfe) {
			throw new InvalidNumberException();
		}

		Dictionary dicts[] = Dictionary.loadDictionaryByRemoteId(getDictionary(),
				remoteId);
		if (dicts.length > 0) {
			return dicts[0];
		} else {
			Dictionary dict = Dictionary.createNew(getDictionary());
			dict.setRemoteId(remoteId);
			dict.setName(remoteId.toString());
			dict.save();
			return dict;
		}
	}

	public String getTypeDisplayName() {
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
