package edu.emory.library.tas.attrGroups;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.InvalidDateException;
import edu.emory.library.tas.InvalidNumberException;
import edu.emory.library.tas.InvalidNumberOfValuesException;
import edu.emory.library.tas.StringTooLongException;

public class Attribute extends AbstractAttribute {
	
	
	public final static int IMPORT_TYPE_IGNORE = -1;
	public final static int IMPORT_TYPE_NUMERIC = 0; 
	public final static int IMPORT_TYPE_STRING = 1; 
	public final static int IMPORT_TYPE_DATE = 2; 
		
	private Integer importType;
	private String importName;
	private String importDateDay;
	private String importDateMonth;
	private String importDateYear;
	
	public Attribute() {
	}
	
	public Attribute(String name, Integer type, String dictionary, Integer importType, String importName, String importDateDay, String importDateMonth, String importDateYear, String userLabel, Integer length, ObjectType objType)
	{
		super(name, type, dictionary, userLabel, objType, length);
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
	}
	
	
	public static AbstractAttribute loadById(Long id)
	{
		return null;
	}

	public String getImportDateDay()
	{
		return importDateDay;
	}
	
	public String getImportDateMonth()
	{
		return importDateMonth;
	}
	
	public String getImportDateYear()
	{
		return importDateYear;
	}
	
	public String getImportName()
	{
		if (importName == null) return getName();
		return importName;
	}
	
	public Integer getImportType()
	{
		return importType;
	}

	public void setImportDateDay(String importDateDay) {
		this.importDateDay = importDateDay;
	}

	public void setImportDateMonth(String importDateMonth) {
		this.importDateMonth = importDateMonth;
	}

	public void setImportDateYear(String importDateYear) {
		this.importDateYear = importDateYear;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

}
