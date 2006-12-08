package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class FateOwnerAttribute extends DictionaryAttribute
{
	
	public FateOwnerAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateOwnerAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}

	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return FateOwner.loadById(parseId(value));
	}
	
}