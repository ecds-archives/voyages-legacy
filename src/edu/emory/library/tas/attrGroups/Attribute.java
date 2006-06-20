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
		
	private int importType;
	private String importName;
	private String importDateDay;
	private String importDateMonth;
	private String importDateYear;
	private int importLength;
	
	public Attribute() {
	}
	
	public Attribute(String name, int type, String dictionary, int importType, String importName, String importDateDay, String importDateMonth, String importDateYear, String userLabel, int importLength, ObjectType objType)
	{
		super(name, type, dictionary, userLabel, objType);
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
		this.importLength = importLength;
	}
	
	public Object parse(String[] values) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		
		String value;
		switch (getType())
		{
			case TYPE_STRING:
				
				if (values.length != 1 || values[0] == null)
					throw new InvalidNumberOfValuesException();
				
				value = values[0].trim();
				if (value.length() == 0)
					return null;
				
				if (importLength != -1 && value.length() > importLength)
					throw new StringTooLongException();
				
				return value;
				
			case TYPE_INTEGER:
				
				if (values.length != 1 || values[0] == null)
					throw new InvalidNumberOfValuesException();
				
				value = values[0].trim();
				if (value.length() == 0)
					return null;
				
				try
				{
					return new Integer(values[0]);
				}
				catch (NumberFormatException nfe)
				{
					throw new InvalidNumberException();
				}
				
			case TYPE_LONG:

				if (values.length != 1 || values[0] == null)
					throw new InvalidNumberOfValuesException();
				
				value = values[0].trim();
				if (value.length() == 0)
					return null;
				
				try
				{
					return new Long(values[0]);
				}
				catch (NumberFormatException nfe)
				{
					throw new InvalidNumberException();
				}
				
			case TYPE_FLOAT:

				if (values.length != 1 || values[0] == null)
					throw new InvalidNumberOfValuesException();
				
				value = values[0].trim();
				if (value.length() == 0)
					return null;
				
				try
				{
					return new Float(values[0]);
				}
				catch (NumberFormatException nfe)
				{
					throw new InvalidNumberException();
				}
				
			case TYPE_DATE:
				
				boolean separate = values.length == 3 && values[0] != null && values[1] != null && values[2] != null;
				boolean single = values.length == 1 && values[0] != null;
				
				if (!separate || !single)
					throw new InvalidNumberOfValuesException();
				
				if (separate)
				{
				
					String day = values[0].trim();
					String month = values[1].trim();
					String year = values[2].trim();
					
					if (day.length() == 0 || month.length() == 0 || year.length() == 0)
						return null;
					
					try
					{
						Calendar cal = Calendar.getInstance();
						cal.clear();
						cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
//						Timestamp tstamp = new Timestamp(Integer.parseInt(year),
//								Integer.parseInt(month),
//								Integer.parseInt(day),
//								0,0,0,0);
						return cal.getTime();
					}
					catch (NumberFormatException nfe)
					{
						throw new InvalidDateException();
					}
					
				}
				else if (single)
				{
					
					value = values[0].trim();

					if (value.length() == 0)
						return null;
					
					try
					{
						DateFormat dateFormat = DateFormat.getDateInstance();
						return dateFormat.parse(value);
					}
					catch (ParseException e)
					{
						throw new InvalidDateException();
					}
				}

			case TYPE_DICT:
				
				if (values.length != 1 || values[0] == null)
					throw new InvalidNumberOfValuesException();
				
				value = values[0].trim();
				if (value.length() == 0)
					return null;

				Integer remoteId = null;
				try
				{
					remoteId = new Integer(value);
				}
				catch (NumberFormatException nfe)
				{
					throw new InvalidNumberException();
				}
				
				Dictionary dicts[] = Dictionary.loadDictionary(getDictionary(), remoteId);
				if (dicts.length > 0)
				{
					return dicts[0];
				}
				else
				{
					Dictionary dict = Dictionary.createNew(getDictionary());
					dict.setRemoteId(remoteId);
					dict.setName(remoteId.toString());
					dict.save();
					return dict;
				}

			default:
				return null;

		}
		
	}

	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(new String[] {value});
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
	
	public int getImportType()
	{
		return importType;
	}
	
	public int getImportLength()
	{
		return importLength;
	}
	
	public void setImportLength(int importLength)
	{
		this.importLength = importLength;
	}

}
