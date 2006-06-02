package edu.emory.library.tas;

import java.util.Calendar;

public class SchemaColumn
{
	
	public final static int IMPORT_TYPE_IGNORE = -1;
	public final static int IMPORT_TYPE_NUMERIC = 0; 
	public final static int IMPORT_TYPE_STRING = 1; 
	public final static int IMPORT_TYPE_DATE = 2; 
	
	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_LONG = 1; 
	public final static int TYPE_FLOAT = 5; 
	public final static int TYPE_STRING = 2; 
	public final static int TYPE_DATE = 3;
	public final static int TYPE_DICT = 4;
	
	private String name;
	private int type;
	private int importType;
	
	private String dictionary;
	
	private String userLabel;
	
	private String importName;
	private String importDateDay;
	private String importDateMonth;
	private String importDateYear;
	
	public SchemaColumn(String name, int type, String dictionary, int importType, String importName, String importDateDay, String importDateMonth, String importDateYear, String userLabel)
	{
		this.userLabel = userLabel;
		this.name = name;
		this.type = type;
		this.dictionary = dictionary;
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
	}
	
	
	public Object parse(String[] values) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException
	{
		
		String value;
		switch (type)
		{
			case TYPE_STRING:
				
				if (values.length != 1 || values[0] == null)
					throw new InvalidNumberOfValuesException();

				value = values[0].trim();
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
				
				if (values.length != 3 || values[0] == null || values[1] == null || values[2] == null)
					throw new InvalidNumberOfValuesException();
				
				String day = values[0].trim();
				String month = values[1].trim();
				String year = values[2].trim();
				
				if (day.length() == 0 || month.length() == 0 || year.length() == 0)
					return null;

				Calendar cal = Calendar.getInstance();
				try
				{
					cal.set(
							Integer.parseInt(year),
							Integer.parseInt(month),
							Integer.parseInt(day));
					return cal.getTime();
				}
				catch (NumberFormatException nfe)
				{
					throw new InvalidDateException();
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
				
				Dictionary dicts[] = Dictionary.loadDictionary(dictionary, remoteId);
				if (dicts.length > 0)
				{
					return dicts[0];
				}
				else
				{
					Dictionary dict = Dictionary.createNew(dictionary);
					dict.setRemoteId(remoteId);
					dict.setName(remoteId.toString());
					return dict;
				}

			default:
				return null;

		}
		
	}

	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException
	{
		return parse(new String[] {value});
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
		if (importName == null) return name;
		return importName;
	}
	
	public int getImportType()
	{
		return importType;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getType()
	{
		return type;
	}
	
	public boolean isDictinaory()
	{
		return dictionary != null;
	}
	
	public String getDictinaory()
	{
		return dictionary;
	}


	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

}
