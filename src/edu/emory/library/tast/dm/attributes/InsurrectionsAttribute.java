package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.Insurrections;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class InsurrectionsAttribute extends DictionaryAttribute
{
	
	public InsurrectionsAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public InsurrectionsAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Insurrections.loadById(parseId(value));
	}

}