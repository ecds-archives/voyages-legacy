package edu.emory.library.tast.dm.attributes;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class FateAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "Fate";
	
	public FateAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateAttribute(Node xmlNode, String objectType)
	{
		super(xmlNode, objectType);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Fate.loadById(parseId(value));
	}

}
