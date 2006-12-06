package edu.emory.library.tast.dm.attributes;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class FateSlavesAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "FateSlaves";
	
	public FateSlavesAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateSlavesAttribute(Node xmlNode, String objectType)
	{
		super(xmlNode, objectType);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return FateSlaves.loadById(parseId(value));
	}

}