package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class RegionAttribute extends DictionaryAttribute
{
	
	public RegionAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public RegionAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Region.loadById(parseId(value));
	}

}
