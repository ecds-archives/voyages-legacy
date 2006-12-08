package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class FateVesselAttribute extends DictionaryAttribute
{
	
	public FateVesselAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateVesselAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return FateVessel.loadById(parseId(value));
	}

}