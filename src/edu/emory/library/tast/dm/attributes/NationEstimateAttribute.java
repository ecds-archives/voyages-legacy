package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class NationEstimateAttribute extends DictionaryAttribute {

	public NationEstimateAttribute(String name, String objType) {
		super(name, objType);
	}

	public NationEstimateAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException {
		// TODO Auto-generated method stub
		return null;
	}

}
