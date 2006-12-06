package edu.emory.library.tast.dm.attributes;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class AreaAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "Area";
	
	public AreaAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public AreaAttribute(Node xmlNode, String objectType)
	{
		super(xmlNode, objectType);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Area.loadById(parseId(value));
	}

}
