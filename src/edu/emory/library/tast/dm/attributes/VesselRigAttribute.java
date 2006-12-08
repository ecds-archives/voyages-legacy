package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class VesselRigAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "VesselRig";
	
	public VesselRigAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public VesselRigAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object importParse(String value) throws InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return VesselRig.loadById(parseId(value));
	}

}