package edu.emory.library.tast.dm.attributes;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class RegionAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "Region";

	public RegionAttribute(String name, String objType)
	{
		super(name, objType);
	}
	
	public RegionAttribute(Node xmlNode, String objectType)
	{
		super(xmlNode, objectType);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Region.loadById(parseId(value));
	}

}
