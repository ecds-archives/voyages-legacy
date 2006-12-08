package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class FateSlavesAttribute extends DictionaryAttribute
{
	
	public FateSlavesAttribute(String name, String objType)
	{
		super(name, objType);
	}
	
	public FateSlavesAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}

	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return FateSlaves.loadById(parseId(value));
	}

}