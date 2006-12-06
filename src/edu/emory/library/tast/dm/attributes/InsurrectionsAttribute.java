package edu.emory.library.tast.dm.attributes;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.Insurrections;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class InsurrectionsAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "Insurrections";
	
	public InsurrectionsAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public InsurrectionsAttribute(Node xmlNode, String objectType)
	{
		super(xmlNode, objectType);
	}
	
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return Insurrections.loadById(parseId(value));
	}

}