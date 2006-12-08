package edu.emory.library.tast.dm.attributes;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public abstract class ImportableAttribute extends Attribute
{
	
	private String importName;

	public ImportableAttribute(String object, String objectType)
	{
		super(object, objectType);
	}

	public ImportableAttribute(String object, String objectType, String importName)
	{
		super(object, objectType);
		this.importName = importName;
	}

	public abstract Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException;

	public String getImportName()
	{
		return importName;
	}

	public void setImportName(String importName)
	{
		this.importName = importName;
	}

}
