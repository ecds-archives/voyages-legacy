package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class NationAttribute extends DictionaryAttribute
{
	
	public NationAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public NationAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Nation.loadById(parseId(value));
	}

}
