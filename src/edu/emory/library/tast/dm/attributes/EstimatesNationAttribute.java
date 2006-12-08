package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class EstimatesNationAttribute extends DictionaryAttribute
{
	
	public EstimatesNationAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public EstimatesNationAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return EstimatesNation.loadById(parseId(value));
	}

}
