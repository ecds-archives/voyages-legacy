package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class FateAttribute extends DictionaryAttribute
{
	
	public FateAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}

	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Fate.loadById(parseId(value));
	}

}
