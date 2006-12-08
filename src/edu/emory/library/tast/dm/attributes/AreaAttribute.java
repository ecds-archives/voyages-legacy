package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class AreaAttribute extends DictionaryAttribute
{
	
	public AreaAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public AreaAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}
	
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Area.loadById(parseId(value));
	}

}
